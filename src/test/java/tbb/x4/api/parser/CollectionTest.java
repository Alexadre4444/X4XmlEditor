package tbb.x4.api.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import tbb.x4.api.model.*;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionTest {
    @Test
    void constructor_expectedCreateInstance_ifValidArguments() {
        TagDescriptor tagDescriptor = new TagDescriptor(
                new ElementName("tag"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet()
        );
        TagDescriptor itemTagDescriptor = new TagDescriptor(
                new ElementName("item"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet()
        );
        CollectionDescriptor descriptor = new CollectionDescriptor(
                new ElementName("collection"), Path.of("/base/path"), new ElementPath("tag"), itemTagDescriptor
        );
        Tag tag = new Tag(tagDescriptor, null, Collections.emptyList(), Collections.emptyList());
        List<Tag> elements = List.of(tag);
        Collection collection = new Collection(descriptor, elements);
        assertNotNull(collection);
        assertEquals(descriptor, collection.descriptor());
        assertEquals(elements, collection.elements());
    }

    @ParameterizedTest
    @NullSource
    void constructor_expectedException_ifDescriptorIsNull(CollectionDescriptor invalidDescriptor) {
        List<Tag> elements = Collections.emptyList();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Collection(invalidDescriptor, elements));
        assertEquals("CollectionDescriptor cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void constructor_expectedException_ifElementsIsNull(List<Tag> invalidElements) {
        TagDescriptor tagDescriptor = new TagDescriptor(
                new ElementName("tag"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet()
        );
        TagDescriptor itemTagDescriptor = new TagDescriptor(
                new ElementName("item"), ValueType.STRING, "desc", Collections.emptySet(), Collections.emptySet()
        );
        CollectionDescriptor descriptor = new CollectionDescriptor(
                new ElementName("collection"), Path.of("/base/path"), new ElementPath("tag"), itemTagDescriptor
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Collection(descriptor, invalidElements));
        assertEquals("Elements cannot be null", exception.getMessage());
    }
}
