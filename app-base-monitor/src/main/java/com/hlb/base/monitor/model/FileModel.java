package com.hlb.base.monitor.model;

public class FileModel {
	
	private long time;
	private String fileName;
	private long total;
	private long usedCnt;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getUsedCnt() {
		return usedCnt;
	}
	public void setUsedCnt(long usedCnt) {
		this.usedCnt = usedCnt;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
