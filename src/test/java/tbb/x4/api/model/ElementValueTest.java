package tbb.x4.api.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ElementValueTest {

    @ParameterizedTest
    @ValueSource(strings = {"value1", "value2", "validValue", ""})
    void validElementValueShouldCreateInstance(String validValue) {
        assertDoesNotThrow(() -> new ElementValue(validValue, ValueType.STRING));
    }

    @ParameterizedTest
    @NullSource
    void nullValueShouldThrowException(String invalidValue) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(invalidValue, ValueType.STRING));
        assertEquals("Value cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"value1", "value2"})
    void nullValueTypeShouldThrowException(String validValue) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(validValue, null));
        assertEquals("ValueType cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.45", "0", "-98765"})
    void validNumberValueShouldCreateInstance(String validNumber) {
        assertDoesNotThrow(() -> new ElementValue(validNumber, ValueType.NUMBER));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123.45.67", "", "NaN"})
    void invalidNumberValueShouldThrowException(String invalidNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(invalidNumber, ValueType.NUMBER));
        assertEquals("Value is not a valid number", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "example", "123"})
    void stringValueShouldReturnCorrectValue(String input) {
        ElementValue elementValue = new ElementValue(input, ValueType.STRING);
        assertEquals(input, elementValue.stringValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.45", "0", "-98765"})
    void bigDecimalValueShouldReturnCorrectValue(String validNumber) {
        ElementValue elementValue = new ElementValue(validNumber, ValueType.NUMBER);
        assertEquals(new BigDecimal(validNumber), elementValue.bigDecimalValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "example", "123"})
    void bigDecimalValueShouldThrowExceptionForNonNumberType(String input) {
        ElementValue elementValue = new ElementValue(input, ValueType.STRING);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, elementValue::bigDecimalValue);
        assertEquals("Value is not a number", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyValue", "123", ""})
    void parentValueTypeShouldThrowException(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(input, ValueType.PARENT));
        assertEquals("ValueType cannot be PARENT", exception.getMessage());
    }
}