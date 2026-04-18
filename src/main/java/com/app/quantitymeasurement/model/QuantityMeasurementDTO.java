package com.app.quantitymeasurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementDTO {

    private double  thisValue;
    private String  thisUnit;
    private String  thisMeasurementType;

    private double  thatValue;
    private String  thatUnit;
    private String  thatMeasurementType;

    private String  operation;

    private String  resultString;
    private double  resultValue;
    private String  resultUnit;
    private String  resultMeasurementType;

    private String  errorMessage;
    private boolean error;

    // ── Static factory: Entity → DTO ─────────────────────────────────────────
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity e) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.thisValue             = e.getThisValue();
        dto.thisUnit              = e.getThisUnit();
        dto.thisMeasurementType   = e.getThisMeasurementType();
        dto.thatValue             = e.getThatValue();
        dto.thatUnit              = e.getThatUnit();
        dto.thatMeasurementType   = e.getThatMeasurementType();
        dto.operation             = e.getOperation();
        dto.resultString          = e.getResultString();
        dto.resultValue           = e.getResultValue();
        dto.resultUnit            = e.getResultUnit();
        dto.resultMeasurementType = e.getResultMeasurementType();
        dto.errorMessage          = e.getErrorMessage();
        dto.error                 = e.isError();
        return dto;
    }

    // ── Static factory: DTO → Entity ─────────────────────────────────────────
    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        e.setThisValue(thisValue);
        e.setThisUnit(thisUnit);
        e.setThisMeasurementType(thisMeasurementType);
        e.setThatValue(thatValue);
        e.setThatUnit(thatUnit);
        e.setThatMeasurementType(thatMeasurementType);
        e.setOperation(operation);
        e.setResultString(resultString);
        e.setResultValue(resultValue);
        e.setResultUnit(resultUnit);
        e.setResultMeasurementType(resultMeasurementType);
        e.setErrorMessage(errorMessage);
        e.setError(error);
        return e;
    }

    // ── List conversion helpers ───────────────────────────────────────────────
    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .collect(Collectors.toList());
    }
}
