package tbb.x4.api.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElementPathTest {
    @Test
    void constructor_expectedNoException_ifPathIsValid() {
        ElementName e1 = new ElementName("root");
        ElementName e2 = new ElementName("child");
        assertDoesNotThrow(() -> new ElementPath(List.of(e1, e2)));
        assertDoesNotThrow(() -> new ElementPath(e1, e2));
    }

    @Test
    void constructor_expectedException_ifPathIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementPath((List<ElementName>) null));
        assertEquals("Path cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_expectedException_ifPathIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementPath(List.of()));
        assertEquals("Path cannot be null or empty", exception.getMessage());
    }

    @Test
    void next_expectedReturnPathWithoutFirstElement_ifPathHasMultipleElements() {
        ElementName e1 = new ElementName("root");
        ElementName e2 = new ElementName("child");
        ElementName e3 = new ElementName("leaf");
        ElementPath path = new ElementPath(e1, e2, e3);
        ElementPath next = path.next();
        assertEquals(List.of(e2, e3), next.path());
    }

    @Test
    void next_expectedException_ifPathHasSingleElement() {
        ElementName e1 = new ElementName("root");
        ElementPath path = new ElementPath(e1);
        IllegalStateException exception = assertThrows(IllegalStateException.class, path::next);
        assertEquals("Cannot get next ElementPath from a single element path", exception.getMessage());
    }

    @Test
    void hasNext_expectedReturnTrue_ifPathHasMultipleElements() {
        ElementName e1 = new ElementName("root");
        ElementName e2 = new ElementName("child");
        ElementPath path = new ElementPath(e1, e2);
        assertTrue(path.hasNext());
    }

    @Test
    void hasNext_expectedReturnFalse_ifPathHasSingleElement() {
        ElementName e1 = new ElementName("root");
        ElementPath path = new ElementPath(e1);
        assertFalse(path.hasNext());
    }
}
