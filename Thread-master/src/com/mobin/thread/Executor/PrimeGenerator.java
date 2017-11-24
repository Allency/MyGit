package com.mobin.thread.Executor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mobin on 2016/4/27.
 */
public class PrimeGenerator implements  Runnable{

    //保存素数
    private final List<BigInteger> primes = new ArrayList<BigInteger>();
    //volatile  BigInteger p = BigInteger.ONE;  不能这样，这样会出现线程安全问题，与i ++
    //保证变量的可见性
    private volatile boolean cancelled;
    public void run() {
         BigInteger p = BigInteger.ONE;
        while (!cancelled){
            p = p.nextProbablePrime();  //大于此BigIntegr的第一个素数
            synchronized (this) {
                System.out.println("素数："+p);
                primes.add(p);
                System.out.println(primes.size()+"......");
            }
        }
    }

    public void canael(){
        cancelled = true;
    }

    public synchronized List<BigInteger> get(){
        return  new ArrayList<BigInteger>(primes);
    }

    public static void main(String[] args) throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        try {
            TimeUnit.MILLISECONDS.sleep(10);   //休眠期时间即是产生素数的时间
        }finally {
            generator.canael();
        }
        List<BigInteger> list = generator.get();      //get完成后，add再次拿到锁再次添加了一个素数，但get方法却已经执行了，所以size会比实际的少1或更少
       // System.out.println("长度"+list.size());
        for (BigInteger i:list){
            System.out.println(i);
        }
    }
    /*输出：
       素数：2
       素数：3
       素数：5
       素数：7
       2
       3
       5
       7
       素数：11

       最终没有输出11，因为在取消请求的时刻与run方法中循环执行下一次检查之间存在延迟
    *
    * */
}
