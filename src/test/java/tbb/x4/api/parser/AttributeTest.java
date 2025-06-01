package tbb.x4.api.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import tbb.x4.api.model.AttributeDescriptor;
import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.ElementValue;
import tbb.x4.api.model.ValueType;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {
    @ParameterizedTest
    @CsvSource({
            "descriptor1, value1, STRING",
            "descriptor2, 1.1, NUMBER"
    })
    void validAttributeShouldCreateInstance(String descriptorName, String value, ValueType valueType) {
        AttributeDescriptor descriptor = new AttributeDescriptor(new ElementName(descriptorName), valueType, "description");
        ElementValue elementValue = new ElementValue(value, valueType);

        Attribute attribute = new Attribute(descriptor, elementValue);

        assertNotNull(attribute);
        assertEquals(descriptor, attribute.descriptor());
        assertEquals(elementValue, attribute.value());
    }

    @ParameterizedTest
    @NullSource
    void nullDescriptorShouldThrowException(AttributeDescriptor descriptor) {
        ElementValue value = new ElementValue("value", ValueType.STRING);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Attribute(descriptor, value));
        assertEquals("AttributeDescriptor cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void nullValueShouldThrowException(ElementValue value) {
        AttributeDescriptor descriptor = new AttributeDescriptor(new ElementName("descriptor"), ValueType.STRING, "description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Attribute(descriptor, value));
        assertEquals("ElementValue cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "descriptor1, STRING, NUMBER",
            "descriptor2, NUMBER, STRING"
    })
    void mismatchedValueTypeShouldThrowException(String descriptorName, ValueType descriptorValueType, ValueType valueType) {
        AttributeDescriptor descriptor = new AttributeDescriptor(new ElementName(descriptorName), descriptorValueType, "description");
        ElementValue value = new ElementValue("1", valueType);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Attribute(descriptor, value));
        assertEquals("Value type does not match descriptor type", exception.getMessage());
    }

}
