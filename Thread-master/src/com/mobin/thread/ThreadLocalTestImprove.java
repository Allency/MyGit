package com.mobin.thread;

import java.util.Random;

/**
 * Created by Mobin on 2016/3/5.
 * 一个ThreadLocal代表一个变量，所以其中只能放一个数据，当你有多个变量需要共享时就应该将定义一个对象来包装这些变量再在ThreadLocal对象中存储这个对象
 */
public class ThreadLocalTestImprove {

    public static  void main(String[] args){
        for (int i = 0 ; i < 2; i ++) {
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();
                    MyThreadLocal myThreadLocal =  MyThreadLocal.getInstance();
                    System.out.println("线程:"+Thread.currentThread().getName()+"data  "+data);
                    myThreadLocal.setName("mobin"+ data);
                    myThreadLocal.setAge(data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
    static class  A{
        public void get(){
            MyThreadLocal AThreadLocal = MyThreadLocal.getInstance();
            System.out.println("A线程"+Thread.currentThread().getName()+"的name"+AThreadLocal.getName()+"    age"+AThreadLocal.getAge());
        }
    }

    static  class B{
        public void get(){
            MyThreadLocal BThreadLocal = MyThreadLocal.getInstance();
            System.out.println("B线程"+Thread.currentThread().getName()+"的name"+BThreadLocal.getName()+"    age"+BThreadLocal.getAge());
        }
    }

    static  class MyThreadLocal{
        private MyThreadLocal(){}//禁止外部直接创建对象
        private static  ThreadLocal<MyThreadLocal> mapThreadLocal = new ThreadLocal<MyThreadLocal>();
        private static  MyThreadLocal getInstance(){
            MyThreadLocal myThreadLocal = mapThreadLocal.get();
            if(myThreadLocal == null){
                myThreadLocal = new MyThreadLocal();
                mapThreadLocal.set(myThreadLocal);
            }
               return myThreadLocal;
        }

        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
