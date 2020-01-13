package yl.redis.client.netty.util;

/**
 * @author ZhengBin
 * @date 2020/1/7
 */
public class OsInfo {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return OS.contains("linux");
    }
}
