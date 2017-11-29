package com.smtsvs.thermopylae2.cmdpipe.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.BaseIoHandlerAdapter;
import com.smtsvs.thermopylae2.cmdpipe.AbstractCmdPipe;
import com.smtsvs.thermopylae2.cmdpipe.CmdPipeHandler;
import com.smtsvs.thermopylae2.portforward.PortForwardCfg;

/**
 * CmdPipeHandler的服务端实现.
 *
 */
public class CmdPipeHandlerS extends CmdPipeHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CmdPipeHandlerS.class);

	/**
	 * Constructs a CmdPipeHandlerS.O
	 *
	 * @param cmdPipe
	 *            用到我的CmdPipeServer
	 */
	public CmdPipeHandlerS(final CmdPipeServer cmdPipe) {
		super(cmdPipe);
	}

	/**
	 * 处理连接请求
	 */
	@Override
	public void handlerConnReq(IoSession session, PortForwardCfg portForwardCfg)
			throws IOException {

		InetSocketAddress address = new InetSocketAddress(
				portForwardCfg.getTargetIp(), portForwardCfg.getTargetPort());

		IoSession otherSession = this.createServerSideSession(
				m_targetConnector, address);

		if (otherSession != null) {
			portForwardCfg.setNegStep(VALUE_NET_STEP_2);
			session.write(IoBuffer.wrap(BaseIoHandlerAdapter
					.encodePSessionInfo(portForwardCfg)));
			session.resumeWrite();

			this.set2session(session, otherSession);
			session.setAttribute(KEY_NET_STEP, VALUE_NET_STEP_3);
		} else {
			session.closeNow();
		}

	}

	@Override
	protected void handlerMainSessionMessageReceived(IoSession session,
			final IoBuffer ioBuffer) throws Exception {
		// 客户端目前不会向服务端发起命令，空实现

	}

}
