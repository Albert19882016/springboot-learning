package com.learning.search.configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseConfiguration {

    private final HbaseProperties properties;

    public HbaseConfiguration(HbaseProperties properties) {
        this.properties = properties;
    }

    public org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        Map<String, String> config = properties.getConfig();
        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }
        return configuration;
    }

    @Bean
    public Connection getConnect(){
        Connection conn = null;
        try {
            conn = ConnectionFactory.createConnection(configuration());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }
}
