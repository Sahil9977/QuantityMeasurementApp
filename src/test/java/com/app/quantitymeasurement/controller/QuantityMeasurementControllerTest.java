package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.app.quantitymeasurement.config.SecurityConfig;

@WebMvcTest(QuantityMeasurementController.class)
@Import(SecurityConfig.class)
public class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private QuantityInputDTO lengthInput;
    private QuantityMeasurementDTO compareResult;
    private QuantityMeasurementDTO addResult;
    private QuantityMeasurementDTO convertResult;

    @BeforeEach
    void setUp() {
        QuantityDTO feet  = new QuantityDTO(1.0, "FEET",   "LengthUnit");
        QuantityDTO inches = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        lengthInput = new QuantityInputDTO(feet, inches);

        compareResult = new QuantityMeasurementDTO();
        compareResult.setThisValue(1.0);      compareResult.setThisUnit("FEET");
        compareResult.setThisMeasurementType("LengthUnit");
        compareResult.setThatValue(12.0);     compareResult.setThatUnit("INCHES");
        compareResult.setThatMeasurementType("LengthUnit");
        compareResult.setOperation("compare");
        compareResult.setResultString("true");
        compareResult.setError(false);

        addResult = new QuantityMeasurementDTO();
        addResult.setThisValue(1.0);          addResult.setThisUnit("FEET");
        addResult.setThisMeasurementType("LengthUnit");
        addResult.setThatValue(12.0);         addResult.setThatUnit("INCHES");
        addResult.setThatMeasurementType("LengthUnit");
        addResult.setOperation("add");
        addResult.setResultValue(2.0);
        addResult.setResultUnit("FEET");
        addResult.setResultMeasurementType("LengthUnit");
        addResult.setError(false);

        convertResult = new QuantityMeasurementDTO();
        convertResult.setThisValue(1.0);      convertResult.setThisUnit("FEET");
        convertResult.setThisMeasurementType("LengthUnit");
        convertResult.setThatValue(0.0);      convertResult.setThatUnit("INCHES");
        convertResult.setThatMeasurementType("LengthUnit");
        convertResult.setOperation("convert");
        convertResult.setResultValue(12.0);
        convertResult.setError(false);
    }

    // ── Compare ───────────────────────────────────────────────────────────────

    @Test
    void compareQuantities_Returns200WithTrueResult() throws Exception {
        Mockito.when(service.compareQuantities(any(), any())).thenReturn(compareResult);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lengthInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"))
                .andExpect(jsonPath("$.error").value(false));
    }

    // ── Convert ───────────────────────────────────────────────────────────────

    @Test
    void convertQuantity_Returns200WithConvertedValue() throws Exception {
        QuantityInputDTO input = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET",   "LengthUnit"),
                new QuantityDTO(0.0, "INCHES", "LengthUnit"));
        Mockito.when(service.convertQuantity(any(), any())).thenReturn(convertResult);

        mockMvc.perform(post("/api/v1/quantities/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("convert"))
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    // ── Add ───────────────────────────────────────────────────────────────────

    @Test
    void addQuantities_Returns200WithSum() throws Exception {
        Mockito.when(service.addQuantities(any(), any())).thenReturn(addResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lengthInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    // ── Validation: bad measurement type ─────────────────────────────────────

    @Test
    void compareQuantities_InvalidMeasurementType_Returns400() throws Exception {
        QuantityInputDTO bad = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "InvalidType"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit"));

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    // ── Validation: bad unit name ─────────────────────────────────────────────

    @Test
    void addQuantities_InvalidUnitName_Returns400() throws Exception {
        QuantityInputDTO bad = new QuantityInputDTO(
                new QuantityDTO(1.0, "FOOT",  "LengthUnit"),
                new QuantityDTO(12.0, "INCHE", "LengthUnit"));

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
    }

    // ── History by operation ──────────────────────────────────────────────────

    @Test
    void getHistoryByOperation_ReturnsListOfRecords() throws Exception {
        Mockito.when(service.getHistoryByOperation("compare"))
               .thenReturn(Collections.singletonList(compareResult));

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("compare"))
                .andExpect(jsonPath("$[0].resultString").value("true"));
    }

    // ── History by type ───────────────────────────────────────────────────────

    @Test
    void getHistoryByType_ReturnsListOfRecords() throws Exception {
        Mockito.when(service.getHistoryByMeasurementType("LengthUnit"))
               .thenReturn(Arrays.asList(compareResult, addResult));

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ── Count ─────────────────────────────────────────────────────────────────

    @Test
    void countByOperation_ReturnsLong() throws Exception {
        Mockito.when(service.countByOperation("compare")).thenReturn(3L);

        mockMvc.perform(get("/api/v1/quantities/count/compare"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    // ── Error history ─────────────────────────────────────────────────────────

    @Test
    void getErrorHistory_ReturnsEmptyList() throws Exception {
        Mockito.when(service.getErrorHistory()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
