package com.hired.onlineshopping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Slf4j
public class RedisService {
    @Resource
    private JedisPool jedisPool;

    public void setValue(String key, String value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value);
        jedisClient.close();
    }

    public String getValue(String key) {
        Jedis client = jedisPool.getResource();
        String value = client.get(key);
        client.close();
        return value;
    }

    public boolean tryToGetDistributedLock(String lockKey, String requestId,  int expireTime) {
        Jedis jedisClient = jedisPool.getResource();
        String result = jedisClient.set(lockKey, requestId, "NX", "PX", expireTime);
        jedisClient.close();
        if ("OK".equals(result)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedisClient = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1]" +
                " then return redis.call('del', KEYS[1])" +
                " else return 0 end";
        Long result = (Long) jedisClient.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (result == 1L) {
            return true;
        } else {
            return false;
        }
    }
    public long stockDeduct(String key) { // commodity:1234
        try (Jedis client = jedisPool.getResource()) {
            // LUA
            String script =
                    "if redis.call('exists', KEYS[1]) == 1 then\n" +
                            "    local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                            "    if (stock<=0) then\n" +
                            "        return -1\n" +
                            "    end\n" +
                            "\n" +
                            "    redis.call('decr', KEYS[1]);\n" +
                            "    return stock - 1;\n" +
                            "end\n" +
                            "\n" +
                            "return -1;";
            Long stock = (Long) client.eval(script, Collections.singletonList(key), Collections.emptyList());

            if (stock < 0) {
                System.out.println("There is no stock available");
                return -1;
            } else {
                System.out.println("Validate and decreased redis stock, current available stock：" + stock);
                return stock;
            }
        } catch (Throwable throwable) {
            System.out.println("Exception on stockDeductValidation：" + throwable.toString());
            return -1;
        }
    }

    public void revertStock(String redisKey) {
        Jedis client = jedisPool.getResource();
        client.incr(redisKey);
        client.close();
    }

    public void addToDenyList(String userId, String commodityId) {
        Jedis jedisClient = jedisPool.getResource();
        // key: denyList: 123
        // values: set of {1,2}
        jedisClient.sadd("denyList:" + userId,
                String.valueOf(commodityId));
        jedisClient.close();
        log.info("Add userId: {} into denyList for commodityId: {}", userId, commodityId);
    }

    public void removeFromDenyList(String userId, String commodityId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("denyList:" + userId,
                String.valueOf(commodityId));
        jedisClient.close();
        log.info("Remove userId: {} into denyList for commodityId: {}", userId, commodityId);
    }

    public boolean isInDenyList(String userId, String commodityId) {
        Jedis jedisClient = jedisPool.getResource();
        Boolean isInDenyList = jedisClient.sismember("denyList:" + userId,
                String.valueOf(commodityId));
        jedisClient.close();
        log.info("userId: {} , commodityId {} is InDenyList result: {}", userId, commodityId,
                isInDenyList);
        return isInDenyList;
    }
}