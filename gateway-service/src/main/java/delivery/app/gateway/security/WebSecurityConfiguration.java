package delivery.app.gateway.security;

import delivery.app.user.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration{

  @Bean
  public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http, RemoteAuthenticationManager manager) {
    return http.authenticationManager(manager)
               .authorizeExchange()
               .pathMatchers("/login").permitAll()
               .pathMatchers("/css/**", "/index").permitAll()
               .pathMatchers(HttpMethod.GET, "/api/products", "/api/products/*").permitAll()
               .anyExchange().authenticated()
               .and()
               .formLogin(spec -> spec.loginPage("/login"))
               .build();
  }

  @Bean
  public RemoteAuthenticationManager remoteAuthenticationManager(AuthenticationService authenticationService) {
    return new RemoteAuthenticationManager(authenticationService);
  }
}