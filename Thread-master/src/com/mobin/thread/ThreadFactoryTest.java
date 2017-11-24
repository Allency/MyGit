package com.mobin.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by MOBIN on 2016/8/18.
 */
public class ThreadFactoryTest implements ThreadFactory{
    private int counter;
    private String name;
    private List<String> stats;
    public ThreadFactoryTest(String name){
        counter = 0;
        this.name = name;
        stats = new ArrayList<>();
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r,name + "-Thread_" + counter);
        counter ++;
        stats.add("Created thread " + t.getId() + " with name " + t.getName() + " on " + new Date());
        return t;
    }

    public String getStatus(){
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = stats.iterator();
        while(it.hasNext()){
            sb.append(it.next());
            sb.append("\n");
        }
        return  sb.toString();
    }

    static class Task implements  Runnable{

        @Override
        public void run() {
            try{
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadFactoryTest factoryTest = new ThreadFactoryTest("MyThreadFactory");
        Task task = new Task();
        Thread thread;
        System.out.println("starting the Threads\n");
        for(int i = 0; i < 10; i ++){
            thread = factoryTest.newThread(task);
            thread.start();
        }

        System.out.println("Factory stats:" + factoryTest.getStatus());
    }
}
