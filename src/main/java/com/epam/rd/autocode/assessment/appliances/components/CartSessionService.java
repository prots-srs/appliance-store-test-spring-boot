package com.epam.rd.autocode.assessment.appliances.components;

public interface CartSessionService {

  CardPageDto getCardView();

  CardLineDto getCardLineView();

  void addApplienceToCard(Long applienceId);

  void removeApplienceFromCard(Long applienceId);

  void quantityApplienceForCard(Long applienceId, Long quantity);

  Long persistOrder(String email);

}
