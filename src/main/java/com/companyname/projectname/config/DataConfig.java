package com.companyname.projectname.config;

import com.companyname.projectname.repository.AeroSpikeDBManager;
import com.google.common.collect.Lists;
import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.snapdeal.snapadpay.repository")
@PropertySource(value = {"classpath:database.properties","classpath:aerospike.properties"})
public class DataConfig {

    private static final String PACKAGES_TO_SCAN = "com.snapdeal.snapadpay.domain";

    @Autowired
    Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        if(env.getProperty("server.profile").equals("dev")) {
            vendorAdapter.setGenerateDdl(true);
        } else {
            vendorAdapter.setGenerateDdl(false);
        }
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(PACKAGES_TO_SCAN);
        factory.setDataSource(dataSource());
        return factory;
    }

    /**
     * Better performance than C3P0 and DBCP.
     * Fine tune as per requirements.
     * {@link "http://jolbox.com/"}
     *
     * @return
     */
    @Bean
    public BoneCPDataSource dataSource() {
        BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
        boneCPDataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
        boneCPDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        boneCPDataSource.setUsername(env.getProperty("jdbc.username"));
        boneCPDataSource.setPassword(env.getProperty("jdbc.password"));
        boneCPDataSource.setIdleConnectionTestPeriodInMinutes(60);
        boneCPDataSource.setIdleMaxAgeInMinutes(420);
        //config.setMinSize(env.getProperty("jdbc.minpoolsize", Integer.class, 1));
        //config.setMaxSize(env.getProperty("jdbc.maxpoolsize", Integer.class, 10));
        boneCPDataSource.setMaxConnectionsPerPartition(30);
        boneCPDataSource.setMinConnectionsPerPartition(10);
        boneCPDataSource.setPartitionCount(3);
        boneCPDataSource.setAcquireIncrement(5);
        boneCPDataSource.setStatementsCacheSize(100);
        return boneCPDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
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