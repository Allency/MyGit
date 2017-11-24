package com.mobin.thread.sleepAndjoin;

import sun.org.mozilla.javascript.internal.Synchronizer;

/**
 * Created by Mobin on 2016/6/3.
 * 验证sleep具有不释放锁的特点
 */
public class SleepTest {

    static class ThreadA extends  Thread{
        private ThreadB b;
        public ThreadA(ThreadB b){
            this.b = b;
        }
        public void run() {
            synchronized (b){
                try {
                    b.start();
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class  ThreadB extends  Thread{
        public void run() {
            try {
                System.out.println("b run begin timer= " + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("b run end timer=  " + System.currentTimeMillis() );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized public void bService(){
            System.out.println("打印了bservice timer=  " + System.currentTimeMillis());
        }
    }

    static class ThreadC extends  Thread{
        private ThreadB threadB;
        public ThreadC(ThreadB threadB){
            this.threadB = threadB;
        }

        public void run() {
            threadB.bService();
        }
    }

    public static void main(String[] args) throws InterruptedException {
           ThreadB b = new ThreadB();
           ThreadA a = new ThreadA(b);
           a.start();
           ThreadC c = new ThreadC(b);
           c.start();   //threadB.bService();方法只在5秒后才运行，说明sleep具有不释放锁的特点
    }
}
