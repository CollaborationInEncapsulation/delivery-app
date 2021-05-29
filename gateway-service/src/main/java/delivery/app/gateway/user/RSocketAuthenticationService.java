package delivery.app.gateway.user;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.dto.UsernameAndPassword;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collection;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

@Service
public class RSocketAuthenticationService implements AuthenticationService {

  final RSocketRequester rsocketRequester;

  RSocketAuthenticationService(RSocketRequester.Builder rsocketRequesterBuilder) {
    this.rsocketRequester =
            rsocketRequesterBuilder.rsocketConnector(
                    c -> c.reconnect(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(100))
                                          .maxBackoff(Duration.ofSeconds(10))))
                                   .tcp("localhost", 8081);
  }

  @Override
  public Mono<Collection<Authority>> authenticate(String username, CharSequence password) {
    final UsernameAndPassword payload = new UsernameAndPassword(username, password);

    return rsocketRequester.route("api.authenticate")
                           .data(payload)
                           .retrieveMono(new ParameterizedTypeReference<Collection<Authority>>() {});
  }
}
