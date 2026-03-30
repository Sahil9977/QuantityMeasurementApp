package com.apps.quantitymeasurement;

public class Quantity<U extends IMeasurable> {
	// from u extends Imeasurable -> we restrics that only that thing will allow to
	// be U that implement the Imeasurable interface

	private double value;
	private U unit;

	public Quantity(double value, U unit) {

		if (unit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Value must be a finite number");

		this.value = value;
		this.unit = unit;
	}

	// base unit is in inch coz we have define the value as per inchlike 1 feet = 12
	// inch 1 yard = 36 inch like that

	public double convertToBaseUnit() {
		return this.unit.convertToBaseUnit(this.value);
	}

	// CONVERT TO TARGET UNIT
	public Quantity<U> convertTo(U targetUnit) {

		double baseValue = this.unit.convertToBaseUnit(this.value);

		double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

		return new Quantity<>(convertedValue, targetUnit);
	}

	// validations
	public void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetRequired) {

		if (other == null)
			throw new IllegalArgumentException("Quantity cannot be null");

		if (this.unit.getClass() != other.unit.getClass())
			throw new IllegalArgumentException("Both quantities must be same measurement category");

		if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
			throw new IllegalArgumentException("Values must be finite");

		if (targetRequired && targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");
	}

	// perform operations
	public double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {

		double baseValue1 = this.convertToBaseUnit();
		double baseValue2 = other.convertToBaseUnit();

		return operation.apply(baseValue1, baseValue2);
	}

	// ADD TWO UNITS

	public Quantity<U> add(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		double result = performBaseArithmetic(other, ArithmeticOperation.ADD);

		double converted = this.unit.convertFromBaseUnit(result);

		return new Quantity<>(converted, this.unit);
	}

	
	// ADD TWO UNITS AND CONVERT TO TARGET UNIT

	public Quantity<U> add(Quantity<U> other, U targetUnit) {

		validateArithmeticOperands(other, targetUnit, true);

		double result = performBaseArithmetic(other, ArithmeticOperation.ADD);

		double converted = targetUnit.convertFromBaseUnit(result);

		return new Quantity<>(converted, targetUnit);
	}

	// SUBTRACT

	public Quantity<U> subtract(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		double result = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);

		double converted = this.unit.convertFromBaseUnit(result);

		return new Quantity<>(converted, this.unit);
	}

	// SUBTRACT WITH TARGET UNIT

	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

		validateArithmeticOperands(other, targetUnit, true);

		double result = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);

		double converted = targetUnit.convertFromBaseUnit(result);

		return new Quantity<>(converted, targetUnit);
	}

	// DIVIDE

	public double divide(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		Quantity<U> other = (Quantity<U>) obj;

		return Math.abs(this.unit.convertToBaseUnit(this.value) - other.unit.convertToBaseUnit(other.value)) <= 0.001;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(this.unit.convertToBaseUnit(this.value));
	}

	@Override
	public String toString() {
		return "Quantity(" + value + ", " + unit + ")";
	}

}
