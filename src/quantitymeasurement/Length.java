package com.apps.quantitymeasurement;

public class Length {
	
	private double value;
	private LengthUnit unit;
	
	public enum LengthUnit{
		
		FEET(12.0),
		INCHES(1.0),
		YARDS(36.0),
		CENTIMETRES(0.393701);
		
		
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
	public double convertToBaseUnit() {
		return this.value*this.unit.getConversionFactor();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this==obj)return true;
		
		if(obj==null || getClass()!=obj.getClass())return false;
		
		Length other = (Length) obj;
		return Math.abs(this.convertToBaseUnit()-other.convertToBaseUnit())<=0.001;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(this.convertToBaseUnit());
	}
	
	
	public static void main(String[] args) {
		Length length1 = new Length(1.0,LengthUnit.FEET);
		Length length2 = new Length(12.0,LengthUnit.INCHES);
		System.out.println("Are lengths equal? "+ length1.equals(length2));
		
		Length length3 = new Length(1.0,LengthUnit.YARDS);
		Length length4 = new Length(36.0,LengthUnit.INCHES);
		System.out.println("Are lengths equal? "+ length3.equals(length4));
		
		Length length5 = new Length(100.0,LengthUnit.CENTIMETRES);
		Length length6 = new Length(39.3701,LengthUnit.INCHES);
		System.out.println("Are lengths equal? "+ length5.equals(length6));
	}
	

}
