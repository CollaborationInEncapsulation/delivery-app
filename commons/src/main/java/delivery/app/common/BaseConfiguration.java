package delivery.app.common;

import delivery.app.common.security.web.client.JWTHeadersClientRequestCustomizer;
import delivery.app.common.security.web.client.JWTHeadersExchangeFilterFunction;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfiguration {

  @Bean
  public JWTHeadersClientRequestCustomizer clientInternalJWTRequestCustomizer() {
    return new JWTHeadersClientRequestCustomizer();
  }

  @Bean
  public WebClientCustomizer jwtWebClientCustomizer() {
    return (builder) -> builder.filter(new JWTHeadersExchangeFilterFunction());
  }
}
