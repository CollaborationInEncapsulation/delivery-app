package delivery.app.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingController {

  static final Map<String, Integer> serviceToBaseUrlMap = new HashMap<>();

  static {
    serviceToBaseUrlMap.put("users", 8081);
    serviceToBaseUrlMap.put("cart", 8082);
    serviceToBaseUrlMap.put("products", 8083);
    serviceToBaseUrlMap.put("orders", 8084);
    serviceToBaseUrlMap.put("payments", 8085);
  }

  final Map<String, RSocketRequester> rsocketRequestersMap;

  public RoutingController(RSocketRequester.Builder rsocketRequesterBuilder) {
    rsocketRequestersMap = new HashMap<>();
    serviceToBaseUrlMap.forEach((service, port) ->
            rsocketRequestersMap.put(service, rsocketRequesterBuilder.tcp("localhost", port)));
  }

  @RequestMapping(value = "/api/{servicePath}")
  public ResponseEntity<Mono<DataBuffer>> handle(@PathVariable("servicePath") String servicePath,
          HttpEntity<Mono<DataBuffer>> httpEntity, HttpMethod httpMethod) {

    return rsocketRequestersMap
            .keySet()
            .stream()
            .filter(servicePath::startsWith)
            .findFirst()
            .map(rsocketRequestersMap::get)
            .map(requester -> requester.route("api.{servicePath}" + (httpMethod == HttpMethod.PATCH ? ".patch" : ""), servicePath.replace('/', '.'))
                                       .data(httpEntity.getBody() == null ? Mono.empty() : httpEntity.getBody())
                                       .retrieveMono(DataBuffer.class))
            .map(body -> ResponseEntity.ok().body(body))
            .orElse(ResponseEntity.notFound().build());
  }
}
