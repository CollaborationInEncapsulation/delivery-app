package delivery.app.gateway.security;

import delivery.app.user.ReactiveAuthenticationServiceApi;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
            .authorizeExchange((exchanges) ->
                    exchanges
                            .pathMatchers("/login").permitAll()
                            .pathMatchers("/css/**", "/index").permitAll()
                            .pathMatchers(HttpMethod.GET, "/api/products", "/api/products/*").permitAll()
                            .anyExchange().authenticated()
            )
            .formLogin((formLogin) ->
                    formLogin
                            .loginPage("/login")
            );
    return http.build();
  }

  @Bean
  public RemoteAuthenticationManager remoteAuthenticationManager(
      ReactiveAuthenticationServiceApi authenticationService) {
    return new RemoteAuthenticationManager(authenticationService);
  }
}