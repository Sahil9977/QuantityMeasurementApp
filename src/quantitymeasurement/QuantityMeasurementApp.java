package com.apps.quantitymeasurement;


public class QuantityMeasurementApp {

    public static void demonstrateLengthComparison(Length l1, Length l2) {

        boolean result = l1.equals(l2);

        System.out.println("Output: Equal(" + result + ")");
    }

    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit from,
                                                   LengthUnit to) {

        double result = Length.convert(value, from, to);

        System.out.println(value + " " + from + " = " + result + " " + to);
    }

    public static void demonstrateLengthConversion(Length length,
                                                   LengthUnit targetUnit) {

        Length converted = length.convertTo(targetUnit);

        System.out.println(length + " = " + converted);
    }

    // addition
    public static Length demonstrateLengthAddition(Length length1, Length length2) {

        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Lengths cannot be null");

        return length1.add(length2);
    }

    //  addition with target unit
    public static Length demonstrateLengthAddition(Length length1,
                                                   Length length2,
                                                   LengthUnit targetUnit) {

        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Lengths cannot be null");

        return length1.add(length2, targetUnit);
    }

    
 // weight comparison
    public static void demonstrateWeightComparison(Weight w1, Weight w2) {

        boolean result = w1.equals(w2);

        System.out.println("Weight Equal(" + result + ")");
    }

    // weight conversion
    public static void demonstrateWeightConversion(Weight weight,
                                                   WeightUnit targetUnit) {

        Weight converted = weight.convertTo(targetUnit);

        System.out.println(weight + " = " + converted);
    }

    // weight addition
    public static Weight demonstrateWeightAddition(Weight w1, Weight w2) {

        return w1.add(w2);
    }

    // weight addition with target unit
    public static Weight demonstrateWeightAddition(Weight w1,
                                                   Weight w2,
                                                   WeightUnit targetUnit) {

        return w1.add(w2, targetUnit);
    }
    public static void main(String[] args) {

        demonstrateLengthComparison(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES));

        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);

     
        Length result1 = demonstrateLengthAddition(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES));

        System.out.println(" 1 FEET + 12 INCHES = " + result1);

       

        Length result2 = demonstrateLengthAddition(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);

        System.out.println(" Result in FEET = " + result2);

        Length result3 = demonstrateLengthAddition(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);

        System.out.println("Result in INCHES = " + result3);

        Length result4 = demonstrateLengthAddition(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES),
                LengthUnit.YARDS);

        System.out.println("Result in YARDS = " + result4);
        
        System.out.println("\nWeight Examples");

        Weight w1 = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1000.0, WeightUnit.GRAM);

        System.out.println("Weight equality: " + w1.equals(w2));

        Weight converted = w1.convertTo(WeightUnit.POUND);
        System.out.println("1 KG in POUND = " + converted);

        Weight sum = w1.add(w2);
        System.out.println("1 KG + 1000 G = " + sum);

        Weight sum2 = w1.add(w2, WeightUnit.GRAM);
        System.out.println("Result in GRAM = " + sum2);
    }
}