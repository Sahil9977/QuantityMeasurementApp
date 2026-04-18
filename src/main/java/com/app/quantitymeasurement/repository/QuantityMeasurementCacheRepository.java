package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory repository (original UC15 implementation).
 * Updated to implement the UC16 expanded interface.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);

    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();

    private static class Holder {
        private static final QuantityMeasurementCacheRepository INSTANCE =
                new QuantityMeasurementCacheRepository();
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        return Holder.INSTANCE;
    }

    private QuantityMeasurementCacheRepository() {
        logger.info("QuantityMeasurementCacheRepository initialised (in-memory)");
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        logger.debug("Saved entity: {}", entity.getOperationType());
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        return new ArrayList<>(cache);
    }

    @Override
    public List<QuantityMeasurementEntity> findByOperationType(String operationType) {
        return cache.stream()
                .filter(e -> operationType.equalsIgnoreCase(e.getOperationType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
        String upper = measurementType.toUpperCase();
        return cache.stream()
                .filter(e -> (e.getOperand1() != null && e.getOperand1().toUpperCase().contains(upper))
                          || (e.getOperand2() != null && e.getOperand2().toUpperCase().contains(upper)))
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalCount() {
        return cache.size();
    }

    @Override
    public void deleteAll() {
        cache.clear();
        logger.info("Cache cleared");
    }
}
