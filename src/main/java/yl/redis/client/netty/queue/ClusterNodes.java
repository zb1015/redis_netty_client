package yl.redis.client.netty.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhengBin
 * @date 2020/1/13
 */
public class ClusterNodes {
    static List<node> clusterNodes = new ArrayList<>();

    public static void init(String host, int port, String password) throws Exception {
        yl.redis.client.netty.pool.Connection connection = new yl.redis.client.netty.pool.Connection(host, port, password);
        String nodesStr = connection.cli("cluster nodes");
        String flag = "\n";
        for (String clusterNode : nodesStr.split(flag)) {
            String[] nodeInfos = clusterNode.split(" ");
            if (nodeInfos[2].contains("master")) {
                node n = new node();
                n.password = password;
                n.ip = nodeInfos[1].split(":")[0];
                n.port = Integer.parseInt(nodeInfos[1].split(":")[1].split("@")[0]);
                n.slot = new int[]{Integer.parseInt(nodeInfos[8].split("-")[0]), Integer.parseInt(nodeInfos[8].split("-")[1])};
                clusterNodes.add(n);
            }
        }
        connection.cli("quit");
    }

}
