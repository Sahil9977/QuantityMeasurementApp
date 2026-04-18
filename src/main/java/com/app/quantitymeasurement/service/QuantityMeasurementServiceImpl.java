package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null)
            throw new IllegalArgumentException("Repository cannot be null");
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl initialised");
    }

    // ── COMPARE ──────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);

            boolean equal = Math.abs(
                    unit1.convertToBaseUnit(q1.getValue()) -
                    unit2.convertToBaseUnit(q2.getValue())) <= 0.001;

            String resultStr = String.valueOf(equal);
            repository.save(new QuantityMeasurementEntity("COMPARE",
                    q1.toString(), q2.toString(), resultStr));
            logger.debug("COMPARE {} == {} → {}", q1, q2, equal);

            return new QuantityDTO(equal ? 1.0 : 0.0, resultStr, "BOOLEAN");

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("COMPARE",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Compare failed: " + e.getMessage(), e);
        }
    }

    // ── CONVERT ──────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO convert(QuantityDTO source, QuantityDTO targetUnitDTO) {
        validateNotNull(source, targetUnitDTO);
        try {
            validateSameCategory(source, targetUnitDTO);

            IMeasurable srcUnit    = resolveUnit(source);
            IMeasurable targetUnit = resolveUnit(targetUnitDTO);

            double baseValue      = srcUnit.convertToBaseUnit(source.getValue());
            double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

            QuantityDTO result = new QuantityDTO(convertedValue,
                    targetUnit.getUnitName(), targetUnitDTO.getMeasurementType());

            repository.save(new QuantityMeasurementEntity("CONVERT",
                    source.toString(), result.toString()));
            logger.debug("CONVERT {} → {}", source, result);

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("CONVERT",
                    source.toString(), targetUnitDTO.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Convert failed: " + e.getMessage(), e);
        }
    }

    // ── ADD ──────────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            unit1.validateOperationSupport(ArithmeticOperation.ADD.name());

            double baseResult = ArithmeticOperation.ADD.apply(
                    unit1.convertToBaseUnit(q1.getValue()),
                    unit2.convertToBaseUnit(q2.getValue()));
            double converted  = unit1.convertFromBaseUnit(baseResult);

            QuantityDTO result = new QuantityDTO(converted, unit1.getUnitName(), q1.getMeasurementType());
            repository.save(new QuantityMeasurementEntity("ADD",
                    q1.toString(), q2.toString(), result.toString()));
            logger.debug("ADD {} + {} = {}", q1, q2, result);

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("ADD",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Add failed: " + e.getMessage(), e);
        }
    }

    // ── SUBTRACT ─────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            unit1.validateOperationSupport(ArithmeticOperation.SUBTRACT.name());

            double baseResult = ArithmeticOperation.SUBTRACT.apply(
                    unit1.convertToBaseUnit(q1.getValue()),
                    unit2.convertToBaseUnit(q2.getValue()));
            double converted  = unit1.convertFromBaseUnit(baseResult);

            QuantityDTO result = new QuantityDTO(converted, unit1.getUnitName(), q1.getMeasurementType());
            repository.save(new QuantityMeasurementEntity("SUBTRACT",
                    q1.toString(), q2.toString(), result.toString()));
            logger.debug("SUBTRACT {} - {} = {}", q1, q2, result);

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("SUBTRACT",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Subtract failed: " + e.getMessage(), e);
        }
    }

    // ── DIVIDE ───────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            unit1.validateOperationSupport(ArithmeticOperation.DIVIDE.name());

            double ratio = ArithmeticOperation.DIVIDE.apply(
                    unit1.convertToBaseUnit(q1.getValue()),
                    unit2.convertToBaseUnit(q2.getValue()));

            QuantityDTO result = new QuantityDTO(ratio, "RATIO", "DIMENSIONLESS");
            repository.save(new QuantityMeasurementEntity("DIVIDE",
                    q1.toString(), q2.toString(), result.toString()));
            logger.debug("DIVIDE {} / {} = {}", q1, q2, ratio);

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("DIVIDE",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Divide failed: " + e.getMessage(), e);
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private IMeasurable resolveUnit(QuantityDTO dto) {
        switch (dto.getMeasurementType().toUpperCase()) {
            case "LENGTH":      return LengthUnit.valueOf(dto.getUnitName().toUpperCase());
            case "WEIGHT":      return WeightUnit.valueOf(dto.getUnitName().toUpperCase());
            case "VOLUME":      return VolumeUnit.valueOf(dto.getUnitName().toUpperCase());
            case "TEMPERATURE": return TemperatureUnit.valueOf(dto.getUnitName().toUpperCase());
            default: throw new QuantityMeasurementException(
                    "Unknown measurement type: " + dto.getMeasurementType());
        }
    }

    private void validateNotNull(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null)
            throw new QuantityMeasurementException("QuantityDTO inputs cannot be null");
    }

    private void validateSameCategory(QuantityDTO q1, QuantityDTO q2) {
        if (!q1.getMeasurementType().equalsIgnoreCase(q2.getMeasurementType()))
            throw new QuantityMeasurementException(
                    "Cannot operate on different categories: "
                    + q1.getMeasurementType() + " vs " + q2.getMeasurementType());
    }
}
