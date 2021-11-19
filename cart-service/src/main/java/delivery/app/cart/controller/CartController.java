package delivery.app.cart.controller;

import delivery.app.cart.service.CartService;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

  final CartService cartService;

  @GetMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public Cart get(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    return cartService.get(authentication.getName());
  }

  @PatchMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public void update(@RequestBody Item item, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    cartService.update(authentication.getName(), item);
  }
}
