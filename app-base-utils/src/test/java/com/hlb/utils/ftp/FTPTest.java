package com.hlb.utils.ftp;

import java.io.File;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Assert;
import org.junit.Test;

/**
 * FTP 的测试来
 * */
public class FTPTest {
	
	
	//测试连接及关闭
	@Test
	public void test1(){
		String ipAddr = "192.16.22.68";
		int port = 22;
		String username = "wbiadmin";
		String password = "wbiadmin";
		FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
		FTPClient ftp = FTPUtil.connectFtp(cfg);
		Assert.assertNotNull(ftp);
		if(ftp!=null){
			boolean res = FTPUtil.closeFtp(ftp);
			Assert.assertTrue(res);
		}
		cfg.setPassword("8888");
		ftp = FTPUtil.connectFtp(cfg);
		Assert.assertNull(ftp);
		
	}
	
	//测试文件上传
	@Test
	public void test2() throws Exception{
		
		String ipAddr = "192.168.22.68";
		int port = 22;
		String username = "wbiadmin";
		String password = "wbiadmin";
		FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
		File file = new File("/Users/liupengbo/Documents/temp/logs/risk.log");
		FTPUtil.upload(cfg, file);
	}
	
	//测试文件夹上传
	@Test
	public void test3() throws Exception{
		String ipAddr = "192.168.22.68";
		int port = 22;
		String username = "wbiadmin";
		String password = "wbiadmin";
		FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
		File file = new File("/Users/liupengbo/Documents/temp/logs");
		FTPUtil.upload(cfg, file);
	}
	
	//测试文件下载
	@Test
	public void test4() throws Exception{
		String ipAddr = "192.168.22.68";
		int port = 22;
		String username = "wbiadmin";
		String password = "wbiadmin";
		FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
		cfg.setRemoteDir("temp");
		File localFile = new File("/Users/liupengbo/Documents/temp/logs/riskA.log");
		String fileName = "risk.log";
		FTPUtil.downloadFile(cfg, localFile, fileName, true);
	}
	
	//测试文件夹下载
	@Test
	public void test5() throws Exception{
		String ipAddr = "192.168.22.68";
		int port = 22;
		String username = "wbiadmin";
		String password = "wbiadmin";
		FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
		cfg.setRemoteDir("temp");
		File localFile = new File("/Users/liupengbo/Documents/temp/log1");
		FTPUtil.downloadFile(cfg, localFile, null, true);
	}
}
