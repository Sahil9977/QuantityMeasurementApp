package com.apps.quantitymeasurement;

public class Length {

    private double value;
    private LengthUnit unit;

    public Length(double value, LengthUnit unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be a finite number");

        this.value = value;
        this.unit = unit;
    }

    // base unit is in inch coz we have define the value as per inchlike 1 feet = 12 inch 1 yard = 36 inch like that
    public double convertToBaseUnit() {
        return this.unit.convertToBaseUnit(this.value);
    }

    public static double convert(double value, LengthUnit source, LengthUnit target) {

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");

        if (source == null || target == null)
            throw new IllegalArgumentException("Units cannot be null");

        double baseValue = source.convertToBaseUnit(value);

        return target.convertFromBaseUnit(baseValue);
    }

    public Length convertTo(LengthUnit targetUnit) {

        double baseValue = this.unit.convertToBaseUnit(this.value);

        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Length(convertedValue, targetUnit);
    }

    public Length add(Length other) {

        if (other == null)
            throw new IllegalArgumentException("Length cannot be null");

        double value1 = this.unit.convertToBaseUnit(this.value);
        double value2 = other.unit.convertToBaseUnit(other.value);

        double sum = value1 + value2;

        double converted = this.unit.convertFromBaseUnit(sum);

        return new Length(converted, this.unit);
    }

    public Length add(Length other, LengthUnit targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Length cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        double value1 = this.unit.convertToBaseUnit(this.value);
        double value2 = other.unit.convertToBaseUnit(other.value);

        double sum = value1 + value2;

        double converted = targetUnit.convertFromBaseUnit(sum);

        return new Length(converted, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Length other = (Length) obj;

        return Math.abs(
                this.unit.convertToBaseUnit(this.value) -
                other.unit.convertToBaseUnit(other.value)
        ) <= 0.001;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.unit.convertToBaseUnit(this.value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}