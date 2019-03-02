package com.learning.search.configuration;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import java.net.InetAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.learning.search",includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ElasticsearchRepository.class) )
public class ESConfiguration  {

    private static final Logger logger = LoggerFactory.getLogger(ESConfiguration.class);

    /**
     * es集群地址, 以逗号分隔
     */
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;
    /**
     * 端口
     */
    @Value("${spring.data.elasticsearch.port}")
    private String port;
    /**
     * 集群名称
     */
    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    /**
     * 连接池
     */
    @Value("${spring.data.elasticsearch.pool}")
    private String poolSize;

    @Bean
    public TransportClient getClient(){
        try {
            Settings settings = Settings.builder().put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)// 增加嗅探机制，找到ES集群
                    .put("thread_pool.search.size", Integer.parseInt(poolSize))
                    .build();    // 增加线程池个数，暂时设为5
            TransportClient client = new PreBuiltTransportClient(settings);
            String[] ips = clusterNodes.split(",");
            for(String ip:ips){
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(ip),Integer.valueOf(port)));
            }
            logger.info("Elaticsearch实例化-clusterNodes:" + clusterNodes + "&& port:" + port + "&& clusterName:" + clusterName);
            return client;
        } catch (Exception e) {
            logger.error("elasticsearch TransportClient create error!!!", e);
            return null;
        }
    }
}
