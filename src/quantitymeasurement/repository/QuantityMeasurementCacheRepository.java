package com.apps.quantitymeasurement.repository;


import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {


    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();


    private static class Holder {
        private static final QuantityMeasurementCacheRepository INSTANCE =
                new QuantityMeasurementCacheRepository();
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        return Holder.INSTANCE;
    }


    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        return new ArrayList<>(cache);  // defensive copy
    }

    @Override
    public List<QuantityMeasurementEntity> findByOperationType(String operationType) {
        return cache.stream()
                .filter(e -> operationType.equalsIgnoreCase(e.getOperationType()))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        cache.clear();
    }

}
