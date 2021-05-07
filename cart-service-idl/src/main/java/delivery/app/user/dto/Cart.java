package delivery.app.user.dto;

import java.util.Set;
import lombok.Value;

@Value
public class Cart {
  Set<Item> items;
}
