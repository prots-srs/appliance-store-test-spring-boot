package com.epam.rd.autocode.assessment.appliances.service.impl;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidParameterException;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import com.epam.rd.autocode.assessment.appliances.web.CardLineDto;

@Service
public class OrderServiceImpl implements OrderService {

  private ApplianceServiceImpl applianceService;
  private Locale locale;

  public OrderServiceImpl(ApplianceServiceImpl applianceService) {
    this.applianceService = applianceService;
    locale = LocaleContextHolder.getLocale();

  }

  @Override
  public Orders createEmptyCard(Client client) {

    Orders order = new Orders();
    order.setClient(client);

    return order;

  }

  @Override
  public CardLineDto getCardLineView(Orders card) {
    if (card == null) {
      throw new InvalidParameterException("Card is null");
    }

    Integer number = getNumber(card);
    BigDecimal amount = getAmount(card);

    return new CardLineDto(number, String.format(locale, "$%,.2f", amount));
  }

  private Integer getNumber(Orders card) {
    Integer number = 0;

    if (card.getOrderRowSet() != null) {
      number = card.getOrderRowSet().size();
    }

    return number;
  }

  private BigDecimal getAmount(Orders card) {
    BigDecimal amount = BigDecimal.ZERO;

    if (card.getOrderRowSet() != null) {

      BigDecimal[] amountItems = new BigDecimal[] { BigDecimal.ZERO };

      card.getOrderRowSet().forEach(rowSet -> {
        if (rowSet.getAppliance() != null
            && rowSet.getAppliance().getPrice() != null
            && rowSet.getNumber() != null) {

          amountItems[0] = amountItems[0]
              .add(rowSet.getAppliance().getPrice().multiply(BigDecimal.valueOf(rowSet.getNumber())));
        }
      });
      amount = amountItems[0];
    }

    return amount;
  }

  // add appliance to card
  // public OrderRow takeIntentionOrderAppliance(Long applianceId, Long number) {
  // if (applianceId == null) {
  // throw new InvalidParameterException("Appliance is null");
  // }
  // if (number == null || number < 1) {
  // throw new InvalidParameterException("Number is wrong");
  // }

  // Optional<Appliance> savedAppliance = repo.findById(applianceId);
  // if (savedAppliance.isPresent()) {
  // Appliance entity = savedAppliance.get();

  // OrderRow orderRow = new OrderRow();
  // orderRow.setAppliance(entity);
  // orderRow.setNumber(number);
  // orderRow.setAmount(entity.getPrice().multiply(BigDecimal.valueOf(number)));

  // return orderRow;
  // }

  // return null;
  // }
}
