package tbb.x4.imp.parser;

import tbb.x4.api.model.*;
import tbb.x4.api.parser.Attribute;
import tbb.x4.api.parser.ParsingException;
import tbb.x4.api.parser.Tag;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * The Parser class is responsible for parsing XML files into a structured format
 * using the provided descriptor repository to validate and interpret the tags.
 * This class is not thread-safe and should be used in a single-threaded context.
 */
public class Parser {
    private final IDescriptorRepository descriptorRepository;
    private final XMLEventReader reader;
    private XMLEvent actualEvent;

    public Parser(IDescriptorRepository descriptorRepository, XMLEventReader reader) {
        this.descriptorRepository = descriptorRepository;
        this.reader = reader;
    }

    /**
     * Parses the XML file and returns a list of tags.
     *
     * @return a list of parsed tags
     */
    public List<Tag> parse() {
        List<Tag> tags = new ArrayList<>();
        while (tryNextParse()) {
            tags.add(parseTag());
        }
        return tags;
    }

    private Tag parseTag() {
        return parseTag(null);
    }

    private Tag parseTag(TagDescriptor parentTagDescriptor) {
        // Parse the start element to get the tag descriptor and attributes
        StartElement startElement = actualEvent.asStartElement();
        TagDescriptor tagDescriptor =
                getTagDescriptor(new ElementName(startElement.getName().getLocalPart()), parentTagDescriptor);
        List<Attribute> attributes = getAttributes(startElement, tagDescriptor);
        tryNextStartTag();
        // Check if the next event is a value or a start element for sub-tags
        ElementValue elementValue = null;
        List<Tag> subTags = List.of();
        if (actualEvent.isCharacters()) {
            elementValue = new ElementValue(actualEvent.asCharacters().getData(), tagDescriptor.valueType());
            tryNextValueTag();
        }
        if (actualEvent.isStartElement()) {
            subTags = parseSubTags(tagDescriptor);
        }
        // End of the tag
        if (actualEvent != null && actualEvent.isEndElement()) {
            tryNextEndTag();
            return new Tag(tagDescriptor, elementValue, attributes, subTags);
        } else {
            throw new ParsingException("Expected end element after value or sub-tags, but found: " + actualEvent);
        }
    }

    private List<Tag> parseSubTags(TagDescriptor tagDescriptor) {
        List<Tag> subTags = new ArrayList<>();
        while (!actualEvent.isEndElement()) {
            subTags.add(parseTag(tagDescriptor));
        }
        return subTags;
    }

    private TagDescriptor getTagDescriptor(ElementName elementName, TagDescriptor parentTagDescriptor) {
        Optional<TagDescriptor> tagDescriptor;
        if (parentTagDescriptor != null) {
            tagDescriptor = parentTagDescriptor.subTags().stream()
                    .filter(subTag -> subTag.name().equals(elementName))
                    .findFirst();
        } else {
            tagDescriptor = descriptorRepository.getTagDescriptor(elementName);
        }
        return tagDescriptor.orElseGet(() -> descriptorRepository.getUnknownTagDescriptor(elementName));
    }

    private AttributeDescriptor getAttributeDescriptor(ElementName elementName, TagDescriptor tagDescriptor) {
        return tagDescriptor.attributes().stream()
                .filter(attributeDescriptor -> attributeDescriptor.name().equals(elementName))
                .findFirst()
                .orElse(descriptorRepository.getUnknowAttributeDescriptor(elementName));
    }

    private List<Attribute> getAttributes(StartElement startElement, TagDescriptor tagDescriptor) {
        List<Attribute> attributes = new ArrayList<>();
        for (Iterator<javax.xml.stream.events.Attribute> it = startElement.getAttributes(); it.hasNext(); ) {
            var attribute = it.next();
            AttributeDescriptor attributeDescriptor =
                    getAttributeDescriptor(new ElementName(attribute.getName().getLocalPart()), tagDescriptor);
            attributes.add(new Attribute(attributeDescriptor, attribute.getValue()));
        }
        return attributes;
    }

    private void tryNextEndTag() {
        if (tryNext()) {
            switch (actualEvent.getEventType()) {
                case XMLEvent.START_ELEMENT:
                case XMLEvent.END_ELEMENT:
                case XMLEvent.END_DOCUMENT:
                    break;
                case XMLEvent.CHARACTERS:
                    Characters characters = actualEvent.asCharacters();
                    if (characters.getData().isBlank()) {
                        tryNextEndTag();
                    } else {
                        throw new ParsingException(
                                "Unexpected characters found after tag end: (%s)"
                                        .formatted(actualEvent));
                    }
                    break;
                case XMLEvent.COMMENT:
                    tryNextEndTag();
                    break;
                default:
                    throw new ParsingException("Unexpected event after tag value: (%s)"
                            .formatted(actualEvent));
            }
        }
    }

    private void tryNextValueTag() {
        if (tryNext()) {
            switch (actualEvent.getEventType()) {
                case XMLEvent.START_ELEMENT:
                case XMLEvent.END_ELEMENT:
                    break;
                case XMLEvent.CHARACTERS:
                    Characters characters = actualEvent.asCharacters();
                    if (characters.getData().isBlank()) {
                        tryNextValueTag();
                    } else {
                        throw new ParsingException(
                                "Unexpected characters found after tag value: (%s)"
                                        .formatted(actualEvent));
                    }
                    break;
                case XMLEvent.COMMENT:
                    tryNextValueTag();
                    break;
                default:
                    throw new ParsingException("Unexpected event after tag value: (%s)"
                            .formatted(actualEvent));
            }
        } else {
            throw new ParsingException("Expected event after tag value but found no next event.");
        }
    }

    private void tryNextStartTag() {
        if (tryNext()) {
            switch (actualEvent.getEventType()) {
                case XMLEvent.START_ELEMENT:
                case XMLEvent.END_ELEMENT:
                    break;
                case XMLEvent.CHARACTERS:
                    Characters characters = actualEvent.asCharacters();
                    if (characters.getData().isBlank()) {
                        tryNextStartTag();
                    }
                    break;
                case XMLEvent.COMMENT:
                    tryNextStartTag();
                    break;
                default:
                    throw new ParsingException("Unexpected event at beginning of tag value: (%s)"
                            .formatted(actualEvent));
            }
        } else {
            throw new ParsingException("Started tag without next event.");
        }
    }

    private boolean tryNextParse() {
        boolean hasNext = tryNext();
        if (hasNext) {
            switch (actualEvent.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    break;
                case XMLEvent.CHARACTERS:
                    Characters characters = actualEvent.asCharacters();
                    if (characters.getData().isBlank()) {
                        // Skip blank characters
                        hasNext = tryNextParse();
                    }
                    break;
                case XMLEvent.START_DOCUMENT:
                case XMLEvent.COMMENT:
                    hasNext = tryNextParse();
                    break;
                default:
                    throw new ParsingException("Unexpected event: (%s)"
                            .formatted(actualEvent));
            }
        }
        return hasNext;
    }

    private boolean tryNext() {
        boolean hasNext = reader.hasNext();
        if (hasNext) {
            try {
                actualEvent = reader.nextEvent();
            } catch (XMLStreamException e) {
                throw new ParsingException("Error reading next event from XML stream", e);
            }
            if (actualEvent.getEventType() == XMLEvent.COMMENT) {
                hasNext = tryNext();
            }
        } else {
            actualEvent = null; // Reset actualEvent if no more events are available
        }
        return hasNext;
    }

    private boolean hasNext() {
        return reader.hasNext();
    }

    private void skipBlank() {
        while (hasNext() && actualEvent.isCharacters() && actualEvent.asCharacters().getData().isBlank()) {
            tryNext();
        }
    }
}
