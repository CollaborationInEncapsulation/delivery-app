package delivery.app.cart.service;

import delivery.app.cart.repository.CartRepository;
import delivery.app.catalog.CatalogService;
import delivery.app.user.CartService;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class DefaultCartService implements CartService {

  final CartRepository cartRepository;
  final CatalogService catalogService;

  @Override
  public Mono<Void> update(String user, Item item) {
    return Mono.defer(() -> {
      final delivery.app.cart.repository.model.Cart cartModel = cartRepository.findOrCreate(user);

      if (cartModel.hasProduct(item.getProductId())) {
        cartModel.update(
            new delivery.app.cart.repository.model.Item(item.getProductId(), item.getQuantity()));
        return Mono.empty();
      }

      return catalogService.exist(item.getProductId())
          .then(Mono.fromRunnable(() -> cartModel.update(
              new delivery.app.cart.repository.model.Item(item.getProductId(),
                  item.getQuantity()))));
    });
  }

  @Override
  public Cart get(String user) {
    final delivery.app.cart.repository.model.Cart cartModel = cartRepository.findOrCreate(user);

    return new Cart(
        cartModel.items()
            .stream()
            .map(i -> new Item(i.getProductId(), i.getQuantity()))
            .collect(Collectors.toSet()));
  }
}
