package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

	private static final double EPSILON = 0.001;

	@Test
	void testAddition_ExplicitTargetUnit_Feet() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.FEET);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Inches() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.INCHES);

		assertEquals(new Quantity<>(24.0, LengthUnit.INCHES), result);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Yards() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.YARDS);

		assertEquals(0.666, result.convertToBaseUnit() / 36, 0.01);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Centimeters() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.INCHES);
		Quantity<LengthUnit> l2 = new Quantity<>(1.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.CENTIMETRES);

		assertEquals(5.08, result.convertToBaseUnit() / 0.393701, 0.1);
	}

	@Test
	void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {

		Quantity<LengthUnit> l1 = new Quantity<>(2.0, LengthUnit.YARDS);
		Quantity<LengthUnit> l2 = new Quantity<>(3.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.YARDS);

		assertEquals(new Quantity<>(3.0, LengthUnit.YARDS), result);
	}

	@Test
	void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {

		Quantity<LengthUnit> l1 = new Quantity<>(2.0, LengthUnit.YARDS);
		Quantity<LengthUnit> l2 = new Quantity<>(3.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.FEET);

		assertEquals(new Quantity<>(9.0, LengthUnit.FEET), result);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Commutativity() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result1 = l1.add(l2, LengthUnit.YARDS);
		Quantity<LengthUnit> result2 = l2.add(l1, LengthUnit.YARDS);

		assertEquals(result1, result2);
	}

	@Test
	void testAddition_ExplicitTargetUnit_WithZero() {

		Quantity<LengthUnit> l1 = new Quantity<>(5.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(0.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.YARDS);

		assertEquals(1.667, result.convertToBaseUnit() / 36, 0.01);
	}

	@Test
	void testAddition_ExplicitTargetUnit_NegativeValues() {

		Quantity<LengthUnit> l1 = new Quantity<>(5.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(-2.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.INCHES);

		assertEquals(new Quantity<>(36.0, LengthUnit.INCHES), result);
	}

	@Test
	void testAddition_ExplicitTargetUnit_NullTargetUnit() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		assertThrows(IllegalArgumentException.class, () -> l1.add(l2, null));
	}

	@Test
	void testAddition_ExplicitTargetUnit_LargeToSmallScale() {

		Quantity<LengthUnit> l1 = new Quantity<>(1000.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(500.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.INCHES);

		assertEquals(new Quantity<>(18000.0, LengthUnit.INCHES), result);
	}

	@Test
	void testAddition_ExplicitTargetUnit_SmallToLargeScale() {

		Quantity<LengthUnit> l1 = new Quantity<>(12.0, LengthUnit.INCHES);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.YARDS);

		assertEquals(0.667, result.convertToBaseUnit() / 36, 0.01);
	}

	// Weight

	@Test
	void testEquality_KilogramToKilogram_SameValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testEquality_KilogramToKilogram_DifferentValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(2.0, WeightUnit.KILOGRAM);

		assertFalse(w1.equals(w2));
	}

	@Test
	void testEquality_KilogramToGram_EquivalentValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testEquality_GramToKilogram_EquivalentValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(1000.0, WeightUnit.GRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testEquality_WeightVsLength_Incompatible() {

		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

		assertFalse(weight.equals(length));
	}

	@Test
	void testEquality_NullComparison() {

		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertFalse(weight.equals(null));
	}

	@Test
	void testEquality_SameReference() {

		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertTrue(weight.equals(weight));
	}

	@Test
	void testEquality_NullUnit() {

		assertThrows(IllegalArgumentException.class, () -> {
			new Quantity<>(1.0, (WeightUnit) null);
		});
	}

	@Test
	void testEquality_TransitiveProperty() {

		Quantity<WeightUnit> a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> b = new Quantity<>(1000.0, WeightUnit.GRAM);
		Quantity<WeightUnit> c = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertTrue(a.equals(b));
		assertTrue(b.equals(c));
		assertTrue(a.equals(c));
	}

	@Test
	void testEquality_ZeroValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(0.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(0.0, WeightUnit.GRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testEquality_NegativeWeight() {

		Quantity<WeightUnit> w1 = new Quantity<>(-1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(-1000.0, WeightUnit.GRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testEquality_LargeWeightValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(1000000.0, WeightUnit.GRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.KILOGRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testEquality_SmallWeightValue() {

		Quantity<WeightUnit> w1 = new Quantity<>(0.001, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1.0, WeightUnit.GRAM);

		assertTrue(w1.equals(w2));
	}

	@Test
	void testConversion_PoundToKilogram() {

		Quantity<WeightUnit> weight = new Quantity<>(2.20462, WeightUnit.POUND);

		Quantity<WeightUnit> result = weight.convertTo(WeightUnit.KILOGRAM);

		assertEquals(1.0, result.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testConversion_KilogramToPound() {

		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = weight.convertTo(WeightUnit.POUND);

		assertEquals(2.20462,
				result.convertTo(WeightUnit.POUND).convertToBaseUnit() / WeightUnit.POUND.getConversionFactor(),
				EPSILON);
	}

	@Test
	void testConversion_SameUnit() {

		Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = weight.convertTo(WeightUnit.KILOGRAM);

		assertEquals(5.0, result.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testConversion_ZeroValue() {

		Quantity<WeightUnit> weight = new Quantity<>(0.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = weight.convertTo(WeightUnit.GRAM);

		assertEquals(0.0, result.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testConversion_NegativeValue() {

		Quantity<WeightUnit> weight = new Quantity<>(-1.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = weight.convertTo(WeightUnit.GRAM);

		assertEquals(-1000.0,
				result.convertTo(WeightUnit.GRAM).convertToBaseUnit() / WeightUnit.GRAM.getConversionFactor(), EPSILON);
	}

	@Test
	void testConversion_RoundTrip() {

		Quantity<WeightUnit> weight = new Quantity<>(1.5, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = weight.convertTo(WeightUnit.GRAM).convertTo(WeightUnit.KILOGRAM);

		assertEquals(1.5, result.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testAddition_SameUnit_KilogramPlusKilogram() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(2.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertTrue(result.equals(new Quantity<>(3.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testAddition_CrossUnit_KilogramPlusGram() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertTrue(result.equals(new Quantity<>(2.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testAddition_CrossUnit_PoundPlusKilogram() {

		Quantity<WeightUnit> w1 = new Quantity<>(2.20462, WeightUnit.POUND);
		Quantity<WeightUnit> w2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(4.40924,
				result.convertTo(WeightUnit.POUND).convertToBaseUnit() / WeightUnit.POUND.getConversionFactor(),
				EPSILON);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Kilogram() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2, WeightUnit.GRAM);

		assertTrue(result.equals(new Quantity<>(2000.0, WeightUnit.GRAM)));
	}

	@Test
	void testAddition_Commutativity() {

		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result1 = w1.add(w2);
		Quantity<WeightUnit> result2 = w2.add(w1);

		assertEquals(result1.convertToBaseUnit(), result2.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testAddition_WithZero() {

		Quantity<WeightUnit> w1 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(0.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertTrue(result.equals(new Quantity<>(5.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testAddition_NegativeValues() {

		Quantity<WeightUnit> w1 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(-2000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertTrue(result.equals(new Quantity<>(3.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testAddition_LargeValues() {

		Quantity<WeightUnit> w1 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1e6, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertTrue(result.equals(new Quantity<>(2e6, WeightUnit.KILOGRAM)));
	}

	// volumes

	@Test
	void testEquality_LitreToLitre_SameValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_LitreToLitre_DifferentValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(2.0, VolumeUnit.LITRE);

		assertFalse(v1.equals(v2));
	}

	@Test
	void testEquality_MillilitreToMillilitre_SameValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_LitreToMillilitre_EquivalentValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_MillilitreToLitre_EquivalentValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_LitreToGallon_EquivalentValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.GALLON);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_GallonToLitre_EquivalentValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		Quantity<VolumeUnit> v2 = new Quantity<>(3.78541, VolumeUnit.LITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_VolumeVsLength_Incompatible() {

		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

		assertFalse(volume.equals(length));
	}

	@Test
	void testEquality_Volume_NullComparison() {

		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertFalse(volume.equals(null));
	}

	@Test
	void testEquality_Volume_SameReference() {

		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(volume.equals(volume));
	}

	@Test
	void testEquality_Volume_NullUnit() {

		assertThrows(IllegalArgumentException.class, () -> {
			new Quantity<>(1.0, (VolumeUnit) null);
		});
	}

	@Test
	void testEquality_Volume_TransitiveProperty() {

		Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(a.equals(b));
		assertTrue(b.equals(c));
		assertTrue(a.equals(c));
	}

	@Test
	void testEquality_Volume_ZeroValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(0.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(0.0, VolumeUnit.MILLILITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_NegativeVolume() {

		Quantity<VolumeUnit> v1 = new Quantity<>(-1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(-1000.0, VolumeUnit.MILLILITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_LargeVolumeValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1000000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.LITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testEquality_SmallVolumeValue() {

		Quantity<VolumeUnit> v1 = new Quantity<>(0.001, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.MILLILITRE);

		assertTrue(v1.equals(v2));
	}

	@Test
	void testConversion_Volume_SameUnit() {

		Quantity<VolumeUnit> volume = new Quantity<>(5.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> result = volume.convertTo(VolumeUnit.LITRE);

		assertEquals(5.0, result.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testConversion_Volume_ZeroValue() {

		Quantity<VolumeUnit> volume = new Quantity<>(0.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> result = volume.convertTo(VolumeUnit.MILLILITRE);

		assertEquals(0.0, result.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testConversion_Volume_RoundTrip() {

		Quantity<VolumeUnit> volume = new Quantity<>(1.5, VolumeUnit.LITRE);

		Quantity<VolumeUnit> result = volume.convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);

		assertEquals(1.5, result.convertToBaseUnit(), EPSILON);
	}

	// Volume

	@Test
	void testVolumeUnitEnum_LitreConstant() {

		assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_MillilitreConstant() {

		assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_GallonConstant() {

		assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
	}

	@Test
	void testConvertToBaseUnit_LitreToLitre() {

		assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPSILON);
	}

	@Test
	void testConvertToBaseUnit_MillilitreToLitre() {

		assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON);
	}

	@Test
	void testConvertToBaseUnit_GallonToLitre() {

		assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
	}

	@Test
	void testConvertFromBaseUnit_LitreToLitre() {

		assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), EPSILON);
	}

	@Test
	void testConvertFromBaseUnit_LitreToMillilitre() {

		assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPSILON);
	}

	@Test
	void testConvertFromBaseUnit_LitreToGallon() {

		assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
	}

	@Test
	void testAddition_SameUnit_LitrePlusLitre() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(2.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(3.0, VolumeUnit.LITRE)));
	}

	@Test
	void testAddition_SameUnit_MillilitrePlusMillilitre() {

		Quantity<VolumeUnit> v1 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testAddition_CrossUnit_LitrePlusMillilitre() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
	}

	@Test
	void testAddition_CrossUnit_MillilitrePlusLitre() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(2000.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testAddition_ExplicitTargetUnit_Litre() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result = v1.add(v2, VolumeUnit.LITRE);

		assertTrue(result.equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
	}

	@Test
	void testAddition_ExplicitTargetUnit_Millilitre() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result = v1.add(v2, VolumeUnit.MILLILITRE);

		assertTrue(result.equals(new Quantity<>(2000.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testAddition_Volume_Commutativity() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result1 = v1.add(v2, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result2 = v2.add(v1, VolumeUnit.LITRE);

		assertEquals(result1.convertToBaseUnit(), result2.convertToBaseUnit(), EPSILON);
	}

	@Test
	void testAddition_Volume_WithZero() {

		Quantity<VolumeUnit> v1 = new Quantity<>(5.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(0.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(5.0, VolumeUnit.LITRE)));
	}

	@Test
	void testAddition_Volume_NegativeValues() {

		Quantity<VolumeUnit> v1 = new Quantity<>(5.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(-2000.0, VolumeUnit.MILLILITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(3.0, VolumeUnit.LITRE)));
	}

	@Test
	void testAddition_Volume_LargeValues() {

		Quantity<VolumeUnit> v1 = new Quantity<>(1e6, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1e6, VolumeUnit.LITRE);

		Quantity<VolumeUnit> result = v1.add(v2);

		assertTrue(result.equals(new Quantity<>(2e6, VolumeUnit.LITRE)));
	}

	// subtract and division

	@Test
	void testSubtraction_Length_SameUnit() {

		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_Length_CrossUnit() {

		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCHES));

		assertEquals(new Quantity<>(9.5, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_Length_TargetUnit() {

		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);

		assertEquals(new Quantity<>(114.0, LengthUnit.INCHES), result);
	}

	@Test
	void testSubtraction_Length_ResultNegative() {

		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(-5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_Weight_SameUnit() {

		Quantity<WeightUnit> result = new Quantity<>(10.0, WeightUnit.KILOGRAM)
				.subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM));

		assertEquals(new Quantity<>(5.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	void testSubtraction_Weight_CrossUnit() {

		Quantity<WeightUnit> result = new Quantity<>(2.0, WeightUnit.KILOGRAM)
				.subtract(new Quantity<>(500.0, WeightUnit.GRAM));

		assertEquals(new Quantity<>(1.5, WeightUnit.KILOGRAM), result);
	}

	@Test
	void testSubtraction_Volume_SameUnit() {

		Quantity<VolumeUnit> result = new Quantity<>(10.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(3.0, VolumeUnit.LITRE));

		assertEquals(new Quantity<>(7.0, VolumeUnit.LITRE), result);
	}

	@Test
	void testSubtraction_Volume_CrossUnit() {

		Quantity<VolumeUnit> result = new Quantity<>(2.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(500.0, VolumeUnit.MILLILITRE));

		assertEquals(new Quantity<>(1.5, VolumeUnit.LITRE), result);
	}

	@Test
	void testDivision_Length_SameUnit() {

		double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(5.0, result);
	}

	@Test
	void testDivision_Length_CrossUnit() {

		double result = new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(1.0, result);
	}

	@Test
	void testDivision_Weight_SameUnit() {

		double result = new Quantity<>(10.0, WeightUnit.KILOGRAM).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM));

		assertEquals(2.0, result);
	}

	@Test
	void testDivision_Weight_CrossUnit() {

		double result = new Quantity<>(2000.0, WeightUnit.GRAM).divide(new Quantity<>(2.0, WeightUnit.KILOGRAM));

		assertEquals(1.0, result);
	}

	@Test
	void testDivision_Volume_SameUnit() {

		double result = new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE));

		assertEquals(2.0, result);
	}

	@Test
	void testDivision_ByZero() {

		assertThrows(ArithmeticException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
	}

	@Test
	void testSubtraction_NullOperand() {

		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
	}

}