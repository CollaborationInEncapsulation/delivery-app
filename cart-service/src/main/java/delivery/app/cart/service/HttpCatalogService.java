package delivery.app.cart.service;

import delivery.app.user.CatalogServiceApi;
import delivery.app.user.dto.Product;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HttpCatalogService implements CatalogServiceApi {

  final WebClient webClient;

  public HttpCatalogService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
  }

  @Override
  public Product find(String productId) {
    return webClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();
  }

  @Override
  public List<Product> findAll() {
    return webClient.get()
                    .uri("/api/products")
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .collectList()
                    .block();
  }

  @Override
  public boolean exist(String productId) {
    final ResponseEntity<Void> responseEntity = webClient
            .head()
            .uri("/api/products/{id}", productId)
            .retrieve()
            .toBodilessEntity()
            .block();

    return responseEntity.getStatusCode().is2xxSuccessful();
  }

  @Override
  public void update(String productId, Product product) {

  }
}
