package tbb.x4.api.model;

import java.util.Set;

public record TagDescriptor(ElementName name, ValueType valueType, String description,
                            Set<AttributeDescriptor> attributes, Set<TagDescriptor> subTags) {
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
        if(subTags == null) {
            throw new IllegalArgumentException("SubTags cannot be null");
        }
        if(ValueType.PARENT.equals(valueType) && subTags.isEmpty()) {
            throw new IllegalArgumentException("SubTags cannot be empty if ValueType is PARENT");
        }
        if (!ValueType.PARENT.equals(valueType) && !subTags.isEmpty()) {
            throw new IllegalArgumentException("SubTags must be empty if ValueType is not PARENT");
        }
    }
}
