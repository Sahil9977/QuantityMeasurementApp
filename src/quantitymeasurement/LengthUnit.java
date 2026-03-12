package com.apps.quantitymeasurement;

public enum LengthUnit implements IMeasurable{

    FEET(12.0),        // 1 feet = 12 inches
    INCHES(1.0),       // base unit
    YARDS(36.0),       // 1 yard = 36 inches
    CENTIMETRES(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
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