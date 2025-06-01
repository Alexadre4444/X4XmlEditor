package tbb.x4.api.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeDescriptorTest {

    @ParameterizedTest
    @ValueSource(strings = {"name1", "name2", "validName"})
    void validAttributeDescriptorShouldCreateInstance(String validName) {
        assertDoesNotThrow(() -> new AttributeDescriptor(new ElementName(validName), ValueType.STRING, "Valid description"));
    }

    @ParameterizedTest
    @NullSource
    void nullNameShouldThrowException(ElementName invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new AttributeDescriptor(invalidName, ValueType.STRING, "Valid description"));
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void nullValueTypeShouldThrowException(ValueType invalidValueType) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new AttributeDescriptor(new ElementName("ValidName"), invalidValueType, "Valid description"));
        assertEquals("ValueType cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void nullDescriptionShouldThrowException(String invalidDescription) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new AttributeDescriptor(new ElementName("ValidName"), ValueType.STRING, invalidDescription));
        assertEquals("Description cannot be null", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource(strings = {"PARENT"})
    void invalidValueTypeShouldThrowException(String invalidValueType) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new AttributeDescriptor(new ElementName("ValidName"), ValueType.valueOf(invalidValueType), "Valid description"));
        assertEquals("ValueType cannot be PARENT", exception.getMessage());
    }
}
