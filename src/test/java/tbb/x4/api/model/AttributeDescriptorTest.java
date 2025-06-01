package tbb.x4.api.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeDescriptorTest {

    @ParameterizedTest
    @ValueSource(strings = {"name1", "name2", "validName"})
    void constructor_expectedNoException_ifValidAttributeDescriptor(String validName) {
        assertDoesNotThrow(() -> new AttributeDescriptor(new ElementName(validName), ValueType.STRING, "Valid description"));
    }

    @ParameterizedTest
    @NullSource
    void constructor_expectedException_ifNameIsNull(ElementName invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new AttributeDescriptor(invalidName, ValueType.STRING, "Valid description"));
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void constructor_expectedException_ifValueTypeIsNull(ValueType invalidValueType) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new AttributeDescriptor(new ElementName("ValidName"), invalidValueType, "Valid description"));
        assertEquals("ValueType cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void constructor_expectedException_ifDescriptionIsNull(String invalidDescription) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new AttributeDescriptor(new ElementName("ValidName"), ValueType.STRING, invalidDescription));
        assertEquals("Description cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"PARENT"})
    void constructor_expectedException_ifValueTypeIsParent(String invalidValueType) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new AttributeDescriptor(new ElementName("ValidName"), ValueType.valueOf(invalidValueType), "Valid description"));
        assertEquals("ValueType cannot be PARENT", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"UNKNOWN"})
    void constructor_expectedNoException_ifValueTypeIsUnknown(String unknownValueType) {
        assertDoesNotThrow(() -> new AttributeDescriptor(new ElementName("ValidName"), ValueType.valueOf(unknownValueType), "Valid description"));
    }
}
