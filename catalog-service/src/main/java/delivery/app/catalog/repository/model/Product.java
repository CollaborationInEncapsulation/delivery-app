package delivery.app.catalog.repository.model;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("products")
public class Product {

  @Id
  String id;

  String     name;
  String     description;
  String     imgLink;
  BigDecimal price;
  Currency   currency;
  boolean    available;
}
