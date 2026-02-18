package quantity_measurement_uc2;



import org.junit.jupiter.api.Test;

import quantity_measurement_uc1.QuantityMeasurementApp;
import quantity_measurement_uc2.QuantityMeasurementApp.Inches;

import static org.junit.jupiter.api.Assertions.*;



public class QuantityMeasurementAppTest {


	


    @Test
    public void testFeetEquality_SameValue() {
    	QuantityMeasurementApp.Feet f1 = new 	QuantityMeasurementApp.Feet(1.0);
    	QuantityMeasurementApp.Feet f2 = new 	QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f2));
    }

    @Test
    public void testFeetEquality_DifferentValue() {
        QuantityMeasurementApp.Feet f1 =
                new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 =
                new QuantityMeasurementApp.Feet(2.0);

        assertFalse(f1.equals(f2));
    }

    @Test
    public void testFeetEquality_NullComparison() {
    	QuantityMeasurementApp.Feet f1 = new 	QuantityMeasurementApp.Feet(1.0);

        assertFalse(f1.equals(null));
    }

    @Test
    public void testFeetEquality_DifferentClass() {
    	QuantityMeasurementApp.Feet f1 = new 	QuantityMeasurementApp.Feet(1.0);

        assertFalse(f1.equals("1.0"));
    }

    @Test
    public void testFeetEquality_SameReference() {
    	QuantityMeasurementApp.Feet f1 = new 	QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f1));
    }

    @Test
    public void testFeetEquality_NonNumericInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.Feet(Double.NaN));
    }


    @Test
    public void testInchesEquality_SameValue() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);

        assertTrue(i1.equals(i2));
    }

    @Test
    public void testInchesEquality_DifferentValue() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(2.0);

        assertFalse(i1.equals(i2));
    }

    @Test
    public void testInchesEquality_NullComparison() {
        Inches i1 = new Inches(1.0);

        assertFalse(i1.equals(null));
    }

    @Test
    public void testInchesEquality_DifferentClass() {
        Inches i1 = new Inches(1.0);

        assertFalse(i1.equals(new QuantityMeasurementApp.Feet(1.0)));
    }

    @Test
    public void testInchesEquality_SameReference() {
        Inches i1 = new Inches(1.0);

        assertTrue(i1.equals(i1));
    }

    @Test
    public void testInchesEquality_NonNumericInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new Inches(Double.NaN));
    }
}
