package tbb.x4.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ElementNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"h1", "link-attr", "valid_name123"})
    void constructor_expectedNoException_ifNameIsValid(String validName) {
        assertDoesNotThrow(() -> new ElementName(validName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1invalid", "invalid@name"})
    void constructor_expectedException_ifNameIsInvalid(String invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementName(invalidName));
        assertEquals("Invalid name: " + invalidName + ". Must match XML tag/attribute naming rules.", exception.getMessage());
    }

    @Test
    void constructor_expectedException_ifNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementName(null));
        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    void constructor_expectedException_ifNameIsBlank() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementName("  "));
        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    void toString_expectedLowerCase_ifNameIsUpperCase() {
        String name = "testName";
        ElementName elementName = new ElementName(name);
        assertEquals(name.toLowerCase(), elementName.toString());
    }

    @Test
    void toString_expectedLowerCase_ifNameIsAllUpperCase() {
        String name = "TESTNAME";
        ElementName elementName = new ElementName(name);
        assertEquals(name.toLowerCase(), elementName.toString());
    }
}

