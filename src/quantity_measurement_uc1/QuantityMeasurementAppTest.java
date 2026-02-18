package quantity_measurement_uc1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testEquality_SameValue() {
        QuantityMeasurementApp.Feet f1 =
                new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 =
                new QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f2),
                "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void testEquality_DifferentValue() {
        QuantityMeasurementApp.Feet f1 =
                new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 =
                new QuantityMeasurementApp.Feet(2.0);

        assertFalse(f1.equals(f2),"1.0 ft should not be equal to 2.0 ft");
    }

    @Test
    void testEquality_NullComparison() {
        QuantityMeasurementApp.Feet f1 =
                new QuantityMeasurementApp.Feet(1.0);

        assertFalse(f1.equals(null),
                "Feet should not be equal to null");
    }

    @Test
    void testEquality_NonNumericInput() {
        QuantityMeasurementApp.Feet f1 =
                new QuantityMeasurementApp.Feet(1.0);

        assertFalse(f1.equals("1.0"),
                "Feet should not be equal to non-numeric input");
    }

    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.Feet f1 =
                new QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f1),
                "Object must be equal to itself (reflexive property)");
    }
}
