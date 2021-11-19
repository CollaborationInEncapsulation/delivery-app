package delivery.app.user.repository;

import delivery.app.user.repository.model.UserModel;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserModel, String> {

  UserModel findByName(String username);
}
