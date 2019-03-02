package com.learning.search.configuration;

import com.learning.search.controller.GenericErrorController;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * 错误处理相关配置类，用来模仿ErrorMvcAutoConfiguration的
 * 处理来注册GenericErrorController到Spring中
 */
@Configuration
public class ErrorConfiguration {

    @Bean
    public GenericErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                                       ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        return new GenericErrorController(errorAttributes, serverProperties.getError(),
                errorViewResolversProvider.getIfAvailable());
    }

}

