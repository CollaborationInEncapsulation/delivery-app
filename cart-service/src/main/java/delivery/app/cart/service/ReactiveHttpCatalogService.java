package delivery.app.cart.service;

import java.util.List;

import delivery.app.user.CatalogServiceApi;
import delivery.app.user.ReactiveCatalogServiceApi;
import delivery.app.user.dto.Product;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReactiveHttpCatalogService implements ReactiveCatalogServiceApi {

  final WebClient webClient;

  public ReactiveHttpCatalogService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
  }

  @Override
  public Mono<Product> find(String productId) {
    return webClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .bodyToMono(Product.class);
  }

  @Override
  public Mono<List<Product>> findAll() {
    return webClient.get()
                    .uri("/api/products")
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .collectList();
  }

  @Override
  public Mono<Void> exist(String productId) {
    return webClient
            .head()
            .uri("/api/products/{id}", productId)
            .retrieve()
            .toBodilessEntity()
            .handle((r, s) -> {
              if (r.getStatusCode()
                   .is2xxSuccessful()) {
                s.complete();
              } else {
                s.error(new HttpServerErrorException(r.getStatusCode()));
              }
            });
  }

  @Override
  public Mono<Void> update(String productId, Product product) {
    return Mono.never();
  }
}
