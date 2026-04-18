package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    @Autowired
    private IQuantityMeasurementService service;

    // ── POST: compare ─────────────────────────────────────────────────────────
    @Operation(summary = "Compare two quantities — returns true/false in resultString")
    @PostMapping("/compare")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /compare called");
        QuantityMeasurementDTO result =
                service.compareQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // ── POST: convert ─────────────────────────────────────────────────────────
    @Operation(summary = "Convert a quantity to another unit — result in resultValue")
    @PostMapping("/convert")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /convert called");
        QuantityMeasurementDTO result =
                service.convertQuantity(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // ── POST: add ─────────────────────────────────────────────────────────────
    @Operation(summary = "Add two quantities — result in resultValue / resultUnit")
    @PostMapping("/add")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /add called");
        QuantityMeasurementDTO result =
                service.addQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // ── POST: subtract ────────────────────────────────────────────────────────
    @Operation(summary = "Subtract second quantity from first — result in resultValue / resultUnit")
    @PostMapping("/subtract")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /subtract called");
        QuantityMeasurementDTO result =
                service.subtractQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // ── POST: divide ──────────────────────────────────────────────────────────
    @Operation(summary = "Divide first quantity by second — result ratio in resultValue")
    @PostMapping("/divide")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /divide called");
        QuantityMeasurementDTO result =
                service.divideQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // ── GET: history by operation ─────────────────────────────────────────────
    @Operation(summary = "Get operation history by operation type (COMPARE, CONVERT, ADD, SUBTRACT, DIVIDE)")
    @GetMapping("/history/operation/{operation}")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByOperation(
            @PathVariable String operation) {
        logger.info("GET /history/operation/{}", operation);
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    // ── GET: history by measurement type ─────────────────────────────────────
    @Operation(summary = "Get operation history by measurement type (LengthUnit, WeightUnit, etc.)")
    @GetMapping("/history/type/{measurementType}")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByType(
            @PathVariable String measurementType) {
        logger.info("GET /history/type/{}", measurementType);
        return ResponseEntity.ok(service.getHistoryByMeasurementType(measurementType));
    }

    // ── GET: count by operation ───────────────────────────────────────────────
    @Operation(summary = "Count successful operations by operation type")
    @GetMapping("/count/{operation}")
    public ResponseEntity<Long> countByOperation(@PathVariable String operation) {
        logger.info("GET /count/{}", operation);
        return ResponseEntity.ok(service.countByOperation(operation));
    }

    // ── GET: error history ────────────────────────────────────────────────────
    @Operation(summary = "Get all errored quantity measurement records")
    @GetMapping("/history/errored")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        logger.info("GET /history/errored");
        return ResponseEntity.ok(service.getErrorHistory());
    }
}
