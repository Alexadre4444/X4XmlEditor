package tbb.x4.imp.model;

import tbb.x4.api.model.*;

import java.util.HashMap;
import java.util.Set;

import static tbb.x4.api.model.ValueType.PARENT;
import static tbb.x4.api.model.ValueType.STRING;

public class DescriptorRepository implements IDescriptorRepository {
    private static final AttributeDescriptor ID = new AttributeDescriptor(new ElementName("id"), STRING, "Unique identifier");
    private static final AttributeDescriptor NAME = new AttributeDescriptor(new ElementName("name"), STRING, "UI Name");
    private static final TagDescriptor WARES_PRODUCTION_METHOD =
            new TagDescriptor(new ElementName("production"), PARENT, "Production", Set.of(ID, NAME), Set.of());
    private static final TagDescriptor WARES_PRODUCTION =
            new TagDescriptor(new ElementName("production"), PARENT, "Wares", Set.of(), Set.of(WARES_PRODUCTION_METHOD));
    private static final TagDescriptor WARES =
            new TagDescriptor(new ElementName("wares"), PARENT, "Wares", Set.of(), Set.of(WARES_PRODUCTION));

    @Override
    public TagDescriptor getTagDescriptor(ElementName tagName) {
        return null;
    }

    @Override
    public AttributeDescriptor getAttributeDescriptor(ElementName attributeName) {
        return null;
    }
}
