package com.mobin.thread.MonitorThread;

/**
 * Created by Mobin on 2017/2/18.
 */
public class MonitorManager {
    //监控线程状态
    private volatile boolean shouldRunMonitor;
    private Daemon monthread;
    //每隔2秒检查一次
    private static final long MonitorSleepMS = 2000;

    class Monitor implements Runnable {
        @Override
        public void run() {
            System.out.println(1);
            for (; shouldRunMonitor ; ) {
                try {
                    System.out.println("已经开始监控");
                    Thread.sleep(MonitorSleepMS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void startMonitor() {
        System.out.println(2);
        shouldRunMonitor = true;
        monthread = new Daemon(new Monitor());
        System.out.println(3);
        monthread.start();
    }

    void stopMonitor() {
        if (monthread != null) {
            shouldRunMonitor = false;
            monthread.interrupt();
            try {
                monthread.join(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //注意释放无用对象，避免内存泄露
        monthread = null;
    }
}