package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuantityMeasurementControllerTest {

    private IQuantityMeasurementService mockService;
    private QuantityMeasurementController controller;

    @Before
    public void setUp() {
        mockService = Mockito.mock(IQuantityMeasurementService.class);
        controller  = new QuantityMeasurementController(mockService);
    }

    // ── performComparison ─────────────────────────────────────────────────────

    @Test
    public void testPerformComparison_CallsService() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
        when(mockService.compare(q1, q2)).thenReturn(new QuantityDTO(1.0, "true", "BOOLEAN"));

        controller.performComparison(q1, q2);
        verify(mockService, times(1)).compare(q1, q2);
    }

    @Test
    public void testPerformComparison_ServiceThrows_NoExceptionPropagated() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(1.0, "CELSIUS", "TEMPERATURE");
        when(mockService.compare(any(), any()))
                .thenThrow(new QuantityMeasurementException("Cross-category error"));

        // controller swallows exception and prints – should not throw
        controller.performComparison(q1, q2);
    }

    // ── performConversion ─────────────────────────────────────────────────────

    @Test
    public void testPerformConversion_CallsService() {
        QuantityDTO source = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO target = new QuantityDTO(0.0, "YARDS", "LENGTH");
        QuantityDTO result = new QuantityDTO(0.333, "YARDS", "LENGTH");
        when(mockService.convert(source, target)).thenReturn(result);

        controller.performConversion(source, target);
        verify(mockService, times(1)).convert(source, target);
    }

    // ── performAddition ───────────────────────────────────────────────────────

    @Test
    public void testPerformAddition_ReturnsResult() {
        QuantityDTO q1  = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2  = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO sum = new QuantityDTO(2.0, "FEET", "LENGTH");
        when(mockService.add(q1, q2)).thenReturn(sum);

        QuantityDTO returned = controller.performAddition(q1, q2);
        assertNotNull(returned);
        assertEquals(2.0, returned.getValue(), 0.001);
    }

    @Test
    public void testPerformAddition_ServiceThrows_ReturnsNull() {
        when(mockService.add(any(), any()))
                .thenThrow(new QuantityMeasurementException("error"));
        QuantityDTO result = controller.performAddition(
                new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE"));
        assertNull(result);
    }

    // ── performSubtraction ────────────────────────────────────────────────────

    @Test
    public void testPerformSubtraction_ReturnsResult() {
        QuantityDTO q1   = new QuantityDTO(2.0, "KILOGRAM", "WEIGHT");
        QuantityDTO q2   = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        QuantityDTO diff = new QuantityDTO(1.5, "KILOGRAM", "WEIGHT");
        when(mockService.subtract(q1, q2)).thenReturn(diff);

        QuantityDTO returned = controller.performSubtraction(q1, q2);
        assertNotNull(returned);
        assertEquals(1.5, returned.getValue(), 0.001);
    }

    // ── performDivision ───────────────────────────────────────────────────────

    @Test
    public void testPerformDivision_CallsService() {
        QuantityDTO q1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO q2 = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        when(mockService.divide(q1, q2)).thenReturn(new QuantityDTO(1.0, "RATIO", "DIMENSIONLESS"));

        controller.performDivision(q1, q2);
        verify(mockService, times(1)).divide(q1, q2);
    }

    // ── null service guard ────────────────────────────────────────────────────

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullService_ThrowsException() {
        new QuantityMeasurementController(null);
    }
}
