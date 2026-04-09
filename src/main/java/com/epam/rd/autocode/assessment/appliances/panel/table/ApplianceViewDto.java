package com.epam.rd.autocode.assessment.appliances.panel.table;

import java.util.Locale;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;

public record ApplianceViewDto(
    Long id,
    String name,
    String category,
    String model,
    String manufacturerName,
    String powerType,
    String characteristic,
    String description,
    Integer power,
    String price) {
  public static ApplianceViewDto convertFromEntity(Appliance entity, Locale locale) {
    return new ApplianceViewDto(
        entity.getId(),
        entity.getName(),
        entity.getCategory().toString(),
        entity.getModel(),
        entity.getManufacturer().getName(),
        entity.getPowerType().toString(),
        entity.getCharacteristic(),
        entity.getDescription(),
        entity.getPower(),
        String.format(locale, "$%,.2f", entity.getPrice()));
  }
}
