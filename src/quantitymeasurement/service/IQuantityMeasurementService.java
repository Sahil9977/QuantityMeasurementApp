package com.apps.quantitymeasurement.service;


import com.apps.quantitymeasurement.DTO.QuantityDTO;

/**

 *
 * CONTRACT:
 *   All methods accept QuantityDTO input and return QuantityDTO output.
 *   The caller never sees Quantity<U>, LengthUnit, etc. — those are internal.
 *
 * OPERATION TYPES:
 *   compare   – boolean equality result embedded in QuantityDTO result field
 *   convert   – converts first DTO to the unit of the second DTO
 *   add       – sum of two quantities, result in first quantity's unit
 *   subtract  – difference, result in first quantity's unit
 *   divide    – dimensionless ratio returned as a "RATIO" QuantityDTO
 */
public interface IQuantityMeasurementService {

    QuantityDTO compare(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO convert(QuantityDTO source, QuantityDTO targetUnitDTO);

    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO divide(QuantityDTO q1, QuantityDTO q2);
}