package delivery.app.user.controller;

import delivery.app.user.dto.User;
import delivery.app.user.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  final UserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Flux<User> list(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    MDC.put("actor", authentication.getName());
    try {
      return userService.findAll();
    } finally {
      MDC.remove("actor");
    }
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public Mono<User> find(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    MDC.put("actor", authentication.getName());
    try {
      return userService.find(authentication.getName());
    } finally {
      MDC.remove("actor");
    }
  }

  @GetMapping("/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<User> find(@PathVariable("username") String username, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    MDC.put("actor", authentication.getName());
    try {
      return userService.find(username);
    } finally {
      MDC.remove("actor");
    }
  }
}
