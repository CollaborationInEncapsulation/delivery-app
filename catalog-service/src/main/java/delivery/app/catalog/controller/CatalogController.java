package delivery.app.catalog.controller;

import delivery.app.catalog.service.CatalogService;
import delivery.app.user.dto.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class CatalogController {

  final CatalogService catalogService;

  @GetMapping
  public List<Product> findAll() {
    return catalogService.findAll();
  }

  @GetMapping("/{id}")
  public Product find(@PathVariable("id") String productId) {
    return catalogService.find(productId);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
  public ResponseEntity<Void> exist(@PathVariable("id") String productId) {
    return catalogService.exist(productId)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public void update(@PathVariable("id") String productId, Product product) {

  }

}
