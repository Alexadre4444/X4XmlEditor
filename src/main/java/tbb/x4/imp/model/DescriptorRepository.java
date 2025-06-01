package tbb.x4.imp.model;

import tbb.x4.api.model.AttributeDescriptor;
import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.IDescriptorRepository;
import tbb.x4.api.model.TagDescriptor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tbb.x4.api.model.ValueType.*;

public class DescriptorRepository implements IDescriptorRepository {
    private static final AttributeDescriptor ID = new AttributeDescriptor(new ElementName("id"), STRING, "Unique identifier");
    private static final AttributeDescriptor NAME = new AttributeDescriptor(new ElementName("name"), STRING, "UI Name");
    private static final TagDescriptor WARES_PRODUCTION_METHOD =
            new TagDescriptor(new ElementName("production"), PARENT, "Production", Set.of(ID, NAME), Set.of());
    private static final TagDescriptor WARES_PRODUCTION =
            new TagDescriptor(new ElementName("production"), PARENT, "Wares", Set.of(), Set.of(WARES_PRODUCTION_METHOD));
    private static final TagDescriptor WARES =
            new TagDescriptor(new ElementName("wares"), PARENT, "Wares", Set.of(), Set.of(WARES_PRODUCTION));
    private static final List<TagDescriptor> ROOT_TAG_DESCRIPTORS = List.of(
            WARES
    );

    @Override
    public Optional<TagDescriptor> getTagDescriptor(ElementName tagName) {
        return ROOT_TAG_DESCRIPTORS.stream()
                .filter(tag -> tag.name().equals(tagName))
                .findFirst();
    }

    @Override
    public TagDescriptor getUnknownTagDescriptor(ElementName elementName) {
        return new TagDescriptor(elementName, UNKNOWN, "Unknown tag", Set.of(), Set.of());
    }

    @Override
    public AttributeDescriptor getUnknowAttributeDescriptor(ElementName elementName) {
        return new AttributeDescriptor(elementName, UNKNOWN, "Unknown attribute");
    }
}
