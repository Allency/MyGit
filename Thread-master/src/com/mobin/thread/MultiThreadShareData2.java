package com.mobin.thread;

/**
 * Created by Mobin on 2016/3/9.
 * 多线程访问共享对象和数据的方式(二)
 * 将这些Runable对象作为某一个类中的内部类，共享数据作为这个外部类中的成员变量，
 * 每个线程对共享数据时的操作方法也分配给外部类，以便实现对共享数据进行的各个操作的互斥和通信，
 * 作为内部类的各个Runable对象调用外部类的这些方法
 */
public class MultiThreadShareData2 {
    private  int count = 10;

    public  synchronized  void inc(){
        count ++;
        System.out.println("线程进行了加操作"+count);
    }
    public  synchronized void dec(){
        count --;
        System.out.println("线程进行了减操作"+count);
    }
    public static void main(String[] args){
        MultiThreadShareData2 multiThreadShareData2 = new MultiThreadShareData2();
        for(int i = 0; i < 5; i ++) {
            new Thread(multiThreadShareData2.new Threadinc()).start();
            new Thread(multiThreadShareData2.new Threaddec()).start();
        }
    }

    class Threadinc implements  Runnable{
       public void run() {
            inc();
       }
   }

      class  Threaddec implements  Runnable{
        public void run() {
            dec();
        }
    }
}
