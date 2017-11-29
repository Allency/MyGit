package com.smtsvs.thermopylae2.cmdpipe;

import com.smtsvs.thermopylae2.cmdpipe.client.CmdPipeClient;
import com.smtsvs.thermopylae2.cmdpipe.server.CmdPipeServer;

/**
 * 构造CmdPipe的工厂.<br>
 * 1、client可以维护多个和server的连接，所以client一个就够了.<br>
 * 2、server根据需要，同样name、port的只能起一个，否则就乱了.<br>
 * 所以，还是不要让开发随便new CmdPipe了，由Factory统一构造、维护.
 *
 * @author Jin Yuan
 */
public class CmdPipeFactory {

    /**
     * 单例模式的实例.
     */
    private static final CmdPipeFactory INSTANCE = new CmdPipeFactory();

    private CmdPipeServer m_server= null;
    /**
     * 维护CmdPipeClient的实例. CmdPipeClient可以一个jvm公用一个
     */
    private CmdPipeClient m_client = null;

    /**
     * 单例，不给其他人构造我.
     */
    private CmdPipeFactory() {
    	
    	  
    }

    /**
     * 得到实例.
     *
     * @return CmdPipeFactory实例.
     */
    public static CmdPipeFactory getInstance() {
        return CmdPipeFactory.INSTANCE;
    }

    

    public CmdPipeServer createServer(final String ip, final int port) {
        if (this.m_server != null)
            return this.m_server;
        synchronized (this) {
            if (this.m_server == null)
                this.m_server = new CmdPipeServer(ip,port);
            return this.m_server;
        }
    }
    
    
    public CmdPipeServer getServer() {
       return this.m_server;
    }

    public CmdPipeClient getClient() {
        return this.m_client;
    }


    public CmdPipeClient createClient(final String ip, final int port) {
        if (this.m_client != null)
            return this.m_client;
        synchronized (this) {
            if (this.m_client == null)
                this.m_client = new CmdPipeClient(ip,port);
            return this.m_client;
        }
    }

    public void removeClient() {
        synchronized (this) {
            if (this.m_client != null) {
                this.m_client.dispose();
                this.m_client = null;
            }
        }
    }
    
    
    public void removeServer() {
        synchronized (this) {
            if (this.m_server != null) {
                this.m_server.dispose();
                this.m_server = null;
            }
        }
    }
}
