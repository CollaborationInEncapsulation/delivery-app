package delivery.app.cart.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8083)
public class ServiceIntegrationTest {

  @LocalServerPort
  int port;

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @Test
  void retrieveAddToCartTest(WireMockRuntimeInfo wmRuntimeInfo) {
    final RestTemplate testRestTemplate = restTemplateBuilder.rootUri("http://localhost:" + port)
        .build();
    final WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(WireMock.head(urlEqualTo("/api/products/1"))
        .willReturn(aResponse().withStatus(200)));

    HttpHeaders headers = new HttpHeaders();
    SecurityHeaders.bearerToken(
        JWT.create()
            .withIssuer("Cart Service")
            .withJWTId(UUID.randomUUID().toString())
            .withClaim("authorities", Collections.singletonList("ROLE_USER"))
            .withSubject("user")
            .sign(Algorithm.none())
    ).accept(headers);

    final ResponseEntity<?> result = testRestTemplate.exchange("/api/cart", HttpMethod.PATCH,
        new HttpEntity<>(new Item("1", 1), headers), Object.class);

    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();

    final ResponseEntity<Cart> cartResult = testRestTemplate.exchange("/api/cart", HttpMethod.GET,
        new HttpEntity<>(null, headers), Cart.class);

    assertThat(cartResult.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(cartResult.getBody()).isNotNull();
    assertThat(cartResult.getBody().getItems()).containsExactly(new Item("1", 1));

    final ResponseEntity<?> result2 = testRestTemplate.exchange("/api/cart", HttpMethod.PATCH,
        new HttpEntity<>(new Item("1", 2), headers), Object.class);

    assertThat(result2.getStatusCode().is2xxSuccessful()).isTrue();

    final ResponseEntity<Cart> cartResult2 = testRestTemplate.exchange("/api/cart", HttpMethod.GET,
        new HttpEntity<>(null, headers), Cart.class);

    assertThat(cartResult2.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(cartResult2.getBody()).isNotNull();
    assertThat(cartResult2.getBody().getItems()).containsExactly(new Item("1", 3));

    final ResponseEntity<?> result3 = testRestTemplate.exchange("/api/cart", HttpMethod.PATCH,
        new HttpEntity<>(new Item("1", -1), headers), Object.class);

    assertThat(result3.getStatusCode().is2xxSuccessful()).isTrue();

    final ResponseEntity<Cart> cartResult3 = testRestTemplate.exchange("/api/cart", HttpMethod.GET,
        new HttpEntity<>(null, headers), Cart.class);

    assertThat(cartResult3.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(cartResult3.getBody()).isNotNull();
    assertThat(cartResult3.getBody().getItems()).containsExactly(new Item("1", 2));

    assertThat(wireMock.getServeEvents()).hasSize(1);
    for (ServeEvent event : wireMock.getServeEvents()) {
      if (urlEqualTo("/api/products/1").match(event.getRequest().getUrl()).isExactMatch()) {
        final String header = event.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        assertThat(header)
            .startsWith("Bearer");
        final DecodedJWT decode = JWT.decode(header.substring(7));

        assertThat(decode.getClaim("authorities").asList(String.class))
            .containsExactly("ROLE_USER");
        assertThat(decode.getSubject()).isEqualTo("user");
        assertThat(decode.getIssuer()).isEqualTo("Gateway Service");
      }
    }
  }
}
