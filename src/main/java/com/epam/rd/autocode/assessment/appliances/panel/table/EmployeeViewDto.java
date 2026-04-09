package com.epam.rd.autocode.assessment.appliances.panel.table;

import com.epam.rd.autocode.assessment.appliances.model.Employee;

public record EmployeeViewDto(
    Long id,
    String name,
    String email,
    String department) {
  public static EmployeeViewDto convertFromEntity(Employee entity) {
    return new EmployeeViewDto(
        entity.getId(),
        entity.getName(),
        entity.getEmail(),
        entity.getDepartment());
  }

}
