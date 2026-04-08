package com.apps.quantitymeasurement.core;

/**
 * UC15 Change: Implemented getMeasurementType() and getUnitByName(String).
 */
public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

    @Override public double getConversionFactor() { return conversionFactor; }
    @Override public double convertToBaseUnit(double value) { return value * conversionFactor; }
    @Override public double convertFromBaseUnit(double baseValue) { return baseValue / conversionFactor; }
    @Override public String getUnitName() { return this.name(); }

    // ── UC15 NEW ──────────────────────────────────────────────────────────────

    @Override
    public String getMeasurementType() { return "WEIGHT"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return WeightUnit.valueOf(unitName.toUpperCase());
    }
}