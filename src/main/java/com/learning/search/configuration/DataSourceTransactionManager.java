package com.learning.search.configuration;

import com.learning.search.interceptors.DataSourceAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceTransactionManager  {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Autowired
    JpaProperties jpaProperties;

    @Autowired
    @Qualifier("writeDruidDataSource")
    private DataSource writeDruidDataSource;

    @Bean(name = "transactionManager")
    public org.springframework.jdbc.datasource.DataSourceTransactionManager transactionManagers() {
        logger.info("-------------------- transactionManager init ---------------------");
        return new org.springframework.jdbc.datasource.DataSourceTransactionManager(writeDruidDataSource);
    }
}
