package delivery.app.cart.repository;

import delivery.app.cart.repository.model.CartModel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

  final ConcurrentMap<String, CartModel> carts = new ConcurrentHashMap<>();

  public CartModel findOrCreate(String cartId) {
    return this.carts.computeIfAbsent(cartId, CartModel::new);
  }

  @Nullable
  public CartModel removeIfPresent(String cartId) {
    return this.carts.remove(cartId);
  }

}
