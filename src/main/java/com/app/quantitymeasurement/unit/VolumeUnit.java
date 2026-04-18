package com.app.quantitymeasurement.unit;

public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

    @Override public double getConversionFactor()         { return conversionFactor; }
    @Override public double convertToBaseUnit(double v)   { return v * conversionFactor; }
    @Override public double convertFromBaseUnit(double b) { return b / conversionFactor; }
    @Override public String getUnitName()                 { return this.name(); }
    @Override public String getMeasurementType()          { return "VOLUME"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return VolumeUnit.valueOf(unitName.toUpperCase());
    }
}
