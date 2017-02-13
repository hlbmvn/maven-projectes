package com.hlb.utils.util.zip;

import java.io.File;

import org.junit.Test;

public class ZipUtils {
	
	@Test
	public void test01(){
		File zipFie = new File("/Users/liupengbo/Documents/logs/test.zip");
		ZipUtil.doZip(new File[]{new File("/Users/liupengbo/logs/")}, zipFie); 
	}
	
}
