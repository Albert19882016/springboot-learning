package com.learning.search.interceptors;

import com.learning.search.configuration.DataSourceConfiguration;
import com.learning.search.configuration.DataSourceHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1)// 保证该AOP在@Transactional之前执行
public class DataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Before("execution(* com.learning.*.mapper..*.select*(..)) " +
            "|| execution(* com.learning.*.mapper..*.get*(..))" +
            "|| execution(* com.learning.*.mapper..*.query*(..))" +
            "|| execution(* com.learning.*.mapper..*.find*(..))" +
            "|| execution(* com.learning.*.mapper..*.search*(..))")
    public void setReadDataSourceType() {
        DataSourceHolder.setDataSource(DataSourceConfiguration.READ_DATASOURCE_KEY);
        logger.info("dataSource 切换到：Read");
    }

    @Before("execution(* com.learning.*.mapper..*.insert*(..)) " +
            "|| execution(* com.learning.*.mapper..*.update*(..))" +
            "|| execution(* com.learning.*.mapper..*.save*(..))")
    public void setWriteDataSourceType() {
        DataSourceHolder.setDataSource(DataSourceConfiguration.WRITE_DATASOURCE_KEY);
        logger.info("dataSource 切换到：Write");
    }

}
