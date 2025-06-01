package tbb.x4.api.model;

import java.util.Set;

import static tbb.x4.api.model.ValueType.PARENT;

public record Tag(TagDescriptor descriptor, ElementValue value, Set<Attribute> attributes, Set<Tag> subTags) {
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
        if(PARENT.equals(descriptor.valueType()) && value != null) {
            throw new IllegalArgumentException("Value cannot be set for PARENT type");
        }
        if (!PARENT.equals(descriptor.valueType()) && value == null) {
            throw new IllegalArgumentException("Value cannot be null for non-PARENT type");
        }
        if (!PARENT.equals(descriptor.valueType()) && !subTags.isEmpty()) {
            throw new IllegalArgumentException("SubTags cannot be set for non-PARENT type");
        }
        attributes.stream()
                .filter(attribute -> !descriptor.attributes().contains(attribute.descriptor()))
                .findFirst()
                .ifPresent(invalidAttribute -> {
                    throw new IllegalArgumentException("Attribute %s not found in descriptor %s."
                            .formatted(invalidAttribute.descriptor().name(), descriptor.name()));
                });
        subTags.stream()
                .filter(subTag -> !descriptor.subTags().contains(subTag.descriptor()))
                .findFirst()
                .ifPresent(invalidSubTag -> {
                    throw new IllegalArgumentException("SubTag %s not found in descriptor %s."
                            .formatted(invalidSubTag.descriptor().name(), descriptor.name()));
                });
    }
}
