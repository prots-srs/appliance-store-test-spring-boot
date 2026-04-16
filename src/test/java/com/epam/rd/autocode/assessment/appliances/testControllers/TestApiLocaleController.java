package com.epam.rd.autocode.assessment.appliances.testControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.LocaleResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Locale;

import com.epam.rd.autocode.assessment.appliances.api.ApiLocaleController;
import com.epam.rd.autocode.assessment.appliances.security.UserLoginService;
import com.epam.rd.autocode.assessment.appliances.utils.LocalizeService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(controllers = ApiLocaleController.class)
class TestApiLocaleController {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private LocaleResolver localeResolver;

  @MockitoBean
  private LocalizeService localizeService;

  @MockitoBean
  private PasswordEncoder passwordEncoder;

  @MockitoBean
  private UserLoginService userLoginService;

  @Test
  @WithMockUser
  void setLocaleEn() throws Exception {
    mockMvc.perform(post("/api/v1/locale/set/en-US").with(csrf()))
        .andExpect(status().isOk());

    verify(localeResolver).setLocale(any(), any(), eq(Locale.US));
  }

  @Test
  @WithMockUser
  void setLocaleUk() throws Exception {
    mockMvc.perform(post("/api/v1/locale/set/uk-UA").with(csrf()))
        .andExpect(status().isOk());

    verify(localeResolver).setLocale(any(), any(), eq(Locale.forLanguageTag("uk-UA")));
  }
}
