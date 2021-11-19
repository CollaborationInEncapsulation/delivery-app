package delivery.app.user;

import delivery.app.user.dto.Authority;
import java.util.Collection;

public interface AuthenticationServiceApi {

  Collection<Authority> authenticate(String username, CharSequence password);
}
