package com.smtsvs.thermopylae2.test;

import java.net.InetSocketAddress;

import com.smtsvs.thermopylae2.cmdpipe.AbstractCmdPipe;
import com.smtsvs.thermopylae2.cmdpipe.CmdPipeFactory;
import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.portforward.TcpPortForward;

public class ClientCmdPipeTest {

	public static void main(String[] args) {
	
		CmdPipeFactory factory = CmdPipeFactory.getInstance();
		factory.createClient("10.10.20.110", 80);
		AbstractCmdPipe client = factory.getClient();
		
		client.start();
		
		PortForwardCfg pfc = new PortForwardCfg(1,"10.10.20.110", 8080,8090);
		
		
		TcpPortForward tcpPortForward= new TcpPortForward(pfc,client);
		tcpPortForward.start();
	}

}
