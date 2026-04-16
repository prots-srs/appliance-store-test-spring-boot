package com.epam.rd.autocode.assessment.appliances.components;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidParameterException;
import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidProcessOrders;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceServiceImpl;
import java.math.BigDecimal;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartSessionImpl implements CartSessionService {

  private Orders cart = new Orders();

  private final String FORMAT_PRICE = "$%,.2f";

  private ApplianceServiceImpl applianceService;
  private Locale locale;
  private ClientRepository clientRepository;
  private OrdersRepository ordersRepository;

  public CartSessionImpl(ApplianceServiceImpl applianceService,
      ClientRepository clientRepository,
      OrdersRepository ordersRepository) {
    this.applianceService = applianceService;
    this.clientRepository = clientRepository;
    this.ordersRepository = ordersRepository;

    locale = LocaleContextHolder.getLocale();
  }

  @Override
  public CardLineDto getCardLineView() {

    Integer number = getNumberRows();
    BigDecimal amount = getTotalAmount();

    return new CardLineDto(number, printPrice(amount));
  }

  private Integer getNumberRows() {
    Integer number = 0;

    if (cart.getOrderRowSet() != null) {
      number = cart.getOrderRowSet().size();
    }

    return number;
  }

  private BigDecimal getTotalAmount() {
    BigDecimal amount = BigDecimal.ZERO;

    if (cart.getOrderRowSet() != null) {

      BigDecimal[] amountItems = new BigDecimal[] { BigDecimal.ZERO };

      cart.getOrderRowSet().forEach(rowSet -> {
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

    return amount;
  }

  private String printPrice(BigDecimal price) {
    return String.format(locale, FORMAT_PRICE, price);
  }

  @Override
  public CardPageDto getCardView() {

    Set<ApplianceInCardDto> appliances = new TreeSet<ApplianceInCardDto>(Comparator.comparing(dto -> dto.name()));

    cart.getOrderRowSet().forEach(row -> {
      if (row.getAppliance() != null) {
        appliances.add(new ApplianceInCardDto(
            row.getAppliance().getId(),
            row.getAppliance().getName(),
            row.getAppliance().getModel(),
            printPrice(row.getAppliance().getPrice()),
            row.getNumber(),
            printPrice(row.getAmount())));
      }
    });

    return new CardPageDto(appliances, printPrice(getTotalAmount()));
  }

  @Override
  public void addApplienceToCard(Long applienceId) {
    if (applienceId == null || applienceId < 0) {
      throw new InvalidParameterException("Appliance id in wrong");
    }

    Appliance appliance = applianceService.getEntity(applienceId);
    if (appliance == null) {
      throw new InvalidProcessOrders("Appliance not found");
    }

    OrderRow orderRow = null;
    if (cart.getOrderRowSet() == null) {
      cart.setOrderRowSet(new HashSet<>());
    }

    // check has added item
    Optional<OrderRow> appRow = cart.getOrderRowSet().stream().filter(o -> o.getAppliance().getId().equals(applienceId))
        .findFirst();
    if (appRow.isPresent()) {
      orderRow = appRow.get();
    }

    // orderRow not null - do nothing
    if (orderRow == null) {
      OrderRow orderRowNew = new OrderRow();
      orderRowNew.setAppliance(appliance);
      orderRowNew.setNumber(1L);
      orderRowNew.setAmount(appliance.getPrice());

      cart.getOrderRowSet().add(orderRowNew);
      // card.addRow(orderRow);
    }

  }

  @Override
  public void removeApplienceFromCard(Long applienceId) {
    if (applienceId == null || applienceId < 0) {
      throw new InvalidParameterException("Appliance id is wrong");
    }

    if (cart.getOrderRowSet() == null) {
      throw new InvalidProcessOrders("Rowset not found");
    }

    if (cart.getOrderRowSet().removeIf(row -> row.getAppliance().getId().equals(applienceId))) {
      return;
    } else {
      throw new InvalidProcessOrders("Remove is wrong");
    }
  }

  @Override
  public void quantityApplienceForCard(Long applienceId, Long quantity) {
    if (applienceId == null || applienceId < 0) {
      throw new InvalidParameterException("Appliance id is wrong");
    }
    if (quantity == null || quantity < 0) {
      throw new InvalidParameterException("Quantity id is wrong");
    }

    if (quantity.equals(0L)) {
      removeApplienceFromCard(applienceId);
      return;
    }

    OrderRow orderRow = null;
    if (cart.getOrderRowSet() == null) {
      throw new InvalidProcessOrders("Rowset not found");
    }

    // check has added item
    Optional<OrderRow> appRow = cart.getOrderRowSet().stream().filter(o -> o.getAppliance().getId().equals(applienceId))
        .findFirst();
    if (appRow.isPresent()) {
      orderRow = appRow.get();
      orderRow.setNumber(quantity);
      orderRow.setAmount(orderRow.getAppliance().getPrice().multiply(BigDecimal.valueOf(quantity)));
    } else {
      throw new InvalidProcessOrders("Appliance not found");

    }
  }

  @Override
  public Long persistOrder(String email) {
    Client client = getClient(email);
    if (client == null) {
      throw new InvalidProcessOrders("Cliend not found");
    }

    cart.setClient(client);
    Orders savedOrder = ordersRepository.saveAndFlush(cart);

    cart = new Orders();
    // cart.setClient(client);

    return savedOrder.getId();
  }

  private Client getClient(String email) {

    List<Client> client = clientRepository.findByEmailIgnoreCase(email);
    if (client.size() > 0) {
      return client.get(0);
    }

    return null;
  }

}
