package delivery.app.catalog.configuration;

import delivery.app.common.security.web.CommonRSocketSecurityConfigurerAdapter;

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
            authorize.route("api.products.*").permitAll()
                     .anyExchange().permitAll()
                     .anyRequest().authenticated());
  }
}