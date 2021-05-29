package delivery.app.gateway.security;

import java.util.stream.Collectors;

import delivery.app.user.AuthenticationService;
import reactor.core.publisher.Mono;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RemoteAuthenticationManager implements ReactiveAuthenticationManager {

  final AuthenticationService authenticationService;

  public RemoteAuthenticationManager(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication)
          throws AuthenticationException {
    if (authentication instanceof AnonymousAuthenticationToken) {
      return Mono.just(authentication);
    }

    return authenticationService
            .authenticate(authentication.getName(), authentication.getCredentials().toString())
            .map(authorities -> {
              final UsernamePasswordAuthenticationToken token =
                      new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                              authentication.getCredentials(),
                              authorities.stream()
                                         .map(authority -> new SimpleGrantedAuthority(
                                                 authority.getName()))
                                         .collect(Collectors.toSet()));

              token.eraseCredentials();

              if (authentication instanceof CredentialsContainer) {
                ((CredentialsContainer) authentication).eraseCredentials();
              }

              return token;
            });
  }
}
