package delivery.app.catalog.dto;

import java.math.BigDecimal;
import java.util.Currency;
import lombok.Value;

@Value
public class Product {

  String id;
  String name;
  String description;
  String imgLink;
  BigDecimal price;
  Currency currency;

  boolean available;
}
