package tbb.x4.api.directory;

import java.io.File;

public interface IXDirectoryService {
    /**
     * Checks if the given file is a valid X4 directory.
     *
     * @param file the file to check
     * @return true if the file is a valid X4 directory, false otherwise
     */
    boolean isXBaseDirectory(File file);

    /**
     * Parses the given file as an XBase directory.
     *
     * @param file the file to parse
     * @return an XDirectory object representing the parsed directory
     * @throws IllegalArgumentException if the file is not a valid XBase directory
     */
    XDirectory parseXBaseDirectory(File file);
}
