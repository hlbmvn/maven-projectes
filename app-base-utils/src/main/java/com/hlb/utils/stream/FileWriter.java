package com.hlb.utils.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {
	/**
	 * 将指定的文件写入到另一个文件中
	 * */
	public static void writeTo(File source,File target){
		try {
			@SuppressWarnings("resource")
			FileInputStream in = new FileInputStream(target);
			@SuppressWarnings("resource")
			FileOutputStream out=new FileOutputStream(source);
			byte[] temp=new byte[1024];
			int len=0;
			while((len=in.read(temp))!=-1){
				out.write(temp, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
