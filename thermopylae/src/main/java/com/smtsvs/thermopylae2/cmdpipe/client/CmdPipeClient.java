package com.smtsvs.thermopylae2.cmdpipe.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.compression.CompressionFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.BaseIoHandlerAdapter;
import com.smtsvs.thermopylae2.cmdpipe.AbstractCmdPipe;
import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.util.Util;


/**
 * CmdPipe的客户端实现. <br>
 * 一个客户端可以保持跟多个服务端的连接，一个jvm公用一个client就好了
 *
 */
public class CmdPipeClient extends AbstractCmdPipe {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(CmdPipeClient.class);
    
    
    
    private InetSocketAddress serverInetSocketAddress;
    
    
    private Sweeper sweeper = new Sweeper(this);
       /**
     * 启动状态，启动后为true，停止后为false.
     */
    private boolean m_start = false;
    /**
     * 连接器，记得关啊.
     */
    private final SocketConnector m_connector = new NioSocketConnector();


    /**
     * 主要是初始化m_connector的参数.
     */
    {
        this.m_connector.getSessionConfig().setReadBufferSize(
            Util.DEFAULT_IO_BUF_SIZE);
        this.m_connector.setConnectTimeoutMillis(10 * 1000);
        this.m_connector.getFilterChain().addFirst("Compression",
            new CompressionFilter(CompressionFilter.COMPRESSION_MAX));
        

       
//        this.m_connector.getFilterChain().addLast("codec",
//            new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        this.m_connector.getSessionConfig().setBothIdleTime(0);
        this.m_connector.getSessionConfig().setTcpNoDelay(false);
        
        
        if (CmdPipeClient.LOGGER.isDebugEnabled()) // 打印日志用
        {   
        	Util.addLogFilter(this.m_connector);
        }
        ExecutorService threadPool =Executors.newFixedThreadPool(150);
        this.m_connector.getFilterChain().addLast("threadPool", new ExecutorFilter(threadPool));
        this.m_connector.setHandler(new CmdPipeHandlerC(this));
    }

    /**
     * 构造一个CmdPipeClient. <br>
     * 最好不要直接造，让CmdPipeFactory来造.
     *
     * @param cmdPipeCfg
     *        我的配置
     * @see com.smtsvs.thermopylae.cmdpipe.CmdPipeFactory
     */
    public CmdPipeClient(String ip,int port) {
    	
    	serverInetSocketAddress = new InetSocketAddress(ip, port);
    }

    @Override
    public void start() {
        synchronized (this) {
            if (this.m_start)
                return;
            this.m_start = true;
            CmdPipeClient.LOGGER.info("start() - start " + this);

            this.createMainSession();
            this.sweeper.start();

            CmdPipeClient.LOGGER.info("start() - end");
        }
    }
    
    
	public synchronized void createMainSession() {
		try {
			this.mainSession = this.createSession();
			LOGGER.info("=============createMainSession mainSession="+mainSession);
			this.mainSession.setAttribute(BaseIoHandlerAdapter.KEY_IS_MAINSESSION, Boolean.TRUE);
			PortForwardCfg portForwardCfg = new PortForwardCfg();
			portForwardCfg.setSessionType(PortForwardCfg.VALUE_SESSIONTYPE_0);
			portForwardCfg.setNegStep(BaseIoHandlerAdapter.VALUE_NET_STEP_3);
			portForwardCfg.setTargetIp(this.serverInetSocketAddress
					.getHostString());
			portForwardCfg
					.setTargetPort(this.serverInetSocketAddress.getPort());
			portForwardCfg.setSrl(Util.buildSrl(portForwardCfg.getTopo()));
			this.mainSession.write(IoBuffer.wrap(BaseIoHandlerAdapter
					.encodePSessionInfo(portForwardCfg)));
			this.mainSession.resumeRead();
			this.mainSession.resumeWrite();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	


    @Override
    public void dispose() {
        synchronized (this) {
            if (!this.m_start)
                return;
            this.m_start = false;
            CmdPipeClient.LOGGER.info("dispose() - start");
            super.dispose();
            this.m_connector.dispose(false);
            CmdPipeClient.LOGGER.info("dispose() - end");
        }
    }

  
    protected IoSession createSession() throws IOException {
        CmdPipeClient.LOGGER
            .info("createSession(InetSocketAddress) - targetAddr=" + serverInetSocketAddress);
        //synchronized (this.m_connector) {
          
            final ConnectFuture cf = this.m_connector.connect(serverInetSocketAddress);
            cf.awaitUninterruptibly();
            final Throwable e = cf.getException();
            if (e == null) {
                final IoSession session = cf.getSession();
                if (session != null) {
                	session.setAttribute(BaseIoHandlerAdapter.KEY_NET_STEP,0);
                    ///this.negotiation(rst);
                    CmdPipeClient.LOGGER
                        .debug("createSession() - end - return value=" + session);
                    return session;
                }
                else
                    throw new ConnectException("连不上" + serverInetSocketAddress);
            }
            else
                throw new IOException(e);
       // }
    }
    

    public IoSession getNewIOSession() {
		try {
			return this.createSession();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}




	
	
	
}
