package delivery.app.user.controller;

import delivery.app.user.UserService;
import delivery.app.user.dto.User;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("api.users")
@AllArgsConstructor
public class UserController {

  final UserService userService;

  @MessageMapping("")
  public Mono<Collection<User>> list() {
    MDC.put("actor", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      return userService.findAll();
    } finally {
      MDC.remove("actor");
    }
  }

  @MessageMapping("/{username}")
  public Mono<User> find(@PathVariable("username") String username) {
    MDC.put("actor", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      return userService.find(username);
    } finally {
      MDC.remove("actor");
    }
  }
}
