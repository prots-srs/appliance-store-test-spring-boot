package com.epam.rd.autocode.assessment.appliances.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.service.impl.ApplianceServiceImpl;
import com.epam.rd.autocode.assessment.appliances.service.impl.OrderServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {

  private final String DEFAULT_PAGE = "1";
  private final String DEFAULT_SIZE = "4";
  // private final String DEFAULT_PATH = "/panel/appliances";

  private ApplianceServiceImpl service;
  private OrderServiceImpl orderService;

  public IndexController(ApplianceServiceImpl service,
      OrderServiceImpl orderService) {
    this.service = service;
    this.orderService = orderService;
  }

  @GetMapping
  public String index(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = DEFAULT_SIZE) int size,
      // @RequestParam(required = false) String sort,
      Model model,
      HttpSession session) {

    model.addAttribute("data", service.getTable(new PaginationRequestDto(page, size, "name")));

    Orders card = (Orders) session.getAttribute("card");
    if (card == null) {
      card = orderService.createEmptyCard(new Client());
    }

    model.addAttribute("card", orderService.getCardLineView(card));

    return "web/index";
  }

}
