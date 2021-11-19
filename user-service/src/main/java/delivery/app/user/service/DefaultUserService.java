package delivery.app.user.service;

import delivery.app.user.dto.User;
import delivery.app.user.repository.UserRepository;
import delivery.app.user.repository.model.UserModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

  final UserRepository userRepository;

  @Override
  public User find(String username) {
    final UserModel userModel = userRepository.findByName(username);

    if (userModel == null) {
      return null;
    }

    return new User(userModel.getName(), userModel.getEmail(), userModel.getAddress(),
        userModel.getPhone());
  }

  @Override
  public List<User> findAll() {
    log.info("Searching for all users");
    return StreamSupport.stream(userRepository.findAll().spliterator(), false)
        .map(userModel -> new User(userModel.getName(), userModel.getEmail(), userModel.getAddress(), userModel.getPhone()))
        .collect(Collectors.toList());
  }
}
