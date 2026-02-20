package quantity_measurement_uc4;


import java.util.Objects;

public class Length {

    private final double value;
    private final LengthUnit unit;

    public enum LengthUnit {

        FEET(12.0),              // 1 foot = 12 inches
        INCHES(1.0),             // base unit
        YARDS(36.0),             // 1 yard = 36 inches
        CENTIMETERS(0.393701);   // 1 cm = 0.393701 inches

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    public Length(double value, LengthUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        this.value = value;
        this.unit = unit;
    }

    // Convert everything to BASE UNIT (INCHES)
    private double convertToBaseUnit() {
        return this.value * unit.getConversionFactor();
    }

    public boolean compare(Length other) {
        return Double.compare(this.convertToBaseUnit(),
                other.convertToBaseUnit()) == 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Length other = (Length) obj;

        return Double.compare(this.convertToBaseUnit(),
                other.convertToBaseUnit()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(convertToBaseUnit());
    }
}