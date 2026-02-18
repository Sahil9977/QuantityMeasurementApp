package quantity_measurement_uc2;



public class QuantityMeasurementApp {

    public static class Feet {

        private final double value;

        public Feet(double value) {
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException();
            }
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            Feet other = (Feet) obj;

            return Double.compare(this.value, other.value) == 0;
        }
    }

    // ==============================
    // Inner Class: Inches
    // ==============================
    public static class Inches {

        private final double value;

        public Inches(double value) {
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException("Inches value must be numeric");
            }
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            Inches other = (Inches) obj;

            return Double.compare(this.value, other.value) == 0;
        }
    }


    public static boolean demonstrateFeetEquality() {

        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);

        boolean result = f1.equals(f2);

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + result + ")");

        return result;
    }

    public static boolean demonstrateInchesEquality() {

        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);

        boolean result = i1.equals(i2);

        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + result + ")");

        return result;
    }

    public static void main(String[] args) {

        demonstrateFeetEquality();
        demonstrateInchesEquality();
    }
}
