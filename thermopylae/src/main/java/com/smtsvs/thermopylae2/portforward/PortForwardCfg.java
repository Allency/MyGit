package com.smtsvs.thermopylae2.portforward;

import java.io.Serializable;

/**
 * 端口映射配置.
 *
 */
public class PortForwardCfg implements Serializable {

    private static final long serialVersionUID = -1L;
    
    //mainSession
    public static final int VALUE_SESSIONTYPE_0 = 0;
    //directSession
    public static final int VALUE_SESSIONTYPE_1 = 1;
    //reverseSession
    public static final int VALUE_SESSIONTYPE_2 = 2;


    private int sessionType;
    /**
     * 流水号.
     */
	private String srl;
    /**
     * 目标IP.
     */
    private  String targetIp;
    /**
     * 目标Port.
     */
    private  int targetPort;
    
    
    /**
     * 本地ip.
     */
    private String localIp;
    /**
     * 本地Port.
     */
    private int localPort;
   
    
    private int negStep;


    
    
    
	public PortForwardCfg() {
		super();
	}

	
	

	public PortForwardCfg(int sessionType,String targetIp, int targetPort, int localPort) {
		super();
		this.sessionType = sessionType;
		this.targetIp = targetIp;
		this.targetPort = targetPort;
		this.localPort = localPort;
	}


    

	public int getSessionType() {
		return sessionType;
	}




	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}




	public String getSrl() {
		return srl;
	}


	public void setSrl(String srl) {
		this.srl = srl;
	}


	public String getTargetIp() {
		return targetIp;
	}


	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}


	public int getTargetPort() {
		return targetPort;
	}


	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}


	public String getLocalIp() {
		return localIp;
	}


	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}


	public int getLocalPort() {
		return localPort;
	}


	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}


	public int getNegStep() {
		return negStep;
	}


	public void setNegStep(int negStep) {
		this.negStep = negStep;
	}

	 public String getTopo(){
	    	
		    return (this.localIp==null ?"local:" : this.localIp) + this.localPort+">>"+this.targetIp+":"+this.targetPort;
		    	
	 }




	@Override
	public String toString() {
		return "PortForwardCfg [sessionType=" + sessionType + ", srl=" + srl
				+ ", targetIp=" + targetIp + ", targetPort=" + targetPort
				+ ", localIp=" + localIp + ", localPort=" + localPort
				+ ", negStep=" + negStep + "]";
	}



 
    

}
