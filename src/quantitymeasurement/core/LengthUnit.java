package com.apps.quantitymeasurement.core;


/**
 * UC15 Change: Implemented getMeasurementType() and getUnitByName(String).
 * WHY: ServiceImpl uses these to convert QuantityDTO → QuantityModel
 *      without knowing the concrete enum type at compile time.
 */
public enum LengthUnit implements IMeasurable {

    FEET(12.0),
    INCHES(1.0),       // base unit
    YARDS(36.0),
    CENTIMETRES(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override public double getConversionFactor() { return conversionFactor; }

    @Override
    public double convertToBaseUnit(double value) { return value * conversionFactor; }

    @Override
    public double convertFromBaseUnit(double baseValue) { return baseValue / conversionFactor; }

    @Override
    public String getUnitName() { return this.name(); }

    // ── UC15 NEW ──────────────────────────────────────────────────────────────

    @Override
    public String getMeasurementType() { return "LENGTH"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return LengthUnit.valueOf(unitName.toUpperCase());
    }
}