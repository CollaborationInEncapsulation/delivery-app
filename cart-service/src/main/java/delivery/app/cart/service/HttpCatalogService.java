package delivery.app.cart.service;

import delivery.app.user.CatalogService;
import delivery.app.user.dto.Product;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpCatalogService implements CatalogService {

  final RestTemplate restTemplate;

  public HttpCatalogService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.rootUri("http://localhost:8083").build();
  }

  @Override
  public Product find(String productId) {
    return restTemplate.getForObject("/api/products/{id}", Product.class, productId);
  }

  @Override
  public List<Product> findAll() {
    final Product[] products = restTemplate.getForObject("/api/products", Product[].class);
    return products != null ? Arrays.asList(products) : Collections.emptyList();
  }

  @Override
  public boolean exist(String productId) {
    final ResponseEntity<Void> responseEntity = restTemplate
        .exchange("/api/products/{id}", HttpMethod.HEAD, null, Void.class, productId);

    return responseEntity.getStatusCode().is2xxSuccessful();
  }

  @Override
  public void update(String productId, Product product) {

  }
}
