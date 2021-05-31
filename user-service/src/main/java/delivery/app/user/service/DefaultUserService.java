package delivery.app.user.service;

import delivery.app.user.UserService;
import delivery.app.user.dto.User;
import delivery.app.user.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

  final UserRepository userRepository;

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<User> find(String username) {
    return userRepository.findByName(username)
        .map(
            userModel -> new User(userModel.getName(), userModel.getEmail(), userModel.getAddress(),
                userModel.getPhone()));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<Collection<User>> findAll() {
    log.info("Searching for all users");
    return userRepository.findAll()
        .map(model -> new User(model.getName(), model.getEmail(), model.getAddress(),
            model.getPhone()))
        .collect(Collectors.toList());
  }
}
