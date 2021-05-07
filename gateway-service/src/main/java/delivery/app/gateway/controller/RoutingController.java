package delivery.app.gateway.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RoutingController {

  static final Map<String, String> serviceToBaseUrlMap = new HashMap<>();

  static {
    serviceToBaseUrlMap.put("users", "http://localhost:8081");
    serviceToBaseUrlMap.put("cart", "http://localhost:8082");
    serviceToBaseUrlMap.put("products", "http://localhost:8083");
    serviceToBaseUrlMap.put("orders", "http://localhost:8084");
    serviceToBaseUrlMap.put("payments", "http://localhost:8085");
  }

  final RestTemplate restTemplate;

  public RoutingController(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.requestFactory(HttpComponentsClientHttpRequestFactory::new).build();
  }

  @RequestMapping("/api/{servicePath}")
  public ResponseEntity<Resource> handle(@PathVariable("servicePath") String servicePath,
      HttpEntity<Resource> httpEntity, HttpMethod httpMethod) {
    return serviceToBaseUrlMap.keySet().stream()
        .filter(servicePath::startsWith).findFirst()
        .map(serviceToBaseUrlMap::get)
        .map(baseUrl -> {
              final ResponseEntity<Resource> responseEntity = restTemplate
                  .exchange(baseUrl + "/api/{servicePath}", httpMethod, httpEntity, Resource.class,
                      servicePath);

              return ResponseEntity.status(responseEntity.getStatusCode())
                  .headers(hh -> responseEntity.getHeaders().forEach((key, values) -> {
                    if (!key.equals(HttpHeaders.SET_COOKIE)) {
                      hh.put(key, values);
                    }
                  }))
                  .body(responseEntity.getBody());
            }
        )
        .orElse(ResponseEntity.notFound().build());
  }
}
