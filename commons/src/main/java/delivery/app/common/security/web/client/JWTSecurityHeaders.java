package delivery.app.common.security.web.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.http.SecurityHeaders;

class JWTSecurityHeaders {

  static void addJWTBearerToken(HttpHeaders headers, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
      final String token = JWT.create()
          .withIssuer("Gateway Service")
          .withJWTId(UUID.randomUUID().toString())
          .withClaim("authorities", authentication.getAuthorities().stream().map(String::valueOf)
              .collect(Collectors.toList()))
          .withSubject(
              authentication.getPrincipal().toString())
          .sign(Algorithm.none());

      SecurityHeaders.bearerToken(token).accept(headers);
    }
  }
}
