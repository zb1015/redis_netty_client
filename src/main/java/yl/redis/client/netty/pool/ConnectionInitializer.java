package yl.redis.client.netty.pool;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {
    private SyncValue syncValue;

    public ConnectionInitializer(SyncValue syncValue) {
        this.syncValue = syncValue;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new MessageEncoder());
        p.addLast(new MessageDecoder(100, 1));
        p.addLast(new ConnectionHandler(syncValue));
    }
}
