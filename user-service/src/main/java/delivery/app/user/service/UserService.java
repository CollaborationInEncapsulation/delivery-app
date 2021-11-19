package delivery.app.user.service;

import delivery.app.user.dto.User;
import java.util.List;

public interface UserService {

  User find(String username);

  List<User> findAll();
}
