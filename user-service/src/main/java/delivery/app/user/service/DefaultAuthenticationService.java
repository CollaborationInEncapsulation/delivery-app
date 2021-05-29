package delivery.app.user.service;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

  final PasswordEncoder passwordEncoder;
  final UserRepository userRepository;

  @Override
  public Mono<Collection<Authority>> authenticate(String username, CharSequence password) {
    return userRepository.findByName(username)
                         .publishOn(Schedulers.boundedElastic())
                         .<Collection<Authority>>handle((user, sink) -> {
                           if (user != null && passwordEncoder.matches(password, user.getEncodedPassword())) {
                             sink.next(Arrays.stream(user.getAuthorities().split(",")).map(Authority::new)
                                          .collect(Collectors.toSet()));
                           }

                           sink.error(new BadCredentialsException("Given username or password are incorrect"));
                         })
                         .switchIfEmpty(Mono.error(() -> new BadCredentialsException("Given username or password are incorrect")));
  }
}
