package com.mobin.thread;

/**
 * Created by Mobin on 2016/3/3.
 */
public class Interrupted {
    public static  void main(String[] args){
        Thread.currentThread().interrupt();
        System.out.println("当前线程是否停止：  "+Thread.interrupted());
        System.out.println("当前线程是否停止：  "+Thread.interrupted());
        System.out.println("当前线程是否停止：  "+Thread.interrupted());
    }
}
