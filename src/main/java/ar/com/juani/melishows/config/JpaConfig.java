package ar.com.juani.melishows.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "ar.com.juani.melishows.dao.repository")
@EnableCaching
@EnableTransactionManagement
public class JpaConfig {

}
