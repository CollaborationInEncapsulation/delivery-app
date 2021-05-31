package delivery.app.cart.service;

import delivery.app.catalog.CatalogService;
import delivery.app.catalog.dto.Product;
import java.util.Collection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RSocketCatalogService implements CatalogService {

  final RSocketRequester rSocketRequester;

  public RSocketCatalogService(RSocketRequester.Builder rsocketRequesterBuilder) {
    this.rSocketRequester = rsocketRequesterBuilder.tcp("localhost", 8083);
  }

  @Override
  public Mono<Product> find(String productId) {
    return rSocketRequester.route("api.products.{id}", productId)
        .retrieveMono(Product.class);
  }

  @Override
  public Mono<Collection<Product>> findAll() {
    return rSocketRequester.route("api.products")
        .retrieveMono(new ParameterizedTypeReference<Collection<Product>>() {});
  }

  @Override
  public Mono<Void> exist(String productId) {
    return rSocketRequester
        .route("api.products.{id}.exist", productId)
        .retrieveMono(Void.class);
  }

  @Override
  public Mono<Void> update(String productId, Product product) {
    return Mono.empty();
  }
}
