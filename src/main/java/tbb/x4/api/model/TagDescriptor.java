package tbb.x4.api.model;

import java.util.Set;

public record TagDescriptor(ElementName name, ValueType valueType, String description,
                            AttributeDescriptor idTagDescriptor,
                            Set<AttributeDescriptor> attributes, Set<TagDescriptor> subTags) {
    public TagDescriptor(ElementName name, ValueType valueType, String description, Set<AttributeDescriptor> attributes, Set<TagDescriptor> subTags) {
        this(name, valueType, description, null, attributes, subTags);
    }

    public TagDescriptor(ElementName name, ValueType valueType, String description, AttributeDescriptor idTagDescriptor, Set<AttributeDescriptor> attributes) {
        this(name, valueType, description, idTagDescriptor, attributes, Set.of());
    }

    public TagDescriptor(ElementName name, ValueType valueType, String description) {
        this(name, valueType, description, null, Set.of(), Set.of());
    }

    public TagDescriptor {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (valueType == null) {
            throw new IllegalArgumentException("ValueType cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        if (subTags == null) {
            throw new IllegalArgumentException("SubTags cannot be null");
        }
        if (ValueType.PARENT.equals(valueType) && subTags.isEmpty()) {
            throw new IllegalArgumentException("SubTags cannot be empty if ValueType is PARENT");
        }
        if (!ValueType.PARENT.equals(valueType) && !ValueType.UNKNOWN.equals(valueType) && !subTags.isEmpty()) {
            throw new IllegalArgumentException("SubTags must be empty if ValueType is not PARENT");
        }
        if (idTagDescriptor != null) {
            if (!attributes.contains(idTagDescriptor)) {
                throw new IllegalArgumentException("idTagDescriptor must be part of attributes");
            }
        }
    }
}
