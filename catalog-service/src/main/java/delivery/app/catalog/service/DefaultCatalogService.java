package delivery.app.catalog.service;

import delivery.app.catalog.repository.ProductRepository;
import delivery.app.user.dto.Product;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultCatalogService implements CatalogService {

  final ProductRepository productRepository;

  @Override
  public Product find(String productId) {
    return productRepository.findById(productId)
        .map(model -> new Product(model.getId(), model.getName(), model.getDescription(), model.getImgLink(),
            model.getPrice(), model.getCurrency(), model.isAvailable())) // TODO add converter
        .orElse(null);
  }

  @Override
  public List<Product> findAll() {
    // TODO:
    return StreamSupport.stream(productRepository.findAll().spliterator(), false)
        .map(model -> new Product(model.getId(), model.getName(), model.getDescription(), model.getImgLink(),
            model.getPrice(), model.getCurrency(), model.isAvailable()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean exist(String productId) {
    return productRepository.existsById(productId);
  }

  @Override
  public void update(String productId, Product product) {
    productRepository.save(
        new delivery.app.catalog.repository.model.Product(productId, product.getName(),
            product.getDescription(), product.getImgLink(), product.getPrice(),
            product.getCurrency(), product.isAvailable()));
  }
}
