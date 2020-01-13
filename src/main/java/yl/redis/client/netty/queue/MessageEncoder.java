package yl.redis.client.netty.queue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class MessageEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) {
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(data);
    }
}
