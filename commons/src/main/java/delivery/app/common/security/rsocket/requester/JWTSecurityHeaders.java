package delivery.app.common.security.rsocket.requester;

import java.util.UUID;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.rsocket.Payload;
import io.rsocket.metadata.AuthMetadataCodec;
import io.rsocket.metadata.CompositeMetadataCodec;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.util.ByteBufPayload;

import org.springframework.security.core.Authentication;

class JWTSecurityHeaders {

  static Payload addJWTBearerToken(Payload payload, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
      try {
        final String token = JWT.create()
                                .withIssuer("Gateway Service")
                                .withJWTId(UUID.randomUUID()
                                               .toString())
                                .withClaim("authorities",
                                        authentication.getAuthorities()
                                                      .stream()
                                                      .map(String::valueOf)
                                                      .collect(Collectors.toList()))
                                .withSubject(authentication.getPrincipal()
                                                           .toString())
                                .sign(Algorithm.none());

        ByteBuf metadata = payload.sliceMetadata()
                                  .retain();
        ByteBufAllocator byteBufAllocator = metadata.alloc();

        CompositeByteBuf bufs = byteBufAllocator.compositeBuffer();
        bufs.addComponent(true, metadata);

        ByteBuf authMetadataPayload = AuthMetadataCodec.encodeBearerMetadata(
                byteBufAllocator,
                token.toCharArray());

        CompositeMetadataCodec.encodeAndAddMetadata(bufs,
                byteBufAllocator,
                WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION,
                authMetadataPayload);

        return ByteBufPayload.create(payload.sliceData()
                                            .retain(), bufs);
      }
      finally {
        payload.release();
      }
    }

    return payload;
  }
}
