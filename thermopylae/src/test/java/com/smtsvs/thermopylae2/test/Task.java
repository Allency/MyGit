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

/**
 * 测试用的.简单的服务端实现,输入什么回显什么.
 *
 * @author Jin Yuan
 */
public class Task implements Runnable {

	private String ip = "10.10.20.110";
	private int port = 9090;

	private String msg="request";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Task.class);

	public Task() {
	}

	public Task(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void run() {
		final NioSocketConnector connector = new NioSocketConnector();
		  //connector.getSessionConfig().setTcpNoDelay(false);
		connector.setHandler(new IoHandlerAdapter() {
			@Override
			public void exceptionCaught(final IoSession session,
					final Throwable cause) throws Exception {
				cause.printStackTrace();
			}

			@Override
			public void messageSent(IoSession session, Object message)
					throws Exception {
				// TODO Auto-generated method stub
				super.messageSent(session, message);
				System.out.println("请求：" + message.toString());
			}

			@Override
			public void messageReceived(final IoSession session,
					final Object message) throws Exception {
				System.out.println("响应：" + message.toString());
				session.closeOnFlush();
				connector.dispose();
			}

			public void sessionCreated(IoSession session) throws Exception {
				Task.LOGGER.info("sessionCreated,session=" + session);
			}

			/**
			 * {@inheritDoc}
			 */
			public void sessionOpened(IoSession session) throws Exception {
				// Empty handler
				Task.LOGGER.info("sessionOpened,session=" + session);
			}

			public void sessionClosed(IoSession session) throws Exception {
				Task.LOGGER.info("sessionClosed,session=" + session);
			}

			/**
			 * {@inheritDoc}
			 */
			public void sessionIdle(IoSession session, IdleStatus status)
					throws Exception {
				Task.LOGGER.info("sessionIdle,session=" + session
						+ ",status=" + status);
			}

		});
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory()));

		ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));

		cf.awaitUninterruptibly();

		IoSession session = cf.getSession();

		String ss = Thread.currentThread().getId() + ","+this.getMsg();

		session.write(ss.toString());

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
