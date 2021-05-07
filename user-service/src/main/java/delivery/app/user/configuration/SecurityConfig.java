
package delivery.app.user.configuration;

import delivery.app.common.security.web.CommonWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends CommonWebSecurityConfigurerAdapter {

  @Override
  protected void doConfigure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests((authorize) -> authorize
            .antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
        );
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(6);
  }
}