package com.smtsvs.thermopylae2.portforward;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.cmdpipe.CmdPipe;
import com.smtsvs.thermopylae2.util.Restartable;
import com.smtsvs.thermopylae2.util.Util;


/**
 * 端口映射，业务逻辑见包说明.
 *
 */
public class TcpPortForward implements Restartable {

    /**
     * session的attr里，引用好基友的那个key.
     */
    public static final String KEY_THE_OTHER_SESSION = "TheOtherSession";

    /**
     * tcp传输buffer的大小.
     */
    public static final int IO_BUF_SIZE = Util.DEFAULT_IO_BUF_SIZE;

    private static final Logger LOGGER = LoggerFactory
        .getLogger(TcpPortForward.class);

    /**
     * 端口映射配置.
     */
    private final PortForwardCfg m_cfg;
    
    private final CmdPipe m_cmdPipe;
    
    private  PortForwardHandler pFSrcHandler;
    /**
     * 给客户端的listener.
     */
    private SocketAcceptor m_acceptor;
    /**
     * 启动状态.
     */
    private boolean m_start = false;

 
    /**
     * Constructs a TcpPortForward.
     *
     * @param cfg
     *        端口映射配置.
     */
    public TcpPortForward(final PortForwardCfg cfg,final CmdPipe cmdPipe) {
        TcpPortForward.LOGGER.info("TcpPortForward(PortForwardCfg,CmdPipe) , cfg="
            + cfg+",cmdPipe="+cmdPipe);
        this.m_cfg = cfg;
        this.m_cmdPipe = cmdPipe;
    }
    
    

    
    public void start() {
        TcpPortForward.LOGGER.debug("startServer() - start " + this);
        synchronized (this) {
            if (this.m_start)
                return;
            this.m_start = true;
            this.m_acceptor = new NioSocketAcceptor();
            this.initSessionCfg(this.m_acceptor);
            if (TcpPortForward.LOGGER.isDebugEnabled()) // 打印日志用的
                Util.addLogFilter(this.m_acceptor);
            this.m_acceptor.setBacklog(100);
            
            
            final PortForwardHandler handler = new PortForwardHandler(this.m_cfg);
            handler.setCmdPipe(this.m_cmdPipe);
            this.pFSrcHandler = handler;
            this.m_cmdPipe.addTcpPortForward(this);
            this.m_acceptor.setHandler(handler);
            try {
				this.m_acceptor.bind(new InetSocketAddress(this.m_cfg
				    .getLocalPort()));
	            TcpPortForward.LOGGER.info("Server on " +this.m_cfg.getTopo());            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        
        }
    }


    public void dispose() {
        synchronized (this) {
            if (!this.m_start)
                return;
            this.m_start = false;
            TcpPortForward.LOGGER.info("dispose() - start");
            this.m_acceptor.dispose(false);
            ((PortForwardHandler)this.m_acceptor.getHandler()).dispose();
            TcpPortForward.LOGGER.info("dispose() - end" + this.m_cfg);
        }
    }

    /**
     * 增加一个监听，例如服务停的时候，可以有事件.<br>
     * 因为acceptor是start之后才赋值的，所以这个方法应该在start之后调用.所以serviceActivated没用
     *
     * @param listener
     *        listener
     */
    public void addListener(final IoServiceListener listener) {
        if (listener != null && this.m_acceptor != null)
            this.m_acceptor.addListener(listener);
    }

    
    public PortForwardHandler getpFSrcHandler() {
		return pFSrcHandler;
	}
	/**
     * 初始化配置.
     */
    private void initSessionCfg(final SocketAcceptor acceptor) {
       // acceptor.addListener(new AutoRestartListener(this));
        final SocketSessionConfig sessionConfig =
            acceptor.getSessionConfig();
        sessionConfig.setReadBufferSize(TcpPortForward.IO_BUF_SIZE);
        // acceptor.getSessionConfig().setReadBufferSize(2);
        sessionConfig.setSendBufferSize(TcpPortForward.IO_BUF_SIZE);
        sessionConfig.setBothIdleTime(0);
        sessionConfig.setKeepAlive(true);
        sessionConfig.setTcpNoDelay(true);
    }



    public boolean isStart() {
        return this.m_start;
    }
    
    
    

    public PortForwardCfg getM_cfg() {
		return m_cfg;
	}




	public CmdPipe getM_cmdPipe() {
		return m_cmdPipe;
	}




	@Override
    public String toString() {
        return this.getClass().getSimpleName() + "."
            + Integer.toHexString(System.identityHashCode(this))
            + " [m_start=" + this.m_start + ", m_cfg=" + this.m_cfg + "]";
    }
}
