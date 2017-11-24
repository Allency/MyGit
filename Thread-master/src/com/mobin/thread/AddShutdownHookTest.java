package com.mobin.thread;

/**
 * Created by Mobin on 2017/6/14.
 */
public class AddShutdownHookTest {
    public static void main(String[] args) {
        Object o = new Object(){
            @Override
            protected void finalize() throws Throwable {
               // 一旦垃圾收集器准备好释放对象占用的存储空间，它首先调用finalize()
                System.out.println("running finalize......");
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("running shutdown hook....");
            }
        });

        o = null;
        System.gc();

        System.out.println("Calling system exit");
        System.exit(0);
    }
}
