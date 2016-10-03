package com.hlb.utils.ftp;

/**
 * FTP 的配置类
 * */
public class FtpConfig {
	
	public static final String MODEL_FTP = "FTP";
	public static final String MODEL_SFTP = "SFTP";
	
	private String ipAddr; 		//ftp IP 地址
	private int port;  		//端口,如果端口为空，则默认连接21端口
	private int timeout = 60*1000;
	private String username;//用户名
	private String password;//密码
	private String remoteDir;//自动cd的目录（当用户以此登录，需要自动跳转到某个目录时，进行配置）
	private String controlEncoding = "UTF-8"; //所有文件均以二进制形式传输
	private String model = MODEL_FTP;//FTP模式
	
	
	
	public FtpConfig(String ipAddr,int port,String username,String password) {
		this.ipAddr = ipAddr;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemoteDir() {
		return remoteDir;
	}
	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getControlEncoding() {
		return controlEncoding;
	}
	public void setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
