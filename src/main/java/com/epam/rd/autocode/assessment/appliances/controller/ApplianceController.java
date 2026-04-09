package com.epam.rd.autocode.assessment.appliances.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.rd.autocode.assessment.appliances.panel.PanelController;
import com.epam.rd.autocode.assessment.appliances.panel.forms.results.ApplianceFormResult;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.service.impl.ApplianceServiceImpl;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/panel/appliances")
public class ApplianceController implements PanelController<ApplianceFormResult> {

  private final String DEFAULT_PAGE = "1";
  private final String DEFAULT_SIZE = "5";
  private final String DEFAULT_PATH = "/panel/appliances";

  private ApplianceServiceImpl service;

  public ApplianceController(ApplianceServiceImpl service) {
    this.service = service;
  }

  @ModelAttribute
  public void commonModelAttribute(Model model) {
    // page bundle
    model.addAttribute("currentPage", DEFAULT_PATH);
    // model.addAttribute("currentUser", accountService.getCurrentUserForView());
  }

  @Override
  @GetMapping
  public String index(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = DEFAULT_SIZE) int size,
      @RequestParam(required = false) String sort,
      Model model) {

    model.addAttribute("data", service.getTable(new PaginationRequestDto(page, size, sort)));
    return "panel/pages/appliances";
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {

    try {
      // throw or true
      if (service.delete(id)) {
        return ResponseEntity.noContent().build();
      }
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(Map.of("warning", e.getMessage()));
    }

    // no run
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of("warning", "?"));
  }

  @Override
  @GetMapping("/create")

  public String create(Model model) {

    var form = service.getForm(null, null, null);

    model.addAttribute("data", form);
    model.addAttribute("action", DEFAULT_PATH + "/create");

    return "panel/forms/appliance";
  }

  @Override
  @PostMapping("/create")
  public String processCreate(final @Valid @ModelAttribute("item") ApplianceFormResult item, BindingResult result,
      Model model) {

    if (result.hasErrors()) {

      // model.addAttribute("warning", result.getAllErrors());

      model.addAttribute("data", service.getForm(null, item, result.getFieldErrors()));
      model.addAttribute("action", DEFAULT_PATH + "/create");

      return "panel/forms/appliance";
    }

    // Saving
    Long savedId = service.create(item);
    if (savedId != null && savedId.compareTo(0L) > 0) {
      return "redirect:" + DEFAULT_PATH + "/" + savedId + "/edit";
    }

    return "redirect:" + DEFAULT_PATH;
  }

  @Override
  @GetMapping("/{id}/edit")
  public String update(@PathVariable Long id, Model model) {

    var form = service.getForm(id, null, null);
    System.out.println("form:" + form);

    model.addAttribute("data", form);
    model.addAttribute("action", DEFAULT_PATH + "/" + id + "/edit");

    return "panel/forms/appliance";
  }

  @Override
  @PostMapping("/{id}/edit")
  public String processUpdate(final @Valid @ModelAttribute("item") ApplianceFormResult item, BindingResult result,
      @PathVariable("id") Long id,
      Model model) {

    if (result.hasErrors()) {

      // model.addAttribute("warning", result.getAllErrors());

      model.addAttribute("data", service.getForm(id, item, result.getFieldErrors()));
      model.addAttribute("action", DEFAULT_PATH + "/" + id + "/edit");

      return "panel/forms/appliance";

    }

    service.update(id, item);
    return "redirect:" + DEFAULT_PATH + "/{id}/edit";

  }

}
