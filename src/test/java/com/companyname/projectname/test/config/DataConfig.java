package com.companyname.projectname.test.config;

import com.google.common.collect.Lists;
import com.companyname.projectname.repository.AeroSpikeDBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.companyname.projectname.repository")
@PropertySource(value = {"classpath:database.properties","classpath:aerospike.properties"})
public class DataConfig {

    private static final String PACKAGES_TO_SCAN = "com.companyname.projectname.domain";
    @Autowired
    Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(PACKAGES_TO_SCAN);
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        return factory;
    }

    @Bean
    public DriverManagerDataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return properties;
    }

    @Bean
    public AeroSpikeDBManager aeroSpikeDBManager() {
        String hostUrls = env.getProperty("aerospike.db.host", String.class);
        String[] split = hostUrls.split(",");
        List<String> list = Lists.newArrayList(split);
        AeroSpikeDBManager dbm = new AeroSpikeDBManager(list);
        return dbm;
    }
}