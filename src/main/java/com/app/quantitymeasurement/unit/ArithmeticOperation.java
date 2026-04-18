package com.app.quantitymeasurement.unit;

public enum ArithmeticOperation {

    ADD {
        @Override public double apply(double a, double b) { return a + b; }
    },
    SUBTRACT {
        @Override public double apply(double a, double b) { return a - b; }
    },
    DIVIDE {
        @Override public double apply(double a, double b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
    };

    public abstract double apply(double a, double b);
}
