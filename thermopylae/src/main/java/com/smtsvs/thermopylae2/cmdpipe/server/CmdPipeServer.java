package com.smtsvs.thermopylae2.cmdpipe.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.compression.CompressionFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.cmdpipe.AbstractCmdPipe;
import com.smtsvs.thermopylae2.util.Restartable;
import com.smtsvs.thermopylae2.util.Util;

/**
 * CmdPipe的服务端实现.
 *
 */
public class CmdPipeServer extends AbstractCmdPipe implements Restartable {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(CmdPipeServer.class);

    /**
     * 服务端监听的ip. 解决多ip的问题
     */
    private final String m_ip;
    /**
     * 服务端监听的端口.
     */
    private final int m_port;
    /**
     * Listener. 记得关啊
     */
    private SocketAcceptor m_acceptor;
    /**
     * 启动状态，启动后为true，停止后为false.
     */
    private boolean m_start = false;


    /**
     * 构造一个CmdPipeServer. <br>
     * 最好不要直接造，让CmdPipeFactory来造.
     *
     * @param ip
     *        监听的ip
     * @param port
     *        监听的端口
     * @param cmdPipeCfg
     *        CmdPipeServer的基本配置
     */
    public CmdPipeServer(final String ip, final int port) {
        this.m_ip = ip;
        this.m_port = port;
    }

    @Override
    public void start(){
        synchronized (this) {
            if (this.m_start)
                return;
            this.m_start = true;
            CmdPipeServer.LOGGER.info("start() " + this);
            super.start();
            this.m_acceptor = new NioSocketAcceptor();
            this.initSessionCfg(this.m_acceptor);
            this.m_acceptor.getFilterChain().addFirst("Compression",
                new CompressionFilter(CompressionFilter.COMPRESSION_MAX));
          
            if (CmdPipeServer.LOGGER.isDebugEnabled()) // 打印日志用的，不调试就让他shut
            {
                Util.addLogFilter(this.m_acceptor);
            }
            ExecutorService threadPool =Executors.newFixedThreadPool(150);
            this.m_acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(threadPool));

            this.m_acceptor.setBacklog(100);
            this.m_acceptor.setHandler(new CmdPipeHandlerS(this));
            try {
				this.m_acceptor.bind(new InetSocketAddress(this.m_ip,
				    this.m_port));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            CmdPipeServer.LOGGER
                .info("CmdPipeServer on port " + this.m_port);
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (!this.m_start)
                return;
            this.m_start = false;
            CmdPipeServer.LOGGER.info("dispose() - start");
            super.dispose();
            this.m_acceptor.dispose(false);
            CmdPipeServer.LOGGER.info("dispose() - end");
        }
    }

    /**
     * 初始化m_acceptor的参数.
     */
    private void initSessionCfg(final SocketAcceptor acceptor) {
        //acceptor.addListener(new AutoRestartListener(this));
        final SocketSessionConfig sessionConfig =
            acceptor.getSessionConfig();
        sessionConfig.setReadBufferSize(Util.DEFAULT_IO_BUF_SIZE);
        sessionConfig.setSendBufferSize(Util.DEFAULT_IO_BUF_SIZE);
        sessionConfig.setBothIdleTime(0); // 长链接.
        sessionConfig.setKeepAlive(true);
        sessionConfig.setTcpNoDelay(true);
    }

    public boolean isStart() {
        return this.m_start;
    }
    
    public synchronized IoSession getNewIOSession() {
    	
    	if(this.mainSession == null)
    	{
    		throw new RuntimeException("CmdPipeServer.mainSession is null");
    	}else if(!this.mainSession.isActive() ){
    		throw new RuntimeException("CmdPipeServer.mainSession is not active");
    	}else if(!this.mainSession.isConnected()){
    		throw new RuntimeException("CmdPipeServer.mainSession is not connected");
    	}else{
    		LOGGER.error("mainSession="+mainSession);
    		return this.mainSession;
    	}
		
	}
    
    


}
