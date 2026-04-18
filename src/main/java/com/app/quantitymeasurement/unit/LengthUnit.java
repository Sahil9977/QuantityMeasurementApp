package com.app.quantitymeasurement.unit;

public enum LengthUnit implements IMeasurable {

    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETRES(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override public double getConversionFactor()            { return conversionFactor; }
    @Override public double convertToBaseUnit(double v)      { return v * conversionFactor; }
    @Override public double convertFromBaseUnit(double b)    { return b / conversionFactor; }
    @Override public String getUnitName()                    { return this.name(); }
    @Override public String getMeasurementType()             { return "LENGTH"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return LengthUnit.valueOf(unitName.toUpperCase());
    }
}
