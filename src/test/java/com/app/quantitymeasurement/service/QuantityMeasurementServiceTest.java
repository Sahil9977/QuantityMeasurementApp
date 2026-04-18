package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuantityMeasurementServiceTest {

    private IQuantityMeasurementRepository mockRepo;
    private QuantityMeasurementServiceImpl service;

    @Before
    public void setUp() {
        mockRepo = Mockito.mock(IQuantityMeasurementRepository.class);
        doNothing().when(mockRepo).save(any());
        service = new QuantityMeasurementServiceImpl(mockRepo);
    }

    // ── compare ──────────────────────────────────────────────────────────────

    @Test
    public void testCompare_FeetEqualsInches() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO result = service.compare(q1, q2);
        assertEquals(1.0, result.getValue(), 0.001);    // equal → 1.0
    }

    @Test
    public void testCompare_NotEqual() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(5.0, "INCHES", "LENGTH");
        QuantityDTO result = service.compare(q1, q2);
        assertEquals(0.0, result.getValue(), 0.001);    // not equal → 0.0
    }

    @Test(expected = QuantityMeasurementException.class)
    public void testCompare_CrossCategory_ThrowsException() {
        service.compare(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(1.0, "CELSIUS", "TEMPERATURE"));
    }

    // ── convert ──────────────────────────────────────────────────────────────

    @Test
    public void testConvert_FeetToYards() {
        QuantityDTO source = new QuantityDTO(3.0, "FEET", "LENGTH");
        QuantityDTO target = new QuantityDTO(0.0, "YARDS", "LENGTH");
        QuantityDTO result = service.convert(source, target);
        assertEquals(1.0, result.getValue(), 0.001);
        assertEquals("YARDS", result.getUnitName());
    }

    @Test
    public void testConvert_CelsiusToFahrenheit() {
        QuantityDTO source = new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE");
        QuantityDTO target = new QuantityDTO(0.0, "FAHRENHEIT", "TEMPERATURE");
        QuantityDTO result = service.convert(source, target);
        assertEquals(212.0, result.getValue(), 0.001);
    }

    // ── add ──────────────────────────────────────────────────────────────────

    @Test
    public void testAdd_FeetAndInches() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO result = service.add(q1, q2);
        // 1 FEET = 12 INCHES; 12 INCHES + 12 INCHES = 24 INCHES = 2 FEET
        assertEquals(2.0, result.getValue(), 0.001);
    }

    @Test(expected = QuantityMeasurementException.class)
    public void testAdd_Temperature_ThrowsException() {
        service.add(
                new QuantityDTO(0.0,   "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"));
    }

    // ── subtract ─────────────────────────────────────────────────────────────

    @Test
    public void testSubtract_KilogramAndGram() {
        QuantityDTO q1 = new QuantityDTO(2.0, "KILOGRAM", "WEIGHT");
        QuantityDTO q2 = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        QuantityDTO result = service.subtract(q1, q2);
        assertEquals(1.5, result.getValue(), 0.001);
    }

    // ── divide ───────────────────────────────────────────────────────────────

    @Test
    public void testDivide_SameUnit_RatioOne() {
        QuantityDTO q1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO q2 = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        QuantityDTO result = service.divide(q1, q2);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    // ── null guards ───────────────────────────────────────────────────────────

    @Test(expected = QuantityMeasurementException.class)
    public void testCompare_NullInput_ThrowsException() {
        service.compare(null, new QuantityDTO(1.0, "FEET", "LENGTH"));
    }

    // ── repository called on every operation ─────────────────────────────────

    @Test
    public void testRepository_SaveCalledOnCompare() {
        service.compare(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCHES", "LENGTH"));
        verify(mockRepo, times(1)).save(any());
    }

    @Test
    public void testRepository_SaveCalledOnError() {
        try {
            service.add(
                    new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE"),
                    new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE"));
        } catch (QuantityMeasurementException ignored) {}
        // save() is still called with the error entity
        verify(mockRepo, times(1)).save(any());
    }
}
