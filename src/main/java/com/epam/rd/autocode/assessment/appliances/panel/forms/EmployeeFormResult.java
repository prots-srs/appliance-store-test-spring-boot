package com.epam.rd.autocode.assessment.appliances.panel.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeFormResult(
    @NotBlank @Size(min = 3, max = 100) String name,
    @NotBlank @Email @Size(min = 3, max = 100) String email,
    @Pattern(regexp = "^[a-zA-Z0-9@#$%^&]{0,16}$") @Size(min = 0, max = 16) String password,
    @NotBlank @Size(min = 3, max = 100) String department) {

}
