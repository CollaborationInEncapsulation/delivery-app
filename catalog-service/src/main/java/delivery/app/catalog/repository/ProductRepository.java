package delivery.app.catalog.repository;

import delivery.app.catalog.repository.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {

}
