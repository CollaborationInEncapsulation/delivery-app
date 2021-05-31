package delivery.app.common.security.rsocket;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.rsocket.authentication.AuthenticationPayloadInterceptor;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

public abstract class CommonRSocketSecurityConfigurerAdapter {

  @Bean
  public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket, ReactiveAuthenticationManager manager) {
    AuthenticationPayloadInterceptor interceptor = new AuthenticationPayloadInterceptor(manager);
    interceptor.setAuthenticationConverter(new SimpleJWTAuthenticationConverter());

    rsocket.addPayloadInterceptor(interceptor);

    return rsocket.build();
  }

  protected abstract void doConfigure(RSocketSecurity rsocket) throws Exception;

  @Bean
  public ReactiveAuthenticationManager noOpsAuthManager() {
    return Mono::just;
  }
}