package delivery.app.common.security.web;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

public class ReactiveJWTAuthenticationConverter implements ServerAuthenticationConverter {
	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		return Mono.<Authentication>fromCallable(() -> {
			           final String header = exchange.getRequest()
			                                         .getHeaders()
			                                         .getFirst(HttpHeaders.AUTHORIZATION);

			           if (header == null || !header.startsWith("Bearer ")) {
				           return null;
			           }

			           String authToken = header.substring(7);

			           final DecodedJWT decodedJWT = JWT.decode(authToken);

			           return new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(),
					           null,
					           decodedJWT.getClaim("authorities")
					                     .asList(String.class)
					                     .stream()
					                     .map(SimpleGrantedAuthority::new)
					                     .collect(Collectors.toList()));
		           })
		           .subscribeOn(Schedulers.boundedElastic());
	}
}
