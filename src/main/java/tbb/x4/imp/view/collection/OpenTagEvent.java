package tbb.x4.imp.view.collection;

import tbb.x4.api.parser.Tag;

public class OpenTagEvent {
    private final Tag tag;

    public OpenTagEvent(Tag tag) {
        this.tag = tag;
    }

    public Tag tag() {
        return tag;
    }
}
