package quantity_measurement_uc4;

public class QuantityMeasurementApp {

    public static boolean demonstrateLengthEquality(Length l1, Length l2) {
        return l1.equals(l2);
    }

    public static void main(String[] args) {

        System.out.println(demonstrateLengthEquality(
                new Length(1.0, Length.LengthUnit.YARDS),
                new Length(3.0, Length.LengthUnit.FEET)));  // true

        System.out.println(demonstrateLengthEquality(
                new Length(1.0, Length.LengthUnit.YARDS),
                new Length(36.0, Length.LengthUnit.INCHES)));  // true

        System.out.println(demonstrateLengthEquality(
                new Length(1.0, Length.LengthUnit.CENTIMETERS),
                new Length(0.393701, Length.LengthUnit.INCHES)));  // true
    }
}