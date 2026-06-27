package com.JIMS.integration.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.JIMS.integration.repository", // ✅ this is the key
    entityManagerFactoryRef = "integrationEntityManagerFactory",
    transactionManagerRef = "integrationTransactionManager"
)
public class IntegrationJpaConfig {
	@Primary
    @Bean(name = "integrationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean integrationEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("jimsDataSource") DataSource dataSource) { // ✅ using the existing DataSource
        return builder
                .dataSource(dataSource)
                .packages("com.JIMS.integration.entity") // your @Entity classes
                .persistenceUnit("integrationPU")
                .build();
    }

    @Bean(name = "integrationTransactionManager")
    public PlatformTransactionManager integrationTransactionManager(
            @Qualifier("integrationEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
