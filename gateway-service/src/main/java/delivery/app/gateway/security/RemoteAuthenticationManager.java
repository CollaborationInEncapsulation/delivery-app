package delivery.app.gateway.security;

import delivery.app.user.AuthenticationServiceApi;
import delivery.app.user.dto.Authority;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RemoteAuthenticationManager implements AuthenticationManager {

  final AuthenticationServiceApi authenticationService;

  public RemoteAuthenticationManager(AuthenticationServiceApi authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (authentication instanceof AnonymousAuthenticationToken) {
      return authentication;
    }

    try {
      final Collection<Authority> authorities = authenticationService
          .authenticate(authentication.getName(), authentication.getCredentials().toString());

      final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
          authentication.getPrincipal(),
          authentication.getCredentials(),
          authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName()))
              .collect(Collectors.toSet())
      );

      token.eraseCredentials();

      if (authentication instanceof CredentialsContainer) {
        ((CredentialsContainer) authentication).eraseCredentials();
      }

      return token;
    } catch (Exception t) {
      authentication.setAuthenticated(false);
      return authentication;
    }
  }
}
