package com.mobin.thread;

/**
 * Created by Mobin on 2016/3/9.
 * 多线程访问共享对象和数据的方式(一)
 * 将共享数据封装到另一个对象中，然后这个对象逐一传递给Runable对象，
 * 每个线程对共享数据的操作方式也分配到那个对象的方法上去完成，这样容易实现对该数据进行的各种操作的互斥和通信。
 */
public class MultiThreadShareData1 {
    public  static  void main(String[] args){
        final Sharedata sharedata = new Sharedata();
        for(int i = 0; i < 5; i ++) {
            new Thread(new Runnable() {
                public void run() {
                    sharedata.inc();
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    sharedata.dec();
                }
            }).start();
        }
    }

    static  class Sharedata{
        private int count = 10;
        private  boolean flag = true;

        public synchronized void inc(){
            /*while(!flag) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            count++;
            System.out.println("线程进行了加操作" + count);
          /*  flag = false;
            this.notify();*/
        }

        public synchronized  void dec(){
          /*  while (flag){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            count --;
            System.out.println("线程进行了减操作"+count);
           /* flag = true;
            this.notify();*/
        }
    }
}
