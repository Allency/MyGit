package com.smtsvs.thermopylae2.cmdpipe.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtsvs.thermopylae2.BaseIoHandlerAdapter;
import com.smtsvs.thermopylae2.cmdpipe.CmdPipeHandler;
import com.smtsvs.thermopylae2.portforward.PortForwardCfg;
import com.smtsvs.thermopylae2.util.Util;


/**
 * CmdPipeHandler客户端的实现.
 *
 */
public class CmdPipeHandlerC extends CmdPipeHandler {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(CmdPipeHandlerC.class);

    /**
     * Constructs a CmdPipeHandlerC.
     *
     * @param cmdPipe
     *        引用我的cmdPipe
     */
    public CmdPipeHandlerC(final CmdPipeClient cmdPipe) {
        super(cmdPipe);
    }


    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        super.sessionClosed(session);
       
        Boolean flag = (Boolean) session.getAttribute(BaseIoHandlerAdapter.KEY_IS_MAINSESSION,Boolean.FALSE);
        if(flag){
        	((CmdPipeClient)this.getCmdPipe()).createMainSession();
        }
    }
    

	@Override
	protected void handlerConnReq(IoSession session,
			PortForwardCfg portForwardCfg) throws IOException {

		//向serverCmpPipe 生成一个session,
		IoSession session1 = this.getCmdPipe().getNewIOSession();
		LOGGER.info("映射回应,portForwardCfg="+portForwardCfg+",mianSession="+session+",session="+session1);

		//连接目标
		InetSocketAddress address = new InetSocketAddress(
				portForwardCfg.getTargetIp(),
				portForwardCfg.getTargetPort());
		IoSession otherSession = this.createServerSideSession(
				m_targetConnector, address);
		
		if(session1 != null && otherSession != null){
			session1.setAttribute(KEY_NET_STEP, VALUE_NET_STEP_3);
			portForwardCfg.setNegStep(VALUE_NET_STEP_2);
			session1.write(IoBuffer.wrap(BaseIoHandlerAdapter
					.encodePSessionInfo(portForwardCfg)));
			session.resumeWrite();
			this.set2session(session1, otherSession);
		}
	
		
		
	}



	@Override
	protected void handlerMainSessionMessageReceived(IoSession session,
			final IoBuffer ioBuffer) throws Exception {
		 //此messsage 可能不只是一个连接请求的数据
		 final byte[] buf = this.prepareBuf(session, ioBuffer);
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
	        PortForwardCfg portForwardCfg = BaseIoHandlerAdapter
					.decodePSessionInfo(buf, iLength);
	        if(portForwardCfg != null){
	        	try{
	        	   //此错必须处理，不能影响 mainSession
	        	   this.handlerConnReq(session, portForwardCfg);
	        	}catch(Exception e){
	        		LOGGER.error("handlerConnReq error="+e.getMessage(),e);
	        	}
	        }
	        if (buf.length > iLength + 4)
	            // 还有剩下的字节要拼成连接请求
	        {
	        	
	        	System.out.println("====================有多个请求===========，buf.length="+buf.length);
	        	 final byte[] writeBuf =
	             ArrayUtils.subarray(buf, iLength+4, buf.length);
	        	  session.setAttribute(BaseIoHandlerAdapter.KEY_BUFFER, writeBuf);
	        	  return;
	        }
		
	}





    
    
    
 

}
