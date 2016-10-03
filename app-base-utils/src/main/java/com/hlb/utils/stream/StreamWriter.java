package com.hlb.utils.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamWriter {
	
	/**
	 * 将一个输入流写入到输出流中
	 * */
	public static void streamWriteTo(InputStream in,OutputStream out) throws IOException{
		int len=0;
		byte[] temp=new byte[1024];
		while((len = in.read(temp))!=-1){
			out.write(temp, 0, len);
		}
		out.close();
		in.close();
	}
	
}
