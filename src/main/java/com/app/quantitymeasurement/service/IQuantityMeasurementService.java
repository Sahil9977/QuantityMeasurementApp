package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;

public interface IQuantityMeasurementService {

    QuantityDTO compare(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO convert(QuantityDTO source, QuantityDTO targetUnitDTO);
    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO divide(QuantityDTO q1, QuantityDTO q2);
}
