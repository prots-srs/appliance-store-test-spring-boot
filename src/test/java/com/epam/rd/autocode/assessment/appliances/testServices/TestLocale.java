package com.epam.rd.autocode.assessment.appliances.testServices;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class TestLocale {

  @Test
  void testLocaleEN() {
    Locale locale = Locale.forLanguageTag("en-US");

    assertEquals(locale.getLanguage(), "en");
    assertEquals(locale.getCountry(), "US");
  }

  @Test
  void testLocaleUk() {
    Locale locale = Locale.forLanguageTag("uk-UA");

    assertEquals(locale.getLanguage(), "uk");
    assertEquals(locale.getCountry(), "UA");
  }
}
