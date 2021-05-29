package delivery.app.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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

  final WebClient webClient;

  public RoutingController(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  @RequestMapping("/api/{servicePath}")
  public Mono<ResponseEntity<Resource>> handle(@PathVariable("servicePath") String servicePath,
          HttpEntity<Resource> httpEntity,
          HttpMethod httpMethod) {

    return serviceToBaseUrlMap.keySet()
                              .stream()
                              .filter(servicePath::startsWith)
                              .findFirst()
                              .map(serviceToBaseUrlMap::get)
                              .map(baseUrl ->
                                      webClient.method(httpMethod)
                                               .uri(baseUrl + "/api/{servicePath}", servicePath)
                                               .headers(hh -> hh.putAll(httpEntity.getHeaders()))
                                               .body(httpEntity.getBody() == null ? BodyInserters.empty() : BodyInserters.fromResource(httpEntity.getBody()))
                                               .retrieve()
                                               .toEntity(Resource.class)
                                               .map(responseEntity ->
                                                       ResponseEntity.status(responseEntity.getStatusCode())
                                                                     .headers(hh ->
                                                                             responseEntity.getHeaders()
                                                                                           .forEach((key, values) -> {
                                                                                             if (!key.equals(HttpHeaders.SET_COOKIE)) {
                                                                                               hh.put(key, values);
                                                                                             }
                                                                                           }))
                                                                     .body(responseEntity.getBody()))
                                               .subscribeOn(Schedulers.boundedElastic())
                                               .contextWrite(Context.of(SecurityContext.class, SecurityContextHolder.getContext())))
                              .orElse(Mono.just(ResponseEntity.notFound().build()));
  }
}
