
package delivery.app.user.configuration;

import delivery.app.common.security.web.CommonWebFluxSecurityConfigurerAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebFluxSecurity
public class SecurityConfig extends CommonWebFluxSecurityConfigurerAdapter {

  @Override
  protected ServerHttpSecurity doConfigure(ServerHttpSecurity http) {
    return http
        .authorizeExchange((authorize) -> authorize
            .pathMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
        );
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(6);
  }
}