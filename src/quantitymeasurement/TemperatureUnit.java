package com.apps.quantitymeasurement;

public enum TemperatureUnit implements IMeasurable {

	CELSIUS, FAHRENHEIT;

	// Temperature does NOT support arithmetic
	private final SupportsArithmetic supportsArithmetic = () -> false;

	@Override
	public boolean supportsArithmetic() {
		return supportsArithmetic.isSupported();
	}

	@Override
	public void validateOperationSupport(String operation) {

		throw new UnsupportedOperationException("Temperature does not support " + operation);

	}

	// Convert to base (Celsius)
	@Override
	public double convertToBaseUnit(double value) {
		if (this == FAHRENHEIT)
			return (value - 32) * 5 / 9;
		return value;
	}

	// Convert from base (Celsius)
	@Override
	public double convertFromBaseUnit(double baseValue) {
		if (this == FAHRENHEIT)
			return (baseValue * 9 / 5) + 32;
		return baseValue;
	}

	@Override
	public double getConversionFactor() {
		return 1.0; // not meaningful for temperature
	}

	@Override
	public String getUnitName() {
		return this.name();
	}
}