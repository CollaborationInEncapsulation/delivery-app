package delivery.app.cart.service;

import delivery.app.cart.repository.CartRepository;
import delivery.app.cart.repository.model.CartModel;
import delivery.app.cart.repository.model.ItemModel;
import delivery.app.user.CatalogServiceApi;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultCartService implements CartService {

  final CartRepository cartRepository;
  final CatalogServiceApi catalogService;

  @Override
  public void update(String user, Item item) {
    final CartModel cartModel = cartRepository.findOrCreate(user);

    if (cartModel.hasProduct(item.getProductId())) {
      cartModel.update(
          new ItemModel(item.getProductId(), item.getQuantity()));
      return;
    }

    if (catalogService.exist(item.getProductId())) {
      cartModel.update(
          new ItemModel(item.getProductId(), item.getQuantity()));
    } else {
      throw new IllegalArgumentException("Product with given id does not exist");
    }
  }

  @Override
  public Cart get(String user) {
    final CartModel cartModel = cartRepository.findOrCreate(user);

    return new Cart(
        cartModel.items()
            .stream()
            .map(i -> new Item(i.getProductId(), i.getQuantity()))
            .collect(Collectors.toSet()));
  }
}
