package delivery.app.common.security.web.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.http.SecurityHeaders;

public class JWTHeadersClientRequestCustomizer implements
    RestTemplateRequestCustomizer<ClientHttpRequest> {

  @Override
  public void customize(ClientHttpRequest request) {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      final String token = JWT.create()
          .withIssuer("Gateway Service")
          .withJWTId(UUID.randomUUID().toString())
          .withClaim("authorities", authentication.getAuthorities().stream().map(String::valueOf)
              .collect(Collectors.toList()))
          .withSubject(
              authentication.getPrincipal().toString())
          .sign(Algorithm.none());

      SecurityHeaders.bearerToken(token).accept(request.getHeaders());
    }
  }
}
