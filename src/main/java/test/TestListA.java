package test;

import yl.redis.client.netty.queue.CallBack;

/**
 * @author ZhengBin
 * @date 2020/1/13
 */
public class TestListA implements CallBack {
    @Override
    public void call(String v) {
        if (!"+OK".equals(v)) {
            System.out.println(v + ":" + System.currentTimeMillis());
        }
    }
}
