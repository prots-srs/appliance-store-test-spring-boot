package com.epam.rd.autocode.assessment.appliances.panel.table;

import java.util.Locale;

import com.epam.rd.autocode.assessment.appliances.model.Orders;
import java.math.BigDecimal;

public record OrderViewDto(
    Long id,
    String clientEmail,
    String employeeEmail,
    String amount,
    Boolean approved) {
  public static OrderViewDto convertFromEntity(Orders entity, Locale locale) {

    BigDecimal amount = BigDecimal.ZERO;

    if (entity.getOrderRowSet() != null) {

      BigDecimal[] amountItems = new BigDecimal[] { BigDecimal.ZERO };

      entity.getOrderRowSet().forEach(rowSet -> {
        if (rowSet != null
            && rowSet.getAppliance() != null
            && rowSet.getAppliance().getPrice() != null
            && rowSet.getNumber() != null) {

          amountItems[0] = amountItems[0]
              .add(rowSet.getAppliance().getPrice().multiply(BigDecimal.valueOf(rowSet.getNumber())));
        }
      });
      amount = amountItems[0];
    }

    return new OrderViewDto(
        entity.getId(),
        entity.getClient() != null ? entity.getClient().getEmail() : "",
        entity.getEmployee() != null ? entity.getEmployee().getEmail() : "",
        String.format(locale, "$%,.2f", amount),
        entity.getApproved());
  }

}
