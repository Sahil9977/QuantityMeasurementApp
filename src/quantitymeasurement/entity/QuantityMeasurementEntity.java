package com.apps.quantitymeasurement.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UC15 NEW CLASS
 *
 * WHY THIS EXISTS:
 *   Every operation the service performs (comparison, conversion, addition, …)
 *   should be recorded for audit / history / debugging.  This entity is the
 *   record that gets persisted in the repository.
 *
 * IMMUTABILITY INTENT:
 *   Fields would ideally be final, but Java Object Serialization requires a
 *   no-arg constructor and does not work reliably with final fields when
 *   loading from disk.  So fields are non-final, but all mutation happens
 *   only inside constructors — treat it as immutable from the outside.
 *
 * SERIALIZABLE:
 *   Implements Serializable so the CacheRepository can write objects to a
 *   .ser file on disk, preserving history across JVM restarts.
 *
 * CONSTRUCTORS:
 *   Three constructors cover the three cases the service needs:
 *     1. Single-operand operation   (conversion)
 *     2. Binary operation           (add, subtract, compare, divide)
 *     3. Error result               (any failed operation)
 */
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── Fields ────────────────────────────────────────────────────────────────
    private String operationType;      // "COMPARE", "CONVERT", "ADD", "SUBTRACT", "DIVIDE"
    private String operand1;           // toString of first QuantityDTO
    private String operand2;           // toString of second QuantityDTO (null for unary)
    private String result;             // human-readable result
    private boolean hasError;
    private String errorMessage;
    private LocalDateTime timestamp;

    // ── Constructor: single-operand (e.g. conversion) ─────────────────────────
    public QuantityMeasurementEntity(String operationType, String operand1, String result) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = null;
        this.result        = result;
        this.hasError      = false;
        this.errorMessage  = null;
        this.timestamp     = LocalDateTime.now();
    }

    // ── Constructor: binary operation (e.g. add, compare) ────────────────────
    public QuantityMeasurementEntity(String operationType, String operand1, String operand2, String result) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = operand2;
        this.result        = result;
        this.hasError      = false;
        this.errorMessage  = null;
        this.timestamp     = LocalDateTime.now();
    }

    // ── Constructor: error result ─────────────────────────────────────────────
    public QuantityMeasurementEntity(String operationType, String operand1, String operand2, String errorMessage, boolean hasError) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = operand2;
        this.result        = null;
        this.hasError      = hasError;
        this.errorMessage  = errorMessage;
        this.timestamp     = LocalDateTime.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String        getOperationType() { return operationType; }
    public String        getOperand1()      { return operand1; }
    public String        getOperand2()      { return operand2; }
    public String        getResult()        { return result; }
    public boolean       hasError()         { return hasError; }
    public String        getErrorMessage()  { return errorMessage; }
    public LocalDateTime getTimestamp()     { return timestamp; }

    @Override
    public String toString() {
        if (hasError)
            return "[" + timestamp + "] " + operationType + " | ERROR: " + errorMessage;
        return "[" + timestamp + "] " + operationType
                + " | op1=" + operand1
                + (operand2 != null ? " | op2=" + operand2 : "")
                + " | result=" + result;
    }
}