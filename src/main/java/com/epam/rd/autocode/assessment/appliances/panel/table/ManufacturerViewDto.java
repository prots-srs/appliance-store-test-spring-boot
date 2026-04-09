package com.epam.rd.autocode.assessment.appliances.panel.table;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

public record ManufacturerViewDto(
    Long id,
    String name) {
  public static ManufacturerViewDto convertFromEntity(Manufacturer entity) {
    return new ManufacturerViewDto(
        entity.getId(),
        entity.getName());
  }
}
