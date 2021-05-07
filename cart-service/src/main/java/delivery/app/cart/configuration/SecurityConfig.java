package delivery.app.cart.configuration;

import delivery.app.common.security.web.CommonWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class SecurityConfig extends CommonWebSecurityConfigurerAdapter {

  @Override
  protected void doConfigure(HttpSecurity http) throws Exception {
    http.authorizeRequests((authorize) -> authorize
        .anyRequest().authenticated()
    );
  }
}