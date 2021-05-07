package delivery.app.cart.repository.model;

import lombok.Value;

@Value
public class Item {

  String productId;
  int quantity;
}
