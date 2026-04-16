package com.epam.rd.autocode.assessment.appliances.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.epam.rd.autocode.assessment.appliances.components.CardPageDto;
import com.epam.rd.autocode.assessment.appliances.components.CartSessionImpl;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceServiceImpl;
import com.epam.rd.autocode.assessment.appliances.service.OrderServiceImpl;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexWebController {

  private final String DEFAULT_PAGE = "1";
  private final String DEFAULT_SIZE = "4";

  private ApplianceServiceImpl service;
  private CartSessionImpl cartService;

  public IndexWebController(ApplianceServiceImpl service,
      CartSessionImpl cartService) {
    this.service = service;
    this.cartService = cartService;
  }

  @GetMapping
  public String index(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = DEFAULT_SIZE) int size,
      // @RequestParam(required = false) String sort,
      Model model) {

    model.addAttribute("data", service.getTable(new PaginationRequestDto(page, size, "name")));

    model.addAttribute("card", cartService.getCardLineView());

    return "web/index";
  }

  @GetMapping("card")

  public String card(Model model) {

    CardPageDto list = cartService.getCardView();
    if (list.appliances().size() == 0) {
      return "redirect:/";
    }

    model.addAttribute("card", list);
    return "web/pages/card";
  }

  @GetMapping("checkout/{orderId}")

  public String card(@PathVariable String orderId, Model model) {

    model.addAttribute("orderId", orderId);
    return "web/pages/checkout_success";
  }

}
