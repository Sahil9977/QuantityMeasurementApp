package com.apps.quantitymeasurement.core;

/**
 * UC15 Change: This class is NO LONGER called directly from the app entry
 * point or controller.  It is now an internal domain helper used exclusively
 * by QuantityMeasurementServiceImpl to perform calculations.
 *
 * WHY: The service layer is responsible for all business logic.  Quantity<U>
 * encapsulates the math; the service wires DTO → Quantity → result → DTO.
 */
public class Quantity<U extends IMeasurable> {

    private double value;
    private U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be a finite number");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public U getUnit()       { return unit;  }

    public double convertToBaseUnit() { return this.unit.convertToBaseUnit(this.value); }

    public Quantity<U> convertTo(U targetUnit) {
        double base = this.unit.convertToBaseUnit(this.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
    }

    public void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetRequired) {
        if (other == null)                            throw new IllegalArgumentException("Quantity cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) throw new IllegalArgumentException("Both quantities must be same measurement category");
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) throw new IllegalArgumentException("Values must be finite");
        if (targetRequired && targetUnit == null)     throw new IllegalArgumentException("Target unit cannot be null");
    }

    public double performBaseArithmetic(Quantity<U> other, ArithmeticOperation op) {
        return op.apply(this.convertToBaseUnit(), other.convertToBaseUnit());
    }

    public Quantity<U> add(Quantity<U> other) {
        this.unit.validateOperationSupport(ArithmeticOperation.ADD.name());
        validateArithmeticOperands(other, null, false);
        return new Quantity<>(this.unit.convertFromBaseUnit(performBaseArithmetic(other, ArithmeticOperation.ADD)), this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        this.unit.validateOperationSupport(ArithmeticOperation.ADD.name());
        validateArithmeticOperands(other, targetUnit, true);
        return new Quantity<>(targetUnit.convertFromBaseUnit(performBaseArithmetic(other, ArithmeticOperation.ADD)), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        this.unit.validateOperationSupport(ArithmeticOperation.SUBTRACT.name());
        validateArithmeticOperands(other, null, false);
        return new Quantity<>(this.unit.convertFromBaseUnit(performBaseArithmetic(other, ArithmeticOperation.SUBTRACT)), this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        this.unit.validateOperationSupport(ArithmeticOperation.SUBTRACT.name());
        validateArithmeticOperands(other, targetUnit, true);
        return new Quantity<>(targetUnit.convertFromBaseUnit(performBaseArithmetic(other, ArithmeticOperation.SUBTRACT)), targetUnit);
    }

    public double divide(Quantity<U> other) {
        this.unit.validateOperationSupport(ArithmeticOperation.DIVIDE.name());
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quantity<U> other = (Quantity<U>) obj;
        return Math.abs(this.unit.convertToBaseUnit(this.value) - other.unit.convertToBaseUnit(other.value)) <= 0.001;
    }

    @Override public int hashCode() { return Double.hashCode(this.unit.convertToBaseUnit(this.value)); }

    @Override public String toString() { return "Quantity(" + value + ", " + unit + ")"; }
}