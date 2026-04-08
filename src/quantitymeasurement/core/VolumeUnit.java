package com.apps.quantitymeasurement.core;

/**
 * UC15 Change: Implemented getMeasurementType() and getUnitByName(String).
 */
public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

    @Override public double getConversionFactor() { return conversionFactor; }
    @Override public double convertToBaseUnit(double value) { return value * conversionFactor; }
    @Override public double convertFromBaseUnit(double baseValue) { return baseValue / conversionFactor; }
    @Override public String getUnitName() { return this.name(); }

    // ── UC15 NEW ──────────────────────────────────────────────────────────────

    @Override
    public String getMeasurementType() { return "VOLUME"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return VolumeUnit.valueOf(unitName.toUpperCase());
    }
}