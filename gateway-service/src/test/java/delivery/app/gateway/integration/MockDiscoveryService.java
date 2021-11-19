package delivery.app.gateway.integration;

import delivery.app.gateway.discovery.DiscoveryService;
import java.net.URI;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MockDiscoveryService implements DiscoveryService {

  @Override
  public Optional<URI> findByName(String serviceName) {
    return Optional.of(URI.create("http://localhost:8081"));
  }
}
