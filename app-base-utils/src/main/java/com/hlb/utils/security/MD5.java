package com.hlb.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用于做MD5的加密
 * */
public class MD5 {
	
	/**
	 * 将原始字符串进行以项目自身字符集相同的方式进行MD5加密
	 * @param orgStr --原始字符串
	 * @return String --加密后的字符串,报错将返回null
	 * */
	public static String encoder(String orgStr){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(orgStr.getBytes());
			StringBuffer sb = null;
			byte[] b = md.digest();
			sb = new StringBuffer();
			for(int i=0;i<b.length;i++){
				sb.append(
						Integer.toHexString(
								(0x000000ff & b[i]) | 0xffffff00).substring(6)
						);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("获取加密算法MD5失败");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将原始字符串进行指定字符集的MD5加密
	 * @param orgStr --原始字符串
	 * @param charset --字符集[UTF-8,GBK...]
	 * @return String --加密后的字符串,报错将返回null
	 * */
	public static String encoder(String orgStr,String charset){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(orgStr.getBytes(charset));
			StringBuffer sb = null;
			byte[] b = md.digest();
			sb = new StringBuffer();
			for(int i=0;i<b.length;i++){
				sb.append(
						Integer.toHexString(
								(0x000000ff & b[i]) | 0xffffff00).substring(6)
						);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e){
			System.out.println("不支持的字符集["+charset+"]");
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			System.out.println("获取加密算法MD5失败");
			e.printStackTrace();
		}
		return null;
	}
}
