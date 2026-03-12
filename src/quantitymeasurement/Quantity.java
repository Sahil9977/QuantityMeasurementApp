package com.apps.quantitymeasurement;

public class Quantity< U extends IMeasurable>{
	// from u extends Imeasurable -> we restrics that only that thing will allow to be U that implement the Imeasurable interface 
	

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

	// ADD TWO UNITS
	public Quantity<U> add(Quantity<U> other) {

		if (other == null)
			throw new IllegalArgumentException("Length cannot be null");

		double value1 = this.convertToBaseUnit();
		double value2 = other.convertToBaseUnit();

		double sum = value1 + value2;

		double converted = this.unit.convertFromBaseUnit(sum);

		return new Quantity<>(converted, this.unit);
	}

	// ADD TWO UNITS AND CONVERT TO TARGET UNIT
	public Quantity<U> add(Quantity<U> other, U targetUnit) {

		if (other == null)
			throw new IllegalArgumentException("Length cannot be null");

		if (targetUnit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		double value1 = this.unit.convertToBaseUnit(this.value);
		double value2 = other.unit.convertToBaseUnit(other.value);

		double sum = value1 + value2;

		double converted = targetUnit.convertFromBaseUnit(sum);

		return new Quantity<>(converted, targetUnit);
	}
	
	// SUBTRACT
	public Quantity<U> subtract(Quantity<U> other) {

	    if (other == null)
	        throw new IllegalArgumentException("Quantity cannot be null");

	    if (this.unit.getClass() != other.unit.getClass())  
	        throw new IllegalArgumentException("Both quantities must be same measurement category");

	    double value1 = this.convertToBaseUnit();
	    double value2 = other.convertToBaseUnit();

	    double res = value1 - value2;

	    double converted = this.unit.convertFromBaseUnit(res);

	    return new Quantity<>(converted, this.unit);
	}

	// SUBTRACT WITH TARGET UNIT
	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

	    if (other == null)
	        throw new IllegalArgumentException("Quantity cannot be null");

	    if (targetUnit == null)                               
	        throw new IllegalArgumentException("Target unit cannot be null");

	    if (this.unit.getClass() != other.unit.getClass())
	        throw new IllegalArgumentException("Both quantities must be same measurement category");

	    double value1 = this.convertToBaseUnit();
	    double value2 = other.convertToBaseUnit();

	    double res = value1 - value2;

	    double converted = targetUnit.convertFromBaseUnit(res);

	    return new Quantity<>(converted, targetUnit);
	}
	
	// DIVIDE
	public double divide(Quantity<U> other) {

	    if (other == null)
	        throw new IllegalArgumentException("Quantity cannot be null");

	    if (this.unit.getClass() != other.unit.getClass())
	        throw new IllegalArgumentException("Both quantities must be same measurement category");

	    double value1 = this.convertToBaseUnit();
	    double value2 = other.convertToBaseUnit();

	    if (value2 == 0.0)
	        throw new ArithmeticException("Cannot divide by zero");

	    return value1 / value2;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		 Quantity<U> other = ( Quantity<U>) obj;

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
