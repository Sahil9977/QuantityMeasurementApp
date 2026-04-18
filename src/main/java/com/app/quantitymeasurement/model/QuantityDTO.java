package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {

    @NotNull(message = "Value cannot be null")
    private Double value;

    @NotEmpty(message = "Unit name cannot be empty")
    private String unit;

    @NotEmpty(message = "Measurement type cannot be empty")
    @Pattern(
        regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit",
        message = "Measurement type must be one of: LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit"
    )
    private String measurementType;

    // ── Validation: unit must be valid for the given measurementType ──────────
    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isUnitValidForType() {
        if (unit == null || measurementType == null) return true; // covered by @NotEmpty
        try {
            switch (measurementType) {
                case "LengthUnit":      LengthUnit.valueOf(unit.toUpperCase());      break;
                case "WeightUnit":      WeightUnit.valueOf(unit.toUpperCase());      break;
                case "VolumeUnit":      VolumeUnit.valueOf(unit.toUpperCase());      break;
                case "TemperatureUnit": TemperatureUnit.valueOf(unit.toUpperCase()); break;
                default: return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
