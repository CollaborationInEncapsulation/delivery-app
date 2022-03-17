package delivery.app.gateway.user;

import java.util.Collection;

import delivery.app.user.AuthenticationServiceApi;
import delivery.app.user.ReactiveAuthenticationServiceApi;
import delivery.app.user.dto.Authority;
import delivery.app.user.dto.UsernameAndPassword;
import reactor.core.publisher.Mono;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
class ReactiveHttpAuthenticationService implements ReactiveAuthenticationServiceApi {

  final WebClient webClient;

  ReactiveHttpAuthenticationService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder
        .baseUrl("http://localhost:8081")
        .build();
  }

  @Override
  public Mono<Collection<Authority>> authenticate(String username, CharSequence password) {
    final UsernameAndPassword payload = new UsernameAndPassword(username, password);

    return webClient
            .post()
            .uri("/api/authenticate")
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Collection<Authority>>(){});
  }
}
