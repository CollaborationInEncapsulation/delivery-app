package delivery.app.user;

import delivery.app.user.dto.Cart;
import delivery.app.user.dto.Item;

public interface CartServiceApi {

  void update(Item item);

  Cart get();
}
