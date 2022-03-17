package delivery.app.gateway.controller;

import delivery.app.gateway.discovery.DiscoveryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class RoutingController {

  final WebClient        webClient;
  final DiscoveryService discoveryService;

  public RoutingController(WebClient.Builder webClientBuilder,
          DiscoveryService discoveryService) {
    this.webClient = webClientBuilder.build();
    this.discoveryService = discoveryService;
  }

  @RequestMapping({"/api/{service}", "/api/{service}/{*path}"})
  public Mono<ResponseEntity<Flux<DataBuffer>>> handle(
      @PathVariable("service") String service,
      @Nullable @PathVariable(value = "path", required = false) String path,
      HttpEntity<Flux<DataBuffer>> httpEntity,
      HttpMethod httpMethod
  ) {
      return discoveryService
        .findByName(service)
        .map(baseUrl -> {
              return webClient
                      .method(httpMethod)
                      .uri(baseUrl + "/api/" + service + (path == null ? "" : path))
                      .body(httpMethod.equals(HttpMethod.GET)
                              ? BodyInserters.empty()
                              : BodyInserters.fromDataBuffers(httpEntity.getBody())
                      )
                      .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                      .retrieve()
                      .toEntityFlux(BodyExtractors.toDataBuffers())
                      .map(responseEntity ->
                          ResponseEntity.status(responseEntity.getStatusCode())
                                  .headers(hh -> responseEntity.getHeaders().forEach((key, values) -> {
                                    if (!key.equals(HttpHeaders.SET_COOKIE)) {
                                      hh.put(key, values);
                                    }
                                  }))
                                  .body(responseEntity.getBody())
                      );
            }
        )
        .orElse(Mono.just(ResponseEntity.notFound().build()));
  }
}
