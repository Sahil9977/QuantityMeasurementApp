package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityMeasurementController {

	private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

	private final IQuantityMeasurementService service;

	public QuantityMeasurementController(IQuantityMeasurementService service) {
		if (service == null)
			throw new IllegalArgumentException("Service cannot be null");
		this.service = service;
		logger.info("QuantityMeasurementController initialised");
	}

	public void performComparison(QuantityDTO q1, QuantityDTO q2) {
		try {
			QuantityDTO result = service.compare(q1, q2);
			boolean equal = result.getValue() == 1.0;
			logger.info("Comparison: {} == {} → Equal({})", q1, q2, equal);
			System.out.println("Comparison: " + q1 + " == " + q2 + " → Equal(" + equal + ")");
		} catch (QuantityMeasurementException e) {
			displayError("Comparison", e);
		}
	}

	public void performConversion(QuantityDTO source, QuantityDTO targetUnitDTO) {
		try {
			QuantityDTO result = service.convert(source, targetUnitDTO);
			logger.info("Conversion: {} = {}", source, result);
			System.out.println("Conversion: " + source + " = " + result);
		} catch (QuantityMeasurementException e) {
			displayError("Conversion", e);
		}
	}

	public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2) {
		try {
			QuantityDTO result = service.add(q1, q2);
			logger.info("Addition: {} + {} = {}", q1, q2, result);
			System.out.println("Addition: " + q1 + " + " + q2 + " = " + result);
			return result;
		} catch (QuantityMeasurementException e) {
			displayError("Addition", e);
			return null;
		}
	}

	public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2) {
		try {
			QuantityDTO result = service.subtract(q1, q2);
			logger.info("Subtraction: {} - {} = {}", q1, q2, result);
			System.out.println("Subtraction: " + q1 + " - " + q2 + " = " + result);
			return result;
		} catch (QuantityMeasurementException e) {
			displayError("Subtraction", e);
			return null;
		}
	}

	public void performDivision(QuantityDTO q1, QuantityDTO q2) {
		try {
			QuantityDTO result = service.divide(q1, q2);
			double ratio = result.getValue();
			String comparison = ratio > 1.0 ? "First is larger"
					: ratio < 1.0 ? "Second is larger" : "Both are equivalent";
			logger.info("Division ratio: {} → {}", ratio, comparison);
			System.out.println("Division ratio: " + ratio + " → " + comparison);
		} catch (QuantityMeasurementException e) {
			displayError("Division", e);
		}
	}

	private void displayError(String operation, QuantityMeasurementException e) {
		logger.warn("{} failed: {}", operation, e.getMessage());
		System.out.println(operation + " failed: " + e.getMessage());
	}
}
