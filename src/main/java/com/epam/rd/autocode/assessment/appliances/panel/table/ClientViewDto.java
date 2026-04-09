package com.epam.rd.autocode.assessment.appliances.panel.table;

import com.epam.rd.autocode.assessment.appliances.model.Client;

public record ClientViewDto(
    Long id,
    String name,
    String email,
    String card) {
  public static ClientViewDto convertFromEntity(Client entity) {
    return new ClientViewDto(
        entity.getId(),
        entity.getName(),
        entity.getEmail(),
        entity.getCard());
  }
}
