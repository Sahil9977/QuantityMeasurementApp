package com.apps.quantitymeasurement;



@FunctionalInterface
interface SupportsArithmetic {
    boolean isSupported();
}

public interface IMeasurable {

    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();

    // all units support arithmetic (Length, Weight, Volume)
    SupportsArithmetic supportsArithmetic = () -> true;

    //  if arithmetic is supported
    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    //  Hook method – only special units (like Temperature) will override
    default void validateOperationSupport(String operation) {
        
    }
}