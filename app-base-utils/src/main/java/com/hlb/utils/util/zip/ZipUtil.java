package com.hlb.utils.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hlb.utils.file.FileUtil;
import com.hlb.utils.stream.StreamWriter;

/**
 * @author hlb
 * 该类用于文件的压缩和解压缩
 * */
public class ZipUtil {
	private static Logger log = LoggerFactory.getLogger(ZipUtil.class); 
	/**
	 * 将压缩文件集合放入到指定的zip文件中
	 * */
	public static void doZip(File[] src,File zipFile){
		OutputStream out;
		try {
			out = new FileOutputStream(zipFile);
			doZip(src, out);
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException-未找到压缩后的压缩路径",e);
		}
	}
	
	/**
	 * 将需要压缩文件放入到指定的输入流中
	 * */
	public static void doZip(File[] src,OutputStream out){
		if(src.length>0){
			int fileNum=src.length;
			ZipOutputStream zipOut=null;
		    zipOut = new ZipOutputStream(out);
		    for(int i=0;i<fileNum;i++){
				File file=src[i];
				doZip(file.getName(), file, zipOut);
			}
			try {
				zipOut.close();
			} catch (IOException e) {
				log.error("压缩文件流进行关闭的时候失败IO异常",e);
			}
		}
	}
	
	/**
	 * @param name 被压缩的文件的文件名称
	 * @param file 被压缩的文件
	 * @param zipOut 压缩后的文件
	 * */
	private static boolean doZip(String name,File file,ZipOutputStream zipOut){
		if(file.isDirectory()){
			File[] files =file.listFiles();
			log.debug("文件夹{}下存在多个文件，文件数为{}",name,files.length);
			if(files.length > 0){//存在文件
				for(int i=0;i<files.length;i++){
					File tempfile = files[i];
					doZip(name+File.separator+tempfile.getName(),tempfile,zipOut);
				}
			}else{//不存在文件的空文件夹
				try {
					zipOut.putNextEntry(new ZipEntry(name+"/"));
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			try {
				zipOut.putNextEntry(new ZipEntry(name));
				FileInputStream fileInputStream=new FileInputStream(file);
				int tempLen;
				byte[] temp=new byte[1024];
				while((tempLen = fileInputStream.read(temp))!=-1){
					zipOut.write(temp,0,tempLen);
				}
				fileInputStream.close();
				log.debug("对文件{}进行了压缩",name);
			} catch (IOException e) {
				log.error("文件{}进行压缩的报错",name,e);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 用于将文件进行解压
	 * @param zipFile 压缩的文件
	 * @param targetPath 文件解压后放置的位置
	 * */
	public static void unZip(ZipFile zipFile,String targetPath){
		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> entryEnum = (Enumeration<ZipEntry>) zipFile.entries();
		while(entryEnum.hasMoreElements()){
			ZipEntry entry = entryEnum.nextElement();
			getZipFile(zipFile,entry,targetPath);
		}
	}
	
	/**
	 * 对文件进行解压
	 * */
	private static void getZipFile(ZipFile zipFile,ZipEntry zipEntry,String targetPath){
		String fileName=zipEntry.getName();
		File file=new File(targetPath+File.separator+fileName);
		if(file.exists()){
			FileUtil.deleteFile(file, true);
		}
		if(zipEntry.isDirectory()){
			file.mkdirs();
		}else{//不是文件夹
			file.getParentFile().mkdirs();
			try {
				InputStream in = zipFile.getInputStream(zipEntry);
				OutputStream out = new FileOutputStream(file);
				StreamWriter.streamWriteTo(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
