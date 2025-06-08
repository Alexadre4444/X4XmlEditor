package tbb.x4.api.parser;

import tbb.x4.api.model.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static tbb.x4.api.model.ValueType.PARENT;
import static tbb.x4.api.model.ValueType.UNKNOWN;

public record Tag(TagDescriptor descriptor, ElementValue value, List<Attribute> attributes, List<Tag> subTags) {
    public Tag {
        if (descriptor == null) {
            throw new IllegalArgumentException("TagDescriptor cannot be null");
        }
        if (subTags == null) {
            throw new IllegalArgumentException("SubTags cannot be null");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        if (value != null && !descriptor.valueType().equals(value.valueType())) {
            throw new IllegalArgumentException("Value type does not match descriptor type");
        }
        if (PARENT.equals(descriptor.valueType()) && value != null) {
            throw new IllegalArgumentException("Value cannot be set for PARENT type");
        }
        if (!UNKNOWN.equals(descriptor.valueType()) && !PARENT.equals(descriptor.valueType()) && !subTags.isEmpty()) {
            throw new IllegalArgumentException("SubTags cannot be set for non-PARENT or non-UNKNOWN type");
        }
        if (!UNKNOWN.equals(descriptor.valueType())) {
            validateAttributesAndSubTags(descriptor, attributes, subTags);
        }
    }

    private void validateAttributesAndSubTags(TagDescriptor descriptor, List<Attribute> attributes, List<Tag> subTags) {
        attributes.stream()
                .filter(attribute -> !attribute.descriptor().valueType().equals(ValueType.UNKNOWN))
                .filter(attribute -> !descriptor.attributes().contains(attribute.descriptor()))
                .findFirst()
                .ifPresent(invalidAttribute -> {
                    throw new IllegalArgumentException("Attribute %s not found in descriptor %s."
                            .formatted(invalidAttribute.descriptor().name(), descriptor.name()));
                });
        subTags.stream()
                .filter(subTag -> !subTag.descriptor.valueType().equals(ValueType.UNKNOWN))
                .filter(subTag -> !descriptor.subTags().contains(subTag.descriptor()))
                .findFirst()
                .ifPresent(invalidSubTag -> {
                    throw new IllegalArgumentException("SubTag %s not found in descriptor %s."
                            .formatted(invalidSubTag.descriptor().name(), descriptor.name()));
                });
    }

    public Optional<Attribute> attribute(ElementName elementName) {
        return attributes.stream()
                .filter(attribute -> attribute.descriptor().name().equals(elementName))
                .findFirst();
    }

    public Optional<Attribute> attribute(String elementName) {
        return attribute(new ElementName(elementName));
    }

    @Override
    public String toString() {
        return findIdAttribute()
                .map(Attribute::value)
                .map(ElementValue::stringValue)
                .map(Objects::toString)
                .orElse(descriptor.name().toString());
    }

    public Optional<Attribute> findIdAttribute() {
        return attributes.stream()
                .filter(attribute -> attribute.descriptor().equals(descriptor.idTagDescriptor()))
                .findFirst();
    }

    public List<Tag> find(ElementPath path) {
        List<Tag> result = List.of();
        ElementName firstElement = path.path().getFirst();
        List<Tag> validTags = subTags.stream()
                .filter(tag -> tag.descriptor.name().equals(firstElement))
                .toList();
        if (!validTags.isEmpty()) {
            if (path.hasNext()) {
                result = validTags.stream()
                        .flatMap(tag -> tag.find(path.next()).stream())
                        .toList();
            } else {
                result = validTags;
            }
        }
        return result;
    }
}
