package delivery.app.cart.configuration;

import delivery.app.common.security.web.CommonWebFluxSecurityConfigurerAdapter;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;

@EnableWebFluxSecurity
public class SecurityConfig extends CommonWebFluxSecurityConfigurerAdapter {

  @Override
  protected ServerHttpSecurity doConfigure(ServerHttpSecurity http) {
    return http.authorizeExchange((authorize) -> authorize.anyExchange().authenticated());
  }
}