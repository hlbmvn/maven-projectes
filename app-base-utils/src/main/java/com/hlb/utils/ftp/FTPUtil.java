package com.hlb.utils.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
/**
 * FTP 工具类
 * @author liupengbo
 * @daae 2016-09-29
 * @desc 通常而言ftp的应用场景经常是用于进行指定文件的下载和上传，
 * 因此我们仅仅需要关注上传和下载两个方法，配置一个FTPConfig对象，
 * 程序将自动连接－上传／下载－关闭连接 ，但存在连续上传、连续下载
 * 或其他FTP操作请根据具体业务场景获取FTP连接编写相关具体场景代码
 * 之后再关闭FTP连接.
 * */
public class FTPUtil {
	    
	    private static Logger log=LoggerFactory.getLogger(FTPUtil.class);
	    
	    /**
	     * 获取ftp连接
	     * @param cfg , ftp配置
	     * @return FTPClient，当存在异常时，返回 null
	     */
	    public static FTPClient connectFtp(FtpConfig cfg)  {
	    	log.debug("开始进行FTP的连接，IP地址：[{}]，端口：[{}]",new Object[]{cfg.getIpAddr(),cfg.getPort()});
	    	FTPClient ftp=new FTPClient();
	        int reply;
	        try{
		        if (cfg.getPort()==0) {
		            ftp.connect(cfg.getIpAddr(),21);
		        }else{
		            ftp.connect(cfg.getIpAddr(),cfg.getPort());
		        }
		        log.debug("连接FTP Success，进行登录授权");
		       
		        ftp.login(cfg.getUsername(), cfg.getPassword());
		        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

		        reply = ftp.getReplyCode();      
		        
		        if (!FTPReply.isPositiveCompletion(reply)) {      
		              ftp.disconnect();
		              log.error("连接FTP授权失败，用户名／密码错，IP地址：[{}]，端口：[{}],replyCode:[{}].",new Object[]{cfg.getIpAddr(),cfg.getPort(),reply});
		              return null;      
		        }
		        
		        if(cfg.getRemoteDir()==null){//没有目录，直接返回
		        	return ftp;
		        }else if(ftp.changeWorkingDirectory(cfg.getRemoteDir())){//存在目录进行跳转
		        	return ftp;
		        }else{//目录跳转失败
		        	log.error("FTP连接且授权成功，但定义的跳转目录失败，跳转目录为:[{}]",cfg.getRemoteDir());
		        	FTPUtil.closeFtp(ftp);
		        	return null;
		        }
		        
	        }catch (Exception e) {
	        	log.error("连接FTP失败，IP地址：[{}]，端口：[{}]",new Object[]{cfg.getIpAddr(),cfg.getPort()});
	        	log.error("异常信息为：",e);
	        	return null;
	        }
	    }
	    
	    /**
	     * 获取sftp连接
	     * @param cfg , ftp配置
	     * @return FTPClient，当存在异常时，返回 null
	     */
	    public static ChannelSftp connectSFtp(FtpConfig cfg)  {
	    	log.debug("开始进行SFTP的连接，IP地址：[{}]，端口：[{}]",new Object[]{cfg.getIpAddr(),cfg.getPort()});
	    	JSch jsch = new JSch();
			Session session = null;
			
	        try{
		        if (cfg.getPort()==0) {
		        	session = jsch.getSession(cfg.getUsername(), cfg.getIpAddr(), 22);
		        }else{
		        	session = jsch.getSession(cfg.getUsername(), cfg.getIpAddr(), cfg.getPort());
		        }
		        log.debug("连接SFTP Success，准备进行登录授权");
		        if(cfg.getPassword() != null){
		            session.setPassword(cfg.getPassword());
		        }
				Properties config = new Properties();
		        config.put("StrictHostKeyChecking", "no");
		        session.setConfig(config);
		        session.setTimeout(cfg.getTimeout());
		        session.connect();
		        ChannelSftp sftp = (ChannelSftp)session.openChannel("sftp");
		        sftp.connect();
		        sftp.setFilenameEncoding(cfg.getControlEncoding());
				log.debug("SFTP连接成功,进行文件夹跳转");
				if(cfg.getRemoteDir()!=null){//没有目录，直接返回
					sftp.cd(cfg.getRemoteDir());
				}
				return sftp;
	        }catch (Exception e) {
	        	log.error("连接SFTP失败，IP地址：[{}]，端口：[{}]",new Object[]{cfg.getIpAddr(),cfg.getPort()});
	        	log.error("异常信息为：",e);
	        	return null;
	        }
	    }
	    
	    
	    /**
	     * 连接FTP判断某个文件是否存在
	     * 目录在cfg中remoteAddr中指定
	     * */
	    public static boolean exists(FtpConfig cfg,String fileName){
	    	if(FtpConfig.MODEL_SFTP.equals(cfg.getModel())){
	    		ChannelSftp sftp = FTPUtil.connectSFtp(cfg);
	    		if(sftp==null){
	    			log.error("连接SFTP失败");
	    			return false;
	    		}
	    		try {
					@SuppressWarnings("unchecked")
					Vector<LsEntry> entrys = sftp.ls(".");
					for(LsEntry entry : entrys){
						if(entry.getFilename().equals(fileName)){
							return true;
						}
					}
				} catch (SftpException e) {
				
				}
	    	}else{
	    		FTPClient ftp = FTPUtil.connectFtp(cfg);
	    		if(ftp==null){
	    			log.error("连接ftp失败");
	    		}
	    		try {
					String[] fileNames = ftp.listNames();
					for(String fileStr:fileNames){
						if(fileStr.equals(fileName)){
							return true;
						}
					}
					
				} catch (IOException e) {
				}
	    	}
	    	return false;
	    }
	    
	    
	    
	    
	    /**
	     * 关闭ftp连接,一般而言不需要判断FTP是否真正关闭，如果需要请根据返回值进行判断
	     * 当不存在ftp、ftp已经关闭完毕或者ftp关闭成功均返回true
	     * 当ftp关闭失败返回为false
	     * @param FTPClient ftp -- 指定关闭的ftp
	     */
	    public static boolean closeFtp(FTPClient ftp){
	        if (ftp!=null && ftp.isConnected()) {
	            try {
	                ftp.logout();
	                ftp.disconnect();
	            } catch (IOException e) {
	                log.error("关闭指定FTP失败，",e);
	                return false;
	            }
	        }
	        return true;
	    }
	    
	    /**
	     * 关闭sftp连接,一般而言不需要判断SFTP是否真正关闭，如果需要请根据返回值进行判断
	     * 当不存在sftp、sftp已经关闭完毕或者sftp关闭成功均返回true
	     * 当ftp关闭失败返回为false
	     * @param FTPClient ftp -- 指定关闭的ftp
	     */
	    public static boolean closeSFtp(ChannelSftp sftp){
	        if (sftp!=null && sftp.isConnected()) {
	        	try {
					sftp.getSession().disconnect();
				} catch (JSchException e) {
					log.debug("获取SFTP Session失败");
				}
	            sftp.disconnect();
	        }
	        return true;
	    }
	    
	    
	    /**
	     * ftp文件上传
	     * @param cfg -- ftp配置信息
	     * @param file -- 上传文件或目录，当为文件时将文件上传，当为目录时将整个目录包含子目录均上传，上传到远程ftp的目录由cfg中的remoteDir指定
	     * @desc ：当发现任何一个文件上传失败，均抛出异常，但不对已经上传的文件进行清理，如存在该异常请根据具体业务逻辑依赖于deleteFile()配合使用
	     * @throws Exception
	     */
	    public static void upload(FtpConfig cfg,File file) throws Exception{
	    	if(FtpConfig.MODEL_SFTP.equals(cfg.getModel())){//进行的是SFTP模式的文件上传
	    		ChannelSftp sftp= FTPUtil.connectSFtp(cfg);
	    		if(sftp==null){
	    			new RuntimeException("SFTP 连接失败");
	    		}
	    		//进行文件上传
	    		if(file.isDirectory()){
	    			File[] files = file.listFiles();
	    			for(File fil:files){
	    			    uploadBySftp(sftp,fil);
	    			}
	    		}else{
	    			 uploadBySftp(sftp,file);
	    		}
    			
	    		FTPUtil.closeSFtp(sftp);
	    	}else{
		    	FTPClient ftp = FTPUtil.connectFtp(cfg);
		    	if(ftp==null){
		    		new RuntimeException("FTP 连接失败");
		    	}
		    	
		    	if(file.isDirectory()){
		    		for(File fil:file.listFiles()){
		    			upload(ftp,fil);
		    		}
		    	}else{
		    		upload(ftp,file);
		    	}
		    	
		    	FTPUtil.closeFtp(ftp);
	    	}
	    }
	    
	    private static void uploadBySftp(ChannelSftp sftp,File file) throws Exception{
	    	if(file.isDirectory()){
				sftp.mkdir(file.getName());
				sftp.cd(file.getName());
				for(File fil:file.listFiles()){
					uploadBySftp(sftp,fil);
				}
				sftp.cd("../");//回到上级目录
	    	}else{
		    	InputStream input = new FileInputStream(file);
				sftp.put(input,file.getName(),ChannelSftp.OVERWRITE);
				input.close();
	    	}
	    }
	    
	    
	    //文件上传
	    private static void upload(FTPClient ftp,File file) throws Exception{
	    	if (file.isDirectory()) {
	            ftp.makeDirectory(file.getName());
	            ftp.changeWorkingDirectory(file.getName());
	            File[] files = file.listFiles();
	            for(File uploadFile:files){
	            	//upload(ftp,uploadFile),迭代的衍生，不能这样写
	            	if(uploadFile.isDirectory()){
	            		upload(ftp,uploadFile);
	            		ftp.changeToParentDirectory();
	            	}else{
	                    FileInputStream input=new FileInputStream(uploadFile);
	                    ftp.storeFile(uploadFile.getName(),input);
	                    input.close();
	            	}
	            }
	        }else{//上传单个文件
	            FileInputStream input=new FileInputStream(file);
	            ftp.storeFile(file.getName(),input);
	            input.close();
	        }
	    }
	    
	    
	    /**
	     * FTP下载
	     * @param cfg FTP配置
	     * @param localFile 本地文件，当文件是目录的时候，将指定目录/文件放入到该目录
	     * @param fileName 远程文件的文件名称，当下载远程目录的指定文件时候上送，如果下载的是远程目录下所有文件，此值为null
	     * @param isForce 是否强制下载，即当发现本地指定目录存在时候会将本地覆盖
	     * @desc 远程目录的指定请在cfg中进行配置
	     * @throws Exception 下载失败将抛出异常
	     */
	    public static void downloadFile(FtpConfig cfg,File localFile,String fileName,boolean isForce) throws Exception{
	    	if(fileName==null && !localFile.isDirectory()){
	    		log.error("文件名为null,但指定存储不是一个目录，参数定义失败！");
	    		new RuntimeException("FTP下载参数异常！");
	    	}

	    	if(fileName!=null && localFile.isDirectory()){
	    		log.error("指定文件名，却未指定下的本地文件！");
	    		new RuntimeException("FTP下载参数异常！");
	    	}
	    	
	    	if(FtpConfig.MODEL_SFTP.equals(cfg.getModel())){//SFTP模式
	    		ChannelSftp sftp = FTPUtil.connectSFtp(cfg);
	    		if(fileName != null){//下载指定文件
	    			if(isForce && localFile.exists()){//本地文件存在，且属于强制下载
	    				localFile.delete();
	    			}else if(localFile.exists()){//本地文件存在，不需要强制下载
	    				return;
	    			}
	    			//下载指定文件
	    			OutputStream out = new FileOutputStream(localFile);
	    			sftp.get(fileName,out);
	    			log.debug("SFTP下载文件完成，文件名[{}]",fileName);
	    		}else{//下载文件夹
	    			new RuntimeException("SFTP not support");
	    		}
	    		
	    		FTPUtil.closeSFtp(sftp);
	    	}else{
	    	
		    	FTPClient ftp = FTPUtil.connectFtp(cfg);
		        
		    	if (ftp != null) {
		            try { 
		                FTPFile[] files = null; 
	                	if(cfg.getControlEncoding() != null){
	                		ftp.setControlEncoding(cfg.getControlEncoding());
	                	}
	                    if(fileName != null){//下载指定文件
	                    	if(localFile.exists() && !isForce){//文件已经存在且不强制下载将直接返回
	                    		log.debug("本地已经存在该文件，将不强制进行下载，文件为[{}]",localFile.getName());
	                    		return;
	                    	}else if(localFile.exists() ){//本地文件存在强制删除，再进行下载
	                    		localFile.delete();
	                    	}
	                    	OutputStream outputStream = new FileOutputStream(localFile);
	                    	ftp.retrieveFile(fileName, outputStream); 
	                        outputStream.flush(); 
	                        outputStream.close(); 
	                    }else{//将远程目录下所有文件夹
	                    	files = ftp.listFiles(); 
	                    	for (int i = 0; i < files.length; i++) { 
		                        try{ 
	                        		downloadFile(ftp,files[i], localFile,isForce); 
		                        }catch(Exception e){ 
		                            log.error("<"+files[i].getName()+">下载失败,异常为：",e); 
		                        } 
		                    }
	                    }
		            } catch (Exception e) { 
		            	log.error("下载过程中出现异常");
		            	 new RuntimeException(e);
		            } finally {
						FTPUtil.closeFtp(ftp);
					}
		        }else{
		            log.error("FTP链接失败！");
		            new RuntimeException("FTP链接失败！");
		        }
	    	}
	    }
	    
	    
	    /** 
	     * 下载FTP文件 
	     * 当你需要下载FTP文件的时候，调用此方法 
	     * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载 
	     * @param FTPClient ftp - ftp客户段
	     * @param ftpFile －ftp文件
	     * @param relativeLocalPath - 本地文件夹
	     * @param isForce － 是否强制下载
	     */ 
	    private  static void downloadFile(FTPClient ftp,FTPFile ftpFile,File relativeLocalPath,boolean isForce) { 
	        if (ftpFile.isFile()) {
	            if (ftpFile.getName().indexOf("?") == -1) { 
	                OutputStream outputStream = null; 
	                try { 
	                    File locaFile= new File(relativeLocalPath,ftpFile.getName()); 
	                    //判断文件是否存在，存在则返回 
	                    if(locaFile.exists() && !isForce){ 
	                        return;
	                    }else{
	                    	if(locaFile.exists()){
	                    		locaFile.delete();
	                    	}
	                        outputStream = new FileOutputStream(new File(relativeLocalPath,ftpFile.getName())); 
	                        ftp.retrieveFile(ftpFile.getName(), outputStream); 
	                        outputStream.flush(); 
	                    } 
	                } catch (Exception e) { 
	                    log.error("获取文件[{}]失败",ftpFile.getName(),e);
	                } finally { 
	                    try { 
	                        if (outputStream != null){ 
	                            outputStream.close(); 
	                        }
	                    } catch (IOException e) { 
	                       log.error("FTP输出文件流异常"); 
	                    } 
	                } 
	            } 
	        } else { 
	            File fl = new File(relativeLocalPath,ftpFile.getName()); 
	            if (!fl.exists()) { 
	                fl.mkdirs(); 
	            }
	            try { 
	                String currentWorkDir = ftpFile.getName().toString(); 
	                boolean changedir = ftp.changeWorkingDirectory(currentWorkDir); 
	                if (changedir) { 
	                    FTPFile[] files = null; 
	                    files = ftp.listFiles(); 
	                    for (int i = 0; i < files.length; i++) { 
	                        downloadFile(ftp,files[i],fl,isForce); 
	                    } 
	                    ftp.changeToParentDirectory();
	                }else{
	                	log.error("打开FTP文件目录失败，文件目录为[{}]",currentWorkDir);
	                } 
	            } catch (Exception e) { 
	                log.error("下载文件失败",e);
	            } 
	        } 
	    } 
	    
	    /**
	     * @desc 进行指定文件路径的文件删除，文件路径在cfg中配置
	     * @param cfg FTP配置
	     * @param fileName 文件名，当文件名为null时候，删除指定目录的所有文件目录（级联）
	     * @return int 返回删除结果，即删除的文件数目，当返回－1 代表失败，其余为文件数目
	     * */
	    public static int deleteFile(FtpConfig cfg,String fileName){
	    	if(FtpConfig.MODEL_SFTP.equals(cfg.getModel())){
	    		ChannelSftp sftp =  FTPUtil.connectSFtp(cfg);
	    		try {
					sftp.rm(fileName);
				} catch (SftpException e) {
					log.error("删除文件失败",e);
					return -1;
				}
	    		return 1;
	    	}else{
		    	//创建FTP连接
		    	FTPClient ftp = FTPUtil.connectFtp(cfg);
		    	if(ftp==null){
		    		log.error("连接FTP失败，无法删除文件");
		    		return -1;
		    	}
		    	//进行文件删除
		    	try {
		    		if(fileName!=null){
		    			ftp.deleteFile(fileName);
		    			return 1;
		    		}else{//删除目录
		    			int res = ftp.dele("/");
		    			log.info("共计删除[{}],个文件",res);
		    			return res;
		    		}
				} catch (IOException e) {
					log.error("删除FTP文件失败，原因为：",e);
				} finally {
					FTPUtil.closeFtp(ftp);
				}
		    	
		    	return -1;
		    }
	    }
	    
}
