package com.hlb.utils.ftp;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;

/**
 * FTP 的测试来
 * */
public class SFTPTest {
    
    
    //测试连接及关闭
    @Test
    public void test1(){
        String ipAddr = "192.16.22.68";
        int port = 22;
        String username = "wbiadmin";
        String password = "wbiadmin";
        FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
        cfg.setModel(FtpConfig.MODEL_SFTP);
        ChannelSftp sftp = FTPUtil.connectSFtp(cfg);
        Assert.assertNotNull(sftp);
        if(sftp!=null){
            boolean res = FTPUtil.closeSFtp(sftp);
            Assert.assertTrue(res);
        }
        cfg.setPassword("8888");
        sftp = FTPUtil.connectSFtp(cfg);
        Assert.assertNull(sftp);
        
    }
    
    //测试文件上传
    @Test
    public void test2() throws Exception{
        
        String ipAddr = "192.16.22.68";
        int port = 22;
        String username = "wbiadmin";
        String password = "wbiadmin";
        FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
        cfg.setModel(FtpConfig.MODEL_SFTP);
        cfg.setRemoteDir("temp");
        
        File file = new File("/Users/liupengbo/Documents/temp/logs/risk.log");
        FTPUtil.upload(cfg, file);
    }
    
    //测试文件夹上传
    @Test
    public void test3() throws Exception{
        String ipAddr = "192.16.22.68";
        int port = 22;
        String username = "wbiadmin";
        String password = "wbiadmin";
        FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
        cfg.setModel(FtpConfig.MODEL_SFTP);
        cfg.setRemoteDir("temp");
        FTPUtil.deleteFile(cfg, "*");
        File file = new File("/Users/liupengbo/Documents/temp/logs");
        FTPUtil.upload(cfg, file);
    }
    
    //测试文件下载
    @Test
    public void test4() throws Exception{
        String ipAddr = "192.16.22.68";
        int port = 22;
        String username = "wbiadmin";
        String password = "wbiadmin";
        FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
        cfg.setModel(FtpConfig.MODEL_SFTP);
        cfg.setRemoteDir("temp");
        File localFile = new File("/Users/liupengbo/Documents/temp/logs/riskA.log");
        String fileName = "ss.xx";
        FTPUtil.downloadFile(cfg, localFile, fileName, true);
    }
    
    //判断文件是否存在
    @Test
    public void test5() throws Exception{
        String ipAddr = "192.16.22.68";
        int port = 22;
        String username = "wbiadmin";
        String password = "wbiadmin";
        FtpConfig cfg = new FtpConfig(ipAddr,port,username,password);
        cfg.setModel(FtpConfig.MODEL_SFTP);
        cfg.setRemoteDir("temp");
        String fileName = "ss.xx";
        System.out.println(FTPUtil.exists(cfg, fileName));
    }
    
}
