package delivery.app.gateway.discovery;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DefaultDiscoveryService implements DiscoveryService {

  static final Map<String, String> serviceToBaseUrlMap = new HashMap<>();

  static {
    serviceToBaseUrlMap.put("users", "http://localhost:8081");
    serviceToBaseUrlMap.put("cart", "http://localhost:8082");
    serviceToBaseUrlMap.put("products", "http://localhost:8083");
    serviceToBaseUrlMap.put("orders", "http://localhost:8084");
    serviceToBaseUrlMap.put("payments", "http://localhost:8085");
  }

  @Override
  public Optional<URI> findByName(String serviceName) {
    return serviceToBaseUrlMap.keySet()
        .stream()
        .filter(serviceName::equalsIgnoreCase)
        .findFirst()
        .map(serviceToBaseUrlMap::get)
        .map(URI::create);
  }
}
