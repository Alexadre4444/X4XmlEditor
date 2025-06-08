package tbb.x4.api.parser;

import tbb.x4.api.model.CollectionDescriptor;

import java.util.List;

public record Collection(CollectionDescriptor descriptor, List<Tag> elements) {
    public Collection {
        if (descriptor == null) {
            throw new IllegalArgumentException("CollectionDescriptor cannot be null");
        }
        if (elements == null) {
            throw new IllegalArgumentException("Elements cannot be null");
        }
    }

    @Override
    public String toString() {
        return descriptor.name().toString();
    }
}
