package com.hlb.utils.validate;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
	public static final String isEmail = "EMAIL";
	public static final String isIP = "IP_Address";
	
	/**
	 * 用于判断字符串是否满足给定的正则表达式
	 *
	 * */
	public static boolean match( String str,String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 用于判断字符串是否满足在properties中定义好的正则
	 * @param str -- 待判定的字符串
	 * @param regex -- 正则表达式key
	 * */
	public static boolean matchFromProperty( String str,String keyName) {
		try{
			ResourceBundle resourceBundle=ResourceBundle.getBundle("com/hlb/util/validate/regular");
			String regex= resourceBundle.getString(keyName);
			if(regex==null){
				return false;
			}
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			return matcher.matches();
		}catch(Exception e){
			System.out.println("读取配置文件失败");
			return false;
		}
	}
	
	
}
