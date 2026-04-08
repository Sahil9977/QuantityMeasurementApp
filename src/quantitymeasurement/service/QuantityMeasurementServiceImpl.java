package com.apps.quantitymeasurement.service;


import com.apps.quantitymeasurement.*;
import com.apps.quantitymeasurement.DTO.QuantityDTO;
import com.apps.quantitymeasurement.core.*;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.QuantityModel;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;

/**
 * UC15 NEW CLASS — core business logic layer
 *
 * WHY THIS EXISTS:
 *   In UC14, all logic lived inside QuantityMeasurementApp (main method +
 *   static demonstrate* methods). That violated SRP — the app class was doing
 *   UI, business logic, and wiring all at once.
 *
 *   This class takes over ALL business logic:
 *     - DTO → QuantityModel conversion
 *     - Delegating math to Quantity<U>
 *     - Exception wrapping into QuantityMeasurementException
 *     - Saving every operation to the repository
 *     - Returning results as QuantityDTO
 *
 * DEPENDENCY INJECTION:
 *   The repository is injected via constructor — the service does not create
 *   it. This allows tests to inject a mock repository easily.
 *
 * BROAD STEPS PER OPERATION:
 *   1. Accept QuantityDTO input
 *   2. Resolve to QuantityModel (typed)
 *   3. Build Quantity<U> objects for math
 *   4. Perform operation
 *   5. Catch any exception → QuantityMeasurementException
 *   6. Save QuantityMeasurementEntity to repository
 *   7. Return result as QuantityDTO
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null)
            throw new IllegalArgumentException("Repository cannot be null");
        this.repository = repository;
    }

    // ── COMPARE ───────────────────────────────────────────────────────────────

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

            return new QuantityDTO(equal ? 1.0 : 0.0, resultStr, "BOOLEAN");

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("COMPARE",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Compare failed: " + e.getMessage(), e);
        }
    }

    // ── CONVERT ───────────────────────────────────────────────────────────────

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

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("CONVERT",
                    source.toString(), targetUnitDTO.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Convert failed: " + e.getMessage(), e);
        }
    }

    // ── ADD ───────────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);

            // validateOperationSupport throws UnsupportedOperationException for Temperature
            unit1.validateOperationSupport(ArithmeticOperation.ADD.name());

            double base1      = unit1.convertToBaseUnit(q1.getValue());
            double base2      = unit2.convertToBaseUnit(q2.getValue());
            double baseResult = ArithmeticOperation.ADD.apply(base1, base2);
            double converted  = unit1.convertFromBaseUnit(baseResult);

            QuantityDTO result = new QuantityDTO(converted, unit1.getUnitName(), q1.getMeasurementType());

            repository.save(new QuantityMeasurementEntity("ADD",
                    q1.toString(), q2.toString(), result.toString()));

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("ADD",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Add failed: " + e.getMessage(), e);
        }
    }

    // ── SUBTRACT ──────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);

            unit1.validateOperationSupport(ArithmeticOperation.SUBTRACT.name());

            double base1      = unit1.convertToBaseUnit(q1.getValue());
            double base2      = unit2.convertToBaseUnit(q2.getValue());
            double baseResult = ArithmeticOperation.SUBTRACT.apply(base1, base2);
            double converted  = unit1.convertFromBaseUnit(baseResult);

            QuantityDTO result = new QuantityDTO(converted, unit1.getUnitName(), q1.getMeasurementType());

            repository.save(new QuantityMeasurementEntity("SUBTRACT",
                    q1.toString(), q2.toString(), result.toString()));

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("SUBTRACT",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Subtract failed: " + e.getMessage(), e);
        }
    }

    // ── DIVIDE ────────────────────────────────────────────────────────────────

    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        validateNotNull(q1, q2);
        try {
            validateSameCategory(q1, q2);

            IMeasurable unit1 = resolveUnit(q1);
            IMeasurable unit2 = resolveUnit(q2);

            unit1.validateOperationSupport(ArithmeticOperation.DIVIDE.name());

            double base1  = unit1.convertToBaseUnit(q1.getValue());
            double base2  = unit2.convertToBaseUnit(q2.getValue());
            double ratio  = ArithmeticOperation.DIVIDE.apply(base1, base2);

            QuantityDTO result = new QuantityDTO(ratio, "RATIO", "DIMENSIONLESS");

            repository.save(new QuantityMeasurementEntity("DIVIDE",
                    q1.toString(), q2.toString(), result.toString()));

            return result;

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("DIVIDE",
                    q1.toString(), q2.toString(), e.getMessage(), true));
            throw new QuantityMeasurementException("Divide failed: " + e.getMessage(), e);
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Resolves a QuantityDTO's measurementType + unitName back to a live IMeasurable.
     * Uses getMeasurementType() and getUnitByName() added to IMeasurable in UC15.
     *
     * WHY: QuantityDTO carries only Strings. To do math we need the enum instance.
     */
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
