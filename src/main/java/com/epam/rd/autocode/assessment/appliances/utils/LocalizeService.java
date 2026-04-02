package com.epam.rd.autocode.assessment.appliances.utils;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocalizeService {

  private final MessageSource messageSource;
  private Locale locale;

  public LocalizeService(MessageSource messageSource) {
    this.messageSource = messageSource;
    locale = LocaleContextHolder.getLocale();
  }

  public String getMessage(String translationKey) {
    if (translationKey == null) {
      return "";
    }

    String outputMessage = "";
    try {
      outputMessage = messageSource.getMessage(translationKey, null, locale);
    } catch (Exception e) {
    }
    return outputMessage;
  }

  public String getMessage(String translationKey, Object[] args) {
    if (translationKey == null) {
      return "";
    }

    String outputMessage = "";
    try {
      outputMessage = messageSource.getMessage(translationKey, args, locale);
    } catch (Exception e) {
    }
    return outputMessage;
  }

  public String getMessage(MessageSourceResolvable resolvable) {
    if (resolvable == null) {
      return "";
    }

    String outputMessage = "";
    try {
      outputMessage = messageSource.getMessage(resolvable, locale);
    } catch (Exception e) {
    }
    return outputMessage;
  }
}
