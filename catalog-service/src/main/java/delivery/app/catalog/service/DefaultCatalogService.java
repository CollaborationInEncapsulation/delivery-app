package delivery.app.catalog.service;

import delivery.app.catalog.CatalogService;
import delivery.app.catalog.dto.Product;
import delivery.app.catalog.repository.ProductRepository;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class DefaultCatalogService implements CatalogService {

  final ProductRepository productRepository;

  @Override
  public Mono<Product> find(String productId) {
    return productRepository.findById(productId)
        .map(model -> new Product(model.getId(), model.getName(), model.getDescription(),
            model.getImgLink(), model.getPrice(), model.getCurrency(), model.isAvailable()))
        .switchIfEmpty(Mono.error(
            () -> new NoSuchElementException("The product with the given id does not exist")));
  }

  @Override
  public Mono<Collection<Product>> findAll() {
    // TODO:
    return productRepository.findAll()
        .map(model -> new Product(model.getId(), model.getName(), model.getDescription(),
            model.getImgLink(), model.getPrice(), model.getCurrency(), model.isAvailable()))
        .collect(Collectors.toList());
  }

  @Override
  public Mono<Void> exist(String productId) {
    return productRepository.existsById(productId)
        .handle(((exist, sink) -> {
          if (exist) {
            sink.complete();
          } else {
            sink.error(new NoSuchElementException("The product with the given id does not exist"));
          }
        }));
  }

  @Override
  public Mono<Void> update(String productId, Product product) {
    return productRepository.save(
        new delivery.app.catalog.repository.model.Product(productId, product.getName(),
            product.getDescription(), product.getImgLink(), product.getPrice(),
            product.getCurrency(), product.isAvailable()))
        .then(Mono.empty());
  }
}
