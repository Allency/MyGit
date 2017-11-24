package com.mobin.thread.Example;

/**
 * Created by Mobin on 2017/8/6.
 */
public class BigFileDownloaderMain {
    public static void main(String[] args) throws Exception {
        String url = "http://cp14-ccp2-2.play.bokecc.com/flvs/ca/Qx8K4/ueJ5KzHbQv-1.flv?t=1501990050&key=39F9FB64FF0AAE833879F3456A21B42F&upid=8980751501982850495&pt=0&pi=1";
        BigFileDownloader downloader = new BigFileDownloader(url);
        int workerThreadCount = 3;
        long reportInterval = 2;
        downloader.download(workerThreadCount, reportInterval);
    }
}
