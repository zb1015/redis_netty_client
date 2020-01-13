package yl.redis.client.netty.queue;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class ConnectionHandler extends SimpleChannelInboundHandler<byte[]> {
    private CallBack cb;
    private int pushNum;
    private StringBuilder sb = new StringBuilder();

    public ConnectionHandler(String key, CallBack cb) {
        this.cb = cb;
        int num = 15;
        for (int i = 0; i < num; i++) {
            sb.append("brpop ");
            sb.append(key);
            sb.append(" 0\n");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) {
        cb.call(new String(bytes));
        if (pushNum == 0) {
            channelHandlerContext.channel().writeAndFlush(sb.toString());
        } else {
            pushNum--;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
