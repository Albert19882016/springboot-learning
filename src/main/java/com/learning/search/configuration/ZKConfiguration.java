package com.learning.search.configuration;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.CountDownLatch;

@Configuration
public class ZKConfiguration implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZKConfiguration.class);

    @Value("${zookeeper.server}")
    private String zookeeperServers;

    @Value(("${zookeeper.sessionTimeoutMs}"))
    private int sessionTimeoutMs;

    @Value("${zookeeper.connectionTimeoutMs}")
    private int connectionTimeoutMs;

    @Value("${zookeeper.maxRetries}")
    private int maxRetries;

    @Value("${zookeeper.baseSleepTimeMs}")
    private int baseSleepTimeMs;

    @Value("${application.service-name}")
    private String serviceName;

    @Value("${application.service-address}")
    private String address;

    private static final String REGISTRY_PATH = "/registry";

    //多线程时，等待，直到一个线程时，在latch被唤醒
    private static CountDownLatch latch = new CountDownLatch(1);

    @Bean
    public ZooKeeper register() {
        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper(zookeeperServers,sessionTimeoutMs,this);
            latch.await(); // latch等待唤醒

            String registryPath = REGISTRY_PATH;
            if (zookeeper.exists(registryPath, false) == null) {
                zookeeper.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); //持久化创建/registry node
            }

            //创建服务节点（持久节点）
            String servicePath = registryPath + "/" + serviceName;
            if (zookeeper.exists(servicePath, false) == null) {
                zookeeper.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            //创建地址节点
            String addressPath = servicePath + "/address-"; // 此处节点是瞬态的节点，当服务断开zookeeper连接时，节点消失，重新连接时，address- 以序列添加末尾序列值。<br>这种序列方法可以判断注册服务的主被，先注册的数字小，后注册的数字大，每次从主上同步数据到被。在服务异常时，节点自动消失，可以探测服务状态
            zookeeper.create(addressPath, address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (Exception e){
            logger.error("初始化zookeeper实例错误！！");
            e.printStackTrace();
        }
        return zookeeper;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
            latch.countDown();
    }
}
