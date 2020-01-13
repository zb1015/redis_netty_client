package yl.redis.client.netty.queue;

import yl.redis.client.netty.util.Crc16;

/**
 * @author ZhengBin
 * @date 2020/1/13
 */
public class Queue {
    public static void add(String key, CallBack cb) {
        int slot = Crc16.getCrc16(key.getBytes());
        for (node n : ClusterNodes.clusterNodes) {
            if (slot >= n.slot[0] && slot <= n.slot[1]) {
                try {
                    new Connection(key, n, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
