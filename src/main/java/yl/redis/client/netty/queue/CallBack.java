package yl.redis.client.netty.queue;

/**
 * @author ZhengBin
 * @date 2020/1/13
 */
public interface CallBack {
    /**
     * 回调方法
     *
     * @param v 返回的内容
     */
    void call(String v);
}
