package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    // Find all records for a given operation type (e.g. "COMPARE", "ADD")
    List<QuantityMeasurementEntity> findByOperation(String operation);

    // Find all records for a given measurement type
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    // Find records created after a given date
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    // Custom JPQL: successful records for an operation
    @Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.operation = :operation AND e.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(@Param("operation") String operation);

    // Count successful records per operation
    long countByOperationAndErrorFalse(String operation);

    // All errored records
    List<QuantityMeasurementEntity> findByErrorTrue();
}
