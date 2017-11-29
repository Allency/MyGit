package com.smtsvs.thermopylae2.test;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.security.ntlm.Client;

/**
 * 测试用的.简单的服务端实现,输入什么回显什么.
 *
 * @author Jin Yuan
 */
public class TCPClientTest {

    private static final int PORT = 9080;

    private static final Logger LOGGER = LoggerFactory
        .getLogger(TCPClientTest.class);

    public static void main(final String[] args) throws IOException {

    	
    	int count=0;
    	int perCount=200;
    	while(count<=500){
    		
    		count++;
    		Task client = new Task("10.10.10.11",PORT);
    		client.setMsg("count="+count);
    		Thread thread = new Thread(client);
    		thread.start();
      
     
        try {
			Thread.sleep(200);
			//session.closeOnFlush(); 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
       

    }
  
}

