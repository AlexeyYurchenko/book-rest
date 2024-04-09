package com.example.bookrest.configuration;

import com.example.bookrest.configuration.properties.BookCacheProperties;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;


import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableConfigurationProperties(BookCacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnExpression("'$(book.cache.cacheType)'.equals('inMemory')")
    public ConcurrentMapCacheManager inMemoryCacheManager(BookCacheProperties bookCacheProperties) {
        var cacheManager = new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(bookCacheProperties.getCaches().get(name).getExpiry())
                                .build()
                                .asMap(),
                        true
                );
            }
        };

        var cacheNames = bookCacheProperties.getCacheNames();

        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }
        return cacheManager;
    }

    @Bean
    @ConditionalOnProperty(prefix = "book.redis", name = "enabled", havingValue = "true")
//    @ConditionalOnExpression("$(app.cache.cacheType).equals('redis')")
    @DependsOn({"lettuceConnectionFactory"})
    public CacheManager redisCacheManager(BookCacheProperties bookCacheProperties, LettuceConnectionFactory lettuceConnectionFactory) {
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

        bookCacheProperties.getCacheNames().forEach(cacheName -> {
            redisCacheConfigurationMap.put(cacheName, RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                    bookCacheProperties.getCaches().get(cacheName).getExpiry()));
        });

        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }
}