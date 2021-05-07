package delivery.app.gateway.security;

import delivery.app.user.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // @formatter:off
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests((authorize) -> authorize
            .antMatchers("/login").permitAll()
            .antMatchers("/css/**", "/index").permitAll()
            .antMatchers(HttpMethod.GET, "/api/products", "/api/products/*").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin((formLogin) -> formLogin
            .loginPage("/login")
        );
  }
  // @formatter:on

  @Bean
  public RemoteAuthenticationManager remoteAuthenticationManager(
      AuthenticationService authenticationService) {
    return new RemoteAuthenticationManager(authenticationService);
  }
}