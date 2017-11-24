package com.mobin.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Mobin on 2016/3/20.
 * 启动10个线程，哪个先运行完就返回那个结果
 */
public class CompletionServiceTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService completionService = new ExecutorCompletionService(executor);
        for (int i =1; i <=10; i ++) {
            final  int result = i;
            completionService.submit(new Callable() {
                public Object call() throws Exception {
                    Thread.sleep(new Random().nextInt(5000));
                    return result;
                }
            });
        }
        System.out.println(completionService.take().get());
    }
}
