package yl.redis.client.netty.pool;

/**
 * @author ZhengBin
 * @date 2020/1/9
 */
public class PoolConfig {
    private int initNum;
    private int maxNum;
    private int minNum;
    private int idleTime;
    private int timeOut;
    private String password;

    public int getInitNum() {
        return initNum;
    }

    public void setInitNum(int initNum) {
        this.initNum = initNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
