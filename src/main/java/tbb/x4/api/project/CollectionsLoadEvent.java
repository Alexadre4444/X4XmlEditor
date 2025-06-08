package tbb.x4.api.project;

import tbb.x4.api.parser.Collection;

import java.util.List;

/**
 * Event triggered when collections are loaded in a project.
 */
public class CollectionsLoadEvent {
    private final List<Collection> collections;

    public CollectionsLoadEvent(List<Collection> collections) {
        this.collections = collections;
    }

    /**
     * Returns the list of collections in the loaded project.
     *
     * @return the list of collections
     */
    public List<Collection> collections() {
        return collections;
    }
}
