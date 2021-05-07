package delivery.app.cart.repository;

import delivery.app.cart.repository.model.Cart;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CartRepository {

  final ConcurrentMap<String, Cart> carts = new ConcurrentHashMap<>();

  public Cart findOrCreate(String cartId) {
    return this.carts.computeIfAbsent(cartId, Cart::new);
  }

  @Nullable
  public Cart removeIfPresent(String cartId) {
    return this.carts.remove(cartId);
  }

}
