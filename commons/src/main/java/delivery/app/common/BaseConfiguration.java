package delivery.app.common;

import delivery.app.common.security.rsocket.requester.JWTMetadataRSocketConnectorConfigurer;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.boot.autoconfigure.rsocket.RSocketMessageHandlerCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import reactor.netty.http.server.HttpServer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

@Configuration
public class BaseConfiguration {

  @Bean
  public JWTMetadataRSocketConnectorConfigurer clientInternalJWTRequestCustomizer() {
    return new JWTMetadataRSocketConnectorConfigurer();
  }

  @Bean
  @Scope("prototype")
  @ConditionalOnClass({RSocketRequester.class, io.rsocket.RSocket.class, HttpServer.class,
      TcpServerTransport.class})
  public RSocketRequester.Builder rSocketRequesterBuilder(RSocketStrategies strategies,
      ObjectProvider<RSocketConnectorConfigurer> rSocketConnectorConfigurers) {
    RSocketRequester.Builder builder = RSocketRequester.builder()
        .rsocketStrategies(strategies);

    rSocketConnectorConfigurers.forEach(builder::rsocketConnector);

    return builder;
  }

  @Bean
  public RSocketMessageHandlerCustomizer rSocketMessageHandlerCustomizer() {
    return messageHandler -> messageHandler.getArgumentResolverConfigurer()
        .addCustomResolver(new AuthenticationPrincipalArgumentResolver());
  }
}
