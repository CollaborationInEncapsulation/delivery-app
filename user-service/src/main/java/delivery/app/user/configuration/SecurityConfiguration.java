package delivery.app.user.configuration;

import delivery.app.common.security.rsocket.CommonRSocketSecurityConfigurerAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration extends CommonRSocketSecurityConfigurerAdapter {

  @Override
  protected void doConfigure(RSocketSecurity rsocket) throws Exception {
    rsocket.authorizePayload(authorize ->
            authorize.route("api.authenticate.*").permitAll()
                     .anyExchange().permitAll()
                     .anyRequest().authenticated());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(6);
  }
}