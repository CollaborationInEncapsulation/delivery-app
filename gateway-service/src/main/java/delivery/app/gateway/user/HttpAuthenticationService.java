package delivery.app.gateway.user;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.dto.UsernameAndPassword;
import java.util.Collection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
class HttpAuthenticationService implements AuthenticationService {

  final WebClient webClient;

  HttpAuthenticationService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder
        .baseUrl("http://localhost:8081")
        .build();
  }

  @Override
  public Collection<Authority> authenticate(String username, CharSequence password) {
    final UsernameAndPassword payload = new UsernameAndPassword(username, password);

    final ResponseEntity<Collection<Authority>> responseEntity = webClient.post()
        .uri("/api/authenticate")
        .bodyValue(payload)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Collection<Authority>>() {
        })
        .block();

    return responseEntity.getBody();
  }
}
