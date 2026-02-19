package quantity_measurement_uc3;


public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0, LengthUnit.INCHES);

        System.out.println("Are Equal? " + length1.equals(length2));

        Length inch1 = new Length(1.0, LengthUnit.INCHES);
        Length inch2 = new Length(1.0, LengthUnit.INCHES);

        System.out.println("Are Equal? " + inch1.equals(inch2));
    }
}
