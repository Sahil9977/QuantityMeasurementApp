package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// comparison
	public static <U extends IMeasurable> void demonstrateComparison(Quantity<U> q1, Quantity<U> q2) {
// static method isiliye yaha <U extends IMea > ye sab likhna pada nhi agar non static hota toh direct clas  ko genric banake work ho jata 
		boolean result = q1.equals(q2);

		System.out.println("Output: Equal(" + result + ")");
	}

	// conversion
	public static <U extends IMeasurable> void demonstrateConversion(Quantity<U> quantity, U targetUnit) {

		Quantity<U> converted = quantity.convertTo(targetUnit);

		System.out.println(quantity + " = " + converted);
	}

	// addition
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2) {

		if (q1 == null || q2 == null)
			throw new IllegalArgumentException("Quantities cannot be null");

		return q1.add(q2);
	}

	// addition with target unit
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {

		if (q1 == null || q2 == null)
			throw new IllegalArgumentException("Quantities cannot be null");

		return q1.add(q2, targetUnit);
	}

	// subtraction
	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2) {

		if (q1 == null || q2 == null)
			throw new IllegalArgumentException("Quantities cannot be null");

		return q1.subtract(q2);
	}

	// subtraction with target unit
	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {

		if (q1 == null || q2 == null)
			throw new IllegalArgumentException("Quantities cannot be null");

		return q1.subtract(q2, targetUnit);
	}

	// divison
	private static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {

		if (q1 == null || q2 == null)
			throw new IllegalArgumentException("Quantities cannot be null");

		return q1.divide(q2);
	}

	public static void main(String[] args) {

		// LENGTH
		System.out.println("LENGTH ");
		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		demonstrateComparison(l1, l2);
		demonstrateConversion(l1, LengthUnit.YARDS);

		System.out.println("Addition: " + demonstrateAddition(l1, l2));
		System.out.println("Target Addition: " + demonstrateAddition(l1, l2, LengthUnit.YARDS));

		System.out.println("Subtraction: " + demonstrateSubtraction(l1, l2));
		System.out.println("Target Subtraction: " + demonstrateSubtraction(l1, l2, LengthUnit.FEET));

		double ratio1 = demonstrateDivision(l1, l2);

		if (ratio1 > 1.0)
			System.out.println("First is larger");
		else if (ratio1 < 1.0)
			System.out.println("Second is larger");
		else
			System.out.println("Both are equivalent");

		// WEIGHT
		System.out.println("\nWEIGHT ");
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		demonstrateComparison(w1, w2);
		demonstrateConversion(w1, WeightUnit.POUND);

		System.out.println("Addition: " + demonstrateAddition(w1, w2));
		System.out.println("Target Addition: " + demonstrateAddition(w1, w2, WeightUnit.KILOGRAM));

		System.out.println("Subtraction: " + demonstrateSubtraction(w1, w2));
		System.out.println("Target Subtraction: " + demonstrateSubtraction(w1, w2, WeightUnit.GRAM));

		double ratio2 = demonstrateDivision(w1, w2);

		if (ratio2 > 1.0)
			System.out.println("First is larger");
		else if (ratio2 < 1.0)
			System.out.println("Second is larger");
		else
			System.out.println("Both are equivalent");

		// VOLUME
		System.out.println("\nVOLUME ");
		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		Quantity<VolumeUnit> v2 = new Quantity<>(3.785, VolumeUnit.LITRE);

		demonstrateComparison(v1, v2);
		demonstrateConversion(v1, VolumeUnit.MILLILITRE);

		System.out.println("Addition: " + demonstrateAddition(v1, v2));
		System.out.println("Target Addition: " + demonstrateAddition(v1, v2, VolumeUnit.LITRE));

		System.out.println("Subtraction: " + demonstrateSubtraction(v1, v2));
		System.out.println("Target Subtraction: " + demonstrateSubtraction(v1, v2, VolumeUnit.LITRE));

		double ratio3 = demonstrateDivision(v1, v2);

		if (ratio3 > 1.0)
			System.out.println("First is larger");
		else if (ratio3 < 1.0)
			System.out.println("Second is larger");
		else
			System.out.println("Both are equivalent");

	}

}