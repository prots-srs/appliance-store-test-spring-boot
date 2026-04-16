package com.epam.rd.autocode.assessment.appliances.panel.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ManufacturerFormResult(
    @NotBlank @Size(min = 3, max = 100) String name) {

}
