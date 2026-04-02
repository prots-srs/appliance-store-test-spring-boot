package com.epam.rd.autocode.assessment.appliances.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private PasswordEncoder passwordEncoder;
  // private ManagerDetailsService userLoginService;

  // private static final Logger LOGGER = LogManager.getLogger();

  public SecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    // this.userLoginService = userLoginService;
  }

  @Bean
  CsrfTokenRepository csrfTokenRepository() {
    return CookieCsrfTokenRepository.withHttpOnlyFalse();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    String[] allowedPaths = {
        "/index",
        "/login",
        "/logout",
        "/panel-assets/**",
        "/favicon.ico",
        "/actuator/**",
        "/h2**",
        "/error"
    };
    String[] securityPaths = { "/panel/**", "/login", "/logout" };
    http
        // .securityMatcher(securityPaths)
        .authorizeHttpRequests((requests) -> requests
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
            .permitAll()
            .anyRequest().permitAll())
        // .requestMatchers(allowedPaths).permitAll()
        // .requestMatchers("/panel/**").hasRole("ADMIN")
        // .anyRequest().authenticated())

        .headers(headers -> headers.frameOptions(f -> f.disable()))
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2/**") // Disable CSRF for H2 Console
            .csrfTokenRepository(csrfTokenRepository()));

    // .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()))
    // .sessionManagement((session) -> session.maximumSessions(1))

    // .formLogin((form) -> form
    // .loginPage("/login")
    // .defaultSuccessUrl("/index", true) // Redirect here AFTER successful login
    // .permitAll());

    // .formLogin((form) -> form
    // .loginPage("/login")
    // .loginProcessingUrl("/login")
    // .permitAll())
    // .logout((logout) -> logout
    // .logoutSuccessUrl("/login?logout")
    // .invalidateHttpSession(true)
    // .clearAuthentication(true)
    // .deleteCookies("JSESSIONID")
    // .permitAll());

    return http.build();
  }
}
