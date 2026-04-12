package com.epam.rd.autocode.assessment.appliances.service;

import java.util.Locale;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.web.CardLineDto;

public interface OrderService {

  Orders createEmptyCard(Client client);

  CardLineDto getCardLineView(Orders card);

  // CardDto getCardView(Orders card);
}
