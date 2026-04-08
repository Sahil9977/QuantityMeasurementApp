package com.apps.quantitymeasurement.repository;


import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import java.util.List;

/**
 * UC15 NEW INTERFACE
 *
 * WHY THIS EXISTS (Interface Segregation Principle):
 *   The ServiceImpl should NOT depend directly on a concrete cache class or a
 *   database class.  It depends on this interface.  Today the implementation is
 *   an in-memory ArrayList + disk file (CacheRepository).  Tomorrow it could be
 *   JPA / Hibernate / MongoDB — the service code would not change at all.
 *
 * METHODS:
 *   save()           – persist one entity (after every operation)
 *   findAll()        – retrieve the full history
 *   findByType()     – filter by operation type (useful for reports)
 *   clear()          – wipe the store (useful in tests)
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> findAll();

    List<QuantityMeasurementEntity> findByOperationType(String operationType);

    void clear();
}