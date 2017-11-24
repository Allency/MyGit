package com.mobin.thread.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by MOBIN on 2016/7/26.
 * 注意：
 * 1.可以并行处理
 *
 * 2.任务处理代码必须是线程安全的，因为有多个任务并以地调用它
 * 缺点:
 * 1.线程生命周期的开销大
 * 2.资源消耗量大
 * 3.稳定性受限
 * 在一定范围内可以提高系统的吞吐量，一旦超出该范围创建更多的线程只会百害无一利
 */
public class ThreadPerWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while(true){
            final Socket connetcion = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                     //执行任务
                }
            };
            new Thread(task).start();
        }
    }
}

