package tbb.x4.api.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ElementValueTest {

    @ParameterizedTest
    @ValueSource(strings = {"value1", "value2", "validValue", ""})
    void constructor_expectedNoException_ifValueIsValid(String validValue) {
        assertDoesNotThrow(() -> new ElementValue(validValue, ValueType.STRING));
    }

    @ParameterizedTest
    @NullSource
    void constructor_expectedException_ifValueIsNull(String invalidValue) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(invalidValue, ValueType.STRING));
        assertEquals("Value cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"value1", "value2"})
    void constructor_expectedException_ifValueTypeIsNull(String validValue) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(validValue, null));
        assertEquals("ValueType cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.45", "0", "-98765"})
    void constructor_expectedNoException_ifNumberValueIsValid(String validNumber) {
        assertDoesNotThrow(() -> new ElementValue(validNumber, ValueType.NUMBER));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123.45.67", "", "NaN"})
    void constructor_expectedException_ifNumberValueIsInvalid(String invalidNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(invalidNumber, ValueType.NUMBER));
        assertEquals("Value is not a valid number", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "example", "123"})
    void stringValue_expectedReturnInput_ifTypeIsString(String input) {
        ElementValue elementValue = new ElementValue(input, ValueType.STRING);
        assertEquals(input, elementValue.stringValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.45", "0", "-98765"})
    void bigDecimalValue_expectedReturnBigDecimal_ifTypeIsNumber(String validNumber) {
        ElementValue elementValue = new ElementValue(validNumber, ValueType.NUMBER);
        assertEquals(new BigDecimal(validNumber), elementValue.bigDecimalValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "example", "123"})
    void bigDecimalValue_expectedException_ifTypeIsNotNumber(String input) {
        ElementValue elementValue = new ElementValue(input, ValueType.STRING);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, elementValue::bigDecimalValue);
        assertEquals("Value is not a number", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyValue", "123", ""})
    void constructor_expectedException_ifValueTypeIsParent(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ElementValue(input, ValueType.PARENT));
        assertEquals("ValueType cannot be PARENT", exception.getMessage());
    }
}

