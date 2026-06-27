//package com.suveechi.integration.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import jakarta.activation.DataSource;
//import jakarta.persistence.EntityManagerFactory;
//
//@Configuration
//@EnableTransactionManagement
//public class JpaConfig {
//
//    // EntityManagerFactory and TransactionManager for the MIS database
//    @Primary
//    @Bean(name = "misEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean misEntityManagerFactory(
//            @Qualifier("misDataSource") DataSource misDataSource,
//            EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource((javax.sql.DataSource) misDataSource)
//                .packages("com.suveechi.integration.mis.entities") // Change to your MIS entity package
//                .persistenceUnit("misPU")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "misTransactionManager")
//    public PlatformTransactionManager misTransactionManager(
//            @Qualifier("misEntityManagerFactory") EntityManagerFactory misEntityManagerFactory) {
//        return new JpaTransactionManager(misEntityManagerFactory);
//    }
//
//    // EntityManagerFactory and TransactionManager for AnotherDB
//    @Bean(name = "anotherEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean anotherEntityManagerFactory(
//            @Qualifier("anotherDataSource") DataSource anotherDataSource,
//            EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource((javax.sql.DataSource) anotherDataSource)
//                .packages("com.suveechi.integration.another.entities") // Change to your AnotherDB entity package
//                .persistenceUnit("anotherPU")
//                .build();
//    }
//
//    @Bean(name = "anotherTransactionManager")
//    public PlatformTransactionManager anotherTransactionManager(
//            @Qualifier("anotherEntityManagerFactory") EntityManagerFactory anotherEntityManagerFactory) {
//        return new JpaTransactionManager(anotherEntityManagerFactory);
//    }
//}
//


