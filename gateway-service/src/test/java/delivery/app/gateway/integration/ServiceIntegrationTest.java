package delivery.app.gateway.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;
import delivery.app.user.dto.User;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8081)
public class ServiceIntegrationTest {

  @LocalServerPort
  int port;

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @Test
  void loginTest(WireMockRuntimeInfo wmRuntimeInfo) {
    final RestTemplate testRestTemplate = restTemplateBuilder.rootUri("http://localhost:" + port)
        .build();
    final WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(WireMock.post("/api/authenticate")
        .willReturn(aResponse().withBody("[{\"name\" : \"ROLE_ADMIN\"}]")));

    final ResponseEntity<String> getForCsrfResponse = testRestTemplate.getForEntity("/login",
        String.class);

    Matcher csrfMatcher = Pattern.compile("name=\"_csrf\"\\n?.*value=\"(.*)\"", Pattern.MULTILINE)
        .matcher(getForCsrfResponse.getBody());

    assertThat(csrfMatcher.find()).isTrue();

    String csrf = csrfMatcher.group(1);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("_csrf", csrf);
    map.add("username", "user");
    map.add("password", "password");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

    ResponseEntity<Object> postForLoginResponse =
        testRestTemplate.exchange("/login",
            HttpMethod.POST,
            entity,
            Object.class);

    assertThat(postForLoginResponse.getStatusCode().isError()).isFalse();
    assertThat(postForLoginResponse.getHeaders().get(HttpHeaders.SET_COOKIE)).anyMatch(
        s -> s.contains("JSESSIONID"));

    wireMock.verifyThat(1,
        postRequestedFor(urlEqualTo("/api/authenticate")).withRequestBody(
            matchingJsonPath("$.username", equalTo("user")).and(
                matchingJsonPath("$.password", equalTo("password")))));
  }

  @Test
  void retrieveUserTest(WireMockRuntimeInfo wmRuntimeInfo) {
    final RestTemplate testRestTemplate = restTemplateBuilder.rootUri("http://localhost:" + port)
        .build();
    final WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(WireMock.post("/api/authenticate")
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("[{\"name\" : \"ROLE_ADMIN\"}]")));
    wireMock.register(WireMock.get("/api/users")
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(
                "[{\"name\" : \"Test\", \"email\" : \"test@example.com\", \"address\" : \"test\", \"phone\" : \"+12345678901\"}]")));

    final ResponseEntity<String> getForCsrfResponse = testRestTemplate.getForEntity("/login",
        String.class);

    Matcher csrfMatcher = Pattern.compile("name=\"_csrf\"\\n?.*value=\"(.*)\"", Pattern.MULTILINE)
        .matcher(getForCsrfResponse.getBody());

    assertThat(csrfMatcher.find()).isTrue();

    String csrf = csrfMatcher.group(1);

    HttpHeaders postForLoginHeaders = new HttpHeaders();
    postForLoginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("_csrf", csrf);
    map.add("username", "user");
    map.add("password", "password");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, postForLoginHeaders);

    ResponseEntity<Object> postForLoginResponse =
        testRestTemplate.exchange("/login",
            HttpMethod.POST,
            entity,
            Object.class);

    HttpHeaders getUsersHeaders = new HttpHeaders();
    getUsersHeaders.put(HttpHeaders.COOKIE,
        postForLoginResponse.getHeaders().get(HttpHeaders.SET_COOKIE));
    final ResponseEntity<User[]> result = testRestTemplate.exchange("/api/users", HttpMethod.GET,
        new HttpEntity<>(null, getUsersHeaders), User[].class);

    assertThat(result.getBody())
        .isNotEmpty()
        .containsExactly(new User("Test", "test@example.com", "test", "+12345678901"));

    for (ServeEvent event : wireMock.getServeEvents()) {
      if (urlEqualTo("/api/users").match(event.getRequest().getUrl()).isExactMatch()) {
        final String header = event.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        assertThat(header)
            .startsWith("Bearer");
        final DecodedJWT decode = JWT.decode(header.substring(7));

        assertThat(decode.getClaim("authorities").asList(String.class))
            .containsExactly("ROLE_ADMIN");
        assertThat(decode.getSubject()).isEqualTo("user");
        assertThat(decode.getIssuer()).isEqualTo("Gateway Service");
      }
    }
  }

  @Test
  void retrieveUsersCartTest(WireMockRuntimeInfo wmRuntimeInfo) {
    final RestTemplate testRestTemplate = restTemplateBuilder.rootUri("http://localhost:" + port)
        .build();
    final WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(WireMock.post("/api/authenticate")
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("[{\"name\" : \"ROLE_ADMIN\"}]")));
    wireMock.register(WireMock.get("/api/cart")
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(
                "{\"items\" : [{\"productId\" : \"1\", \"quantity\" : \"1\"}, {\"productId\" : \"2\", \"quantity\" : \"3\"}] }")));

    final ResponseEntity<String> getForCsrfResponse = testRestTemplate.getForEntity("/login",
        String.class);

    Matcher csrfMatcher = Pattern.compile("name=\"_csrf\"\\n?.*value=\"(.*)\"", Pattern.MULTILINE)
        .matcher(getForCsrfResponse.getBody());

    assertThat(csrfMatcher.find()).isTrue();

    String csrf = csrfMatcher.group(1);

    HttpHeaders postForLoginHeaders = new HttpHeaders();
    postForLoginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("_csrf", csrf);
    map.add("username", "user");
    map.add("password", "password");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, postForLoginHeaders);

    ResponseEntity<Object> postForLoginResponse =
        testRestTemplate.exchange("/login",
            HttpMethod.POST,
            entity,
            Object.class);

    HttpHeaders getUsersHeaders = new HttpHeaders();
    getUsersHeaders.put(HttpHeaders.COOKIE,
        postForLoginResponse.getHeaders().get(HttpHeaders.SET_COOKIE));
    final ResponseEntity<Cart> result = testRestTemplate.exchange("/api/cart", HttpMethod.GET,
        new HttpEntity<>(null, getUsersHeaders), Cart.class);

    assertThat(result.getBody()).isEqualTo(
        new Cart(new HashSet<>(Arrays.asList(new Item("1", 1), new Item("2", 3))))
    );
  }
}
