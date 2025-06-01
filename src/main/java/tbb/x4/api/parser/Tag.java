package tbb.x4.api.parser;

import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.ElementValue;
import tbb.x4.api.model.TagDescriptor;
import tbb.x4.api.model.ValueType;

import java.util.List;
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
}
