package com.hlb.utils.urlConnection;

public class HttpUrlResponse {
	
	private String contentEncoding;
	private String contentType; 
	private long connectTime;
	private String htmlContent;
	
	public String getContentEncoding() {
		return contentEncoding;
	}
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getConnectTime() {
		return connectTime;
	}
	public void setConnectTime(long connectTime) {
		this.connectTime = connectTime;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
}
