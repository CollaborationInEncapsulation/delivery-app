package delivery.app.user;

import delivery.app.user.dto.User;
import java.util.Collection;
import java.util.List;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> find(String username);

  Mono<Collection<User>> findAll();
}
