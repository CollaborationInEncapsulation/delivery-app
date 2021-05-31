package delivery.app.cart.controller;

import delivery.app.user.CartService;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("api.cart")
@AllArgsConstructor
public class CartController {

  final CartService cartService;

  @MessageMapping("")
  public Cart get(@AuthenticationPrincipal String user) {
    return cartService.get(user);
  }

  @MessageMapping("patch")
  public Mono<Void> patch(@Payload Item item, @AuthenticationPrincipal String user) {
    return cartService.update(user, item);
  }
}
