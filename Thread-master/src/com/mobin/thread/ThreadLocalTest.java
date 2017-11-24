package com.mobin.thread;

import java.util.Random;

/**
 * Created by Mobin on 2016/3/5.
 */
public class ThreadLocalTest {

    private static ThreadLocal threadLocal = new ThreadLocal();
    public static void main(String[] args){
        for (int i = 0; i < 2; i ++) {
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();
                    threadLocal.set(data);
                    System.out.println(Thread.currentThread().getName()+"--data:  "+data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }

   static class  A{
        public  void get(){
              int data = (int)threadLocal.get();
              System.out.println("A线程 :  "+Thread.currentThread().getName()+"   data:  "+data);
        }
    }

    static    class B{
        public void get(){
            int data = (int)threadLocal.get();
            System.out.println("B线程 :  "+Thread.currentThread().getName()+"   data:  "+data);
        }
    }
}
