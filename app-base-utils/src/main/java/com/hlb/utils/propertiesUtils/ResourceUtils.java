package com.hlb.utils.propertiesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceUtils {
	
private static Properties roperties;
	
	static{
		InputStream in = ResourceUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
		roperties = new Properties();
		try {
			roperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getParamByKey(String key){
		return roperties.getProperty(key);
	}
	
	public static void loadPropertiesByClassPath(String fileNmae){
		InputStream in = ResourceUtils.class.getResourceAsStream(fileNmae);
		roperties = new Properties();
		try {
			roperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
