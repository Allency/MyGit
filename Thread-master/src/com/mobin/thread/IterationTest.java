package com.mobin.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Mobin on 2016/3/31.
 * /**
 * /CopyOnWriteArrayList会先复制一个集合副本，当对集合进行修改时普遍的上修改的是副本里的值，
 * 修改完后再将原因集合的引用指向这个副本，避免抛出ConcurrentModificationException异常
 * (具体分析见笔记)
 * 多线程可以同时对这个容器进行迭代，而不会干扰或者与修改容器的线程相互干扰
 */
public class IterationTest {

    public static void main(String[] args) {
      //  Collection num = new ArrayList<String>();
        final Collection num = new CopyOnWriteArrayList();
        num.add("One");
        num.add("Two");
        num.add("Three");

        new Thread(new Runnable() {
            public void run() {
                Iterator<String> iterator= num.iterator();
                while(iterator.hasNext()){
                    String element = iterator.next();
                    if("Two".equals(element)){
                       num.remove(element);
                    }else {
                        System.out.println(element);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                Iterator<String> iterator= num.iterator();
                while(iterator.hasNext()){
                    String element = iterator.next();
                        System.out.println(element+"ppp");
                }
            }
        }).start();
    }
}
