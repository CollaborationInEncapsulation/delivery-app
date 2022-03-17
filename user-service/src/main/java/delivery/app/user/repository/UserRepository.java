package delivery.app.user.repository;

import delivery.app.user.repository.model.UserModel;
import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<UserModel, String> {

  Mono<UserModel> findByName(String username);
}
