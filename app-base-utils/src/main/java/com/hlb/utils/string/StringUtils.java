package com.hlb.utils.string;

public class StringUtils {
	
	/**
	 * 判断一个字符串是否为 空 ""/null
	 * @param str 判断的字符串
	 * @return boolean
	 * */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取一个被trim的值
	 * */
	public static String getVal(Object obj){
		if(obj==null || isEmpty(obj.toString())){
			return "";
		}else{
			return obj.toString().trim();
		}
	}
	
	
}
