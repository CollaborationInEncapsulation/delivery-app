package delivery.app.user.service;

import delivery.app.user.dto.Authority;
import java.util.Collection;

public interface AuthenticationService {

  Collection<Authority> authenticate(String username, CharSequence password);
}
