package delivery.app.cart.configuration;

import delivery.app.common.security.rsocket.CommonRSocketSecurityConfigurerAdapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration extends CommonRSocketSecurityConfigurerAdapter {

  @Override
  protected void doConfigure(RSocketSecurity rsocket) throws Exception {
    rsocket.authorizePayload(authorize ->
            authorize.anyExchange().authenticated());
  }
}