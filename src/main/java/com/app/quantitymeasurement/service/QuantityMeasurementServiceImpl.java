package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    @Autowired
    private QuantityMeasurementRepository repository;

    // ── COMPARE ──────────────────────────────────────────────────────────────

    @Override
    public QuantityMeasurementDTO compareQuantities(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(q1.getValue());
        entity.setThisUnit(q1.getUnit());
        entity.setThisMeasurementType(q1.getMeasurementType());
        entity.setThatValue(q2.getValue());
        entity.setThatUnit(q2.getUnit());
        entity.setThatMeasurementType(q2.getMeasurementType());
        entity.setOperation("compare");
        try {
            validateSameCategory(q1, q2);
            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            boolean equal = Math.abs(
                    unit1.convertToBaseUnit(q1.getValue()) -
                    unit2.convertToBaseUnit(q2.getValue())) <= 0.001;
            entity.setResultString(String.valueOf(equal));
            entity.setError(false);
            logger.debug("COMPARE {} {} → {}", q1, q2, equal);
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            throw new QuantityMeasurementException("compare Error: " + e.getMessage(), e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── CONVERT ──────────────────────────────────────────────────────────────

    @Override
    public QuantityMeasurementDTO convertQuantity(QuantityDTO source, QuantityDTO targetUnit) {
        validateNotNull(source, targetUnit);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(source.getValue());
        entity.setThisUnit(source.getUnit());
        entity.setThisMeasurementType(source.getMeasurementType());
        entity.setThatValue(targetUnit.getValue());
        entity.setThatUnit(targetUnit.getUnit());
        entity.setThatMeasurementType(targetUnit.getMeasurementType());
        entity.setOperation("convert");
        try {
            validateSameCategory(source, targetUnit);
            IMeasurable srcUnit  = resolveUnit(source);
            IMeasurable tgtUnit  = resolveUnit(targetUnit);
            double base          = srcUnit.convertToBaseUnit(source.getValue());
            double converted     = tgtUnit.convertFromBaseUnit(base);
            entity.setResultValue(converted);
            entity.setError(false);
            logger.debug("CONVERT {} → {} {}", source, converted, tgtUnit.getUnitName());
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            throw new QuantityMeasurementException("convert Error: " + e.getMessage(), e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── ADD ──────────────────────────────────────────────────────────────────

    @Override
    public QuantityMeasurementDTO addQuantities(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(q1.getValue());
        entity.setThisUnit(q1.getUnit());
        entity.setThisMeasurementType(q1.getMeasurementType());
        entity.setThatValue(q2.getValue());
        entity.setThatUnit(q2.getUnit());
        entity.setThatMeasurementType(q2.getMeasurementType());
        entity.setOperation("add");
        try {
            validateSameCategory(q1, q2);
            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            unit1.validateOperationSupport(ArithmeticOperation.ADD.name());
            double base      = ArithmeticOperation.ADD.apply(
                    unit1.convertToBaseUnit(q1.getValue()),
                    unit2.convertToBaseUnit(q2.getValue()));
            double converted = unit1.convertFromBaseUnit(base);
            entity.setResultValue(converted);
            entity.setResultUnit(unit1.getUnitName());
            entity.setResultMeasurementType(q1.getMeasurementType());
            entity.setError(false);
            logger.debug("ADD {} + {} = {}", q1, q2, converted);
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            throw new QuantityMeasurementException("add Error: " + e.getMessage(), e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── SUBTRACT ─────────────────────────────────────────────────────────────

    @Override
    public QuantityMeasurementDTO subtractQuantities(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(q1.getValue());
        entity.setThisUnit(q1.getUnit());
        entity.setThisMeasurementType(q1.getMeasurementType());
        entity.setThatValue(q2.getValue());
        entity.setThatUnit(q2.getUnit());
        entity.setThatMeasurementType(q2.getMeasurementType());
        entity.setOperation("subtract");
        try {
            validateSameCategory(q1, q2);
            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            unit1.validateOperationSupport(ArithmeticOperation.SUBTRACT.name());
            double base      = ArithmeticOperation.SUBTRACT.apply(
                    unit1.convertToBaseUnit(q1.getValue()),
                    unit2.convertToBaseUnit(q2.getValue()));
            double converted = unit1.convertFromBaseUnit(base);
            entity.setResultValue(converted);
            entity.setResultUnit(unit1.getUnitName());
            entity.setResultMeasurementType(q1.getMeasurementType());
            entity.setError(false);
            logger.debug("SUBTRACT {} - {} = {}", q1, q2, converted);
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            throw new QuantityMeasurementException("subtract Error: " + e.getMessage(), e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── DIVIDE ───────────────────────────────────────────────────────────────

    @Override
    public QuantityMeasurementDTO divideQuantities(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(q1.getValue());
        entity.setThisUnit(q1.getUnit());
        entity.setThisMeasurementType(q1.getMeasurementType());
        entity.setThatValue(q2.getValue());
        entity.setThatUnit(q2.getUnit());
        entity.setThatMeasurementType(q2.getMeasurementType());
        entity.setOperation("divide");
        try {
            validateSameCategory(q1, q2);
            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);
            unit1.validateOperationSupport(ArithmeticOperation.DIVIDE.name());
            double ratio = ArithmeticOperation.DIVIDE.apply(
                    unit1.convertToBaseUnit(q1.getValue()),
                    unit2.convertToBaseUnit(q2.getValue()));
            entity.setResultValue(ratio);
            entity.setResultUnit("RATIO");
            entity.setResultMeasurementType("DIMENSIONLESS");
            entity.setError(false);
            logger.debug("DIVIDE {} / {} = {}", q1, q2, ratio);
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            throw new QuantityMeasurementException("divide Error: " + e.getMessage(), e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── HISTORY / AUDIT ───────────────────────────────────────────────────────

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByOperation(operation.toLowerCase()));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public long countByOperation(String operation) {
        return repository.countByOperationAndErrorFalse(operation.toLowerCase());
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private IMeasurable resolveUnit(QuantityDTO dto) {
        switch (dto.getMeasurementType()) {
            case "LengthUnit":      return LengthUnit.valueOf(dto.getUnit().toUpperCase());
            case "WeightUnit":      return WeightUnit.valueOf(dto.getUnit().toUpperCase());
            case "VolumeUnit":      return VolumeUnit.valueOf(dto.getUnit().toUpperCase());
            case "TemperatureUnit": return TemperatureUnit.valueOf(dto.getUnit().toUpperCase());
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
                    "Cannot perform arithmetic between different measurement categories: "
                    + q1.getMeasurementType() + " and " + q2.getMeasurementType());
    }

    /** Convert incoming QuantityDTO (with measurementType like "LengthUnit") to model. */
    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable unit = resolveUnit(dto);
        return new QuantityModel<>(dto.getValue(), unit);
    }
}
