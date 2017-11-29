package com.smtsvs.thermopylae2.test;

import java.net.InetSocketAddress;

import com.smtsvs.thermopylae2.cmdpipe.AbstractCmdPipe;
import com.smtsvs.thermopylae2.cmdpipe.CmdPipeFactory;
import com.smtsvs.thermopylae2.cmdpipe.client.CmdPipeClient;
import com.smtsvs.thermopylae2.cmdpipe.server.CmdPipeServer;
import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.portforward.TcpPortForward;

public class SeverCmdPipeTest {

	public static void main(String[] args) {
	
		CmdPipeFactory factory = CmdPipeFactory.getInstance();
		factory.createServer("10.10.20.110", 80);
		AbstractCmdPipe server = factory.getServer();
		factory.createClient("10.10.20.110", 80);
		server.start();
	

		PortForwardCfg pfc = new PortForwardCfg(2,"10.10.20.110", 9080,9090);
		
		
		TcpPortForward tcpPortForward= new TcpPortForward(pfc,server);
		tcpPortForward.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
