package utils;

public class GameTimer {

    private long startTime;
    private long endTime;

    // 开始计时
    public void start() {
        startTime = System.currentTimeMillis();
    }

    // 结束计时
    public void finish() {
        endTime = System.currentTimeMillis();
    }

    // 获取计时的秒数
    public long lastingSeconds() {
        return (endTime - startTime) / 1000;
    }

}
