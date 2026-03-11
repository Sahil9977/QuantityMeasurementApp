package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2, Length.LengthUnit.FEET);

        assertEquals(new Length(2.0, Length.LengthUnit.FEET), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2, Length.LengthUnit.INCHES);

        assertEquals(new Length(24.0, Length.LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2, Length.LengthUnit.YARDS);

        assertEquals(0.666, result.convertToBaseUnit() / 36, 0.01);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {

        Length l1 = new Length(1.0, Length.LengthUnit.INCHES);
        Length l2 = new Length(1.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2, Length.LengthUnit.CENTIMETRES);

        assertEquals(5.08, result.convertToBaseUnit() / 0.393701, 0.1);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {

        Length l1 = new Length(2.0, Length.LengthUnit.YARDS);
        Length l2 = new Length(3.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2, Length.LengthUnit.YARDS);

        assertEquals(new Length(3.0, Length.LengthUnit.YARDS), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {

        Length l1 = new Length(2.0, Length.LengthUnit.YARDS);
        Length l2 = new Length(3.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2, Length.LengthUnit.FEET);

        assertEquals(new Length(9.0, Length.LengthUnit.FEET), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        Length result1 = l1.add(l2, Length.LengthUnit.YARDS);
        Length result2 = l2.add(l1, Length.LengthUnit.YARDS);

        assertEquals(result1, result2);
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {

        Length l1 = new Length(5.0, Length.LengthUnit.FEET);
        Length l2 = new Length(0.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2, Length.LengthUnit.YARDS);

        assertEquals(1.667, result.convertToBaseUnit() / 36, 0.01);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {

        Length l1 = new Length(5.0, Length.LengthUnit.FEET);
        Length l2 = new Length(-2.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2, Length.LengthUnit.INCHES);

        assertEquals(new Length(36.0, Length.LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        assertThrows(IllegalArgumentException.class,
                () -> l1.add(l2, null));
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {

        Length l1 = new Length(1000.0, Length.LengthUnit.FEET);
        Length l2 = new Length(500.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2, Length.LengthUnit.INCHES);

        assertEquals(new Length(18000.0, Length.LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {

        Length l1 = new Length(12.0, Length.LengthUnit.INCHES);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2, Length.LengthUnit.YARDS);

        assertEquals(0.667, result.convertToBaseUnit() / 36, 0.01);
    }
}