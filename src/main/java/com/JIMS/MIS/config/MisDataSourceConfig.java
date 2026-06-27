package com.JIMS.MIS.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.JIMS.MIS.Repository",
    entityManagerFactoryRef = "misEntityManagerFactory",
    transactionManagerRef = "misTransactionManager"
)
public class MisDataSourceConfig {
    @Value("${mis.username}")
    private String MIS_USERNAME;
    @Value("${datasource.mis}")
    private String MIS_URL;
	   @Value("${dabatbase.password}")
	    private String PASSWORD;
	   
	@Bean(name = "misJpaDataSource")
    public DataSource misJpaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(MIS_URL);
        dataSource.setUsername(MIS_USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

    @Bean(name = "misEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean misEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("misJpaDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.JIMS.MIS.model")
            .persistenceUnit("misPU")
            .build();
    }

    @Bean(name = "misTransactionManager")
    public PlatformTransactionManager misTransactionManager(
            @Qualifier("misEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
