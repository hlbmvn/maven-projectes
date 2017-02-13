package com.hlb.utils.number;

/**
 * 数字相关的校验
 */
public class NumberUtils {

	/**
	 * 判断一个是否是数字
	 */
	public static boolean isNumber(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断是否是正数
	 */
	public static boolean isInteger(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

}
