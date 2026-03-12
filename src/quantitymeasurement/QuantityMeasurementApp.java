package com.apps.quantitymeasurement;


public class QuantityMeasurementApp {

    public static void demonstrateLengthComparison(Quantity<LengthUnit> l1,Quantity<LengthUnit> l2) {

        boolean result = l1.equals(l2);

        System.out.println("Output: Equal(" + result + ")");
    }

    
    public static void demonstrateLengthConversion(Quantity<LengthUnit> length,
                                                   LengthUnit targetUnit) {

    	Quantity<LengthUnit> converted = length.convertTo(targetUnit);

        System.out.println(length + " = " + converted);
    }

    // addition
    public static Quantity<LengthUnit> demonstrateLengthAddition(Quantity<LengthUnit> length1, Quantity<LengthUnit> length2) {

        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Lengths cannot be null");

        return length1.add(length2);
    }

    //  addition with target unit
    public static Quantity<LengthUnit> demonstrateLengthAddition(Quantity<LengthUnit> length1,
    		Quantity<LengthUnit> length2, LengthUnit targetUnit) {

        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Lengths cannot be null");

        return length1.add(length2, targetUnit);
    }

    
 // weight comparison
    public static void demonstrateWeightComparison(Quantity<WeightUnit> w1, Quantity<WeightUnit> w2) {

        boolean result = w1.equals(w2);

        System.out.println("Weight Equal(" + result + ")");
    }

    // weight conversion
    public static void demonstrateWeightConversion(Quantity<WeightUnit> weight,
                                                   WeightUnit targetUnit) {

    	Quantity<WeightUnit> converted = weight.convertTo(targetUnit);

        System.out.println(weight + " = " + converted);
    }

    // weight addition
    public static Quantity<WeightUnit> demonstrateWeightAddition(Quantity<WeightUnit> w1, Quantity<WeightUnit> w2) {

        return w1.add(w2);
    }

    // weight addition with target unit
    public static Quantity<WeightUnit> demonstrateWeightAddition(Quantity<WeightUnit> w1,
    		Quantity<WeightUnit> w2, WeightUnit targetUnit) {

        return w1.add(w2, targetUnit);
    }
    public static void main(String[] args) {

        demonstrateLengthComparison(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCHES));
     
        Quantity<LengthUnit> result1 = demonstrateLengthAddition(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCHES));

        System.out.println(" 1 FEET + 12 INCHES = " + result1);

       

        Quantity<LengthUnit> result2 = demonstrateLengthAddition(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);

        System.out.println(" Result in FEET = " + result2);

        Quantity<LengthUnit> result3 = demonstrateLengthAddition(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);

        System.out.println("Result in INCHES = " + result3);

        Quantity<LengthUnit> result4 = demonstrateLengthAddition(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCHES),
                LengthUnit.YARDS);

        System.out.println("Result in YARDS = " + result4);
        
        System.out.println("\nWeight Examples");

        Quantity<WeightUnit> w1 = new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<WeightUnit>(1000.0, WeightUnit.GRAM);

        System.out.println("Weight equality: " + w1.equals(w2));

        Quantity<WeightUnit> converted = w1.convertTo(WeightUnit.POUND);
        System.out.println("1 KG in POUND = " + converted);

        Quantity<WeightUnit> sum = w1.add(w2);
        System.out.println("1 KG + 1000 G = " + sum);

        Quantity<WeightUnit> sum2 = w1.add(w2, WeightUnit.GRAM);
        System.out.println("Result in GRAM = " + sum2);
    }
}