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

package delivery.app.common.security.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class SimpleJWTAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith("Bearer ")) {
      return null;
    }

    String authToken = header.substring(7);

    final DecodedJWT decodedJWT = JWT.decode(authToken);

    return new UsernamePasswordAuthenticationToken(
        decodedJWT.getSubject(),
        null,
        decodedJWT.getClaim("authorities").asList(String.class).stream()
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList())
    );
  }
}
