package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.DTO.QuantityDTO;
import com.apps.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;


public class QuantityMeasurementApp {

	// ── Factory methods ───────────────────────────────────────────────────────

	private static QuantityMeasurementCacheRepository createRepository() {
		return QuantityMeasurementCacheRepository.getInstance();
	}

	private static IQuantityMeasurementService createService(QuantityMeasurementCacheRepository repository) {
		return new QuantityMeasurementServiceImpl(repository);
	}

	private static QuantityMeasurementController createController(IQuantityMeasurementService service) {
		return new QuantityMeasurementController(service);
	}

	// ── Entry point ───────────────────────────────────────────────────────────

	public static void main(String[] args) {

		QuantityMeasurementCacheRepository repository = createRepository();
		IQuantityMeasurementService service = createService(repository);
		QuantityMeasurementController controller = createController(service);

		runDemonstrations(controller);

		// Show persisted history
		System.out.println("\n── Repository History (" + repository.findAll().size() + " records) ──");
		repository.findAll().forEach(System.out::println);
	}

	// ── All demonstrations delegated to controller ────────────────────────────

	private static void runDemonstrations(QuantityMeasurementController controller) {

		// ── LENGTH ────────────────────────────────────────────────────────────
		System.out.println("\n═══ LENGTH ═══");

		QuantityDTO l1 = new QuantityDTO(1.0, "FEET", "LENGTH");
		QuantityDTO l2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
		QuantityDTO l3 = new QuantityDTO(0.0, "YARDS", "LENGTH"); // target unit placeholder

		controller.performComparison(l1, l2);
		controller.performConversion(l1, l3);
		controller.performAddition(l1, l2);
		controller.performSubtraction(l1, l2);
		controller.performDivision(l1, l2);

		// ── WEIGHT ────────────────────────────────────────────────────────────
		System.out.println("\n═══ WEIGHT ═══");

		QuantityDTO w1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
		QuantityDTO w2 = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
		QuantityDTO w3 = new QuantityDTO(0.0, "POUND", "WEIGHT"); // target unit

		controller.performComparison(w1, w2);
		controller.performConversion(w1, w3);
		controller.performAddition(w1, w2);
		controller.performSubtraction(w1, w2);
		controller.performDivision(w1, w2);

		// ── VOLUME ────────────────────────────────────────────────────────────
		System.out.println("\n═══ VOLUME ═══");

		QuantityDTO v1 = new QuantityDTO(1.0, "GALLON", "VOLUME");
		QuantityDTO v2 = new QuantityDTO(3.785, "LITRE", "VOLUME");
		QuantityDTO v3 = new QuantityDTO(0.0, "MILLILITRE", "VOLUME"); // target unit

		controller.performComparison(v1, v2);
		controller.performConversion(v1, v3);
		controller.performAddition(v1, v2);
		controller.performSubtraction(v1, v2);
		controller.performDivision(v1, v2);

		// ── TEMPERATURE ───────────────────────────────────────────────────────
		System.out.println("\n═══ TEMPERATURE ═══");

		QuantityDTO t1 = new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE");
		QuantityDTO t2 = new QuantityDTO(32.0, "FAHRENHEIT", "TEMPERATURE");
		QuantityDTO t3 = new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE");
		QuantityDTO t4 = new QuantityDTO(212.0, "FAHRENHEIT", "TEMPERATURE");
		QuantityDTO t5 = new QuantityDTO(0.0, "FAHRENHEIT", "TEMPERATURE"); // target unit

		controller.performComparison(t1, t2); // 0°C == 32°F → true
		controller.performComparison(t3, t4); // 100°C == 212°F → true
		controller.performConversion(t3, t5); // 100°C → 212°F
		controller.performConversion(t2, t1); // 32°F → 0°C

		System.out.println("\n── Unsupported Temperature Operations ──");
		controller.performAddition(t1, t3); // should show error
		controller.performSubtraction(t1, t3); // should show error
		controller.performDivision(t1, t3); // should show error

		// ── CROSS-CATEGORY PREVENTION ─────────────────────────────────────────
		System.out.println("\n── Cross-Category Prevention ──");
		QuantityDTO len = new QuantityDTO(1.0, "FEET", "LENGTH");
		QuantityDTO tmp = new QuantityDTO(1.0, "CELSIUS", "TEMPERATURE");
		controller.performAddition(len, tmp); // should show error
	}
}