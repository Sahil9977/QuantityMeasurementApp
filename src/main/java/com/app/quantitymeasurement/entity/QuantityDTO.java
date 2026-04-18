package com.app.quantitymeasurement.entity;

public class QuantityDTO {

    public static final String TYPE_LENGTH      = "LENGTH";
    public static final String TYPE_WEIGHT      = "WEIGHT";
    public static final String TYPE_VOLUME      = "VOLUME";
    public static final String TYPE_TEMPERATURE = "TEMPERATURE";

    private final double value;
    private final String unitName;
    private final String measurementType;

    public QuantityDTO(double value, String unitName, String measurementType) {
        this.value           = value;
        this.unitName        = unitName;
        this.measurementType = measurementType;
    }

    public double getValue()           { return value; }
    public String getUnitName()        { return unitName; }
    public String getMeasurementType() { return measurementType; }

    @Override
    public String toString() {
        return "QuantityDTO(" + value + " " + unitName + " [" + measurementType + "])";
    }
}
