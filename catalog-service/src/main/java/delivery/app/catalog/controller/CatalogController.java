package delivery.app.catalog.controller;

import delivery.app.catalog.CatalogService;
import delivery.app.catalog.dto.Product;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("api.products")
@AllArgsConstructor
public class CatalogController {

  final CatalogService catalogService;

  @MessageMapping("")
  public Mono<Collection<Product>> list() {
    return catalogService.findAll();
  }

  @MessageMapping("{productId}")
  public Mono<Product> find(@DestinationVariable("productId") String productId) {
    return catalogService.find(productId);
  }

  @MessageMapping("{productId}.exist")
  public Mono<Void> exist(@DestinationVariable("productId") String productId) {
    return catalogService.exist(productId);
  }
}
