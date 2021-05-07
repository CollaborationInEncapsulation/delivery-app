package delivery.app.user;

import delivery.app.user.dto.Product;
import java.util.List;

public interface CatalogService {

  Product find(String productId);

  List<Product> findAll();

  boolean exist(String productId);

  void update(String productId, Product product);
}
