package delivery.app.user.controller;

import java.util.function.Function;

import delivery.app.user.dto.UsernameAndPassword;
import delivery.app.user.service.AuthenticationService;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public Mono<ResponseEntity<?>> authenticate(@RequestBody UsernameAndPassword userAndPassword) {
    return authenticationService
          .authenticate(userAndPassword.getUsername(), userAndPassword.getPassword())
          .map(ResponseEntity::<Object>ok)
          .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage())))
          .map(Function.identity());
  }
}
