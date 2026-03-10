package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {
	
	public static class Feet{
		
		private final double value ;
		
		public Feet(double value) {
			this.value = value;	
		}
		
		public double getValue() {
			return value;
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if(this == obj) return true;
			if(obj==null || getClass()!=obj.getClass()) return false;
			
			Feet other = (Feet) obj;
			
			return Double.compare(this.value, other.value)==0;
			
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(this.value);
		}
		
	}
	
	
	public static void main(String[] args) {
		
		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(1.0);
		
		boolean result = f1.equals(f2);
		
		System.out.println("For input : 1.0ft and 1.0ft\n"
				+ "Output is "+result);
	}

}
