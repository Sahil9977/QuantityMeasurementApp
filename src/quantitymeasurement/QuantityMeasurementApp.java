package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.Length.LengthUnit;

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
    
    
    public static Length demonstrateLengthAddition(Length length1, Length length2) {

        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Lengths cannot be null");

        return length1.add(length2);
    }

    public static void main(String[] args) {

        // Equality demonstrations
        demonstrateLengthComparison(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES));

        demonstrateLengthComparison(
                new Length(1.0, LengthUnit.YARDS),
                new Length(36.0, LengthUnit.INCHES));

        demonstrateLengthComparison(
                new Length(100.0, LengthUnit.CENTIMETRES),
                new Length(39.3701, LengthUnit.INCHES));

        // Conversion demonstrations
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);

        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);

        demonstrateLengthConversion(new Length(2.0, LengthUnit.YARDS),
                LengthUnit.INCHES);
        
        // additionn demonstation
        
        Length result1 = demonstrateLengthAddition(
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES));

        System.out.println("1 FEET + 12 INCHES = " + result1);

        Length result2 = demonstrateLengthAddition(
                new Length(12.0, LengthUnit.INCHES),
                new Length(1.0, LengthUnit.FEET));

        System.out.println("12 INCHES + 1 FEET = " + result2);

        Length result3 = demonstrateLengthAddition(
                new Length(1.0, LengthUnit.YARDS),
                new Length(3.0, LengthUnit.FEET));

        System.out.println("1 YARD + 3 FEET = " + result3);
    }
}