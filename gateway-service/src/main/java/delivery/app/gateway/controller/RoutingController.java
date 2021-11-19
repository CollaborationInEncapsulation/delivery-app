package delivery.app.gateway.controller;

import delivery.app.gateway.discovery.DiscoveryService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RoutingController {

  final RestTemplate restTemplate;
  final DiscoveryService discoveryService;

  public RoutingController(RestTemplateBuilder restTemplateBuilder,
      DiscoveryService discoveryService) {
    this.restTemplate = restTemplateBuilder
        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
        .build();
    this.discoveryService = discoveryService;
  }

  @RequestMapping({"/api/{service}", "/api/{service}/{*path}"})
  public ResponseEntity<Resource> handle(@PathVariable("service") String service, @Nullable @PathVariable(value = "path", required = false) String path,
      HttpEntity<Resource> httpEntity, HttpMethod httpMethod) {
      return discoveryService.findByName(service)
        .map(baseUrl -> {
              final ResponseEntity<Resource> responseEntity = restTemplate
                  .exchange(baseUrl + "/api/" + service + (path == null ? "" : path), httpMethod, httpEntity, Resource.class, service, path);

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
