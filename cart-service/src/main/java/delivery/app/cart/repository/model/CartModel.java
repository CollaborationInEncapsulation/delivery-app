package delivery.app.cart.repository.model;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CartModel {
  final String id;
  final ConcurrentMap<String, ItemModel> items = new ConcurrentHashMap<>();

  public boolean hasProduct(String productId) {
    return items.containsKey(productId);
  }

  public void update(ItemModel itemUpdate) {
    items.compute(itemUpdate.getProductId(), (key, item) -> {
      if (item != null) {
        final int nextQuantity = item.getQuantity() + itemUpdate.getQuantity();

        if (nextQuantity > 0) {
          return new ItemModel(key, nextQuantity);
        }

        return null;
      }

      return itemUpdate;
    });
  }

  public Collection<ItemModel> items() {
    return items.values();
  }
}
