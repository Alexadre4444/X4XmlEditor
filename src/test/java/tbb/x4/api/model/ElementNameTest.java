package tbb.x4.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ElementNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"h1", "link-attr", "valid_name123"})
    void validNameShouldCreateElementName(String validName) {
        assertDoesNotThrow(() -> new ElementName(validName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1invalid", "invalid@name"})
    void invalidNameShouldThrowException(String invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementName(invalidName));
        assertEquals("Invalid name: " + invalidName + ". Must match XML tag/attribute naming rules.", exception.getMessage());
    }

    @Test
    void nullNameShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementName(null));
        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    void blankNameShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementName("  "));
        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    void toStringShouldReturnName() {
        String name = "testName";
        ElementName elementName = new ElementName(name);
        assertEquals(name.toLowerCase(), elementName.toString());
    }

    @Test
    void nameShoudlBeLowerCase() {
        String name = "TESTNAME";
        ElementName elementName = new ElementName(name);
        assertEquals(name.toLowerCase(), elementName.toString());
    }
}