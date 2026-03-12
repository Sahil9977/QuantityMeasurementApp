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
    
    
    
    // volume comparison
    public static void demonstrateVolumeComparison(Quantity<VolumeUnit> v1, Quantity<VolumeUnit> v2) {

        boolean result = v1.equals(v2);

        System.out.println("Weight Equal(" + result + ")");
    }

    // volume conversion
    public static void demonstrateVolumeConversion(Quantity<VolumeUnit>  volume,
                                                   VolumeUnit targetUnit) {

    	Quantity<VolumeUnit> converted = volume.convertTo(targetUnit);

        System.out.println(volume + " = " + converted);
    }

    // volume addition
    public static Quantity<VolumeUnit> demonstrateVolumeAddition(Quantity<VolumeUnit> v1, Quantity<VolumeUnit> v2) {

        return v1.add(v2);
    }

    // weight addition with target unit
    public static Quantity<VolumeUnit> demonstrateVolumeAddition(Quantity<VolumeUnit> v1,
    		Quantity<VolumeUnit> v2, VolumeUnit targetUnit) {

        return v1.add(v2, targetUnit);
    }
    
    
    public static void main(String[] args) {
    	
    	// LENGTH 
        System.out.println("LENGTH ");
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

        demonstrateLengthComparison(l1, l2); 
        demonstrateLengthConversion(l1, LengthUnit.YARDS);
        
        System.out.println("Addition: " + demonstrateLengthAddition(l1, l2)); 
        System.out.println("Target Addition: " + demonstrateLengthAddition(l1, l2, LengthUnit.YARDS));
        
        // WEIGHT 
        System.out.println("\nWEIGHT ");
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        demonstrateWeightComparison(w1, w2); 
        demonstrateWeightConversion(w1, WeightUnit.POUND);
        
        System.out.println("Addition: " + demonstrateWeightAddition(w1, w2)); 
        System.out.println("Target Addition: " + demonstrateWeightAddition(w1, w2, WeightUnit.KILOGRAM));


        // VOLUME 
        System.out.println("\nVOLUME ");
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> v2 = new Quantity<>(3.785, VolumeUnit.LITRE);

        demonstrateVolumeComparison(v1, v2); 
        demonstrateVolumeConversion(v1, VolumeUnit.MILLILITRE); 
        
        System.out.println("Addition: " + demonstrateVolumeAddition(v1, v2, null)); 
        System.out.println("Target Addition: " + demonstrateVolumeAddition(v1, v2, VolumeUnit.LITRE));
        
    }


}