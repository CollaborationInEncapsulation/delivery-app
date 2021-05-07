package delivery.app.user.service;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.repository.UserRepository;
import delivery.app.user.repository.model.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

  final PasswordEncoder passwordEncoder;
  final UserRepository userRepository;

  public DefaultAuthenticationService(PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public Collection<Authority> authenticate(String username, CharSequence password) {
    final User user = userRepository.findByName(username);

    if (user != null && passwordEncoder.matches(password, user.getEncodedPassword())) {
      return Arrays.stream(user.getAuthorities().split(",")).map(Authority::new)
          .collect(Collectors.toSet());
    }

    throw new BadCredentialsException("Given username or password are incorrect");
  }
}
