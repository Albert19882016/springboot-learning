package com.learning.search.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfiguration {

    public final static String WRITE_DATASOURCE_KEY = "writeDruidDataSource";

    public final static String READ_DATASOURCE_KEY = "readDruidDataSource";

    @Configuration
    public static class WriteDataSource {

        //DruidDataSource中url属性叫做jdbcUrl，所以需要相应修改
        @Value("${spring.datasource.druid.write.url}")
        private String jdbcUrl;

        @Value("${spring.datasource.druid.write.driver-class-name}")
        private String jdbcDriverClassName;

        @Value("${spring.datasource.druid.write.username}")
        private String jdbcUsername;

        @Value("${spring.datasource.druid.write.password}")
        private String jdbcPassword;

        @Bean(name = "writeDataSource")
        @SuppressWarnings("all")
        public DataSource getWriteDataSource(){
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
    }

    @Configuration
    public static class ReadDataSource {

        //DruidDataSource中url属性叫做jdbcUrl，所以需要相应修改
        @Value("${spring.datasource.druid.read.url}")
        private String jdbcUrl;

        @Value("${spring.datasource.druid.read.driver-class-name}")
        private String jdbcDriverClassName;

        @Value("${spring.datasource.druid.read.username}")
        private String jdbcUsername;

        @Value("${spring.datasource.druid.read.password}")
        private String jdbcPassword;

        @Bean(name = "readDataSource")
        @SuppressWarnings("all")
        public DataSource getReadDataSource(){
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

    }

    /**
     * 有多少个从库就要配置多少个
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
    @Bean(name = READ_DATASOURCE_KEY)
    @ConditionalOnBean(name = "readDataSource")
    public DataSource readDruidDataSource() {
        return new ReadDataSource().getReadDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    @Bean(name = WRITE_DATASOURCE_KEY)
    @Primary
    @ConditionalOnBean(name = "writeDataSource")
    public DataSource writeDruidDataSource() {
        return new WriteDataSource().getWriteDataSource();
    }

    /**
     * 注入AbstractRoutingDataSource
     * 设置数据源路由，通过该类中的determineCurrentLookupKey决定使用哪个数据源
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource(
            @Qualifier(READ_DATASOURCE_KEY) DataSource readDruidDataSource,
            @Qualifier(WRITE_DATASOURCE_KEY) DataSource writeDruidDataSource) {
        RoutingDataSource dataSource = new RoutingDataSource("1");
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(WRITE_DATASOURCE_KEY, writeDruidDataSource);
        targetDataSources.put(READ_DATASOURCE_KEY, readDruidDataSource);
        dataSource.setTargetDataSources(targetDataSources);// 配置数据源
        dataSource.setDefaultTargetDataSource(writeDruidDataSource);// 默认为主库用于写数据
        return dataSource;
    }


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        try {
            sqlSessionFactoryBean.setDataSource(dataSource);
            // 设置别名包（实体类）
            sqlSessionFactoryBean.setTypeAliasesPackage("com.learning.search");
            // 设置mybatis的主配置文件
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            //设置sql配置文件路径
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
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
