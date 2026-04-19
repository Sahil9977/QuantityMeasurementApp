package com.app.quantitymeasurement;

import com.app.quantitymeasurement.auth.dto.AuthResponse;
import com.app.quantitymeasurement.auth.dto.LoginRequest;
import com.app.quantitymeasurement.auth.dto.RegisterRequest;
import com.app.quantitymeasurement.auth.dto.UserProfileResponse;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UC18 Integration Tests
 *
 * Flow:
 *   1. Register a test user → receive JWT
 *   2. Use JWT in Authorization header for all /api/v1/quantities/** calls
 *   3. Verify all quantity measurement operations still work correctly
 *   4. Verify auth endpoints (login, /me) work
 *   5. Verify protected endpoints reject requests without JWT
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuantityMeasurementApplicationTests {

    @LocalServerPort private int port;
    @Autowired       private TestRestTemplate restTemplate;

    // Shared JWT token — obtained in test @Order(1), used by all subsequent tests
    private static String jwtToken;

    private String auth()  { return "http://localhost:" + port + "/api/v1/auth"; }
    private String base()  { return "http://localhost:" + port + "/api/v1/quantities"; }

    /** Build an HttpEntity with the JWT Bearer header */
    private <T> HttpEntity<T> withJwt(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<Void> withJwt() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return new HttpEntity<>(headers);
    }

    // ── 1. Register test user + get JWT ──────────────────────────────────────
    @Test @Order(1)
    void registerUser_AndReceiveJwt() {
        RegisterRequest req = new RegisterRequest(
                "Integration Tester", "test@example.com", "password123");

        ResponseEntity<AuthResponse> resp =
                restTemplate.postForEntity(auth() + "/register", req, AuthResponse.class);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getAccessToken());
        assertEquals("Bearer", resp.getBody().getTokenType());

        jwtToken = resp.getBody().getAccessToken();
        System.out.println("JWT token obtained for integration tests.");
    }

    // ── 2. Login with same user ───────────────────────────────────────────────
    @Test @Order(2)
    void login_ValidCredentials_ReturnsJwt() {
        LoginRequest req = new LoginRequest("test@example.com", "password123");

        ResponseEntity<AuthResponse> resp =
                restTemplate.postForEntity(auth() + "/login", req, AuthResponse.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody().getAccessToken());
    }

    // ── 3. /me endpoint ───────────────────────────────────────────────────────
    @Test @Order(3)
    void getMe_WithJwt_ReturnsProfile() {
        ResponseEntity<UserProfileResponse> resp =
                restTemplate.exchange(auth() + "/me", HttpMethod.GET,
                        withJwt(), UserProfileResponse.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("test@example.com", resp.getBody().getEmail());
        assertEquals("Integration Tester",  resp.getBody().getName());
    }

    // ── 4. /me without JWT → 401 ─────────────────────────────────────────────
    @Test @Order(4)
    void getMe_WithoutJwt_Returns401() {
        ResponseEntity<String> resp =
                restTemplate.getForEntity(auth() + "/me", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    // ── 5. Register duplicate email → 400 ────────────────────────────────────
    @Test @Order(5)
    void register_DuplicateEmail_Returns400() {
        RegisterRequest req = new RegisterRequest(
                "Another Person", "test@example.com", "password123");

        ResponseEntity<String> resp =
                restTemplate.postForEntity(auth() + "/register", req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    // ── 6. Quantity endpoint without JWT → 401 ────────────────────────────────
    @Test @Order(6)
    void compareQuantities_WithoutJwt_Returns401() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<String> resp =
                restTemplate.postForEntity(base() + "/compare", input, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    // ── 7. Compare: 1 FEET == 12 INCHES ──────────────────────────────────────
    @Test @Order(7)
    void compareQuantities_FeetEqualsInches_ReturnsTrue() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/compare", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("compare", resp.getBody().getOperation());
        assertEquals("true", resp.getBody().getResultString());
        assertFalse(resp.getBody().isError());
    }

    // ── 8. Compare: 1 FEET != 1 INCHES ───────────────────────────────────────
    @Test @Order(8)
    void compareQuantities_NotEqual_ReturnsFalse() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(1.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/compare", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("false", resp.getBody().getResultString());
    }

    // ── 9. Convert: 1 FEET → 12 INCHES ───────────────────────────────────────
    @Test @Order(9)
    void convertQuantity_FeetToInches_Returns12() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(0.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/convert", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(12.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── 10. Convert: 100°C → 212°F ───────────────────────────────────────────
    @Test @Order(10)
    void convertQuantity_CelsiusToFahrenheit_Returns212() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit"),
                new QuantityDTO(0.0, "FAHRENHEIT", "TemperatureUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/convert", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(212.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── 11. Add: 1 FEET + 12 INCHES = 2 FEET ─────────────────────────────────
    @Test @Order(11)
    void addQuantities_FeetPlusInches_Returns2Feet() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/add", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2.0, resp.getBody().getResultValue(), 0.001);
        assertEquals("FEET", resp.getBody().getResultUnit());
    }

    // ── 12. Add: 1 KG + 1000g = 2 KG ────────────────────────────────────────
    @Test @Order(12)
    void addQuantities_KgPlusGram_Returns2Kg() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "KILOGRAM", "WeightUnit"),
                new QuantityDTO(1000.0, "GRAM", "WeightUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/add", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── 13. Subtract: 2 FEET - 12 INCHES = 1 FEET ────────────────────────────
    @Test @Order(13)
    void subtractQuantities_Returns1Foot() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(2.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/subtract", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── 14. Divide: 2 FEET / 12 INCHES = 2.0 ratio ───────────────────────────
    @Test @Order(14)
    void divideQuantities_Returns2Ratio() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(2.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/divide", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2.0, resp.getBody().getResultValue(), 0.001);
    }

    // ── 15. Volume: 1 LITRE = 1000 MILLILITRES ───────────────────────────────
    @Test @Order(15)
    void convertQuantity_LitreToMillilitre_Returns1000() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "LITRE", "VolumeUnit"),
                new QuantityDTO(0.0, "MILLILITRE", "VolumeUnit"));

        ResponseEntity<QuantityMeasurementDTO> resp =
                restTemplate.exchange(base() + "/convert", HttpMethod.POST,
                        withJwt(input), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1000.0, resp.getBody().getResultValue(), 0.01);
    }

    // ── 16. Error: mismatched types → 400 ────────────────────────────────────
    @Test @Order(16)
    void addQuantities_MismatchedTypes_Returns400() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(1.0, "KILOGRAM", "WeightUnit"));

        ResponseEntity<String> resp =
                restTemplate.exchange(base() + "/add", HttpMethod.POST,
                        withJwt(input), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Cannot perform arithmetic"));
    }

    // ── 17. Error: invalid unit → 400 ────────────────────────────────────────
    @Test @Order(17)
    void compareQuantities_InvalidUnit_Returns400() {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FOOT", "LengthUnit"),
                new QuantityDTO(1.0, "INCHES", "LengthUnit"));

        ResponseEntity<String> resp =
                restTemplate.exchange(base() + "/compare", HttpMethod.POST,
                        withJwt(input), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    // ── 18. History endpoints persist correctly ───────────────────────────────
    @Test @Order(18)
    void getHistoryByOperation_ReturnsPersistedRecords() {
        ResponseEntity<QuantityMeasurementDTO[]> resp =
                restTemplate.exchange(base() + "/history/operation/compare",
                        HttpMethod.GET, withJwt(), QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test @Order(19)
    void getHistoryByType_ReturnsLengthUnitRecords() {
        ResponseEntity<QuantityMeasurementDTO[]> resp =
                restTemplate.exchange(base() + "/history/type/LengthUnit",
                        HttpMethod.GET, withJwt(), QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test @Order(20)
    void countByOperation_ReturnsNonZeroCount() {
        ResponseEntity<Long> resp =
                restTemplate.exchange(base() + "/count/compare",
                        HttpMethod.GET, withJwt(), Long.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody() >= 1L);
    }

    // ── 21. Actuator health ───────────────────────────────────────────────────
    @Test @Order(21)
    void actuatorHealth_ReturnsUp() {
        ResponseEntity<String> resp =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/actuator/health", String.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().contains("UP"));
    }

//    // ── 22. Admin endpoint: ROLE_USER → 403 ──────────────────────────────────
//    @Test @Order(22)
//    void adminUsers_WithUserRole_Returns403() {
//        ResponseEntity<String> resp =
//                restTemplate.exchange(auth() + "/admin/users",
//                        HttpMethod.GET, withJwt(), String.class);
//
//        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
//    }
}
