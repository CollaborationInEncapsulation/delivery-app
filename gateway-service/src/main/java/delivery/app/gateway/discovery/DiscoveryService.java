package delivery.app.gateway.discovery;

import java.net.URI;
import java.util.Optional;

public interface DiscoveryService {

  Optional<URI> findByName(String serviceName);
}
