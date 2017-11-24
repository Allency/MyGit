package com.mobin.thread;

/**
 * Created by Mobin on 2016/3/3.
 * 关于CurrentThread的点
 */
public class CurrentThread {
    public static  void main(String[] args){
        Other other = new Other();
        other.start();
        other.interrupt();
        System.out.println("当前线程是："+other.getName()+"当前线程是否停止：  "+other.interrupted());
        System.out.println("当前线程是："+other.currentThread().getName()+"当前线程是否停止：  "+other.interrupted());
      //other.currentThread().getName()返回调用这个方法（currentThread）的线程的名字
        //other.getName()返回返回的是other的引用，而other没有重写getName方法，所以调用的是父类Thread的getName方法。
    }
}

class  Other extends  Thread{
    @Override
    public void run() {
        System.out.println("当前线程的名字："+this.getName());
        System.out.println("当前线程的名字："+this.currentThread().getName());

    }
}