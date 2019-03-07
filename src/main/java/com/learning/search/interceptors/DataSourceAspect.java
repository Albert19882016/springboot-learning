package com.learning.search.interceptors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *  AOP的实例演示
 */
@Aspect
@Component
@Order(-1)// 保证顺序
public class DataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    /*
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
    */


    @Pointcut("execution(* com.learning.*.mapper..*.select*(..)) " +
            "|| execution(* com.learning.*.mapper..*.get*(..))" +
            "|| execution(* com.learning.*.mapper..*.query*(..))" +
            "|| execution(* com.learning.*.mapper..*.find*(..))" +
            "|| execution(* com.learning.*.mapper..*.search*(..))")
    public void slavePointCut(){}

    @Pointcut("execution(* com.learning.*.mapper..*.insert*(..)) " +
            "|| execution(* com.learning.*.mapper..*.update*(..))" +
            "|| execution(* com.learning.*.mapper..*.save*(..))")
    public void masterPointCut(){}


    @Around("slavePointCut()")
    public Object processedSlave(ProceedingJoinPoint point) throws Throwable{
        try{
            logger.info("dataSource 切换到：Read");
            Object result = point.proceed();
            return result;
        }finally {
        }
    }
    @Around("masterPointCut()")
    public Object processedMaster(ProceedingJoinPoint point) throws Throwable {
        try {
            logger.info("dataSource 切换到：Write");
            Object result = point.proceed();
            return result;
        } finally {
        }
    }
}
