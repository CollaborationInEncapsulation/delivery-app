package delivery.app.cart.service;

import java.util.stream.Collectors;

import delivery.app.cart.repository.CartRepository;
import delivery.app.cart.repository.model.CartModel;
import delivery.app.cart.repository.model.ItemModel;
import delivery.app.user.ReactiveCatalogServiceApi;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultCartService implements CartService {

  final CartRepository cartRepository;
  final ReactiveCatalogServiceApi catalogService;

  @Override
  public Mono<Void> update(String user, Item item) {
    return Mono.defer(() -> {
      final CartModel cartModel = cartRepository.findOrCreate(user);

      if (cartModel.hasProduct(item.getProductId())) {
        cartModel.update(new ItemModel(item.getProductId(), item.getQuantity()));
        return Mono.empty();
      }

      return catalogService.exist(item.getProductId())
        .then(Mono.<Void>fromRunnable(() -> cartModel.update(new ItemModel(item.getProductId(), item.getQuantity()))))
        .onErrorMap(t -> new IllegalArgumentException("Product with given id does not exist", t));
    });
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
