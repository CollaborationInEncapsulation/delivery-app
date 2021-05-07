package delivery.app.gateway.user;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.dto.UsernameAndPassword;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class HttpAuthenticationService implements AuthenticationService {

  final RestTemplate restTemplate;

  HttpAuthenticationService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder
        .rootUri("http://localhost:8081")
        .build();
  }

  @Override
  public Collection<Authority> authenticate(String username, CharSequence password) {
    final UsernameAndPassword payload = new UsernameAndPassword(username, password);

    final Authority[] authorities = restTemplate
        .postForObject("/api/authenticate", payload, Authority[].class);
    return authorities != null ? Arrays.asList(authorities) : Collections.emptyList();
  }
}
