package com.learning.search.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.learning.search", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JpaRepository.class))
public class MyBatisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisConfiguration.class);

    //DruidDataSource中url属性叫做jdbcUrl，所以需要相应修改
    @Value("${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriverClassName;

    @Value("${spring.datasource.username}")
    private String jdbcUsername;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Bean(name = "dataSource",destroyMethod = "")
    public DataSource dataSource() {
//        DruidDataSourceBuilder.create().build();
        DruidDataSource datasource = new DruidDataSource();
        // 数据库驱动
        datasource.setDriverClassName(jdbcDriverClassName);
        // 相应驱动的jdbcUrl
        datasource.setUrl(jdbcUrl);
        // 数据库的用户名
        datasource.setUsername(jdbcUsername);
        // 数据库的密码
        datasource.setPassword(jdbcPassword);
        // 每个分区最大的连接数
        datasource.setMaxActive(20);
        // 每个分区最小的连接数
        datasource.setMinIdle(5);
        return datasource;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        try {
            sqlSessionFactoryBean.setDataSource(dataSource);
            // 设置别名包（实体类）
            sqlSessionFactoryBean.setTypeAliasesPackage("com.learning.search");
            // 设置mybatis的主配置文件
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            //设置sql配置文件路径
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:**/*Mapper.xml"));
            //-- 加载mybatis的全局配置文件
            Resource mybatisConfigXml = resolver.getResource("classpath:mybatis/mybatis-config.xml");
            sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);

            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
