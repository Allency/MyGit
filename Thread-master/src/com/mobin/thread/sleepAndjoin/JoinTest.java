package com.mobin.thread.sleepAndjoin;

/**
 * Created by Mobin on 2016/6/3.
 */
public class JoinTest {
    static class ThreadA extends  Thread{
        private ThreadB b;
        public ThreadA(ThreadB b){
            this.b = b;
        }
        public void run() {
            synchronized (b){
                try {
                    b.start();
                    b.join(6000);
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
                Thread.sleep(6000);
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
        c.start();   //threadB.bService();方法能被立即调用，说明join方法具有释放锁的特点
    }
}
