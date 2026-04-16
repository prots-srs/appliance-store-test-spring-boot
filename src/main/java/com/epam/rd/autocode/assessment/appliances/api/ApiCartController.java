package com.epam.rd.autocode.assessment.appliances.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epam.rd.autocode.assessment.appliances.components.CartSessionImpl;
import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidProcessOrders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/cart/")
public class ApiCartController {

  private CartSessionImpl cartService;

  public ApiCartController(CartSessionImpl cartService) {
    this.cartService = cartService;
  }

  @PostMapping("add/{id}")
  public ResponseEntity<?> addToCard(@PathVariable Long id) {

    try {
      cartService.addApplienceToCard(id);
      return ResponseEntity.noContent().build();

    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(Map.of("warning", e.getMessage()));
    }
  }

  @PostMapping("remove/{id}")
  public ResponseEntity<?> removeFromCard(@PathVariable Long id) {

    try {
      cartService.removeApplienceFromCard(id);
      return ResponseEntity.noContent().build();

    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(Map.of("warning", e.getMessage()));
    }
  }

  @PostMapping("quantity/{id}/{quantity}")
  public ResponseEntity<?> removeFromCard(@PathVariable Long id,
      @PathVariable Long quantity) {

    try {
      cartService.quantityApplienceForCard(id, quantity);
      return ResponseEntity.noContent().build();

    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(Map.of("warning", e.getMessage()));
    }
  }

  @PostMapping("checkout")
  public ResponseEntity<?> checkout() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken)) {

      boolean isClient = authentication.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .anyMatch("ROLE_CLIENT"::equals);

      if (isClient) {

        try {
          Long orderId = cartService.persistOrder(authentication.getName());
          return ResponseEntity.ok().body(Map.of("order_id", orderId));

        } catch (Exception e) {
          return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(Map.of("warning", e.getMessage()));
        }

      } else {

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("warning", "User isn't client"));
      }

    }

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of("warning", "User isn't authenticated"));

  }
}
