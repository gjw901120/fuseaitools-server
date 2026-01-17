package com.fuse.ai.server.inner;

import com.fuse.common.cache.RedisLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {
                "com.fuse",
        })
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fuse.**.feign"})
@ComponentScan(value = {"com.fuse"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {RedisLock.class})
})
@EnableAsync
@EnableCaching
@EnableConfigurationProperties
@EnableTransactionManagement
public class InnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnerApplication.class, args);
    }

}
