package yl.redis.client.netty.pool;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import yl.redis.client.netty.util.OsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class ConnectionPool {
    static int slotAverage;
    static int slotNum;
    static int nThreads = 1;
    static EventLoopGroup group = OsInfo.isLinux() ? new EpollEventLoopGroup(nThreads) : new NioEventLoopGroup(nThreads);
    static RedisClient[] redisClients;

    public static void setConfig(PoolConfig poolConfig) throws Exception {
        Connection connection = new Connection("172.18.1.250", 17000, poolConfig.getPassword());
        String nodes = connection.cli("cluster nodes");
        String[] clusterNodes = nodes.split("\n");
        List<RedisClient> listRcs = new ArrayList<>();
        for (String clusterNode : clusterNodes) {
            String[] node_infos = clusterNode.split(" ");
            if (node_infos[2].contains("master")) {
                RedisClient redisClient = new RedisClient(poolConfig.getMaxNum());
                redisClient.setIp(node_infos[1].split(":")[0]);
                redisClient.setPort(Integer.parseInt(node_infos[1].split(":")[1].split("@")[0]));
                redisClient.setSlots(new int[]{Integer.parseInt(node_infos[8].split("-")[0]), Integer.parseInt(node_infos[8].split("-")[1])});
                listRcs.add(redisClient);
            }
        }
        connection.cli("quit");
        slotNum = listRcs.size();
        slotAverage = 16384 / slotNum;
        redisClients = new RedisClient[slotNum];
        for (RedisClient redisClient : listRcs) {
            redisClients[redisClient.getSlots()[0] / slotAverage] = redisClient;
        }
        for (RedisClient redisClient : redisClients) {
            for (int i = 0; i < poolConfig.getInitNum(); i++) {
                Connection c = new Connection(redisClient.getIp(), redisClient.getPort(), poolConfig.getPassword());
                redisClient.addConnection(c);
            }
        }
    }
}
