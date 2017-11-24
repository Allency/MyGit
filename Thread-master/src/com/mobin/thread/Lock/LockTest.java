package com.mobin.thread.Lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mobin on 2016/3/20.
 */
public class LockTest {

    public static  void init(){
        final Outprint outprint= new Outprint();
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(2000);
                        outprint.out("hadoop");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();


        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(2000);
                        outprint.out("spark");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    static  class Outprint{
        Lock lock = new ReentrantLock();
        public   void out(String str) {
            lock.lock();
            try {
                for (int i = 0; i < str.length(); i++) {
                    System.out.print(str.charAt(i));
                }
                System.out.println();
               // lock.unlock();//释放锁（如果上面的代码在unlock之前出错，那么锁将不会被释放，所以最好放到finally中）
            }finally {
                lock.unlock();//释放锁
            }
            }
    }

    public static void main(String[] args) {
        LockTest.init();
    }
}
