package delivery.app.catalog;

import delivery.app.catalog.dto.Product;
import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CatalogService {

  Mono<Product> find(String productId);

  Mono<Collection<Product>> findAll();

  Mono<Void> exist(String productId);

  Mono<Void> update(String productId, Product product);
}
