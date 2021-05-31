package delivery.app.user;

import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import reactor.core.publisher.Mono;

public interface CartService {

  Mono<Void> update(String user, Item item);

  Cart get(String user);
}
