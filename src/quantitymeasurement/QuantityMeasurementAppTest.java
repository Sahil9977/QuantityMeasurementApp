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
}
