package com.apps.quantitymeasurement;

public enum VolumeUnit implements IMeasurable{
	

	LITRE(1.0), // (already base unit)
	MILLILITRE( 0.001), // (1 mL = 0.001 L, or 1 L = 1000 mL)
	GALLON(3.78541); //(1 US gallon ≈ 3.78541 L, or 1 L ≈ 0.264172 gallons)

    private final double conversionFactor;

    
	VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    //  convert value of this unit to base unit (inch)
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    // convert value from base unit (inch) to this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }


	@Override
	public String getUnitName() {
//		What is this.name() in an Enum?
//				In Java, every enum automatically inherits from java.lang.Enum, which provides a built-in method called name().
//				java// This is built into java.lang.Enum — you get it for FREE
//				public final String name() {
//				    return this.name; // returns the exact constant name as declared
//				}
		return this.name();
	}

	
	
}
