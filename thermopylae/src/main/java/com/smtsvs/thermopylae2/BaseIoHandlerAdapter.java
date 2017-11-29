package com.smtsvs.thermopylae2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.util.Util;

public class BaseIoHandlerAdapter extends IoHandlerAdapter{

	 private static final Logger LOGGER = LoggerFactory
		        .getLogger(BaseIoHandlerAdapter.class);
	 
	 /**
     * session的attribute里，放对方PipeName的Key.
     */
	public static final String KEY_SERVER_NAME = "serverName";
	public static final String KEY_IS_MAINSESSION = "isMainSession";
	public static final String KEY_SESSION_TEYP = "sessionType";
	public static final String VALUE_SESSION_TEYP_DIRECT = "direct";
	public static final String VALUE_SESSION_TEYP_REVERSE = "reverse";
	
    public static final String KEY_BUFFER = "buffer";
    public static final String KEY_NET_STEP = "negstep";
    public static final String KEY_THE_OTHER_SESSION = "theOtherSession";
    public static final int VALUE_NET_STEP_0 = 0;
    public static final int VALUE_NET_STEP_1 = 1;
    public static final int VALUE_NET_STEP_2 = 2;
    public static final int VALUE_NET_STEP_3 = 3;
    
    public static final int FREE_SESSION_SIZE = 5;
    
    
    
    /**
     * 写到好基友那边.
     *
     * @param session
     *        session
     * @param message
     *        要写的消息
     */
    protected void write2TheOtherSide(final IoSession session,
        final IoBuffer message) {
        final IoSession otherSession =
            (IoSession)session
                .getAttribute(KEY_THE_OTHER_SESSION);
        if (otherSession == null)
            throw new NullPointerException("找不到另一侧的session去写了！");
        //this.checkWriteThreshold(otherSession);
        otherSession.write(message);
    }

    /**
     * 我挂了，好基友要陪着一起挂.
     */
    protected void closeTheOther(final IoSession session) throws Exception {

        	LOGGER.debug("closeTheOther(session="
                + session + ") - start");
        final IoSession sSession =
            (IoSession)session
                .getAttribute(KEY_THE_OTHER_SESSION);
        if (sSession != null) {
            session.removeAttribute(KEY_THE_OTHER_SESSION);
            sSession.closeOnFlush();
         
        }
    }
    
    
    /**
     * 连接创建完成后，两个session互相引用.
     *
     */
    protected void set2session(final IoSession session,
        final IoSession targetSession) {
        targetSession.setAttribute(KEY_THE_OTHER_SESSION,
            session);
        session.setAttribute(KEY_THE_OTHER_SESSION,
            targetSession);
        LOGGER.info("[" + targetSession.getId() + "] && ["
            + session.getId() + "]");
        targetSession.resumeRead();
        targetSession.resumeWrite();
    }
    /**
     * 把握手包响应构造出来.
     */
    public static byte[] encodePSessionInfo(final PortForwardCfg req) {
        // 开头4个byte是
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            final ByteArrayOutputStream buf2 = new ByteArrayOutputStream();
            final ObjectOutputStream out = new ObjectOutputStream(buf2);
            out.writeObject(req);
            buf.write(Util.int2byte(buf2.size()));
            buf.write(buf2.toByteArray());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return buf.toByteArray();
    }
    
    
    /**
     * 把握手包请求构造出来.
     */
    public static PortForwardCfg decodePSessionInfo(final byte[] buf,
        final int length) throws Exception {
        final ByteArrayInputStream bIn =
            new ByteArrayInputStream(buf, 4, length);
        try {
            final ObjectInputStream oIn = new ObjectInputStream(bIn);
            return (PortForwardCfg)oIn.readObject();
        }
        catch (final Exception e) {
            throw e;
        }
    }
    
    
}
