/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package delivery.app.common.security.rsocket;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.netty.buffer.ByteBuf;
import io.rsocket.metadata.CompositeMetadata;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;

public class SimpleJWTAuthenticationConverter implements PayloadExchangeAuthenticationConverter {

  private static final String BEARER_MIME_TYPE_VALUE =
          BearerTokenMetadata.BEARER_AUTHENTICATION_MIME_TYPE.toString();

  @Override
  public Mono<Authentication> convert(PayloadExchange exchange) {
    ByteBuf metadata = exchange.getPayload().metadata();
    CompositeMetadata compositeMetadata = new CompositeMetadata(metadata, false);

    for (CompositeMetadata.Entry entry : compositeMetadata) {
      if (BEARER_MIME_TYPE_VALUE.equals(entry.getMimeType())) {
        ByteBuf content = entry.getContent();
        String token = content.toString(StandardCharsets.UTF_8);
        final DecodedJWT decodedJWT = JWT.decode(token);

        return Mono.just(new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(),
                null,
                decodedJWT.getClaim("authorities")
                          .asList(String.class)
                          .stream()
                          .map(SimpleGrantedAuthority::new)
                          .collect(Collectors.toList())));
      }
    }
    return Mono.empty();
  }
}
