package com.mobin.thread.Example;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Mobin on 2017/8/5.
 */
public class DownloadBuffer implements Closeable{
    //当前Buffer中缓冲的数据相对于整个存储文件的公交车偏移
    private long globalOffser;
    private long upperBound;
    private int offset = 0;
    public final ByteBuffer byteBuf;
    private final Storage storage;

    public DownloadBuffer(long globalOffser, long upperBound, Storage storage){
        this.globalOffser = globalOffser;
        this.upperBound = upperBound;
        this.byteBuf = ByteBuffer.allocateDirect(1024 * 1024);
        this.storage = storage;
    }

    public void write(ByteBuffer buf) throws IOException {
        int length = buf.position();
        final int capacity = byteBuf.capacity();
        //当前缓冲区已满
        if ((offset + length) > capacity || length ==capacity){
            flush();
        }
        byteBuf.position(offset);
        buf.flip();
        byteBuf.put(buf);
        offset += length;
    }

    private void flush() throws IOException {
        int length;
        byteBuf.flip();
        length = storage.store(globalOffser, byteBuf);
        byteBuf.clear();
        globalOffser += length;
        offset = 0;
    }

    @Override
    public void close() throws IOException {
        if (globalOffser < upperBound){
            flush();
        }
    }
}
