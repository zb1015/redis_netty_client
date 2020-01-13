package yl.redis.client.netty.pool;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.locks.LockSupport;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class ConnectionHandler extends SimpleChannelInboundHandler<byte[]> {
    private SyncValue syncValue;

    public ConnectionHandler(SyncValue syncValue) {
        this.syncValue = syncValue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) {
        syncValue.value = new String(bytes);
        LockSupport.unpark(syncValue.curThread);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
