package com.learning.search.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.time.Duration;

/**
 * 多Redis集群配置。
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfiguration {

    @Bean
    @ConditionalOnBean(name = "alphaRedisConfig")
    public LettuceConnectionFactory alphaLettuceConnectionFactory(RedisStandaloneConfiguration alphaRedisConfig,
                                                                  GenericObjectPoolConfig alphaPoolConfig) {
        LettuceClientConfiguration clientConfig =
                LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(100))
                        .poolConfig(alphaPoolConfig).build();
        return new LettuceConnectionFactory(alphaRedisConfig, clientConfig);
    }

    @Bean
    @ConditionalOnBean(name = "alphaLettuceConnectionFactory")
    public RedisTemplate<String, String> localRedisTemplate(LettuceConnectionFactory alphaLettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(alphaLettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Configuration
    @ConditionalOnProperty(name = "host", prefix = "spring.alpharedis")
    public static class AlphaRedisConfig {

        @Value("${spring.alpharedis.host}")
        private String host;

        @Value("${spring.alpharedis.port}")
        private Integer port;

        @Value("${spring.alpharedis.password}")
        private String password;

        @Value("${spring.alpharedis.database}")
        private Integer database;

        @Value("${spring.alpharedis.lettuce.pool.max-active}")
        private Integer maxActive;

        @Value("${spring.alpharedis.lettuce.pool.max-idle}")
        private Integer maxIdle;

        @Value("${spring.alpharedis.lettuce.pool.max-wait}")
        private Long maxWait;

        @Value("${spring.alpharedis.lettuce.pool.min-idle}")
        private Integer minIdle;

        @Bean
        @SuppressWarnings("all")
        public GenericObjectPoolConfig alphaPoolConfig() {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(maxActive);
            config.setMaxIdle(maxIdle);
            config.setMinIdle(minIdle);
            config.setMaxWaitMillis(maxWait);
            return config;
        }

        @Bean
        @SuppressWarnings("all")
        public RedisStandaloneConfiguration alphaRedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPassword(RedisPassword.of(password));
            config.setPort(port);
            config.setDatabase(database);
            return config;
        }
    }





/*    @Bean
    @ConditionalOnBean(name = "betaRedisConfig")
    public LettuceConnectionFactory betaLettuceConnectionFactory(RedisStandaloneConfiguration betaRedisConfig,
                                                                    GenericObjectPoolConfig betaPoolConfig) {
        LettuceClientConfiguration clientConfig =
                LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(100))
                        .poolConfig(betaPoolConfig).build();
        return new LettuceConnectionFactory(betaRedisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> defaultRedisTemplate(
            LettuceConnectionFactory betaLettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(betaLettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

   @Configuration
   @ConditionalOnProperty(name = "host", prefix = "spring.betaredis")
   public static class BetaRedisConfig {
        @Value("${spring.betaredis.host}")
        private String host;
        @Value("${spring.betaredis.port}")
        private Integer port;
        @Value("${spring.betaredis.password}")
        private String password;
        @Value("${spring.betaredis.database}")
        private Integer database;

        @Value("${spring.betaredis.lettuce.pool.max-active}")
        private Integer maxActive;
        @Value("${spring.betaredis.lettuce.pool.max-idle}")
        private Integer maxIdle;
        @Value("${spring.betaredis.lettuce.pool.max-wait}")
        private Long maxWait;
        @Value("${spring.betaredis.lettuce.pool.min-idle}")
        private Integer minIdle;

        @Bean
        @SuppressWarnings("all")
        public GenericObjectPoolConfig betaPoolConfig() {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(maxActive);
            config.setMaxIdle(maxIdle);
            config.setMinIdle(minIdle);
            config.setMaxWaitMillis(maxWait);
            return config;
        }

        @Bean
        @SuppressWarnings("all")
        public RedisStandaloneConfiguration betaRedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPassword(RedisPassword.of(password));
            config.setPort(port);
            config.setDatabase(database);
            return config;
        }
    }*/
}
