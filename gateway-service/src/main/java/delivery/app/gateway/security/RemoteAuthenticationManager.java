package delivery.app.gateway.security;

import delivery.app.user.AuthenticationServiceApi;
import delivery.app.user.ReactiveAuthenticationServiceApi;
import delivery.app.user.dto.Authority;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RemoteAuthenticationManager implements ReactiveAuthenticationManager {

  final ReactiveAuthenticationServiceApi authenticationService;

  public RemoteAuthenticationManager(ReactiveAuthenticationServiceApi authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
    if (authentication instanceof AnonymousAuthenticationToken) {
      return Mono.just(authentication);
    }

    return authenticationService.authenticate(authentication.getName(), authentication.getCredentials().toString())
               .map(authorities -> new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                       authentication.getCredentials(),
                       authorities.stream()
                                  .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                                  .collect(Collectors.toSet())))
               .doOnNext(token -> {
                 token.eraseCredentials();
                 if (authentication instanceof CredentialsContainer) {
                   ((CredentialsContainer) authentication).eraseCredentials();
                 }
               })
               .cast(Authentication.class);
  }
}
