package com.app.quantitymeasurement.model;

public enum OperationType {
    COMPARE,
    CONVERT,
    ADD,
    SUBTRACT,
    DIVIDE;

    public static OperationType fromString(String value) {
        try {
            return OperationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid operation type: " + value +
                    ". Valid values: COMPARE, CONVERT, ADD, SUBTRACT, DIVIDE");
        }
    }
}
