package com.apps.quantitymeasurement;

public class Weight {

    private final double value;
    private final WeightUnit unit;

    public Weight(double value, WeightUnit unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");

        this.value = value;
        this.unit = unit;
    }

    public double convertToBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public Weight convertTo(WeightUnit targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);

        return new Weight(converted, targetUnit);
    }

    public Weight add(Weight other) {

        if (other == null)
            throw new IllegalArgumentException("Weight cannot be null");

        double sum = this.convertToBaseUnit() + other.convertToBaseUnit();

        double result = unit.convertFromBaseUnit(sum);

        return new Weight(result, unit);
    }

    public Weight add(Weight other, WeightUnit targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Weight cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        double sum = this.convertToBaseUnit() + other.convertToBaseUnit();

        double result = targetUnit.convertFromBaseUnit(sum);

        return new Weight(result, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Weight other = (Weight) obj;

        return Math.abs(
                this.convertToBaseUnit() - other.convertToBaseUnit()
        ) <= 0.000001;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(convertToBaseUnit());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}