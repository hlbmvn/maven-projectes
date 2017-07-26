package com.hlb.utils.socket;

public class SocketCfg {
	private String ip;
	private int port;
	private String encoding = "UTF-8";
	private int timeOut = 6000;
	private int bufLen = 1024;
	
	public SocketCfg(String ip,int port) {
		this.ip = ip;
		this.port = port;
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
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public int getBufLen() {
		return bufLen;
	}
	public void setBufLen(int bufLen) {
		this.bufLen = bufLen;
	}
}
