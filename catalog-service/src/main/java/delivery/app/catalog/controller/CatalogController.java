package delivery.app.catalog.controller;

import delivery.app.user.CatalogService;
import delivery.app.user.dto.Product;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("api.products")
@AllArgsConstructor
public class CatalogController {

  final CatalogService catalogService;

  @MessageMapping("")
  public Flux<Product> list() {
    return catalogService.findAll();
  }

  @MessageMapping("find")
  public Mono<Product> find(@Payload String productId) {
    return catalogService.find(productId);
  }

  @MessageMapping("exist")
  public Mono<Void> exist(@Payload String productId) {
    return catalogService.exist(productId);
  }
}
