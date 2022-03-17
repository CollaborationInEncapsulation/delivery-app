package delivery.app.cart.service;

import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import reactor.core.publisher.Mono;

import org.springframework.security.access.prepost.PreAuthorize;

public interface CartService {

  Mono<Void> update(String user, Item item);

  Cart get(String user);
}
