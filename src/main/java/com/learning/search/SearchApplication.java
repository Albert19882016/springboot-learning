package com.learning.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan(basePackages = "com.learning.search.mapper") //需要扫描的Mapper类的包
@SpringBootApplication
@ImportResource(locations = {"classpath*:/META-INF/spring/spring-job.xml"}) //当当定时任务库配置文件
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

   /*
   @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents("com.learning.search.service");
        beanDefinitionSet.addAll(scanner.findCandidateComponents("com.learning.search.xxx"));
        beanDefinitionSet.addAll(scanner.findCandidateComponents("com.learning.search.xxx"));
        Set<Class<?>> clazzSet = new HashSet<Class<?>>();
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            clazzSet.add(ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), resourceConfig.getClassLoader()));
        }
        resourceConfig.registerClasses(clazzSet);
        resourceConfig.property(ServletProperties.FILTER_FORWARD_ON_404, true);
        return resourceConfig;
    }
    */

}

