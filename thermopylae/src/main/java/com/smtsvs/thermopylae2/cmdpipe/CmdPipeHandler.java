package com.smtsvs.thermopylae2.cmdpipe;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.BaseIoHandlerAdapter;
import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.util.Util;

/**
 * CmdPipe的Handler. <br>
 *
 */
public abstract class CmdPipeHandler extends BaseIoHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CmdPipeHandler.class);
	/**
	 * 使用我的那个CmdPipe.
	 */
	private final CmdPipe m_cmdPipe;

	protected final SocketConnector m_targetConnector = new NioSocketConnector();

	{
		this.m_targetConnector.getSessionConfig().setReadBufferSize(
				Util.DEFAULT_IO_BUF_SIZE);
		this.m_targetConnector.setConnectTimeoutMillis(10 * 1000);
		this.m_targetConnector.getSessionConfig().setBothIdleTime(0);
		this.m_targetConnector.getSessionConfig().setTcpNoDelay(false);
		this.m_targetConnector.setHandler(new PlainServerSideHandler());

	}

	public CmdPipeHandler() {
		this.m_cmdPipe = null;
	}

	/**
	 * Constructs a CmdPipeSrvHandler.
	 *
	 * @param cmdPipe
	 *            cmdPipe
	 */
	public CmdPipeHandler(final CmdPipe cmdPipe) {
		this.m_cmdPipe = cmdPipe;
	}

	@Override
	public void sessionCreated(final IoSession session) throws Exception {
		CmdPipeHandler.LOGGER.warn("sessionCreated(IoSession) - session="
				+ session);
		session.setAttribute(KEY_NET_STEP, 0);
		this.m_cmdPipe.addSession(session);
		session.resumeRead();
		session.resumeWrite();
	}

	@Override
	public void sessionClosed(final IoSession session) throws Exception {
		CmdPipeHandler.LOGGER.warn("sessionClosed(IoSession) - session="
				+ session);
		this.closeTheOther(session);
		this.m_cmdPipe.removeSession(session);
	}
	
	@Override
	public void exceptionCaught(final IoSession session, final Throwable cause)
			throws Exception {
		if (cause != null)
			CmdPipeHandler.LOGGER.error(
					"session [" + session + "] " + cause.getMessage(), cause);
		else
			CmdPipeHandler.LOGGER.error("session [" + session + "] ");
		final boolean isConnected = session.isConnected();
		session.closeOnFlush();
		if (!isConnected)
			// 如果session已经被close过了，就不会触发sessionClosed
			this.sessionClosed(session);
	}

	protected CmdPipe getCmdPipe() {
		return this.m_cmdPipe;
	}
	

    @Override
	public void messageReceived(final IoSession session, final Object message)
			throws Exception {
		IoBuffer ioBuffer = (IoBuffer) message;
		if (ioBuffer.remaining() == 0)
			return;
		super.messageReceived(session, message);
		Boolean isMainSession = (Boolean) session.getAttribute(BaseIoHandlerAdapter.KEY_IS_MAINSESSION,false);
		if(isMainSession){
			this.handlerMainSessionMessageReceived(session,ioBuffer);
		}else{
			
			if (VALUE_NET_STEP_3 == (Integer) session.getAttribute(KEY_NET_STEP, 0))
				// 握手完了就直接转发
				this.write2TheOtherSide(session, ioBuffer.duplicate());
			else {
				this.negotiation(session, ioBuffer);
			}
		}
		
		Thread.yield();


	}

	


	
	 /**
     * 握手.
     */
    private void negotiation(final IoSession session, final IoBuffer in)
        throws Exception {
        final byte[] buf = this.prepareBuf(session, in);
        if (buf.length < 4) {
            // 握手包的前4byte是包体的长度，不到4就不必处理了
            session.setAttribute(BaseIoHandlerAdapter.KEY_BUFFER, buf);
            return;
        }
        final int iLength = Util.byte2Int(ArrayUtils.subarray(buf, 0, 4));
        // 握手包长度
        if (buf.length < iLength + 4) {
            // 握手包没有收全，先不处理
            session.setAttribute(BaseIoHandlerAdapter.KEY_BUFFER, buf);
            return;
        }
        this.doNegotiation(session, buf, iLength);
        if (buf.length > iLength + 4)
            // 还有剩下的字节要发送
            this.writeRemain(session, buf, iLength+4);
    }
    
    /**
	 * 收到完整的握手包，真的要握手了.
	 */
	private void doNegotiation(final IoSession session, final byte[] buf,
			final int length) throws Exception {

		
		final PortForwardCfg portForwardCfg = BaseIoHandlerAdapter
				.decodePSessionInfo(buf, length);
		if (portForwardCfg != null) {

			if (portForwardCfg.getSessionType() == PortForwardCfg.VALUE_SESSIONTYPE_0) {
				IoSession tmp = ((AbstractCmdPipe) this.getCmdPipe())
						.getMainSession();
				synchronized (this) {
					if (tmp == null || !tmp.isConnected() || !tmp.isActive()) {
						((AbstractCmdPipe) this.getCmdPipe())
								.setMainSession(session);
						session.setAttribute(
								BaseIoHandlerAdapter.KEY_IS_MAINSESSION,
								Boolean.TRUE);
					}
				}
			} else {
				if (portForwardCfg.getNegStep() == VALUE_NET_STEP_1) {
					this.handlerConnReq(session, portForwardCfg);
				} else if (portForwardCfg.getNegStep() == VALUE_NET_STEP_2) {
					this.handlerConnResq(session, portForwardCfg);
				}

			}
			
			

		}

	}

    /**
     * 如果之前有读剩下的，拼在一起.
     */
    protected byte[] prepareBuf(final IoSession session, final IoBuffer in) {
        final byte[] prevBuf =
            (byte[])session.getAttribute(BaseIoHandlerAdapter.KEY_BUFFER);
        final byte[] thisBuf = new byte[in.remaining()];
        in.get(thisBuf);
        final byte[] buf =
            prevBuf != null ? ArrayUtils.addAll(prevBuf, thisBuf) : thisBuf;
        return buf;
    }

    /**
     * 握手完了，剩下的字节要发给好基友.
     */
    private void writeRemain(final IoSession session, final byte[] buf,
        final int iLength) {
        final byte[] writeBuf =
            ArrayUtils.subarray(buf, iLength, buf.length);
        this.write2TheOtherSide(session, IoBuffer.wrap(writeBuf));
    }
	


	/**
	 * 处理连接响庆
	 */
	private void handlerConnResq(IoSession session,PortForwardCfg portForwardCfg)  throws IOException {
		
		portForwardCfg.setNegStep(VALUE_NET_STEP_3);
		session.setAttribute(KEY_NET_STEP, VALUE_NET_STEP_3);
		this.getCmdPipe().NotifyTcpPortForward(portForwardCfg, session);
	}

	
	protected abstract void handlerMainSessionMessageReceived(final IoSession session, final IoBuffer ioBuffer) throws Exception;

	protected abstract void handlerConnReq(IoSession session,
			PortForwardCfg portForwardCfg)  throws Exception;

	protected IoSession createServerSideSession(final IoConnector connector,
			final InetSocketAddress addr) throws IOException {
		
			final ConnectFuture cf = connector.connect(addr);
			cf.awaitUninterruptibly();
			if (cf.getException() != null)
				throw new IOException(cf.getException());
			return cf.getSession();
		
	}

}
