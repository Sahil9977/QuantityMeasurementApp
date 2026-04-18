package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private final IQuantityMeasurementRepository repository;
    private final IQuantityMeasurementService    service;
    private final QuantityMeasurementController  controller;

    public QuantityMeasurementApp() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        String repoType = config.getRepositoryType();

        logger.info("Initialising QuantityMeasurementApp with repository type: {}", repoType);

        if ("database".equalsIgnoreCase(repoType)) {
            ConnectionPool pool = new ConnectionPool(config);
            this.repository = new QuantityMeasurementDatabaseRepository(pool);
            logger.info("Using DATABASE repository – {}", repository.getPoolStatistics());
        } else {
            this.repository = QuantityMeasurementCacheRepository.getInstance();
            logger.info("Using CACHE (in-memory) repository");
        }

        this.service    = new QuantityMeasurementServiceImpl(repository);
        this.controller = new QuantityMeasurementController(service);
    }

    // ── Close resources ──────────────────────────────────────────────────────

    public void closeResources() {
        repository.releaseResources();
        logger.info("Application resources released");
    }

    // ── Delete all measurements ──────────────────────────────────────────────

    public void deleteAllMeasurements() {
        repository.deleteAll();
        logger.info("All measurements deleted");
    }

    // ── Report all measurements ──────────────────────────────────────────────

    public void reportAllMeasurements() {
        List<QuantityMeasurementEntity> all = repository.findAll();
        System.out.println("\n── Repository History (" + all.size() + " records) ──");
        all.forEach(System.out::println);
        logger.info("Pool statistics: {}", repository.getPoolStatistics());
    }

    // ── Entry point ──────────────────────────────────────────────────────────

    public static void main(String[] args) {
        QuantityMeasurementApp app = new QuantityMeasurementApp();
        try {
            app.runDemonstrations();
            app.reportAllMeasurements();
            app.deleteAllMeasurements();
            logger.info("Records after delete: {}", app.repository.getTotalCount());
        } finally {
            app.closeResources();
        }
    }

    // ── Demonstrations ───────────────────────────────────────────────────────

    private void runDemonstrations() {

        // ── LENGTH ────────────────────────────────────────────────────────
        System.out.println("\n═══ LENGTH ═══");
        QuantityDTO l1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO l2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO l3 = new QuantityDTO(0.0, "YARDS", "LENGTH");

        controller.performComparison(l1, l2);
        controller.performConversion(l1, l3);
        controller.performAddition(l1, l2);
        controller.performSubtraction(l1, l2);
        controller.performDivision(l1, l2);

        // ── WEIGHT ────────────────────────────────────────────────────────
        System.out.println("\n═══ WEIGHT ═══");
        QuantityDTO w1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO w2 = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        QuantityDTO w3 = new QuantityDTO(0.0, "POUND", "WEIGHT");

        controller.performComparison(w1, w2);
        controller.performConversion(w1, w3);
        controller.performAddition(w1, w2);
        controller.performSubtraction(w1, w2);
        controller.performDivision(w1, w2);

        // ── VOLUME ────────────────────────────────────────────────────────
        System.out.println("\n═══ VOLUME ═══");
        QuantityDTO v1 = new QuantityDTO(1.0, "GALLON", "VOLUME");
        QuantityDTO v2 = new QuantityDTO(3.785, "LITRE", "VOLUME");
        QuantityDTO v3 = new QuantityDTO(0.0, "MILLILITRE", "VOLUME");

        controller.performComparison(v1, v2);
        controller.performConversion(v1, v3);
        controller.performAddition(v1, v2);
        controller.performSubtraction(v1, v2);
        controller.performDivision(v1, v2);

        // ── TEMPERATURE ───────────────────────────────────────────────────
        System.out.println("\n═══ TEMPERATURE ═══");
        QuantityDTO t1 = new QuantityDTO(0.0,   "CELSIUS",    "TEMPERATURE");
        QuantityDTO t2 = new QuantityDTO(32.0,  "FAHRENHEIT", "TEMPERATURE");
        QuantityDTO t3 = new QuantityDTO(100.0, "CELSIUS",    "TEMPERATURE");
        QuantityDTO t4 = new QuantityDTO(212.0, "FAHRENHEIT", "TEMPERATURE");
        QuantityDTO t5 = new QuantityDTO(0.0,   "FAHRENHEIT", "TEMPERATURE");

        controller.performComparison(t1, t2);
        controller.performComparison(t3, t4);
        controller.performConversion(t3, t5);
        controller.performConversion(t2, t1);

        System.out.println("\n── Unsupported Temperature Operations ──");
        controller.performAddition(t1, t3);
        controller.performSubtraction(t1, t3);
        controller.performDivision(t1, t3);

        // ── CROSS-CATEGORY ────────────────────────────────────────────────
        System.out.println("\n── Cross-Category Prevention ──");
        controller.performAddition(
                new QuantityDTO(1.0, "FEET",    "LENGTH"),
                new QuantityDTO(1.0, "CELSIUS", "TEMPERATURE"));
    }
}
