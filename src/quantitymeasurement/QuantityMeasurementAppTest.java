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

        assertThrows(IllegalArgumentException.class,
                () -> l1.add(l2, null));
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


    // ------------------------------------------------
    // Weight Equality Tests (UC9 — logic unchanged)
    // ------------------------------------------------

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

    // ------------------------------------------------
    // Weight Conversion Tests (UC9 — logic unchanged)
    // ------------------------------------------------

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
                result.convertTo(WeightUnit.POUND).convertToBaseUnit()
                        / WeightUnit.POUND.getConversionFactor(),
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
                result.convertTo(WeightUnit.GRAM).convertToBaseUnit()
                        / WeightUnit.GRAM.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {

        Quantity<WeightUnit> weight = new Quantity<>(1.5, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> result = weight
                .convertTo(WeightUnit.GRAM)
                .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.5, result.convertToBaseUnit(), EPSILON);
    }

    // ------------------------------------------------
    // Weight Addition Tests (UC9 — logic unchanged)
    // ------------------------------------------------

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
                result.convertTo(WeightUnit.POUND).convertToBaseUnit()
                        / WeightUnit.POUND.getConversionFactor(),
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

        assertEquals(result1.convertToBaseUnit(),
                result2.convertToBaseUnit(),
                EPSILON);
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
}