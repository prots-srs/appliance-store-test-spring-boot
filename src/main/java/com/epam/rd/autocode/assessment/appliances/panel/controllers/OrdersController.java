package com.epam.rd.autocode.assessment.appliances.panel.controllers;

import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.service.OrderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/panel/orders")

public class OrdersController {

  private final String DEFAULT_PAGE = "1";
  private final String DEFAULT_SIZE = "5";
  private final String DEFAULT_PATH = "/panel/orders";

  private OrderServiceImpl service;

  public OrdersController(OrderServiceImpl service) {
    this.service = service;
  }

  @ModelAttribute
  public void commonModelAttribute(Model model) {
    model.addAttribute("currentPage", DEFAULT_PATH);
  }

  @GetMapping
  public String index(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = DEFAULT_SIZE) int size,
      @RequestParam(required = false) String sort,
      Model model) {

    model.addAttribute("data", service.getTable(new PaginationRequestDto(page, size, sort)));
    return "panel/pages/orders";
  }
}
