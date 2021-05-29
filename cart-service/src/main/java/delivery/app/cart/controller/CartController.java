package delivery.app.cart.controller;

import delivery.app.user.CartService;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import lombok.AllArgsConstructor;

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
  public Cart get() {
    return cartService.get();
  }

  @PatchMapping
  public void patch(@RequestBody Item item) {
    cartService.update(item);
  }
}
