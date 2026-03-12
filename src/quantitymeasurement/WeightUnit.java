package com.apps.quantitymeasurement;

public enum WeightUnit {

    KILOGRAM(1.0),        // base unit
    GRAM(0.001),          // 1 g = 0.001 kg
    POUND(0.453592);      // 1 lb ≈ 0.453592 kg

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    // convert value to base unit (kg)
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    // convert value from base unit (kg)
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}