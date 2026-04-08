package com.apps.quantitymeasurement.exception;


/**
 * UC15 NEW CLASS
 *
 * WHY THIS EXISTS:
 *   Without a custom exception, the service would leak internal exception types
 *   (IllegalArgumentException, UnsupportedOperationException, ArithmeticException)
 *   up to the controller, which then has to know about all of them.
 *
 *   QuantityMeasurementException wraps every domain error into one type.
 *   The controller only catches THIS class — it doesn't care about internals.
 *
 * WHY EXTENDS RuntimeException:
 *   Unchecked — callers are not forced to declare or catch it, but can if they
 *   want consistent error handling (like the controller does).
 */
public class QuantityMeasurementException extends RuntimeException {

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}