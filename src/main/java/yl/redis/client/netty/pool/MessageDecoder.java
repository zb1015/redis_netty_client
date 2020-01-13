package yl.redis.client.netty.pool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;

import java.util.List;

/**
 * @author ZhengBin
 * @date 2020/1/7
 * 消息解码，可根据具体协议改动
 * maxLength：最大长度限制
 * minLength：最小长度限制
 */
public class MessageDecoder extends ByteToMessageDecoder {

    private static final char FIND_R = '\r';
    private static final char TYPE_LEN = '$';
    private final int maxLength;
    private final int minLength;
    private boolean isValue = false;
    private int vLen;

    MessageDecoder(final int maxLength, final int minLength) {
        this.maxLength = maxLength;
        this.minLength = minLength;
    }

    private static int findEndOfLine(final ByteBuf buffer) {
        int i = buffer.forEachByte(ByteProcessor.FIND_LF);
        if (i > 0 && buffer.getByte(i - 1) == FIND_R) {
            i--;
        }
        return i;
    }

    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    private byte[] decode(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (isValue) {
            final int length = buffer.readableBytes();
            if (length >= vLen) {
                isValue = false;
                byte[] msg = new byte[vLen];
                buffer.readBytes(msg);
                return msg;
            } else {
                return null;
            }
        } else {
            final int eol = findEndOfLine(buffer);
            if (eol >= 0) {
                final int length = eol - buffer.readerIndex();
                //最大，最小长度限制
                if (length > maxLength) {
                    failMaxError(ctx, maxLength);
                    return null;
                }
                if (length < minLength) {
                    buffer.skipBytes(buffer.getByte(eol) == FIND_R ? 2 : 1);
                    return null;
                }
                final int delimLength = buffer.getByte(eol) == FIND_R ? 2 : 1;
                byte[] msg = new byte[length];
                buffer.readBytes(msg);
                buffer.skipBytes(delimLength);
                char type = (char) msg[0];
                if (type == TYPE_LEN) {
                    vLen = Integer.parseInt(new String(msg, 1, length - 1));
                    if (vLen == -1) {
                        return new byte[0];
                    }
                    isValue = true;
                    return null;
                } else {
                    return msg;
                }
            } else {
                final int length = buffer.readableBytes();
                //最大，最小长度限制
                if (length > maxLength) {
                    failMaxError(ctx, maxLength);
                }
                return null;
            }
        }
    }

    private void failMaxError(final ChannelHandlerContext ctx, int length) {
        ctx.fireExceptionCaught(new TooLongFrameException("frame length (" + length + ") exceeds the allowed maximum (" + maxLength + ')'));
    }
}
