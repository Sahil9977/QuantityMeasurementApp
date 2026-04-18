package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * UC16 – Enhanced repository interface.
 *
 * New additions over UC15:
 *   - getMeasurementsByMeasurementType() – filter by LENGTH, WEIGHT, etc.
 *   - getTotalCount()                    – count all records
 *   - deleteAll()                        – remove all records (useful for testing)
 *   - getPoolStatistics()                – default method (DB impl overrides)
 *   - releaseResources()                 – default method (DB impl overrides)
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> findAll();

    List<QuantityMeasurementEntity> findByOperationType(String operationType);

    /** UC16 NEW – filter by measurement type stored inside operand strings */
    List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

    /** UC16 NEW */
    int getTotalCount();

    /** UC16 NEW – clear() renamed deleteAll() for clarity; clear() delegates here */
    void deleteAll();

    /** Backward-compatible alias */
    default void clear() { deleteAll(); }

    /** UC16 NEW default – overridden by ConnectionPool-backed implementations */
    default String getPoolStatistics() {
        return "No pool statistics available for this repository type.";
    }

    /** UC16 NEW default – overridden to close JDBC connections / pool */
    default void releaseResources() {
        // no-op for cache repository
    }
}
