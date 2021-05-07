package delivery.app.user.repository.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("USERS")
public class User {

  @Id
  String id;

  String name;
  String encodedPassword;
  String email;
  String phone;
  String address;

  @CreatedDate
  LocalDateTime createdAt;
  String        authorities;
}
