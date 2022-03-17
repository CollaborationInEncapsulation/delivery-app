package delivery.app.user;

import java.util.Collection;

import delivery.app.user.dto.Authority;
import reactor.core.publisher.Mono;

public interface ReactiveAuthenticationServiceApi {

  Mono<Collection<Authority>> authenticate(String username, CharSequence password);
}
