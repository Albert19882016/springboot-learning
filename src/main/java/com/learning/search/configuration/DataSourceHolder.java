package com.learning.search.configuration;

public class DataSourceHolder {

    // 使用ThreadLocal把数据源与当前线程绑定
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    public static void setDataSource(String dataSourceName) {
        dataSources.set(dataSourceName);
    }

    public static String getDataSource() {
        return (String) dataSources.get();
    }

    public static void clearDataSource() {
        dataSources.remove();
    }
}
