package com.mobin.thread.Volatile;

/**
 * Created by Mobin on 2016/4/19.
 * volatile原子性
 * 步骤1：从内存中取出value
 * 步骤2:计算value值
 * 步骤3:重新将值刷新回内存
 *
 * 当线程1在步骤2对value进行计算时，刚好其他线程也对value进行了修改，这时线程1返回的值就不是我们期望的值了，
 * 于是出现线程安全问题，所以volatile不能保证复合操作具有原子性；解决办法就是给increment方法加锁(lock/synchronized)或将变量原子类类型。
 */
public class VolatileTest1{
    private volatile  int value;  //将value变量声明成volatile类型

    public  synchronized void increment(){
         value ++;
        System.out.println(value);
    }

    public static void main(String[] args) {
        final VolatileTest1 volatileTest1 = new VolatileTest1();
        for(int i = 0; i < 10; i ++){
            new Thread(new Runnable() {
                public void run() {
                     volatileTest1.increment();
                }
            }).start();
        }
    }
}
