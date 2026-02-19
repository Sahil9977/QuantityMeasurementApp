package quantity_measurement_uc3;


import java.util.Objects;

public class Length {

    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        this.value = value;
        this.unit = unit;
    }

    // Convert to base unit (inches)
    private double toBaseUnit() {
        return this.value * this.unit.getConversionFactor();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;  

        if (obj == null || getClass() != obj.getClass())
            return false;

        Length other = (Length) obj;

        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toBaseUnit());
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
