package com.apps.quantitymeasurement.model;

import com.apps.quantitymeasurement.core.IMeasurable;

/**
 * UC15 NEW CLASS
 *
 * WHY THIS EXISTS:
 *   QuantityDTO uses plain Strings (unitName, measurementType) because it must
 *   be serializable and framework-agnostic.  But the ServiceImpl needs a
 *   typed, generic object to perform type-safe calculations.
 *
 *   QuantityModel<U> is that internal typed representation.  The ServiceImpl
 *   converts QuantityDTO → QuantityModel before doing any math, and converts
 *   back to QuantityDTO before returning a result.
 *
 * DIFFERENCE FROM Quantity<U>:
 *   - Quantity<U> contains business behaviour (add, subtract, convertTo, …)
 *   - QuantityModel<U> is a POJO — only holds value + unit, no methods
 *
 * SCOPE: Used only within the service layer. Never exposed to controller.
 */
public class QuantityModel<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        if (unit == null)  throw new IllegalArgumentException("Unit cannot be null in QuantityModel");
        this.value = value;
        this.unit  = unit;
    }

    public double getValue() { return value; }
    public U      getUnit()  { return unit; }

    @Override
    public String toString() {
        return "QuantityModel(" + value + ", " + unit.getUnitName() + ")";
    }
}