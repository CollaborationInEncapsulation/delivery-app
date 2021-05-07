package delivery.app.common.security.web;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public abstract class CommonWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  // @formatter:off
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), new SimpleJWTAuthenticationConverter());
    filter.setSuccessHandler((request, response, authentication) -> { });

    http
        .csrf().disable()
        .addFilterAfter(filter, BasicAuthenticationFilter.class);

    doConfigure(http);
  }
  // @formatter:on

  protected abstract void doConfigure(HttpSecurity http) throws Exception;

  @Bean
  public AuthenticationManager noOpsAuthManager() {
    return authentication -> authentication;
  }
}