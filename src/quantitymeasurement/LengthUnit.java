package com.apps.quantitymeasurement;

public enum LengthUnit {

    FEET(12.0),        // 1 feet = 12 inches
    INCHES(1.0),       // base unit
    YARDS(36.0),       // 1 yard = 36 inches
    CENTIMETRES(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    //  convert value of this unit to base unit (inch)
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    // convert value from base unit (inch) to this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}