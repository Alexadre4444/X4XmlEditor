package tbb.x4.api.directory;

import java.nio.file.Path;
import java.util.List;

public record XDirectory(Path path, List<XFsElement> children) implements XFsElement {
    public XDirectory {
        if (path == null || !path.toFile().isDirectory()) {
            throw new IllegalArgumentException("Path must be a valid directory");
        }
        if (children == null) {
            throw new IllegalArgumentException("Children list cannot be null");
        }
    }

    @Override
    public String name() {
        return path.getFileName().toString();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
