package delivery.app.user;

import delivery.app.user.dto.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CatalogService {

  Mono<Product> find(String productId);

  Flux<Product> findAll();

  Mono<Void> exist(String productId);

  Mono<Void> update(String productId, Product product);
}
