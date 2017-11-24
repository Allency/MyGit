package com.mobin.thread.MonitorThread;

/**
 * Created by Mobin on 2017/2/18.
 */
public class MonitorTest {
    public static void main(String[] args) throws InterruptedException {
       MonitorManager monitor = new MonitorManager();
        monitor.startMonitor();
        Thread.sleep(500000);
    }
}
