package com.apps.quantitymeasurement.core;


/**
 * UC15 Change: Added two new helper methods —
 *   getMeasurementType() : returns the category name (e.g., "LENGTH")
 *   getUnitByName(String) : returns an enum constant from its string name
 *
 * WHY: The ServiceImpl needs to reconstruct a typed IMeasurable from a plain
 * String stored in QuantityDTO (because DTOs carry only strings/primitives,
 * not live enum references).  Having these methods on the interface keeps
 * the service layer free of big if-else / switch blocks.
 */
@FunctionalInterface
interface SupportsArithmetic {
    boolean isSupported();
}

public interface IMeasurable {

    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();

    /**
     * UC15 NEW – returns the measurement category name.
     * e.g. LengthUnit.FEET.getMeasurementType() → "LENGTH"
     */
    String getMeasurementType();

    /**
     * UC15 NEW – returns an enum constant of this type by name.
     * e.g. LengthUnit.FEET.getUnitByName("YARDS") → LengthUnit.YARDS
     */
    IMeasurable getUnitByName(String unitName);

    // all units support arithmetic (Length, Weight, Volume)
    SupportsArithmetic supportsArithmetic = () -> true;

    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    // Hook method – only Temperature will override
    default void validateOperationSupport(String operation) {
        // no-op by default
    }
}