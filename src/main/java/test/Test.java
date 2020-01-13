package test;

import yl.redis.client.netty.pool.Connection;
import yl.redis.client.netty.pool.RedisCluster;
import yl.redis.client.netty.queue.CallBack;
import yl.redis.client.netty.queue.ClusterNodes;
import yl.redis.client.netty.queue.Queue;
import yl.redis.client.netty.util.Crc16;


/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class Test {
    public static Connection connection;

    public static void main(String[] args) {
        try {
//            PoolConfig poolConfig = new PoolConfig();
//            poolConfig.setMaxNum(10);
//            poolConfig.setInitNum(5);
//            poolConfig.setPassword("Work~731!");
//            ConnectionPool.setConfig(poolConfig);
//            RedisCluster.set("user_name","zb1015");
//            forTestGet(1000);

            ClusterNodes.init("172.18.1.250", 17000, "Work~731!");
            CallBack cb = new TestListA();
            Queue.add("a", cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void forTestGet(int num) {
        int count = 100;
        for (int k = 0; k < count; k++) {
            long b = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                try {
                    RedisCluster.get("your_mark");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            long e = System.currentTimeMillis();
            System.out.println(e - b);
        }
    }

    public static void forTestCrc16(int num, String key) {
        int count = 100;
        for (int k = 0; k < count; k++) {
            long b = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                try {
                    Crc16.getCrc16(key.getBytes());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            long e = System.currentTimeMillis();
            System.out.println(e - b);
        }
    }
}
