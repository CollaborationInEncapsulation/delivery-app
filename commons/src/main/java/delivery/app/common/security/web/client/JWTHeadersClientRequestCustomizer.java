package delivery.app.common.security.web.client;

import static delivery.app.common.security.web.client.JWTSecurityHeaders.addJWTBearerToken;

import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JWTHeadersClientRequestCustomizer implements
    RestTemplateRequestCustomizer<ClientHttpRequest> {

  @Override
  public void customize(ClientHttpRequest request) {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    addJWTBearerToken(request.getHeaders(), authentication);
  }
}
