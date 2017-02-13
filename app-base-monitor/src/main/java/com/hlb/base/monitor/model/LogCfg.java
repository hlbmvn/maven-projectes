package com.hlb.base.monitor.model;

public class LogCfg {
	//请求登记参数表达式
	private String context;
	//响应请求表达式
	private String rspMsg;
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getRspMsg() {
		return rspMsg;
	}
	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
}
