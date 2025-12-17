package com.fuse.ai.server.inner.config.cache;

import com.fuse.ai.server.common.constants.Constants;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnBean(MyCacheConfig.class)
public class CacheManagerConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private Integer database;

    private final CacheProperties cacheProperties;

    @Autowired
    private MyCacheConfig myCacheConfig;

    CacheManagerConfiguration(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    public interface CacheManagerNames {
        String REDIS_CACHE_MANAGER = "redisCacheManager";
        String CAFFINE_CACHE_MANAGER = "caffeineCacheManager";
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig poolConfig,
                                                         RedisStandaloneConfiguration standaloneConfiguration) {
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                .build();
        return new JedisConnectionFactory(standaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(CacheProperties cacheProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        if (StringUtils.hasLength(password)) {
            configuration.setPassword(password);
        }
        configuration.setDatabase(database);
        System.out.println(configuration);
        return configuration;
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Primary
    @Bean(CacheManagerNames.REDIS_CACHE_MANAGER)
    public RedisCacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory) {
        Map<String, RedisCacheConfiguration> configurationMap = buildRedisCacheConfigurationMap();
        return RedisCacheManager
                .builder(jedisConnectionFactory)
                .initialCacheNames(configurationMap.keySet())
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }

    private Map<String, RedisCacheConfiguration> buildRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        if (myCacheConfig.getRedis() != null) {
            myCacheConfig.getRedis().entrySet().stream()
                    .forEach(entry -> configurationMap.put(
                            entry.getKey(),
                            buildRedisCacheConfiguration(entry.getValue().getExpireTime())));
        }
        return configurationMap;
    }

    private RedisCacheConfiguration buildRedisCacheConfiguration(long expireTime) {
        RedisSerializer redisSerializer = new StringRedisSerializer();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(expireTime))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .prefixCacheNameWith(Constants.CACHE_KEY_PREFIX);
        //.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        return redisCacheConfiguration;
    }

    @Bean(CacheManagerNames.CAFFINE_CACHE_MANAGER)
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        if (myCacheConfig.getCaffeine() != null) {
            List<CaffeineCache> caches =
                    myCacheConfig.getCaffeine().entrySet().stream()
                            .map(entry -> buildCaffeineCache(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList());
            simpleCacheManager.setCaches(caches);
        }
        return simpleCacheManager;
    }

    private CaffeineCache buildCaffeineCache(String name, MyCacheConfig.CacheSpec spec) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .maximumSize(spec.getMaxSize())
                .recordStats()
                .expireAfterWrite(spec.getExpireTime(), TimeUnit.SECONDS)
                .ticker(Ticker.systemTicker());
        return new CaffeineCache(name, caffeine.build());
    }
}
