package com.apps.quantitymeasurement;

public class Length {
	
	private double value;
	private LengthUnit unit;
	
	public enum LengthUnit{
		
		FEET(12.0),
		INCHES(1.0);
		
		private final double conversionFactor;
		
		LengthUnit(double conversionFactor){
			this.conversionFactor=conversionFactor;
		}
		
		public double getConversionFactor() {
			return conversionFactor;
		}
	}
	
	public Length(double value , LengthUnit unit) {
		if(unit==null) throw new IllegalArgumentException("unit cannot be null");
		
		this.value = value;
		this.unit = unit;
	}
	
	//convert to base unit(inches)
	public double toBaseUnit() {
		return this.value*this.unit.getConversionFactor();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this==obj)return true;
		
		if(obj==null || getClass()!=obj.getClass())return false;
		
		Length other = (Length) obj;
		return Double.compare(this.toBaseUnit(), other.toBaseUnit())==0;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(this.toBaseUnit());
	}

}
