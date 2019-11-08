package za.co.digitalplatform.invoiceservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "za.co.digitalplatform.invoiceservice.repositories")
@EnableTransactionManagement
public class JpaConfig {
}
