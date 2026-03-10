package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.Length.LengthUnit;

public class QuantityMeasurementApp {

	
	public static void demonstrateLengthEquality() {

		Length l1 = new Length(1.0 , LengthUnit.FEET);
		Length l2 = new Length(12.0 , LengthUnit.INCHES);
		
		boolean result = l1.equals(l2);
		
		System.out.println("Input : 1.0 ft and 12.0 inches\n"
				+ "Output: Equal("+result+")");
	}
	
	public static void demonstrateFeetEquality() {

		Length f1 = new Length(1.0 , LengthUnit.FEET);
		Length f2 = new Length(1.0 , LengthUnit.FEET);
		
		boolean result = f1.equals(f2);
		
		System.out.println("Input : 1.0 ft and 1.0 ft\n"
				+ "Output: Equal("+result+")");
	}

		
	public static void demonstrateInchesEquality() {
		
		Length i1 = new Length(1.0 , LengthUnit.INCHES);
		Length i2 = new Length(1.0 , LengthUnit.INCHES);
		
		boolean result = i1.equals(i2);
		
		System.out.println("Input : 1.0 inch and 1.0 inch\n"
				+ "Output: Equal("+result+")");
	

	}
	
	
	public static void main(String[] args) {
		
		demonstrateLengthEquality();
		demonstrateFeetEquality();
		demonstrateInchesEquality();
		
	}

}
