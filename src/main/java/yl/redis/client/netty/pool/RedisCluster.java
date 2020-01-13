package yl.redis.client.netty.pool;

import yl.redis.client.netty.util.Crc16;

/**
 * @author ZhengBin
 * @date 2020/1/9
 */
public class RedisCluster {
    public static String get(String key) {
        RedisClient rc = ConnectionPool.redisClients[Crc16.getCrc16(key.getBytes()) / ConnectionPool.slotAverage];
        return rc.get(key);
    }

    public static String set(String key, String value) {
        RedisClient rc = ConnectionPool.redisClients[Crc16.getCrc16(key.getBytes()) / ConnectionPool.slotAverage];
        return rc.set(key, value);
    }
}
