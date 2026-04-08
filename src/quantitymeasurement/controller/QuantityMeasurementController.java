package com.apps.quantitymeasurement.controller;


import com.apps.quantitymeasurement.DTO.QuantityDTO;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;

/**
 *
 * RESPONSIBILITIES:
 *   - Accept QuantityDTO inputs (future: map from HTTP request body)
 *   - Call the appropriate service method
 *   - Print / format the result for the user
 *   - Catch QuantityMeasurementException and display errors cleanly
 *   - NO business logic lives here — fully delegated to the service
 *
 *
 * REST READY:
 *   Methods are named performXXX (not demonstrateXXX) to signal they map
 *   naturally to REST verbs:
 *     POST /api/quantity/compare   → performComparison
 *     POST /api/quantity/convert   → performConversion
 *     POST /api/quantity/add       → performAddition
 *     POST /api/quantity/subtract  → performSubtraction
 *     POST /api/quantity/divide    → performDivision
 *
 * DEPENDENCY INJECTION:
 *   Service injected via constructor — controller never instantiates the impl.
 */
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null)
            throw new IllegalArgumentException("Service cannot be null");
        this.service = service;
    }

    // ── COMPARE ───────────────────────────────────────────────────────────────

    public void performComparison(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.compare(q1, q2);
            boolean equal = result.getValue() == 1.0;
            System.out.println("Comparison: " + q1 + " == " + q2
                    + " → Equal(" + equal + ")");
        } catch (QuantityMeasurementException e) {
            displayError("Comparison", e);
        }
    }

    // ── CONVERT ───────────────────────────────────────────────────────────────

    public void performConversion(QuantityDTO source, QuantityDTO targetUnitDTO) {
        try {
            QuantityDTO result = service.convert(source, targetUnitDTO);
            System.out.println("Conversion: " + source + " = " + result);
        } catch (QuantityMeasurementException e) {
            displayError("Conversion", e);
        }
    }

    // ── ADD ───────────────────────────────────────────────────────────────────

    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.add(q1, q2);
            System.out.println("Addition: " + q1 + " + " + q2 + " = " + result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Addition", e);
            return null;
        }
    }

    // ── SUBTRACT ──────────────────────────────────────────────────────────────

    public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.subtract(q1, q2);
            System.out.println("Subtraction: " + q1 + " - " + q2 + " = " + result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Subtraction", e);
            return null;
        }
    }

    // ── DIVIDE ────────────────────────────────────────────────────────────────

    public void performDivision(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.divide(q1, q2);
            double ratio = result.getValue();
            String comparison = ratio > 1.0 ? "First is larger"
                             : ratio < 1.0 ? "Second is larger"
                             : "Both are equivalent";
            System.out.println("Division ratio: " + ratio + " → " + comparison);
        } catch (QuantityMeasurementException e) {
            displayError("Division", e);
        }
    }

    // ── Private helper ────────────────────────────────────────────────────────

    private void displayError(String operation, QuantityMeasurementException e) {
        System.out.println(operation + " failed: " + e.getMessage());
    }
}