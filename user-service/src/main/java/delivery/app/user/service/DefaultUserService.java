package delivery.app.user.service;

import delivery.app.user.dto.User;
import delivery.app.user.repository.UserRepository;
import delivery.app.user.repository.model.UserModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

  final UserRepository userRepository;

  @Override
  public Mono<User> find(String username) {
    return userRepository.findByName(username)
        .map(userModel -> new User(
                userModel.getName(),
                userModel.getEmail(),
                userModel.getAddress(),
                userModel.getPhone()
        ));
  }

  @Override
  public Flux<User> findAll() {
    log.info("Searching for all users");
    return userRepository.findAll()
        .map(userModel -> new User(
                userModel.getName(),
                userModel.getEmail(),
                userModel.getAddress(),
                userModel.getPhone()
        ));
  }
}
