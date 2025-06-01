package tbb.x4.api.model;

public record AttributeDescriptor(ElementName name, ValueType valueType, String description) {
    public AttributeDescriptor {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (valueType == null) {
            throw new IllegalArgumentException("ValueType cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if(valueType == ValueType.PARENT) {
            throw new IllegalArgumentException("ValueType cannot be PARENT");
        }
    }
}
