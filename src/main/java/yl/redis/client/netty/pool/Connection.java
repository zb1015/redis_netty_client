package yl.redis.client.netty.pool;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import yl.redis.client.netty.util.OsInfo;

import java.util.concurrent.locks.LockSupport;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class Connection {
    private static final String OK = "+OK";
    public SyncValue syncValue = new SyncValue();
    private Channel ch;

    public Connection(String host, int port, String password) throws Exception {
        Bootstrap b = new Bootstrap();
        b.group(ConnectionPool.group)
                .channel(OsInfo.isLinux() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ConnectionInitializer(syncValue));
        ch = b.connect(host, port).sync().channel();
        ch.closeFuture().addListener((ChannelFutureListener) (ChannelFuture f2) -> System.out.println("close:" + f2.channel().id()));
        if (!auth(password)) {
            ch.close();
            throw new Exception("redis auth error");
        }
    }

    private boolean auth(String password) {
        syncValue.curThread = Thread.currentThread();
        ch.writeAndFlush("auth " + password + "\n");
        LockSupport.park();
        return OK.equals(syncValue.value);
    }

    public String get(String key) {
        syncValue.curThread = Thread.currentThread();
        ch.writeAndFlush("get " + key + "\n");
        LockSupport.park();
        return syncValue.value;
    }

    public String set(String key, String value) {
        syncValue.curThread = Thread.currentThread();
        ch.writeAndFlush("set " + key + " " + value + "\n");
        LockSupport.park();
        return syncValue.value;
    }

    public String cli(String c) throws InterruptedException {
        syncValue.curThread = Thread.currentThread();
        ch.writeAndFlush(c + "\n");
        LockSupport.park();
        return syncValue.value;
    }
}
