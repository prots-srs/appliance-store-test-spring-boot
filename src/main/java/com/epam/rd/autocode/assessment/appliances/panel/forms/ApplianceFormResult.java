package com.epam.rd.autocode.assessment.appliances.panel.forms;

import java.math.BigDecimal;

import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplianceFormResult(
    @NotBlank @Size(min = 3, max = 100) String name,
    @NotNull Category category,
    @NotBlank @Size(min = 2, max = 100) String model,
    @NotNull PowerType powerType,
    @NotNull Long manufacturer,
    @NotBlank @Size(min = 2, max = 255) String characteristic,
    @NotBlank @Size(min = 2, max = 255) String description,
    @NotNull @Min(0) Integer power,
    @NotNull @DecimalMin("0.0") BigDecimal price

) {

}
