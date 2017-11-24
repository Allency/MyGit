package com.mobin.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mobin on 2016/3/28.
 * 用于实现两个人之间的数据交换，每个人在完成一定的事务后想与对方交换数据，第一个先拿出数据的人将一直等待第二个人拿着数据到时才能彼此交换
 */
public class ExchangeTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        final Exchanger exchanger = new Exchanger();
        service.execute(new Runnable() {
            public void run() {
                try {
                    String mobin = "mobin";
                    System.out.println("线程" + Thread.currentThread().getName() +
                            "正在把数据" + mobin + "换出去");

                    Thread.sleep((long) (Math.random() * 1000));
                    String mobin1 = (String) exchanger.exchange(mobin);
                    System.out.println("线程" + Thread.currentThread().getName() +
                            "换回来的数据为" + mobin1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        service.execute(new Runnable() {
            public void run() {
                try {
                    String kpop = "kpop";
                    System.out.println("线程" + Thread.currentThread().getName() +
                            "正在把数据" + kpop + "换出去");

                    Thread.sleep((long) (Math.random() * 1000));
                    String kpop1 = (String) exchanger.exchange(kpop);
                    System.out.println("线程" + Thread.currentThread().getName() +
                            "换回来的数据为" + kpop);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
