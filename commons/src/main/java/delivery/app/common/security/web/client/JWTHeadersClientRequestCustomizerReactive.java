package delivery.app.common.security.web.client;

import static delivery.app.common.security.web.client.JWTSecurityHeaders.addJWTBearerToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class JWTHeadersClientRequestCustomizerReactive implements ExchangeFilterFunction {

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    return ReactiveSecurityContextHolder.getContext()
        .flatMap(sc -> next.exchange(ClientRequest.from(request)
            .headers(httpHeaders -> addJWTBearerToken(httpHeaders, sc.getAuthentication()))
            .build()))
        .switchIfEmpty(next.exchange(request));
  }
}
