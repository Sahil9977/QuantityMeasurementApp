package com.app.quantitymeasurement.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private long          id;            // DB-assigned; 0 for in-memory
    private String        operationType;
    private String        operand1;
    private String        operand2;
    private String        result;
    private boolean       hasError;
    private String        errorMessage;
    private LocalDateTime timestamp;

    // Constructor: single operand (e.g. conversion)
    public QuantityMeasurementEntity(String operationType, String operand1, String result) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = null;
        this.result        = result;
        this.hasError      = false;
        this.errorMessage  = null;
        this.timestamp     = LocalDateTime.now();
    }

    // Constructor: binary operation
    public QuantityMeasurementEntity(String operationType, String operand1, String operand2, String result) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = operand2;
        this.result        = result;
        this.hasError      = false;
        this.errorMessage  = null;
        this.timestamp     = LocalDateTime.now();
    }

    // Constructor: error result
    public QuantityMeasurementEntity(String operationType, String operand1, String operand2,
                                     String errorMessage, boolean hasError) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = operand2;
        this.result        = null;
        this.hasError      = hasError;
        this.errorMessage  = errorMessage;
        this.timestamp     = LocalDateTime.now();
    }

    // Getters
    public long          getId()            { return id; }
    public String        getOperationType() { return operationType; }
    public String        getOperand1()      { return operand1; }
    public String        getOperand2()      { return operand2; }
    public String        getResult()        { return result; }
    public boolean       hasError()         { return hasError; }
    public String        getErrorMessage()  { return errorMessage; }
    public LocalDateTime getTimestamp()     { return timestamp; }

    // Setters needed for DB hydration
    public void setId(long id)                        { this.id = id; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

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
