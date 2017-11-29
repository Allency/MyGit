package com.smtsvs.thermopylae2.cmdpipe;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.portforward.TcpPortForward;


/**
 * CmdPipe的抽象类，大部分的CmdPipe逻辑在这里.
 *
 */
public abstract class AbstractCmdPipe implements CmdPipe {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(AbstractCmdPipe.class);

    
    protected IoSession mainSession;
    
    protected ConcurrentMap<String,TcpPortForward> pTcpPortForwardMap = new ConcurrentHashMap<String,TcpPortForward>();

 
    /**
     * 启动状态.
     */
    private boolean m_start = false;


    /**
     * 保持所有连接. <br>
     * key是远端ip、port，注意，未必是客户端ip、port，可能过了网闸，那么就是网闸的信息。
     */
    private final ConcurrentHashMap<InetSocketAddress, IoSession> m_sessionMap =
        new ConcurrentHashMap<InetSocketAddress, IoSession>();


    /**
     * Constructs a AbstractCmdPipe.
     *
     * @param cmdPipeCfg
     *        配置
     */
    public AbstractCmdPipe() {
        AbstractCmdPipe.LOGGER.info("AbstractCmdPipe() - start");
    }

    
    public void start() {
        synchronized (this) {
            if (this.m_start)
                return;
            this.m_start = true;
            AbstractCmdPipe.LOGGER.info("start() - start");
            AbstractCmdPipe.LOGGER.info("start() - end");
        }
    }

    public void dispose() {
        synchronized (this) {
            if (!this.m_start)
                return;
            this.m_start = false;
            AbstractCmdPipe.LOGGER.info("dispose() - start");
            this.m_sessionMap.clear();
          
        }
    }
    

    public void addSession(IoSession session) {

	}

   
    
    

	

	public void removeSession(IoSession session) {
		
	}

	

	


	
	


	public IoSession getMainSession() {
		return mainSession;
	}


	public void setMainSession(IoSession mainSession) {
		this.mainSession = mainSession;
	}


	public void NotifyTcpPortForward(PortForwardCfg portForwardCfg,
			IoSession session) {
		String key = portForwardCfg.getTopo();
		System.out.println("NotifyTcpPortForward,key="+key);
		TcpPortForward tcpPortForward = this.pTcpPortForwardMap.get(key);
		
		tcpPortForward.getpFSrcHandler().regSession2(portForwardCfg, session);
		
	}


	public void addTcpPortForward(TcpPortForward tcpPortForward) {
		String key = tcpPortForward.getM_cfg().getTopo();
		TcpPortForward temp = this.pTcpPortForwardMap.get(key);
		System.out.println("addTcpPortForward,key="+key);
		if(temp != null){
			throw new RuntimeException("此端口映射已存在");
		}
		this.pTcpPortForwardMap.put(key, tcpPortForward);
		
	}

}
