package com.mobin.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Mobin on 2016/3/4.
 * 线程间的数据共享及static线程安全问题
 */
public class ShareData {

  //  private static  int data = 0;  static变量为非线程安全(如果注释掉该行，行线程共享这个变量会出现线程安全问题)
    private static Map<Thread,Integer> threadData = new HashMap<Thread,Integer>();
    public static  void main(String[] args){
        for (int i = 0; i < 2; i ++) {
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();  //局部变量属于线程安全
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "  data:  " +  data);
                    threadData.put(Thread.currentThread(), data);

                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }

    static class A{
        public    void get(){
            int data = threadData.get(Thread.currentThread());
            System.out.println("A："+Thread.currentThread().getName() + "  data:  " + data);
        }
    }

    static  class B{
        public    void get(){
            int data = threadData.get(Thread.currentThread());
            System.out.println("B："+Thread.currentThread().getName() + " data:  " + data);
        }
    }
}
