package tbb.x4.api.parser;

import tbb.x4.api.model.AttributeDescriptor;
import tbb.x4.api.model.ElementValue;

public record Attribute(AttributeDescriptor descriptor, ElementValue value) {
    public Attribute {
        if (descriptor == null) {
            throw new IllegalArgumentException("AttributeDescriptor cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("ElementValue cannot be null");
        }
        if (!descriptor.valueType().equals(value.valueType())) {
            throw new IllegalArgumentException("Value type does not match descriptor type");
        }
    }

    public Attribute(AttributeDescriptor descriptor, String value) {
        this(descriptor, new ElementValue(value, descriptor.valueType()));
    }
}
