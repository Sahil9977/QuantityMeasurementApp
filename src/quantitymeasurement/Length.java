package com.apps.quantitymeasurement;

public class Length {

   
    private double value;

    private LengthUnit unit;

  
    public enum LengthUnit {

        FEET(12.0),       // 1 feet = 12 inch 
        INCHES(1.0),        
        YARDS(36.0),        // 1 yard = 36 inch 
        CENTIMETRES(0.393701); 

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

     
        public double getConversionFactor() {
            return conversionFactor;
        }
    }


    public Length(double value, LengthUnit unit) {

      
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be a finite number");

        this.value = value;
        this.unit = unit;
    }
    
    // base unit is in inch coz we have define the value as per inchlike 1 feet = 12 inch 1 yard = 36 inch like that
    public double convertToBaseUnit() {
        return this.value * this.unit.getConversionFactor();
    }

 
    public static double convert(double value, LengthUnit source, LengthUnit target) {

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");

        if (source == null || target == null)
            throw new IllegalArgumentException("Units cannot be null");

        //convert source value to base unit
        double baseValue = value * source.getConversionFactor();

        //  convert base unit to target unit
        double result = baseValue / target.getConversionFactor();

        return result;
    }


    public Length convertTo(LengthUnit targetUnit) {

        double convertedValue = convert(this.value, this.unit, targetUnit);

        return new Length(convertedValue, targetUnit);
    }
    
    
    public Length add(Length other) {
    	
    	if(other ==null) throw new IllegalArgumentException("length can not be null");
    	
    	double value1 = this.convertToBaseUnit();
    	double value2 = other.convertToBaseUnit();
    	
    	double sum = value1 + value2;
    	double converted = sum/this.unit.getConversionFactor();
    	
    	return new Length(converted , this.unit);
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Length other = (Length) obj;

        return Math.abs(this.convertToBaseUnit() - other.convertToBaseUnit()) <= 0.001;
    }


    @Override
    public int hashCode() {
        return Double.hashCode(this.convertToBaseUnit());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }

    public static void main(String[] args) {

        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0, LengthUnit.INCHES);

        System.out.println("Are lengths equal? " + length1.equals(length2));

      
        double result = Length.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);

        System.out.println("Converted: 1 FEET  to " + result + " INCHES");
        
        System.out.println(length1.add(length2));
        
        
    }
}