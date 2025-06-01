package tbb.x4.api.directory;

import java.nio.file.Path;

public sealed interface XFsElement permits XDirectory {
    /**
     * Returns the name of the element.
     *
     * @return the name of the element
     */
    String name();

    /**
     * Returns the path of the element.
     *
     * @return the path of the element
     */
    Path path();

    /**
     * Checks if the element is a directory.
     *
     * @return true if the element is a directory, false otherwise
     */
    boolean isDirectory();
}
