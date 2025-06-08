package tbb.x4.api.parser;

import org.junit.jupiter.api.Test;
import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.TagDescriptor;
import tbb.x4.api.model.ValueType;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParsedFileTest {
    @Test
    void constructor_expectedCreateInstance_ifTagsValid() {
        TagDescriptor descriptor = new TagDescriptor(new ElementName("test"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet());
        Tag tag = new Tag(descriptor, null, Collections.emptyList(), Collections.emptyList());
        assertDoesNotThrow(() -> new ParsedFile(List.of(tag)));
    }

    @Test
    void constructor_expectedException_ifTagsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ParsedFile(null));
        assertEquals("Tags cannot be null", exception.getMessage());
    }

    @Test
    void find_expectedException_ifElementPathNull() {
        TagDescriptor descriptor = new TagDescriptor(new ElementName("test"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet());
        Tag tag = new Tag(descriptor, null, Collections.emptyList(), Collections.emptyList());
        ParsedFile parsedFile = new ParsedFile(List.of(tag));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parsedFile.find(null));
        assertEquals("Element path cannot be null", exception.getMessage());
    }

    @Test
    void tags_expectedReturnSubTags() {
        TagDescriptor descriptor = new TagDescriptor(new ElementName("test"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet());
        Tag tag = new Tag(descriptor, null, Collections.emptyList(), Collections.emptyList());
        ParsedFile parsedFile = new ParsedFile(List.of(tag));
        assertEquals(1, parsedFile.tags().size());
        assertEquals(tag, parsedFile.tags().get(0));
    }
}
