package com.epam.rd.autocode.assessment.appliances.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import org.springframework.web.servlet.LocaleResolver;

@Configuration
@EnableWebMvc
public class MainConfig implements WebMvcConfigurer {
  @Bean
  LocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    // new Locale("uk", "UA");
    sessionLocaleResolver.setDefaultLocale(Locale.US);
    return sessionLocaleResolver;
  }

  @Bean
  LocaleChangeInterceptor siteLocaleChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("language");
    return lci;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    if (registry == null) {
      throw new IllegalArgumentException("InterceptorRegistry must not be null");
    }
    registry.addInterceptor(siteLocaleChangeInterceptor());
  }

  // public void addViewControllers(ViewControllerRegistry registry) {
  // if (registry == null) {
  // throw new IllegalArgumentException("ResourceHandlerRegistry must not be
  // null");
  // }
  // registry.addViewController("/login").setViewName("login");
  // }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (registry == null) {
      throw new IllegalArgumentException("ResourceHandlerRegistry must not be null");
    }
    // registry
    // .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));

    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
    registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
    registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
  }
}
