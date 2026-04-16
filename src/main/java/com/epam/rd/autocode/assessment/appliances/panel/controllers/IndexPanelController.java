package com.epam.rd.autocode.assessment.appliances.panel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panel/")
public class IndexPanelController {

  @GetMapping
  public String index() {
    return "panel/index";

  }
}
