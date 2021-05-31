package delivery.app.common.security.rsocket.requester;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.plugins.RSocketInterceptor;
import io.rsocket.util.RSocketProxy;
import org.reactivestreams.Publisher;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import static delivery.app.common.security.rsocket.requester.JWTSecurityHeaders.addJWTBearerToken;

public class JWTMetadataRSocketConnectorConfigurer implements RSocketConnectorConfigurer {

  @Override
  public void configure(RSocketConnector connector) {
    connector
        .interceptors(ir -> ir.forRequester(new SecurityMetadataPopulatingRSocketInterceptor()));
  }

  static class SecurityMetadataPopulatingRSocketInterceptor implements RSocketInterceptor {

    @Override
    public RSocket apply(RSocket socket) {
      return new SecurityMetadataPopulatingRSocketProxy(socket);
    }
  }

  static class SecurityMetadataPopulatingRSocketProxy extends RSocketProxy {

    public SecurityMetadataPopulatingRSocketProxy(RSocket socket) {
      super(socket);
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
      return ReactiveSecurityContextHolder.getContext()
          .defaultIfEmpty(SecurityContextHolder.createEmptyContext())
          .flatMap(
              sc -> super.fireAndForget(addJWTBearerToken(payload, sc.getAuthentication())));
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
      return ReactiveSecurityContextHolder.getContext()
          .defaultIfEmpty(SecurityContextHolder.createEmptyContext())
          .flatMap(sc -> super
              .requestResponse(addJWTBearerToken(payload, sc.getAuthentication())));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
      return ReactiveSecurityContextHolder.getContext()
          .defaultIfEmpty(SecurityContextHolder.createEmptyContext())
          .flatMapMany(
              sc -> super.requestStream(addJWTBearerToken(payload, sc.getAuthentication())));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
      return ReactiveSecurityContextHolder.getContext()
          .materialize()
          .flatMapMany(signal -> {
            final SecurityContext sc = signal.get();
            if (sc != null && sc.getAuthentication() != null && sc.getAuthentication()
                .isAuthenticated()) {
              return Flux.from(payloads).switchOnFirst((firstSignal, wholeFlux) -> {
                Payload payload = firstSignal.get();

                if (payload != null) {
                  return super.requestChannel(wholeFlux.skip(1)
                      .startWith(addJWTBearerToken(payload, sc.getAuthentication())));
                }

                return wholeFlux;
              });
            }

            return super.requestChannel(payloads);
          });
    }
  }
}
