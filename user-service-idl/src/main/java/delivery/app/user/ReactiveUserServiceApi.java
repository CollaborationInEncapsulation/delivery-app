package delivery.app.user;

import delivery.app.user.dto.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserServiceApi {

  Mono<User> find(String username);

  Flux<User> findAll();
}
