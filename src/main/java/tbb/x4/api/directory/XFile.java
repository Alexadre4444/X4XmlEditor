package tbb.x4.api.directory;

import java.nio.file.Path;

public record XFile(Path path) implements XFsElement {
    public XFile {
        if (path == null || path.toFile().isDirectory()) {
            throw new IllegalArgumentException("Path must be a valid file");
        }
    }

    @Override
    public String name() {
        return path.getFileName().toString();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public String toString() {
        return name();
    }
}
