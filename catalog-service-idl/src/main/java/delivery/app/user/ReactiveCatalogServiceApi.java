package delivery.app.user;

import java.util.List;

import delivery.app.user.dto.Product;
import reactor.core.publisher.Mono;

public interface ReactiveCatalogServiceApi {

  Mono<Product> find(String productId);

  Mono<List<Product>> findAll();

  Mono<Void> exist(String productId);

  Mono<Void> update(String productId, Product product);
}
