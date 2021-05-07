package delivery.app.user.controller;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.dto.UsernameAndPassword;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationController {

  final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping
  public ResponseEntity<?> authenticate(@RequestBody UsernameAndPassword userAndPassword) {
    try {
      final Collection<Authority> authorities = authenticationService
          .authenticate(userAndPassword.getUsername(), userAndPassword.getPassword());

      return ResponseEntity.ok(authorities);
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }
}
