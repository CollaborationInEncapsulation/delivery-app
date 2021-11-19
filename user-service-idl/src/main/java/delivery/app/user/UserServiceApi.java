package delivery.app.user;

import delivery.app.user.dto.User;
import java.util.List;

public interface UserServiceApi {

  User find(String username);

  List<User> findAll();
}
