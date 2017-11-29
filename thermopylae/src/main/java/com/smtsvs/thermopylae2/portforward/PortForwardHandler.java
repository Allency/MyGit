package com.smtsvs.thermopylae2.portforward;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.BaseIoHandlerAdapter;
import com.smtsvs.thermopylae2.cmdpipe.AbstractCmdPipe;
import com.smtsvs.thermopylae2.cmdpipe.CmdPipe;
import com.smtsvs.thermopylae2.util.Util;


/**
 * 端口映射，面向客户端的Handler.
 *
 */
public class PortForwardHandler extends BaseIoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(PortForwardHandler.class);
   
    private PortForwardCfg cfg;
    
    private CmdPipe m_cmdPipe;
    /**
     * 启动状态，启动后为true，停止后为false.
     */
    private boolean m_start = false;

    /**
     * Constructs a PortForwardHandler.
     *
     * @param cfg
     *        端口映射的配置.
     */
    public PortForwardHandler(final PortForwardCfg cfg) {
      this.cfg = cfg;   
    }

   

    
    @Override
    public void sessionCreated(final IoSession session) {
        ConnectFuture connectFuture =this.createServerSide();
        if(connectFuture != null && connectFuture.getException() == null){
        	IoSession otherSession = connectFuture.getSession();
        	this.set2session(session, otherSession);
        }else{
        	session.closeNow();
        }
    }
    
    
    public void sessionClosed(IoSession session) throws Exception {
       this.closeTheOther(session);
    }
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
    		this.write2TheOtherSide(session, (IoBuffer) message);
    }

    
    @Override
    public void exceptionCaught(final IoSession session,
        final Throwable cause) throws Exception {
    	
    	IoSession mainSession =((AbstractCmdPipe)this.getCmdPipe()).getMainSession();
    	LOGGER.error("mainSession="+mainSession);
    	
        if (cause != null)
            LOGGER.error(
                "session [" + session + "] " + cause.getMessage(), cause);
        else
            LOGGER.error("session [" + session + "] ");
        final boolean isConnected = session.isConnected();
        session.closeOnFlush();
        if (!isConnected)
            // 如果session已经被close过了，就不会触发sessionClosed
            this.sessionClosed(session);
    }
    /**
     * 保存正在阻塞着等待对面应答的握手信息.key:srl.
     */
    private final ConcurrentHashMap<String, BlockingQueue<IoSession>> m_rspContainer =
        new ConcurrentHashMap<String, BlockingQueue<IoSession>>();
    /**
     * m_rspContainer里value的cache，省得不断new LinkedBlockingQueue了.<br>
     * 这样效率未必高，没时间实测了。
     */
    private final BlockingQueue<BlockingQueue<IoSession>> m_rspContainerCache =
        new LinkedBlockingQueue<BlockingQueue<IoSession>>(50) {

            private static final long serialVersionUID =-1L;

            @Override
            public boolean offer(final BlockingQueue<IoSession> e) {
                if (e == null)
                    return false;
                e.clear();
                return super.offer(e);
            }
        };

       


    protected ConnectFuture createServerSide() {
        try {

            final BlockingQueue<IoSession> container =
                this.createRspContainer();      
          
            IoSession session = this.getCmdPipe().getNewIOSession();
            PortForwardCfg portForwardCfg= new PortForwardCfg();
            portForwardCfg.setSessionType(this.cfg.getSessionType());
            portForwardCfg.setNegStep(VALUE_NET_STEP_1);
            portForwardCfg.setTargetIp(this.cfg.getTargetIp());
            portForwardCfg.setTargetPort(this.cfg.getTargetPort());
            portForwardCfg.setLocalPort(this.cfg.getLocalPort());
            portForwardCfg.setSrl(Util.buildSrl(portForwardCfg.getTopo()));
            session.write(IoBuffer.wrap(BaseIoHandlerAdapter.encodePSessionInfo(portForwardCfg)));
            session.resumeRead();
            session.resumeWrite();
            
            System.out.println("映射请求,portForwardCfg="+portForwardCfg);
            this.m_rspContainer.put(portForwardCfg.getSrl(), container);
            // 构造一个BlockingQueue，线程通讯用.等会我就等着CmdPipeHandler往里面放结果就好了
            try {
                final IoSession rstSession =
                    container.poll(30, TimeUnit.SECONDS);
                // 等着Handler2往里放结果.
                if (rstSession == null)
                    throw new IOException("没有连接到对方！");
                final DefaultConnectFuture cf = new DefaultConnectFuture();
                cf.setValue(rstSession);
                return cf;
            }
            finally {
                this.m_rspContainerCache.offer(this.m_rspContainer
                    .remove(portForwardCfg.getSrl()));
            }
        }
        catch (final Exception e) {
        	e.printStackTrace();
            return DefaultConnectFuture.newFailedFuture(e);
        }
    }
    
    
    /**
     * 收到对面的握手信息了，来握手一下.
     *
     */
    public void regSession2(final PortForwardCfg portForwardCfg,
        final IoSession session) {
    	System.out.println("映射回应,portForwardCfg="+portForwardCfg+",session="+session);
        final BlockingQueue<IoSession> container =
            this.m_rspContainer.get(portForwardCfg.getSrl());
        if (container != null)
            container.offer(session);
        else
            throw new NullPointerException("根据流水找不到客户端信息了！！");
    }

    /**
     * 从池子里捞一个Container，如果没有就搞个新的.
     */
    private BlockingQueue<IoSession> createRspContainer() {
        BlockingQueue<IoSession> rst = this.m_rspContainerCache.poll();
        if (rst == null)
            rst = new LinkedBlockingQueue<IoSession>();
        return rst;
    }


   

    
    /**
     * 关掉.
     */
    public void dispose() {
        synchronized (this) {
            if (!this.m_start)
                return;
            this.m_start = false;
            PortForwardHandler.LOGGER.info("dispose() - start");
          
            PortForwardHandler.LOGGER.info("dispose() - end");
        }
    }

	public PortForwardCfg getCfg() {
		return cfg;
	}

	public void setCfg(PortForwardCfg cfg) {
		this.cfg = cfg;
	}




	public CmdPipe getCmdPipe() {
		return m_cmdPipe;
	}




	public void setCmdPipe(CmdPipe m_cmdPipe) {
		this.m_cmdPipe = m_cmdPipe;
	}
    
	

}
