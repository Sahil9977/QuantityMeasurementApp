package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.Length.LengthUnit;

import quantity_measurement_uc4.Length;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testEquality_YardToYard_SameValue() {
        assertEquals(
                new Length(1.0, Length.LengthUnit.YARDS),
                new Length(1.0, Length.LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        assertNotEquals(
                new Length(1.0, Length.LengthUnit.YARDS),
                new Length(2.0, Length.LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToFeet() {
        assertEquals(
                new Length(1.0, Length.LengthUnit.YARDS),
                new Length(3.0, Length.LengthUnit.FEET));
    }

    @Test
    void testEquality_YardToInches() {
        assertEquals(
                new Length(1.0, Length.LengthUnit.YARDS),
                new Length(36.0, Length.LengthUnit.INCHES));
    }

    @Test
    void testEquality_CentimeterToInches() {
        assertEquals(
                new Length(1.0, Length.LengthUnit.CENTIMETERS),
                new Length(0.393701, Length.LengthUnit.INCHES));
    }

    @Test
    void testInequality_CentimeterToFeet() {
        assertNotEquals(
                new Length(1.0, Length.LengthUnit.CENTIMETERS),
                new Length(1.0, Length.LengthUnit.FEET));
    }

    @Test
    void testTransitiveProperty() {
        Length yard = new Length(1.0, Length.LengthUnit.YARDS);
        Length feet = new Length(3.0, Length.LengthUnit.FEET);
        Length inches = new Length(36.0, Length.LengthUnit.INCHES);

        assertEquals(yard, feet);
        assertEquals(feet, inches);
        assertEquals(yard, inches);
    }

    @Test
    void testReflexive() {
        Length yard = new Length(2.0, Length.LengthUnit.YARDS);
        assertEquals(yard, yard);
    }

    @Test
    void testNullComparison() {
        Length yard = new Length(2.0, Length.LengthUnit.YARDS);
        assertNotEquals(yard, null);
    }

    @Test
    void testComplexScenario() {
        assertEquals(
                new Length(2.0, Length.LengthUnit.YARDS),
                new Length(6.0, Length.LengthUnit.FEET));

        assertEquals(
                new Length(6.0, Length.LengthUnit.FEET),
                new Length(72.0, Length.LengthUnit.INCHES));
    }
}
