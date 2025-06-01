package tbb.x4.api.model;

import java.math.BigDecimal;

public record ElementValue(String value, ValueType valueType) {
    public ElementValue {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (valueType == null) {
            throw new IllegalArgumentException("ValueType cannot be null");
        }
        if(valueType == ValueType.PARENT) {
            throw new IllegalArgumentException("ValueType cannot be PARENT");
        }
        if(valueType == ValueType.NUMBER && !isNumeric(value)) {
            throw new IllegalArgumentException("Value is not a valid number");
        }
    }

    private boolean isNumeric(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String stringValue() {
        return value;
    }

    public BigDecimal bigDecimalValue() {
        if (valueType != ValueType.NUMBER) {
            throw new IllegalArgumentException("Value is not a number");
        }
        return new BigDecimal(value);
    }
}
