package delivery.app.user.dto;

import lombok.Value;

@Value
public class User {

  String name;
  String email;
  String address;
  String phone;
}
