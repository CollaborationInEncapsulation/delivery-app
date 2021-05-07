package delivery.app.user.dto;

import lombok.Value;

@Value
public class UsernameAndPassword {

  String username;
  CharSequence password;
}
