package tbb.x4.api.directory;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Override
    public String toString() {
        return name();
    }

    /**
     * Finds a child element by its relative path.
     *
     * @param relativePath the relative path of the child element
     * @return an Optional containing the found XFsElement, or empty if not found
     * @throws IllegalArgumentException if the relative path is null or absolute
     */
    public Optional<XFsElement> find(Path relativePath) {
        XFsElement foundElement = null;
        if (relativePath == null || relativePath.isAbsolute()) {
            throw new IllegalArgumentException("Relative path must be non-null and relative");
        }

        Path absolutePath = path.resolve(relativePath);
        for (XFsElement child : allChildren()) {
            if (child.path().equals(absolutePath)) {
                foundElement = child;
            }
        }
        return Optional.ofNullable(foundElement);
    }

    /**
     * Returns a list of all children, including nested directories.
     *
     * @return a list of all XFsElement children
     */
    public List<XFsElement> allChildren() {
        return Stream.concat(children.stream(),
                        children.stream().filter(XFsElement::isDirectory)
                                .map(XDirectory.class::cast)
                                .flatMap(xDirectory -> xDirectory.allChildren().stream()))
                .toList();
    }
}
