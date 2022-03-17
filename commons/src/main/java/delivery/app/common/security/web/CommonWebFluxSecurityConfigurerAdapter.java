package delivery.app.common.security.web;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

public abstract class CommonWebFluxSecurityConfigurerAdapter {

  // @formatter:off
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ReactiveAuthenticationManager reactiveAuthenticationManager, ServerHttpSecurity http) {
    final AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager);
//    filter.setAuthenticationSuccessHandler((exchange, authentication) -> Mono.empty());
    filter.setServerAuthenticationConverter(new ReactiveJWTAuthenticationConverter());

    return doConfigure(
          http
            .csrf().disable()
            .addFilterAfter(filter, SecurityWebFiltersOrder.AUTHENTICATION)
    ).build();
  }
  // @formatter:on

  protected abstract ServerHttpSecurity doConfigure(ServerHttpSecurity http);

  @Bean
  public ReactiveAuthenticationManager noOpsAuthManager() {
    return Mono::just;
  }
}