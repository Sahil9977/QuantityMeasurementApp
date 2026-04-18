package com.app.quantitymeasurement;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuantityMeasurementApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String base() { return "http://localhost:" + port + "/api/v1/quantities"; }

    // ── Application context loads ─────────────────────────────────────────────
    @Test
    @Order(1)
    void contextLoads() {
        assertNotNull(restTemplate);
    }

    // ── Compare: 1 FEET == 12 INCHES ─────────────────────────────────────────
    @Test
    @Order(2)
    void testCompare_FeetEqualsInches() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET",   "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("compare", resp.getBody().getOperation());
        assertEquals("true", resp.getBody().getResultString());
        assertFalse(resp.getBody().isError());
    }

    // ── Compare: unequal quantities ───────────────────────────────────────────
    @Test
    @Order(3)
    void testCompare_FeetNotEqualToInches() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET",  "LengthUnit"),
                new QuantityDTO(1.0, "INCHES","LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("false", resp.getBody().getResultString());
    }

    // ── Convert: 1 FEET → INCHES ─────────────────────────────────────────────
    @Test
    @Order(4)
    void testConvert_FeetToInches() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET",   "LengthUnit"),
                new QuantityDTO(0.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/convert", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("convert", resp.getBody().getOperation());
        assertEquals(12.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── Convert: CELSIUS → FAHRENHEIT ────────────────────────────────────────
    @Test
    @Order(5)
    void testConvert_CelsiusToFahrenheit() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(100.0, "CELSIUS",    "TemperatureUnit"),
                new QuantityDTO(0.0,   "FAHRENHEIT", "TemperatureUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/convert", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(212.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── Add: length ───────────────────────────────────────────────────────────
    @Test
    @Order(6)
    void testAdd_FeetPlusInches() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0,  "FEET",   "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("add", resp.getBody().getOperation());
        assertEquals(2.0, resp.getBody().getResultValue(), 0.001);
        assertEquals("FEET", resp.getBody().getResultUnit());
    }

    // ── Add: weight ───────────────────────────────────────────────────────────
    @Test
    @Order(7)
    void testAdd_KilogramPlusGram() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0,    "KILOGRAM", "WeightUnit"),
                new QuantityDTO(1000.0, "GRAM",     "WeightUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── Subtract ──────────────────────────────────────────────────────────────
    @Test
    @Order(8)
    void testSubtract_FeetMinusInches() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(2.0,  "FEET",   "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/subtract", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("subtract", resp.getBody().getOperation());
        assertEquals(1.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── Divide ────────────────────────────────────────────────────────────────
    @Test
    @Order(9)
    void testDivide_FeetByInches() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(2.0,  "FEET",   "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/divide", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("divide", resp.getBody().getOperation());
        assertEquals(2.0, resp.getBody().getResultValue(), 0.001); // 24/12 = 2
    }

    // ── Error: divide by zero ─────────────────────────────────────────────────
    @Test
    @Order(10)
    void testDivide_ByZero_Returns400() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET",   "LengthUnit"),
                new QuantityDTO(0.0, "INCHES", "LengthUnit"));

        ResponseEntity<String> resp =
                restTemplate.postForEntity(base() + "/divide", input, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    // ── Error: mismatched types ───────────────────────────────────────────────
    @Test
    @Order(11)
    void testAdd_MismatchedTypes_Returns400() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET",     "LengthUnit"),
                new QuantityDTO(1.0, "KILOGRAM", "WeightUnit"));

        ResponseEntity<String> resp =
                restTemplate.postForEntity(base() + "/add", input, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Cannot perform arithmetic"));
    }

    // ── Error: invalid unit name in body ─────────────────────────────────────
    @Test
    @Order(12)
    void testCompare_InvalidUnit_Returns400() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FOOT",  "LengthUnit"),
                new QuantityDTO(1.0, "INCHES","LengthUnit"));

        ResponseEntity<String> resp =
                restTemplate.postForEntity(base() + "/compare", input, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    // ── History: after operations above persist ───────────────────────────────
    @Test
    @Order(13)
    void testGetHistoryByOperation_Compare() {
        ResponseEntity<QuantityMeasurementDTO[]> resp =
                restTemplate.getForEntity(base() + "/history/operation/compare",
                                          QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test
    @Order(14)
    void testGetHistoryByType_LengthUnit() {
        ResponseEntity<QuantityMeasurementDTO[]> resp =
                restTemplate.getForEntity(base() + "/history/type/LengthUnit",
                                          QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test
    @Order(15)
    void testCountByOperation_Compare() {
        ResponseEntity<Long> resp =
                restTemplate.getForEntity(base() + "/count/compare", Long.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody() >= 1L);
    }

    @Test
    @Order(16)
    void testGetErrorHistory_ReturnsErrors() {
        ResponseEntity<QuantityMeasurementDTO[]> resp =
                restTemplate.getForEntity(base() + "/history/errored",
                                          QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
    }

    // ── Actuator health ───────────────────────────────────────────────────────
    @Test
    @Order(17)
    void testActuatorHealth() {
        ResponseEntity<String> resp =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/actuator/health", String.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().contains("UP"));
    }

    // ── Volume operations ─────────────────────────────────────────────────────
    @Test
    @Order(18)
    void testConvert_LitreToMillilitre() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "LITRE",      "VolumeUnit"),
                new QuantityDTO(0.0, "MILLILITRE", "VolumeUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.postForEntity(base() + "/convert", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1000.0, resp.getBody().getResultValue(), 0.01);
    }

    // ── Temperature: disallow arithmetic ─────────────────────────────────────
    @Test
    @Order(19)
    void testAdd_Temperature_Returns400() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(10.0, "CELSIUS", "TemperatureUnit"),
                new QuantityDTO(20.0, "CELSIUS", "TemperatureUnit"));

        ResponseEntity<String> resp =
                restTemplate.postForEntity(base() + "/add", input, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }
}
