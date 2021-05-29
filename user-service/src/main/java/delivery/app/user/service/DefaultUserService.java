package delivery.app.user.service;

import delivery.app.user.UserService;
import delivery.app.user.dto.User;
import delivery.app.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

  final UserRepository userRepository;

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public User find(String username) {
    final delivery.app.user.repository.model.User userModel =
            userRepository.findByName(username)
                          .block();

    if (userModel == null) {
      return null;
    }

    return new User(userModel.getName(), userModel.getEmail(), userModel.getAddress(), userModel.getPhone());
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<User> findAll() {
    log.info("Searching for all users");
    return userRepository.findAll()
                         .map(model -> new User(model.getName(), model.getEmail(), model.getAddress(), model.getPhone()))
                         .collect(Collectors.toList())
                         .block();
  }
}
