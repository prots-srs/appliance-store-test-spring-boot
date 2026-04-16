package com.epam.rd.autocode.assessment.appliances;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ModelAttribute("user")
  public String username(Authentication authentication) {

    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return null;
  }
}
