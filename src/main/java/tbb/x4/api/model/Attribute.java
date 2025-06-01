package tbb.x4.api.model;

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
}
