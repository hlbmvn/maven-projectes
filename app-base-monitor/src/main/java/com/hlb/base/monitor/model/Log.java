package com.hlb.base.monitor.model;

public class Log {
	
	private String type;// 日志类型 00-表示URL资源响应效率登记  01-表示资源日志登记
	private String content;//日志内容
	private String crtTime;//创建时间
	private String oprId;//操作员ID
	private int costTime;//耗时
	private String rspMsg;//返回消息，结果
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCrtTime() {
		return crtTime;
	}
	public void setCrtTime(String crtTime) {
		this.crtTime = crtTime;
	}
	public String getOprId() {
		return oprId;
	}
	public void setOprId(String oprId) {
		this.oprId = oprId;
	}
	public int getCostTime() {
		return costTime;
	}
	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}
	public String getRspMsg() {
		return rspMsg;
	}
	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
}
