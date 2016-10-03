package com.hlb.utils.file;

import java.io.File;

/**
 * @author hlb
 * */
public class FileUtil {
	
	/**
	 * 删除指定的文件
	 * @param file  --需要删除的文件
	 * @param force  --对于文件夹中含有文件是否强制删除该文件
	 * */
	public static void deleteFile(File file,boolean force){
		if(file.isDirectory()&&file.listFiles().length>0){
			if(force){
				deleteFile(file);
			}
		}else{
			file.delete();
		}
	}
	
	/**
	 * 强制删除文件夹
	 * */
	private static void deleteFile(File file){
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			File item=files[i];
			if(item.isDirectory()){
				deleteFile(item);
			}
			item.delete();
		}
	}
	
}
