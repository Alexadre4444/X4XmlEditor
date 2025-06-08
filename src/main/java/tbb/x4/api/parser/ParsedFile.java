package tbb.x4.api.parser;

import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.ElementPath;
import tbb.x4.api.model.TagDescriptor;
import tbb.x4.api.model.ValueType;

import java.util.Collections;
import java.util.List;

public class ParsedFile {
    private final Tag rootTag;

    public ParsedFile(List<Tag> tags) {
        if (tags == null) {
            throw new IllegalArgumentException("Tags cannot be null");
        }
        TagDescriptor rootTagDescriptor = new TagDescriptor(
                new ElementName("root"), ValueType.UNKNOWN, "Root tag", Collections.emptySet(), Collections.emptySet());
        rootTag = new Tag(rootTagDescriptor, null, Collections.emptyList(), tags);
    }

    public List<Tag> find(ElementPath elementPath) {
        if (elementPath == null) {
            throw new IllegalArgumentException("Element path cannot be null");
        }
        return rootTag.find(elementPath);
    }

    public List<Tag> tags() {
        return rootTag.subTags();
    }
}
