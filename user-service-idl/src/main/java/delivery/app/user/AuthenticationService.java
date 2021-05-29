package delivery.app.user;

import delivery.app.user.dto.Authority;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface AuthenticationService {

  Mono<Collection<Authority>> authenticate(String username, CharSequence password);
}
