package com.app.quantitymeasurement.integrationTests;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * End-to-end integration tests: Controller → Service → Repository → H2 DB.
 * Each test starts with a clean database (deleteAll in @Before).
 */
public class QuantityMeasurementIntegrationTest {

    private static ConnectionPool pool;
    private static QuantityMeasurementDatabaseRepository repository;

    private QuantityMeasurementServiceImpl    service;
    private QuantityMeasurementController     controller;

    @BeforeClass
    public static void setUpPool() {
        System.setProperty("db.url",      "jdbc:h2:mem:integrationdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        System.setProperty("db.driver",   "org.h2.Driver");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        System.setProperty("pool.size",   "5");

        pool       = new ConnectionPool(ApplicationConfig.getInstance());
        repository = new QuantityMeasurementDatabaseRepository(pool);
    }

    @AfterClass
    public static void tearDownPool() {
        if (pool != null) pool.close();
        System.clearProperty("db.url");
        System.clearProperty("db.driver");
        System.clearProperty("db.username");
        System.clearProperty("db.password");
        System.clearProperty("pool.size");
    }

    @Before
    public void setUp() {
        repository.deleteAll();
        service    = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    // ── Comparison persisted ──────────────────────────────────────────────────

    @Test
    public void testCompare_PersistedToDatabase() {
        controller.performComparison(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCHES", "LENGTH"));

        assertEquals(1, repository.getTotalCount());
        QuantityMeasurementEntity e = repository.findAll().get(0);
        assertEquals("COMPARE", e.getOperationType());
        assertFalse(e.hasError());
    }

    // ── Conversion persisted ──────────────────────────────────────────────────

    @Test
    public void testConvert_PersistedToDatabase() {
        controller.performConversion(
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(0.0,   "FAHRENHEIT", "TEMPERATURE"));

        assertEquals(1, repository.getTotalCount());
        assertEquals("CONVERT", repository.findAll().get(0).getOperationType());
    }

    // ── Add persisted ─────────────────────────────────────────────────────────

    @Test
    public void testAdd_PersistedToDatabase() {
        controller.performAddition(
                new QuantityDTO(1.0, "KILOGRAM", "WEIGHT"),
                new QuantityDTO(500.0, "GRAM", "WEIGHT"));

        assertEquals(1, repository.getTotalCount());
        assertEquals("ADD", repository.findAll().get(0).getOperationType());
    }

    // ── Error persisted ───────────────────────────────────────────────────────

    @Test
    public void testTemperatureAdd_ErrorPersistedToDatabase() {
        controller.performAddition(
                new QuantityDTO(0.0,   "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"));

        assertEquals(1, repository.getTotalCount());
        assertTrue(repository.findAll().get(0).hasError());
    }

    // ── Filter by operation type ──────────────────────────────────────────────

    @Test
    public void testFindByOperationType_AfterMultipleOps() {
        controller.performComparison(
                new QuantityDTO(1.0, "GALLON", "VOLUME"),
                new QuantityDTO(3.785, "LITRE", "VOLUME"));
        controller.performAddition(
                new QuantityDTO(1.0, "GALLON", "VOLUME"),
                new QuantityDTO(3.785, "LITRE", "VOLUME"));
        controller.performAddition(
                new QuantityDTO(2.0, "LITRE", "VOLUME"),
                new QuantityDTO(1.0, "LITRE", "VOLUME"));

        List<QuantityMeasurementEntity> adds = repository.findByOperationType("ADD");
        assertEquals(2, adds.size());

        List<QuantityMeasurementEntity> compares = repository.findByOperationType("COMPARE");
        assertEquals(1, compares.size());
    }

    // ── Filter by measurement type ────────────────────────────────────────────

    @Test
    public void testFindByMeasurementType_FiltersCorrectly() {
        controller.performComparison(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCHES", "LENGTH"));
        controller.performComparison(
                new QuantityDTO(1.0, "KILOGRAM", "WEIGHT"),
                new QuantityDTO(1000.0, "GRAM", "WEIGHT"));

        List<QuantityMeasurementEntity> length = repository.findByMeasurementType("LENGTH");
        assertEquals(1, length.size());

        List<QuantityMeasurementEntity> weight = repository.findByMeasurementType("WEIGHT");
        assertEquals(1, weight.size());
    }

    // ── deleteAll resets count ────────────────────────────────────────────────

    @Test
    public void testDeleteAll_ResetsCount() {
        controller.performComparison(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCHES", "LENGTH"));
        assertEquals(1, repository.getTotalCount());

        repository.deleteAll();
        assertEquals(0, repository.getTotalCount());
    }

    // ── Pool statistics available ─────────────────────────────────────────────

    @Test
    public void testPoolStatistics_NotNull() {
        String stats = repository.getPoolStatistics();
        assertNotNull(stats);
        assertTrue(stats.length() > 0);
    }

    // ── Full demo run persists all records ────────────────────────────────────

    @Test
    public void testFullDemoRun_PersistsAllOperations() {
        // Mirrors the runDemonstrations() calls in QuantityMeasurementApp
        controller.performComparison(new QuantityDTO(1.0, "FEET", "LENGTH"), new QuantityDTO(12.0, "INCHES", "LENGTH"));
        controller.performConversion(new QuantityDTO(1.0, "FEET", "LENGTH"), new QuantityDTO(0.0, "YARDS", "LENGTH"));
        controller.performAddition(new QuantityDTO(1.0, "FEET", "LENGTH"), new QuantityDTO(12.0, "INCHES", "LENGTH"));
        controller.performSubtraction(new QuantityDTO(1.0, "FEET", "LENGTH"), new QuantityDTO(12.0, "INCHES", "LENGTH"));
        controller.performDivision(new QuantityDTO(1.0, "FEET", "LENGTH"), new QuantityDTO(12.0, "INCHES", "LENGTH"));

        assertEquals(5, repository.getTotalCount());
    }
}
