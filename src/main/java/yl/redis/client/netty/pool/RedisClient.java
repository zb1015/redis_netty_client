package yl.redis.client.netty.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author ZhengBin
 * @date 2020/1/9
 */
public class RedisClient {
    static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private BlockingQueue<Connection> connections;
    private String ip;
    private int port;
    private int[] slots;

    public RedisClient(int maxNum) {
        connections = new ArrayBlockingQueue<>(maxNum);
    }

    private Connection getConnection() throws InterruptedException {
        return connections.take();
    }

    public void addConnection(Connection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            logger.error("connections put() throws InterruptedException", e);
        }
    }

    public String set(String key, String value) {
        Connection connection = null;
        try {
            connection = getConnection();
            return connection.set(key, value);
        } catch (InterruptedException e) {
            logger.error("connections take() throws InterruptedException", e);
            return "";
        } finally {
            if (connection != null) {
                addConnection(connection);
            }
        }
    }

    public String get(String key) {
        Connection connection = null;
        try {
            connection = getConnection();
            return connection.get(key);
        } catch (InterruptedException e) {
            logger.error("connections take() throws InterruptedException", e);
            return "";
        } finally {
            if (connection != null) {
                addConnection(connection);
            }
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int[] getSlots() {
        return slots;
    }

    public void setSlots(int[] slots) {
        this.slots = slots;
    }
}
