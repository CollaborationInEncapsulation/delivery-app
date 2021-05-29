package delivery.app.common.security.web;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.AuthorizePayloadsSpec;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.rsocket.api.PayloadInterceptor;
import org.springframework.security.rsocket.authentication.AuthenticationPayloadInterceptor;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public abstract class CommonRSocketSecurityConfigurerAdapter {

  // @formatter:off
  @Bean
  public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket, ReactiveAuthenticationManager reactiveAuthenticationManager) {
    AuthenticationPayloadInterceptor interceptor =
            new AuthenticationPayloadInterceptor(reactiveAuthenticationManager);

    interceptor.setAuthenticationConverter(new SimpleJWTAuthenticationConverter());

    rsocket.addPayloadInterceptor(interceptor);

    return rsocket.build();
  }
  // @formatter:on

  protected abstract void doConfigure(RSocketSecurity rsocket) throws Exception;

  @Bean
  public ReactiveAuthenticationManager noOpsAuthManager() {
    return Mono::just;
  }
}