package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testConversion_FeetToInches() {

        double result = Length.convert(1.0,
                Length.LengthUnit.FEET,
                Length.LengthUnit.INCHES);

        assertEquals(12.0, result, 0.001);
    }

    @Test
    void testConversion_InchesToFeet() {

        double result = Length.convert(24.0,
                Length.LengthUnit.INCHES,
                Length.LengthUnit.FEET);

        assertEquals(2.0, result, 0.001);
    }

    @Test
    void testConversion_YardsToInches() {

        double result = Length.convert(1.0,
                Length.LengthUnit.YARDS,
                Length.LengthUnit.INCHES);

        assertEquals(36.0, result, 0.001);
    }

    @Test
    void testConversion_ZeroValue() {

        double result = Length.convert(0.0,
                Length.LengthUnit.FEET,
                Length.LengthUnit.INCHES);

        assertEquals(0.0, result);
    }

    // -------- UC6 Tests --------

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(2.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2);

        assertEquals(new Length(3.0, Length.LengthUnit.FEET), result);
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {

        Length l1 = new Length(1.0, Length.LengthUnit.FEET);
        Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2);

        assertEquals(new Length(2.0, Length.LengthUnit.FEET), result);
    }

    @Test
    void testAddition_CrossUnit_InchesPlusFeet() {

        Length l1 = new Length(12.0, Length.LengthUnit.INCHES);
        Length l2 = new Length(1.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2);

        assertEquals(new Length(24.0, Length.LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_YardPlusFeet() {

        Length l1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length l2 = new Length(3.0, Length.LengthUnit.FEET);

        Length result = l1.add(l2);

        assertEquals(new Length(2.0, Length.LengthUnit.YARDS), result);
    }

    @Test
    void testAddition_WithZero() {

        Length l1 = new Length(5.0, Length.LengthUnit.FEET);
        Length l2 = new Length(0.0, Length.LengthUnit.INCHES);

        Length result = l1.add(l2);

        assertEquals(new Length(5.0, Length.LengthUnit.FEET), result);
    }

}