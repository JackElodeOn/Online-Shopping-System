package com.hired.onlineshopping.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.MessageFormat;
import java.time.Duration;

@Configuration
@Slf4j
public class JedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWait(Duration.ofMillis(maxWaitMillis));
        config.setMaxTotal(maxActive);
        JedisPool jedisPool = new JedisPool(config, host, port, timeout,
                null);
        log.info("JedisPool 注入成功");
        log.info(MessageFormat.format("Redis address: {0}: {1}",
                host,port));
        return jedisPool;
    }
}