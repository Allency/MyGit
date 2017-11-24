package com.mobin.thread.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Mobin on 2016/4/14.
 */
public class CountDownLatchTest {
    static CountDownLatch c = new CountDownLatch(2);
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            public void run() {
                System.out.println(1);
                c.countDown();
                System.out.println(2);
                c.countDown();
            }
        }).start();

        c.await();//await会一直阻塞到计数器为零，或者等待中的线程中断，或者等待超时
        System.out.println(3);
    }
}
