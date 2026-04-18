package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class QuantityMeasurementDatabaseRepositoryTest {

    private static ConnectionPool pool;
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeClass
    public static void setUpPool() {
        // Force H2 in-memory DB for tests regardless of application.properties
        System.setProperty("db.url",    "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        System.setProperty("db.driver", "org.h2.Driver");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        System.setProperty("pool.size", "5");

        pool = new ConnectionPool(ApplicationConfig.getInstance());
    }

    @AfterClass
    public static void tearDownPool() {
        if (pool != null) pool.close();
        // Clear system props
        System.clearProperty("db.url");
        System.clearProperty("db.driver");
        System.clearProperty("db.username");
        System.clearProperty("db.password");
        System.clearProperty("pool.size");
    }

    @Before
    public void setUp() {
        repository = new QuantityMeasurementDatabaseRepository(pool);
        repository.deleteAll();   // clean slate before every test
    }

    // ── save + findAll ───────────────────────────────────────────────────────

    @Test
    public void testSaveAndFindAll() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity(
                "COMPARE", "QuantityDTO(1.0 FEET [LENGTH])", "QuantityDTO(12.0 INCHES [LENGTH])", "true");
        repository.save(e);

        List<QuantityMeasurementEntity> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals("COMPARE", all.get(0).getOperationType());
    }

    // ── getTotalCount ────────────────────────────────────────────────────────

    @Test
    public void testGetTotalCount() {
        assertEquals(0, repository.getTotalCount());

        repository.save(new QuantityMeasurementEntity("ADD",
                "QuantityDTO(1.0 FEET [LENGTH])", "QuantityDTO(12.0 INCHES [LENGTH])", "24.0 INCHES"));
        repository.save(new QuantityMeasurementEntity("SUBTRACT",
                "QuantityDTO(2.0 KILOGRAM [WEIGHT])", "QuantityDTO(500.0 GRAM [WEIGHT])", "1.5 KILOGRAM"));

        assertEquals(2, repository.getTotalCount());
    }

    // ── findByOperationType ──────────────────────────────────────────────────

    @Test
    public void testFindByOperationType() {
        repository.save(new QuantityMeasurementEntity("ADD",
                "QuantityDTO(1.0 FEET [LENGTH])", "QuantityDTO(12.0 INCHES [LENGTH])", "24.0 INCHES"));
        repository.save(new QuantityMeasurementEntity("COMPARE",
                "QuantityDTO(1.0 KILOGRAM [WEIGHT])", "QuantityDTO(1000.0 GRAM [WEIGHT])", "true"));
        repository.save(new QuantityMeasurementEntity("ADD",
                "QuantityDTO(2.0 GALLON [VOLUME])", "QuantityDTO(1.0 LITRE [VOLUME])", "9.0 LITRE"));

        List<QuantityMeasurementEntity> addOps = repository.findByOperationType("ADD");
        assertEquals(2, addOps.size());
        addOps.forEach(e -> assertEquals("ADD", e.getOperationType()));
    }

    // ── findByMeasurementType ────────────────────────────────────────────────

    @Test
    public void testFindByMeasurementType() {
        repository.save(new QuantityMeasurementEntity("ADD",
                "QuantityDTO(1.0 FEET [LENGTH])", "QuantityDTO(12.0 INCHES [LENGTH])", "24.0 INCHES"));
        repository.save(new QuantityMeasurementEntity("COMPARE",
                "QuantityDTO(1.0 KILOGRAM [WEIGHT])", "QuantityDTO(1000.0 GRAM [WEIGHT])", "true"));

        List<QuantityMeasurementEntity> lengthOps = repository.findByMeasurementType("LENGTH");
        assertEquals(1, lengthOps.size());

        List<QuantityMeasurementEntity> weightOps = repository.findByMeasurementType("WEIGHT");
        assertEquals(1, weightOps.size());
    }

    // ── deleteAll ────────────────────────────────────────────────────────────

    @Test
    public void testDeleteAll() {
        repository.save(new QuantityMeasurementEntity("ADD",
                "QuantityDTO(1.0 FEET [LENGTH])", "QuantityDTO(12.0 INCHES [LENGTH])", "24.0 INCHES"));
        assertEquals(1, repository.getTotalCount());

        repository.deleteAll();
        assertEquals(0, repository.getTotalCount());
    }

    // ── error entity persisted ───────────────────────────────────────────────

    @Test
    public void testSaveErrorEntity() {
        QuantityMeasurementEntity err = new QuantityMeasurementEntity(
                "ADD", "QuantityDTO(1.0 CELSIUS [TEMPERATURE])",
                "QuantityDTO(100.0 CELSIUS [TEMPERATURE])",
                "Temperature does not support ADD", true);
        repository.save(err);

        List<QuantityMeasurementEntity> all = repository.findAll();
        assertEquals(1, all.size());
        assertTrue(all.get(0).hasError());
        assertNotNull(all.get(0).getErrorMessage());
    }

    // ── pool statistics ──────────────────────────────────────────────────────

    @Test
    public void testGetPoolStatistics() {
        String stats = repository.getPoolStatistics();
        assertNotNull(stats);
        assertTrue(stats.contains("ConnectionPool"));
    }

    // ── generated ID populated ───────────────────────────────────────────────

    @Test
    public void testSavePopulatesId() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity(
                "CONVERT", "QuantityDTO(1.0 FEET [LENGTH])", "QuantityDTO(0.0277778 YARDS [LENGTH])");
        assertEquals(0, e.getId());    // before save

        repository.save(e);
        assertTrue(e.getId() > 0);     // after save DB assigned an id
    }

    // ── large dataset performance ────────────────────────────────────────────

    @Test
    public void testLargeDataset() {
        for (int i = 0; i < 200; i++) {
            repository.save(new QuantityMeasurementEntity("ADD",
                    "QuantityDTO(" + i + ".0 FEET [LENGTH])",
                    "QuantityDTO(12.0 INCHES [LENGTH])", "result"));
        }
        assertEquals(200, repository.getTotalCount());
        List<QuantityMeasurementEntity> all = repository.findAll();
        assertEquals(200, all.size());
    }
}
