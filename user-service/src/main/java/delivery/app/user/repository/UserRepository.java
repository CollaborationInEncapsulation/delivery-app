package delivery.app.user.repository;

import delivery.app.user.repository.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

  User findByName(String username);
}
