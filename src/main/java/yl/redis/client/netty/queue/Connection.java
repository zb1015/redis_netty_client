package yl.redis.client.netty.queue;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import yl.redis.client.netty.util.OsInfo;

/**
 * @author ZhengBin
 * @date 2020/1/13
 */
public class Connection {
    static int nThreads = 1;
    static EventLoopGroup group = OsInfo.isLinux() ? new EpollEventLoopGroup(nThreads) : new NioEventLoopGroup(nThreads);

    public Connection(String key, node n, CallBack cb) throws Exception {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(OsInfo.isLinux() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ConnectionInitializer(key, cb));
        b.connect(n.ip, n.port).addListener((ChannelFutureListener) (ChannelFuture f1) -> {
            f1.channel().closeFuture().addListener((ChannelFutureListener) (ChannelFuture f2) -> System.out.println("close:" + f2.channel().id()));
            f1.channel().writeAndFlush("auth " + n.password + "\n");
        });
    }
}
