package tbb.x4.api.model;

import java.util.List;
import java.util.stream.Stream;

public record ElementPath(List<ElementName> path) {

    public ElementPath {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
    }

    public ElementPath(ElementName... elements) {
        this(List.of(elements));
    }

    public ElementPath(String... elements) {
        this(Stream.of(elements).map(ElementName::new).toList());
    }

    /**
     * Returns the next element in the path.
     * For example, if the path is [A, B, C],
     * this method will return a new ElementPath containing [B, C].
     *
     * @return the last {@link ElementName} in the path
     * @throws IllegalStateException if the path contains only one element
     */
    public ElementPath next() {
        if (path.size() == 1) {
            throw new IllegalStateException("Cannot get next ElementPath from a single element path");
        }
        return new ElementPath(path.subList(1, path.size()));
    }

    /**
     * Returns true if the path has more than one element.
     * This indicates that there are still elements to traverse in the path.
     *
     * @return true if there are more elements in the path, false otherwise
     */
    public boolean hasNext() {
        return path.size() > 1;
    }
}
