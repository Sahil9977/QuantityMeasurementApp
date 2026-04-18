package com.app.quantitymeasurement.unit;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS, FAHRENHEIT;

    private final SupportsArithmetic supportsArithmetic = () -> false;

    @Override
    public boolean supportsArithmetic() { return supportsArithmetic.isSupported(); }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException("Temperature does not support " + operation);
    }

    @Override
    public double convertToBaseUnit(double value) {
        if (this == FAHRENHEIT) return (value - 32) * 5.0 / 9.0;
        return value;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        if (this == FAHRENHEIT) return (baseValue * 9.0 / 5.0) + 32;
        return baseValue;
    }

    @Override public double getConversionFactor()  { return 1.0; }
    @Override public String getUnitName()          { return this.name(); }
    @Override public String getMeasurementType()   { return "TEMPERATURE"; }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        return TemperatureUnit.valueOf(unitName.toUpperCase());
    }
}
