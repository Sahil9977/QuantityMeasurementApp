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
    }
}