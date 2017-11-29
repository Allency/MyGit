package com.smtsvs.thermopylae2.util;

import java.io.IOException;


/**
 * 可以restart的.<br>
 * 和AutoRestartListener配合，当异常停止时，自动重新启动.
 *
 * @author Jin Yuan
 */
public interface Restartable {

    /**
     * start.
     *
     * @throws IOException
     *         IOException
     */
    public void start() throws IOException;

    /**
     * dispose.
     */
    public void dispose();

    /**
     * 是否已经启动了.<br>
     * 如果是手动dispose的，要先设置start状态，这样就不会自动启动了.
     *
     * @return 是否已经启动了.
     */
    public boolean isStart();
}
