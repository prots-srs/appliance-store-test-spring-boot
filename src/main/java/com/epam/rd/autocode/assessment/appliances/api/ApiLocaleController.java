package com.epam.rd.autocode.assessment.appliances.api;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

@Controller
@RequestMapping("/api/v1/locale/")

public class ApiLocaleController {

  private LocaleResolver localeResolver;

  public ApiLocaleController(LocaleResolver localeResolver) {
    this.localeResolver = localeResolver;
  }

  @PostMapping("set/{language}")
  public ResponseEntity<?> changeLocale(@PathVariable String language,
      HttpServletRequest request,
      HttpServletResponse response) {

    Locale locale = Locale.US;
    if (language.equals("uk-UA")) {
      locale = Locale.forLanguageTag(language);
    }

    localeResolver.setLocale(request, response, locale);
    return ResponseEntity.ok().build();
  }
}
