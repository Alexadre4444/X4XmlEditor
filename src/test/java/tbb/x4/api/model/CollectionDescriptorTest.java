package tbb.x4.api.model;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionDescriptorTest {
    @Test
    void constructor_expectedNoException_ifArgumentsAreValid() {
        ElementName name = new ElementName("collection");
        Path filePath = Path.of("/tmp/file.xml");
        ElementPath elementPath = new ElementPath(name);
        TagDescriptor tagDescriptor = new TagDescriptor(name, ValueType.UNKNOWN, "desc");
        assertDoesNotThrow(() -> new CollectionDescriptor(name, filePath, elementPath, tagDescriptor));
    }

    @Test
    void constructor_expectedException_ifNameIsNull() {
        Path filePath = Path.of("/tmp/file.xml");
        ElementName name = new ElementName("collection");
        ElementPath elementPath = new ElementPath(name);
        TagDescriptor tagDescriptor = new TagDescriptor(name, ValueType.UNKNOWN, "desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CollectionDescriptor(null, filePath, elementPath, tagDescriptor));
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @Test
    void constructor_expectedException_ifFilePathIsNull() {
        ElementName name = new ElementName("collection");
        ElementPath elementPath = new ElementPath(name);
        TagDescriptor tagDescriptor = new TagDescriptor(name, ValueType.UNKNOWN, "desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CollectionDescriptor(name, null, elementPath, tagDescriptor));
        assertEquals("Base path cannot be null", exception.getMessage());
    }

    @Test
    void constructor_expectedException_ifItemTagDescriptorIsNull() {
        ElementName name = new ElementName("collection");
        Path filePath = Path.of("/tmp/file.xml");
        ElementPath elementPath = new ElementPath(name);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CollectionDescriptor(name, filePath, elementPath, null));
        assertEquals("Item tag descriptor cannot be null", exception.getMessage());
    }
}
