package com.smtsvs.thermopylae2.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试用的.简单的服务端实现,输入什么回显什么.
 *
 * @author Jin Yuan
 */
public class TCPServerTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TCPServerTest.class);

	public static void main(final String[] args) throws IOException {

		List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
		if(args != null && args.length>0){
			for(String s:args){
				addresses.add(new InetSocketAddress(Integer.valueOf(s)));
			}
		}else{
			addresses.add(new InetSocketAddress(8080));
		}

		final IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory()));

		ExecutorService threadPool = Executors.newFixedThreadPool(150);
		acceptor.getFilterChain().addLast("threadPool",
				new ExecutorFilter(threadPool));
		acceptor.setHandler(new IoHandlerAdapter() {

			@Override
			public void exceptionCaught(final IoSession session,
					final Throwable cause) throws Exception {
				TCPServerTest.LOGGER.warn(cause.getMessage(), cause);
			}

			@Override
			public void messageReceived(final IoSession session,
					final Object message) throws Exception {
				TCPServerTest.LOGGER.info("msg=" + message.toString());
				session.write(message.toString().toUpperCase());
			}

			public void sessionCreated(IoSession session) throws Exception {
				TCPServerTest.LOGGER.info("sessionCreated,session=" + session);
			}

			/**
			 * {@inheritDoc}
			 */
			public void sessionOpened(IoSession session) throws Exception {
				// Empty handler
				TCPServerTest.LOGGER.info("sessionOpened,session=" + session);
			}

			public void sessionClosed(IoSession session) throws Exception {
				TCPServerTest.LOGGER.info("sessionClosed,session=" + session);
			}

			/**
			 * {@inheritDoc}
			 */
			public void sessionIdle(IoSession session, IdleStatus status)
					throws Exception {
				TCPServerTest.LOGGER.info("sessionIdle,session=" + session
						+ ",status=" + status);
			}

		});
		acceptor.bind(addresses);
		TCPServerTest.LOGGER.info("启动服务：addresses=" + addresses);
	}
}
