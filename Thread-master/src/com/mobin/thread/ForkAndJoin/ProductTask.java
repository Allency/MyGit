package com.mobin.thread.ForkAndJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mobin on 2016/4/24.
 * 使用10来作为参考大小（ReferenceSize），如果一个任务需要更新大于10个元素，它会将这个列表分解成为两部分，
 * 然后分别创建两个任务用来更新各自部分的产品价格。
 */
public class ProductTask extends RecursiveAction{

    //声明该类的serialVersionUID,因为RecursiveAction的父类ForkJoin Task实现了Seriallizable接口
    private static  final long serialVersionUID = 1L;

    private List<Product> products;

    private int first;
    private int last;
    private double increment;

    public ProductTask(List<Product> products, int first, int last, double increment){
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    protected void compute() {
        if(last - first < 10)
            updatePrices();
        else {
            int middle = (last+first)/2;
            System.out.println("Task:Pending tasks:"+getQueuedTaskCount());
            ProductTask t1 = new ProductTask(products,first,middle,increment);
            ProductTask t2 = new ProductTask(products,middle+1,last,increment);
            //执行主任务所创建的所有子任务
            invokeAll(t1,t2);
        }
    }

    public void updatePrices(){
        for(int i = first;i < last; i ++){
            Product product = products.get(i);
            product.setPrice(product.getPrice()*(1+increment));
        }
    }

    //生成随机产品列表
    static class ProductListGenerator{
        public List<Product> generate(int size){
            List<Product> ret = new ArrayList<Product>();
            for (int i = 0; i < size; i ++){
                Product product = new Product();
                product.setName("Product" + i);
                product.setPrice(10);
                ret.add(product);
            }
            return  ret;
        }
    }


    static class Product{
        private String name;
        private double price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    public static void main(String[] args) {
        ProductListGenerator generator = new ProductListGenerator();

        List<Product> products = generator.generate(10000);

        ProductTask task = new ProductTask(products, 0 ,products.size(), 0.20);

        ForkJoinPool pool = new ForkJoinPool();//创建ForkJoinPool对象，数量取决于CPU数目

        pool.execute(task);

        do{
            System.out.println("Main:Thread Count:"+pool.getActiveThreadCount());

            System.out.println("Main:Thread steal:"+pool.getStealCount());

            System.out.println("Main:Parallelism:"+pool.getParallelism());
            try {
                //每五秒输出线程池的一些参数值，与Thread.sleep功能一样，只不过可读性更好，存在于concurrent包中
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (!task.isDone());

        pool.shutdown();

        if(task.isCompletedAbnormally()){
            System.out.println("任务正常完成！！");
        }

    }
}
