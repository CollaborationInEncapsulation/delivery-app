package delivery.app.user.repository;

import delivery.app.user.repository.model.User;
import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

  Mono<User> findByName(String username);
}
