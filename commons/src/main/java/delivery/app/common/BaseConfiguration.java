package delivery.app.common;

import delivery.app.common.security.web.client.JWTHeadersClientRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfiguration {

  @Bean
  public JWTHeadersClientRequestCustomizer clientInternalJWTRequestCustomizer() {
    return new JWTHeadersClientRequestCustomizer();
  }
}
