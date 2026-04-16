package com.epam.rd.autocode.assessment.appliances.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private PasswordEncoder passwordEncoder;
  private UserLoginService userLoginService;

  public SecurityConfig(PasswordEncoder passwordEncoder, UserLoginService userLoginService) {
    this.passwordEncoder = passwordEncoder;
    this.userLoginService = userLoginService;
  }

  @Bean
  CsrfTokenRepository csrfTokenRepository() {
    return CookieCsrfTokenRepository.withHttpOnlyFalse();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    String[] allowedPaths = {
        "/api/**",
        "/",
        "/card",
        "/login",
        "/logout",
        "/css/**",
        "/fonts/**",
        "/js/**",
        "/favicon.ico",
        "/actuator/**",
        "/h2/**",
        "/error"
    };

    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    http
        .authorizeHttpRequests((requests) -> requests
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
            .permitAll()
            .requestMatchers(allowedPaths).permitAll()
            .requestMatchers("/panel/**").hasRole("EMPLOYEE")
            .anyRequest().authenticated())
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .headers(headers -> headers.frameOptions(f -> f.disable()))
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2/**") // Disable CSRF for H2 Console
            .csrfTokenRepository(csrfTokenRepository())
            .csrfTokenRequestHandler(requestHandler))
        .formLogin((form) -> form
            // .loginPage("/login")
            .loginProcessingUrl("/login")
            // .successHandler(successHandler())
            // .defaultSuccessUrl("/index", true) // Redirect here AFTER successful login
            .permitAll())
        .logout((logout) -> logout
            // .logoutSuccessUrl("/login?logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            // .deleteCookies("JSESSIONID")
            .permitAll());

    return http.build();
  }

  private AuthenticationProvider authenticationProvider() {
    // from v3.5.4
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userLoginService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  // @Bean
  // public AuthenticationSuccessHandler successHandler() {
  // return (request, response, authentication) -> {
  // String email = authentication.getName();
  // List<Client> clients = clientRepository.findByEmailIgnoreCase(email);
  // if (!clients.isEmpty()) {
  // request.getSession().setAttribute("client", clients.get(0));
  // }

  // response.sendRedirect("/");
  // };
  // }
}
