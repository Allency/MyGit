package com.smtsvs.thermopylae2.cmdpipe.client;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 *
 */
public class Sweeper implements Runnable {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(Sweeper.class);
    /**
     * 使用我的那个CmdPipe.
     */
    private final CmdPipeClient m_pipe;
    /**
     * 启动状态.
     */
    private boolean m_run = true;

    /**
     * Constructs a Sweeper.
     *
     * @param pipe
     *        用到我的那个CmdPipe
     */
    public Sweeper(final CmdPipeClient pipe) {
        this.m_pipe = pipe;
    }

    /**
     * 停.
     */
    public void stop() {
        this.m_run = false;
    }

    /**
     * 状态复位，其实没启动，要重新做个Thread来跑.
     */
    public void start() {
        this.m_run = true;
        Thread thread = new Thread(this);
        thread.start();
    }


    public void run() {
        while (this.m_run) {
            try {
            	LOGGER.info("sweep.run start");
            	//每隔20秒检查mainSessin是否是活的
                Thread.sleep(20000);
            }
            catch (final InterruptedException e) {
                Sweeper.LOGGER.error(e.getMessage(), e);
            }
            IoSession session = this.m_pipe.getMainSession();
            if(session == null || !session.isActive() || !session.isConnected()){
            	 this.m_pipe.createMainSession();
            }else{
            	LOGGER.info("sweep.run  mainSesson is ok");
            }
           
        }
        Sweeper.LOGGER.info("Sweeper run end");
    }

    
}
