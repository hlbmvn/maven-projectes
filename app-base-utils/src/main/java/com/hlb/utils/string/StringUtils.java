package com.hlb.utils.string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;

public class StringUtils {

	/**
	 * 判断一个字符串是否为 空 ""/null
	 * 
	 * @param str
	 *            判断的字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 获取一个被trim的值
	 */
	public static String getVal(Object obj) {
		if (obj == null || isEmpty(obj.toString())) {
			return "";
		} else {
			return obj.toString().trim();
		}
	}

	/**
	 * 获取一个被trim的值,如该值为null或者空字符，返回默认值
	 */
	public static String getVal(Object obj, String defaultVal) {
		if (obj == null) {
			return defaultVal;
		}
		return obj.toString().trim().equals("") ? defaultVal : obj.toString().trim();
	}
	
	/**
	 * 进行字符的填充
	 * */
	public static String fillString(String str, char fill, int len, boolean isEnd) {
		int fillLen = len - str.getBytes().length;
		if (len <= 0) {
			return str;
		}
		for (int i = 0; i < fillLen; i++) {
			if (isEnd)
				str = str + fill;
			else {
				str = fill + str;
			}
		}
		return str;
	}
	
	/**将Blob转化为String*/
	public static String getBlobToString(Blob blob,String charSet){
		String result ="";
		if(blob==null){
			return result;
		}
		try {
			InputStream msgContent = blob.getBinaryStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(msgContent,charSet));      
			StringBuilder sb = new StringBuilder();      
			String line = null;      
	         try {      
	             while ((line = reader.readLine()) != null) {      
	                  sb.append(line + "\n");      
	              }      
	          } catch (IOException e) {      
	              e.printStackTrace();      
	          } finally {      
	             try {      
	            	 msgContent.close();      
	             } catch (IOException e) {      
	                  e.printStackTrace();      
	              }      
	          }      
			result = sb.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
