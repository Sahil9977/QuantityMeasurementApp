package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class QuantityMeasurementAppTest {

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {

        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.FEET);

        assertEquals(new Length(2.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {

        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.INCHES);

        assertEquals(new Length(24.0, LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {

        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2,LengthUnit.YARDS);

        assertEquals(0.666, result.convertToBaseUnit() / 36, 0.01);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {

        Length l1 = new Length(1.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0,LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.CENTIMETRES);

        assertEquals(5.08, result.convertToBaseUnit() / 0.393701, 0.1);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {

        Length l1 = new Length(2.0, LengthUnit.YARDS);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertEquals(new Length(3.0,LengthUnit.YARDS), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {

        Length l1 = new Length(2.0, LengthUnit.YARDS);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.FEET);

        assertEquals(new Length(9.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {

        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result1 = l1.add(l2, LengthUnit.YARDS);
        Length result2 = l2.add(l1, LengthUnit.YARDS);

        assertEquals(result1, result2);
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {

        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(0.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertEquals(1.667, result.convertToBaseUnit() / 36, 0.01);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {

        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(-2.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.INCHES);

        assertEquals(new Length(36.0, LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {

        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);
        assertThrows(IllegalArgumentException.class,
                () -> l1.add(l2, null));
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {

        Length l1 = new Length(1000.0, LengthUnit.FEET);
        Length l2 = new Length(500.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.INCHES);

        assertEquals(new Length(18000.0, LengthUnit.INCHES), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {

        Length l1 = new Length(12.0, LengthUnit.INCHES);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertEquals(0.667, result.convertToBaseUnit() / 36, 0.01);
    }
    
    
    
    // weight tests 
    
    private static final double EPSILON = 0.001;

    // ------------------------------------------------
    // Equality Tests
    // ------------------------------------------------

    @Test
    void testEquality_KilogramToKilogram_SameValue() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1.0, WeightUnit.KILOGRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(2.0, WeightUnit.KILOGRAM);

        assertFalse(w1.equals(w2));
    }

    @Test
    void testEquality_KilogramToGram_EquivalentValue() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1000.0, WeightUnit.GRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_GramToKilogram_EquivalentValue() {

        Weight w1 = new Weight(1000.0, WeightUnit.GRAM);
        Weight w2 = new Weight(1.0, WeightUnit.KILOGRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_WeightVsLength_Incompatible() {

        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);
        Length length = new Length(1.0, LengthUnit.FEET);

        assertFalse(weight.equals(length));
    }

    @Test
    void testEquality_NullComparison() {

        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);

        assertFalse(weight.equals(null));
    }

    @Test
    void testEquality_SameReference() {

        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);

        assertTrue(weight.equals(weight));
    }

    @Test
    void testEquality_NullUnit() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Weight(1.0, null);
        });
    }

    @Test
    void testEquality_TransitiveProperty() {

        Weight a = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight b = new Weight(1000.0, WeightUnit.GRAM);
        Weight c = new Weight(1.0, WeightUnit.KILOGRAM);

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    void testEquality_ZeroValue() {

        Weight w1 = new Weight(0.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(0.0, WeightUnit.GRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_NegativeWeight() {

        Weight w1 = new Weight(-1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(-1000.0, WeightUnit.GRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_LargeWeightValue() {

        Weight w1 = new Weight(1000000.0, WeightUnit.GRAM);
        Weight w2 = new Weight(1000.0, WeightUnit.KILOGRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_SmallWeightValue() {

        Weight w1 = new Weight(0.001, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1.0, WeightUnit.GRAM);

        assertTrue(w1.equals(w2));
    }

    // ------------------------------------------------
    // Conversion Tests
    // ------------------------------------------------

    @Test
    void testConversion_PoundToKilogram() {

        Weight weight = new Weight(2.20462, WeightUnit.POUND);

        Weight result = weight.convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.0, result.convertToBaseUnit(), EPSILON);
    }

    @Test
    void testConversion_KilogramToPound() {

        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);

        Weight result = weight.convertTo(WeightUnit.POUND);

        assertEquals(2.20462,
                result.convertTo(WeightUnit.POUND).convertToBaseUnit()
                        / WeightUnit.POUND.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testConversion_SameUnit() {

        Weight weight = new Weight(5.0, WeightUnit.KILOGRAM);

        Weight result = weight.convertTo(WeightUnit.KILOGRAM);

        assertEquals(5.0, result.convertToBaseUnit(), EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {

        Weight weight = new Weight(0.0, WeightUnit.KILOGRAM);

        Weight result = weight.convertTo(WeightUnit.GRAM);

        assertEquals(0.0, result.convertToBaseUnit(), EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {

        Weight weight = new Weight(-1.0, WeightUnit.KILOGRAM);

        Weight result = weight.convertTo(WeightUnit.GRAM);

        assertEquals(-1000.0,
                result.convertTo(WeightUnit.GRAM).convertToBaseUnit()
                        / WeightUnit.GRAM.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {

        Weight weight = new Weight(1.5, WeightUnit.KILOGRAM);

        Weight result = weight
                .convertTo(WeightUnit.GRAM)
                .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.5, result.convertToBaseUnit(), EPSILON);
    }

    // ------------------------------------------------
    // Addition Tests
    // ------------------------------------------------

    @Test
    void testAddition_SameUnit_KilogramPlusKilogram() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(2.0, WeightUnit.KILOGRAM);

        Weight result = w1.add(w2);

        assertTrue(result.equals(new Weight(3.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_CrossUnit_KilogramPlusGram() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1000.0, WeightUnit.GRAM);

        Weight result = w1.add(w2);

        assertTrue(result.equals(new Weight(2.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_CrossUnit_PoundPlusKilogram() {

        Weight w1 = new Weight(2.20462, WeightUnit.POUND);
        Weight w2 = new Weight(1.0, WeightUnit.KILOGRAM);

        Weight result = w1.add(w2);

        assertEquals(4.40924,
                result.convertTo(WeightUnit.POUND).convertToBaseUnit()
                        / WeightUnit.POUND.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Kilogram() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1000.0, WeightUnit.GRAM);

        Weight result = w1.add(w2, WeightUnit.GRAM);

        assertTrue(result.equals(new Weight(2000.0, WeightUnit.GRAM)));
    }

    @Test
    void testAddition_Commutativity() {

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1000.0, WeightUnit.GRAM);

        Weight result1 = w1.add(w2);
        Weight result2 = w2.add(w1);

        assertEquals(result1.convertToBaseUnit(),
                result2.convertToBaseUnit(),
                EPSILON);
    }

    @Test
    void testAddition_WithZero() {

        Weight w1 = new Weight(5.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(0.0, WeightUnit.GRAM);

        Weight result = w1.add(w2);

        assertTrue(result.equals(new Weight(5.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_NegativeValues() {

        Weight w1 = new Weight(5.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(-2000.0, WeightUnit.GRAM);

        Weight result = w1.add(w2);

        assertTrue(result.equals(new Weight(3.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_LargeValues() {

        Weight w1 = new Weight(1e6, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1e6, WeightUnit.KILOGRAM);

        Weight result = w1.add(w2);

        assertTrue(result.equals(new Weight(2e6, WeightUnit.KILOGRAM)));
    }
}