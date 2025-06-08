package tbb.x4.api.model;

import java.nio.file.Path;

public record CollectionDescriptor(ElementName name, Path filePath, ElementPath elementPath,
                                   TagDescriptor itemTagDescriptor) {

    public CollectionDescriptor {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (filePath == null) {
            throw new IllegalArgumentException("Base path cannot be null");
        }
        if (itemTagDescriptor == null) {
            throw new IllegalArgumentException("Item tag descriptor cannot be null");
        }
    }
}
