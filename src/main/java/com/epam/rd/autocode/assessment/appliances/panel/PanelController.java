package com.epam.rd.autocode.assessment.appliances.panel;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

// T is form results
public interface PanelController<T> {

  String index(int page, int size, String sort, Model model);

  ResponseEntity<Map<String, String>> delete(Long id);

  String create(Model model);

  String processCreate(T item, BindingResult result, Model model);

  String update(Long id, Model model);

  String processUpdate(final T item, BindingResult result, Long id, Model model);
}
