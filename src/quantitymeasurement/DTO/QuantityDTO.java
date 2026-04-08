package com.apps.quantitymeasurement.DTO;



public class QuantityDTO {

    public interface IMeasurableUnit {
        String getUnitName();
        String getMeasurementType();
    }

    // ── Supported measurement types as constants ──────────────────────────────
    public static final String TYPE_LENGTH      = "LENGTH";
    public static final String TYPE_WEIGHT      = "WEIGHT";
    public static final String TYPE_VOLUME      = "VOLUME";
    public static final String TYPE_TEMPERATURE = "TEMPERATURE";

    // ── Fields ────────────────────────────────────────────────────────────────
    private final double value;
    private final String unitName;          // e.g. "FEET", "KILOGRAM"
    private final String measurementType;   // e.g. "LENGTH", "WEIGHT"

    // ── Constructor ──────────────────────────────────────────────────────────
    public QuantityDTO(double value, String unitName, String measurementType) {
        this.value           = value;
        this.unitName        = unitName;
        this.measurementType = measurementType;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public double getValue()           { return value; }
    public String getUnitName()        { return unitName; }
    public String getMeasurementType() { return measurementType; }

    @Override
    public String toString() {
        return "QuantityDTO(" + value + " " + unitName + " [" + measurementType + "])";
    }
}