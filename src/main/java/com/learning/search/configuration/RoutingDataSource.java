package com.learning.search.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.util.concurrent.atomic.AtomicInteger;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Value("spring.datasource.read.num")
    private final String dataSourceNumber;

    private AtomicInteger count = new AtomicInteger(0);

    public RoutingDataSource(String dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 可以做一个简单的负载均衡策略
        String lookupKey = DataSourceHolder.getDataSource();
        System.out.println("------------lookupKey---------" + lookupKey);
        return lookupKey;
    }
}
