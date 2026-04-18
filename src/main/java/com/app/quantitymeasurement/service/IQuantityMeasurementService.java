package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compareQuantities(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO convertQuantity(QuantityDTO source, QuantityDTO targetUnit);
    QuantityMeasurementDTO addQuantities(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO subtractQuantities(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO divideQuantities(QuantityDTO q1, QuantityDTO q2);

    // History / audit
    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);
    long countByOperation(String operation);
    List<QuantityMeasurementDTO> getErrorHistory();
}
