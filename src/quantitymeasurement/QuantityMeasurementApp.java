package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.Length.LengthUnit;

public class QuantityMeasurementApp {

	public static void demonstrateLengthComparison(Length l1 , Length l2) {
		
		boolean result = l1.equals(l2);
		
		System.out.println("Output: Equal("+result+")");
	}
	
	public static void main(String[] args) {
		
		demonstrateLengthComparison( new Length(1.0 , LengthUnit.FEET ),
				new Length(12.0 , LengthUnit.INCHES));
		
		demonstrateLengthComparison( new Length(1.0 , LengthUnit.YARDS ),
				new Length(36.0 , LengthUnit.INCHES));
		
		demonstrateLengthComparison( new Length(100.0 , LengthUnit.CENTIMETRES ),
				new Length(39.3701 , LengthUnit.INCHES));
		
		demonstrateLengthComparison( new Length(3.0 , LengthUnit.FEET ),
				new Length(1.0 , LengthUnit.YARDS));
		
		demonstrateLengthComparison( new Length(30.48 , LengthUnit.CENTIMETRES ),
				new Length(1.0 , LengthUnit.FEET));
	}

}
