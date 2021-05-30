package delivery.app.catalog.repository;

import delivery.app.catalog.repository.model.Product;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

}
