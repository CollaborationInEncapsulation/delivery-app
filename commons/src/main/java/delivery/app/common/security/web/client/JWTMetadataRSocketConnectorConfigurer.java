package delivery.app.common.security.web.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.plugins.RSocketInterceptor;
import io.rsocket.util.RSocketProxy;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import static delivery.app.common.security.web.client.JWTSecurityHeaders.addJWTBearerToken;

public class JWTMetadataRSocketConnectorConfigurer implements RSocketConnectorConfigurer {

  @Override
  public void configure(RSocketConnector connector) {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    connector.interceptors(ir -> ir.forRequester(new RSocketInterceptor() {
      @Override
      public RSocket apply(RSocket socket) {
        return new RSocketProxy(socket) {
          @Override
          public Mono<Void> fireAndForget(Payload payload) {
            return ReactiveSecurityContextHolder.getContext()
                                         .switchIfEmpty(Mono.fromRunnable(payload::release))
                                         .flatMap(sc -> super.fireAndForget(addJWTBearerToken(payload, authentication)));
          }

          @Override
          public Mono<Payload> requestResponse(Payload payload) {
            return ReactiveSecurityContextHolder.getContext()
                                                .switchIfEmpty(Mono.fromRunnable(payload::release))
                                                .flatMap(sc -> super.requestResponse(addJWTBearerToken(payload, authentication)));
          }

          @Override
          public Flux<Payload> requestStream(Payload payload) {
            return ReactiveSecurityContextHolder.getContext()
                                                .switchIfEmpty(Mono.fromRunnable(payload::release))
                                                .flatMapMany(sc -> super.requestStream(addJWTBearerToken(payload, authentication)));
          }

          @Override
          public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
            return Flux.from(payloads).switchOnFirst((firstSignal, wholeFlux) -> {
              Payload payload = firstSignal.get();

              if (payload != null) {
                return wholeFlux.skip(1).startWith(addJWTBearerToken(payload, authentication));
              }

              return wholeFlux;
            });
          }
        };
      }
    }));
  }
}
