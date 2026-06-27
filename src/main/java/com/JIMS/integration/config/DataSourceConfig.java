package com.JIMS.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    private static final String PASSWORD = "G2dev@05";
	   @Value("${dabatbase.password}")
	    private String PASSWORD;
    private static final String DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

//    private static final String MIS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JMIS;encrypt=false";
//    private static final String JIMS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JIMS;encrypt=false";
//    private static final String MIS2_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=MIS;encrypt=false";
//    private static final String JDMS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JDMS;encrypt=false";
    
//    private static final String MIS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=MIS;encrypt=false";
//    private static final String JIMS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JIMS;encrypt=false";
//    private static final String MIS2_URL = "jdbc:sqlserver://10.10.92.56:60502;databaseName=MIS;encrypt=false";
//    private static final String JDMS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JDMS;encrypt=false";
//    private static final String WEBSMART_URL = "jdbc:sqlserver://10.10.92.54:60502;databaseName=WebSmart;encrypt=false";
    @Value("${datasource.mis}")
    private String MIS_URL;
    @Value("${datasource.jims}")
    private String JIMS_URL;
    @Value("${datasource.mis2}")
    private String MIS2_URL;
    
    
//    private static final String MIS2_URL = "jdbc:sqlserver://10.10.92.145:1433;databaseName=MIS;encrypt=false";
    @Value("${datasource.jdms}")
    private String JDMS_URL;
    @Value("${datasource.websmart}")
    private String WEBSMART_URL;
//    private static final String MIS_USERNAME = "sa";
//    private static final String JIMS_USERNAME = "sa";
//    private static final String MIS2_USERNAME = "sa";
//    private static final String JDMS_USERNAME = "sa";
    @Value("${mis.username}")
    private String MIS_USERNAME;

    @Value("${jims.username}")
    private String JIMS_USERNAME;

    @Value("${mis2.username}")
    private String MIS2_USERNAME;

    @Value("${jdms.username}")
    private String JDMS_USERNAME;
    @Value("${websmart.username}")
    private String WEBSMART_USERNAME;
    @Value("${dabatbase.password}")
    private String WEBSMART_password;
//    private static final String WEBSMART_USERNAME = "sa";

    @Bean(name="webDataSource")
    public DataSource webDataSource() {
    	return createDataSourcess(WEBSMART_URL,WEBSMART_USERNAME,WEBSMART_password);
    }
    
    @Bean(name = "misDataSource")
    public DataSource misDataSource() {
        return createDataSource(MIS_URL, MIS_USERNAME);
    }

    @Bean(name = "misDataSource2")
    public DataSource misDataSource2() {
        return createDataSourcess(MIS2_URL, MIS2_USERNAME,MIS2_USERNAME);
    }

    @Bean(name = "jimsDataSource")
    @Primary
    public DataSource jimsDataSource() {
        return createDataSource(JIMS_URL, JIMS_USERNAME);
    }

    @Bean(name = "jdmsDataSource")
    public DataSource jdmsDataSource() {
        return createDataSource(JDMS_URL, JDMS_USERNAME);
    }
    
    // --------------------------------------------------------
    // ⭐ ADD THESE TWO NEW DATASOURCES
    // --------------------------------------------------------

//    @Bean(name = "bellaryDataSource")
//    public DataSource bellaryDataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        ds.setUrl("jdbc:sqlserver://10.10.94.4:1433;instanceName=g2dev;databaseName=MIS;encrypt=false;trustServerCertificate=true");
//        ds.setUsername("sa");
//        ds.setPassword("G2dev@05");
//        return ds;
//    }
    @Value("${datasourcebellary}")
    private String datasourcebellary;
    
    @Value("${bellary.username}")
    private String bellaryusername;
    
    @Value("${bellary.password}")
    private String bellarypassword;
    
    @Value("${datasourcegujrat}")
    private String datasourcegujrat;
    
    @Value("${gujrat.username}")
    private String gujratusername;
    
    @Value("${gujrat.password}")
    private String gujaratpassword;
    @Bean(name = "bellaryDataSource")
    public DataSource bellaryDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUrl(datasourcebellary);
        ds.setUsername(bellaryusername);
        ds.setPassword(bellarypassword);
        return ds;
    }
    
//    @Bean(name = "gujaratDataSource")
//    public DataSource gujaratDataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        ds.setUrl("jdbc:sqlserver://10.10.92.145;instanceName=S2016;databaseName=MIS;encrypt=false;trustServerCertificate=true");
//        ds.setUsername("sa");
//        ds.setPassword("sa");
//        return ds;
//    }
    
    @Bean(name = "gujaratDataSource")
    public DataSource gujaratDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUrl(datasourcegujrat);
        ds.setUsername(gujratusername);
        ds.setPassword(gujaratpassword);
        return ds;
    }

    // Utility method
    private DataSource createDataSource(String url, String username) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
    
    
    private DataSource createDataSourcess(String url, String username,String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}


