package quantity_measurement_uc1;


public class QuantityMeasurementApp {

 public static class Feet {

     private final double value;

     public Feet(double value) {
    	
         this.value = value;
     }

     public double getValue() {
         return value;
     }

     @Override
     public boolean equals(Object obj) {

         if (this == obj) return true;

         if (obj == null || getClass() != obj.getClass())
             return false;

         Feet other = (Feet) obj;

         return Double.compare(this.value, other.value) == 0;
     }
     
     @Override
     public int hashCode() {
         return Double.hashCode(value);
     }
 }

 public static void main(String[] args) {

     Feet feet1 = new Feet(1.0);
     Feet feet2 = new Feet(1.0);

     boolean result = feet1.equals(feet2);

     System.out.println("Input: 1.0 ft and 1.0 ft");
     System.out.println("Output: Equal (" + result + ")");
 }
}
