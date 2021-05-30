package delivery.app.common.r2dbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class SqlInitializerConfiguration {

//  @Bean
//  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//    initializer.setConnectionFactory(connectionFactory);
//    CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
//    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("./sql" +
//            "/schema.sql")));
//    initializer.setDatabasePopulator(populator);
//    return initializer;
//  }
}
