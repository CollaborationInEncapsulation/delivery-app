package delivery.app.catalog.repository.model;

import java.math.BigDecimal;
import java.util.Currency;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PRODUCTS")
public class Product {

  @Id
  String id;
  String name;
  String description;
  String imgLink;
  BigDecimal price;
  Currency currency;

  boolean available;

}
