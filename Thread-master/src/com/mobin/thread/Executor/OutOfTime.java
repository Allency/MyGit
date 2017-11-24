package com.mobin.thread.Executor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mobin on 2016/4/25.
 */
public class OutOfTime {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(),1);
        TimeUnit.SECONDS.sleep(1);
        timer.schedule(new ThrowTask(),1);
        TimeUnit.SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask{
        public void run() {
            throw  new RuntimeException();
        }
    }
    //最终程序会抛出 java.lang.IllegalStateException: Timer already cancelled.错误，说明
    // TimerTask并不能很好的处理异常，程序在1秒后就结束了整个Timer，所以再调用Schedule时就报错误了
}
