package delivery.app.common.security.web.client;

import reactor.core.publisher.Mono;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

public class JWTHeadersExchangeFilterFunction implements ExchangeFilterFunction {

	@Override
	public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
		return ReactiveSecurityContextHolder
			.getContext()
			.switchIfEmpty(Mono.fromCallable(SecurityContextHolder::getContext))
			.flatMap(securityContext -> next.exchange(
				ClientRequest.from(request)
					.headers((headers) -> JWTSecurityHeaders.addJWTBearerToken(headers, securityContext.getAuthentication()))
					.build()
			));
	}
}
