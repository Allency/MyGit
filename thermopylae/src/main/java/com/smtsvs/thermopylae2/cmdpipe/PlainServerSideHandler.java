package com.smtsvs.thermopylae2.cmdpipe;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.smtsvs.thermopylae2.BaseIoHandlerAdapter;


/**
 * 最简单的handler，一般是面向服务端的.<br>
 * 收到什么就转发什么
 *
 */
public class PlainServerSideHandler extends  BaseIoHandlerAdapter {

	
	public PlainServerSideHandler() {

	}

	

	/**
     * 不能先开始读写， 等握完手再说.
     */
    @Override
    public void sessionCreated(final IoSession session) {
        session.suspendRead();
        session.suspendWrite();
    }
    
    public void sessionClosed(IoSession session) throws Exception {
        this.closeTheOther(session);
    }

    @Override
    public void
        messageReceived(final IoSession session, final Object message)
            throws Exception {
        if (((IoBuffer)message).remaining() == 0)
            return;
        this.write2TheOtherSide(session, ((IoBuffer)message).duplicate());
        Thread.yield();
    }
}
