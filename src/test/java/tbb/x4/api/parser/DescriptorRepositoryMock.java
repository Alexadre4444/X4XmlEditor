package tbb.x4.api.parser;

import tbb.x4.api.model.AttributeDescriptor;
import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.IDescriptorRepository;
import tbb.x4.api.model.TagDescriptor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tbb.x4.api.model.ValueType.*;

class DescriptorRepositoryMock implements IDescriptorRepository {
    public static final AttributeDescriptor ID = new AttributeDescriptor(new ElementName("id"), STRING, "Unique identifier");
    public static final AttributeDescriptor NAME = new AttributeDescriptor(new ElementName("name"), STRING, "UI Name");
    public static final AttributeDescriptor RACE = new AttributeDescriptor(new ElementName("race"), STRING, "Race id");
    public static final TagDescriptor WARES_PRODUCTION_METHOD_DEFAULT =
            new TagDescriptor(new ElementName("default"), STRING, "Default", Set.of(RACE), Set.of());
    public static final TagDescriptor WARES_PRODUCTION_METHOD =
            new TagDescriptor(new ElementName("method"), PARENT, "Method", Set.of(ID, NAME), Set.of(WARES_PRODUCTION_METHOD_DEFAULT));
    public static final TagDescriptor WARES_PRODUCTION =
            new TagDescriptor(new ElementName("production"), PARENT, "Production", Set.of(), Set.of(WARES_PRODUCTION_METHOD));
    public static final TagDescriptor WARES =
            new TagDescriptor(new ElementName("wares"), PARENT, "Wares", Set.of(), Set.of(WARES_PRODUCTION));
    public static final List<TagDescriptor> ROOT_TAG_DESCRIPTORS = List.of(
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
