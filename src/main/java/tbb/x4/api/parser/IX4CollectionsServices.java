package tbb.x4.api.parser;

import tbb.x4.api.directory.XDirectory;

import java.util.List;

public interface IX4CollectionsServices {
    /**
     * Retrieves a list of collections from the specified directory.
     *
     * @param directory the directory from which to retrieve collections
     * @return a list of collections found in the directory
     */
    List<Collection> getCollections(XDirectory directory);
}
