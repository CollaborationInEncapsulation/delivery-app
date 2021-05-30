package delivery.app.user.controller;

import delivery.app.user.AuthenticationService;
import delivery.app.user.dto.Authority;
import delivery.app.user.dto.UsernameAndPassword;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collection;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("api")
@AllArgsConstructor
public class AuthenticationController {

  final AuthenticationService authenticationService;

  @MessageMapping("authenticate")
  public Mono<Collection<Authority>> authenticate(@Payload UsernameAndPassword userAndPassword) {
    return authenticationService.authenticate(userAndPassword.getUsername(), userAndPassword.getPassword());
  }
}
