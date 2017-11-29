package com.smtsvs.thermopylae2.cmdpipe;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.portforward.TcpPortForward;


/**
 * 命令通道.
 *
 */
public interface CmdPipe {

 

    /**
     * 启动、初始化.
     *
     * @throws IOException
     *         IOException
     */
    public void start();

    /**
     * 停.
     */
    public void dispose();
    
    
    public void addSession(IoSession session);
    
    
    public void removeSession(IoSession session);
    
    
    
    public IoSession getNewIOSession();
    
    public void  NotifyTcpPortForward(PortForwardCfg portForwardCfg, IoSession session);
    
    public void  addTcpPortForward(TcpPortForward tcpPortForward);


}
