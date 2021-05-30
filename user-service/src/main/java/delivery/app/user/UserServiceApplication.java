package delivery.app.user;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import delivery.app.user.repository.UserRepository;
import delivery.app.user.repository.model.User;
import io.r2dbc.spi.ConnectionFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@SpringBootApplication
public class UserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }

  @Bean
  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("sql/schema.sql"), new ClassPathResource("sql/data.sql")));

    return initializer;
  }


  @Bean
  public CommandLineRunner demo(UserRepository userRepository) {
    return (args) -> {
      userRepository.saveAll(
              Arrays.asList(
                      new User(UUID.randomUUID().toString(), "user", "$2a$06$fJhpqOTtOsY6MpxpnwjPnO97TsrQsoc.C.DNtWJyu4yQHR/9PkiSK","user@example.com", "+123456789001", "hello world, land, hello, 012345", LocalDateTime.now(), "ROLE_USER"),
                      new User(UUID.randomUUID().toString(), "admin", "$2a$06$fJhpqOTtOsY6MpxpnwjPnO97TsrQsoc.C.DNtWJyu4yQHR/9PkiSK","admin@example.com", "+123456789001", "hello world, land, hello, 012345", LocalDateTime.now(), "ROLE_USER,ROLE_ADMIN"))
      );
    };
  }
}
