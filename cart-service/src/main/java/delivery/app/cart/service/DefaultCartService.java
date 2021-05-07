package delivery.app.cart.service;

import delivery.app.cart.repository.CartRepository;
import delivery.app.user.CartService;
import delivery.app.user.CatalogService;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultCartService implements CartService {

  final CartRepository cartRepository;
  final CatalogService catalogService;

  @Override
  @PreAuthorize("hasRole('ROLE_USER')")
  public void update(Item item) {
    final String user = SecurityContextHolder.getContext().getAuthentication().getName();
    final delivery.app.cart.repository.model.Cart cartModel = cartRepository.findOrCreate(user);

    if (cartModel.hasProduct(item.getProductId())) {
      cartModel.update(
          new delivery.app.cart.repository.model.Item(item.getProductId(), item.getQuantity()));
      return;
    }

    if (catalogService.exist(item.getProductId())) {
      cartModel.update(
          new delivery.app.cart.repository.model.Item(item.getProductId(), item.getQuantity()));
    } else {
      throw new IllegalArgumentException("Product with given id does not exist");
    }
  }

  @Override
  @PreAuthorize("hasRole('ROLE_USER')")
  public Cart get() {
    final String user = SecurityContextHolder.getContext().getAuthentication().getName();
    final delivery.app.cart.repository.model.Cart cartModel = cartRepository.findOrCreate(user);

    return new Cart(
        cartModel.items()
            .stream()
            .map(i -> new Item(i.getProductId(), i.getQuantity()))
            .collect(Collectors.toSet()));
  }
}
