package yl.redis.client.netty.queue;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {
    private String key;
    private CallBack cb;

    public ConnectionInitializer(String key, CallBack cb) {
        this.key = key;
        this.cb = cb;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new MessageEncoder());
        p.addLast(new MessageDecoder(100, 1));
        p.addLast(new ConnectionHandler(key, cb));
    }
}
