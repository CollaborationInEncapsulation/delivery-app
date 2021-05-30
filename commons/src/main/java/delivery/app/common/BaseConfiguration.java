package delivery.app.common;

import delivery.app.common.security.web.client.JWTMetadataRSocketConnectorConfigurer;
import io.rsocket.transport.netty.server.TcpServerTransport;
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
}
