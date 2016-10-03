package com.hlb.test.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.hlb.utils.security.CertUtil;


public class TestCer {
	
	public static void main(String[] args) {
		String cer ="/Users/liupengbo/Documents/temp/shangfu0106.der";//证书公钥路径
		String pfc ="/Users/liupengbo/Documents/temp/shangfu0106.pfx";//证书私钥路径
		String sign = "123";
		// 获取私钥
		
		try {
			PrivateKey pk = CertUtil.getPrivateKey(pfc, "");
			byte[] temp = CertUtil.sign(pk, sign.getBytes());
			PublicKey pubKey = CertUtil.getPublicKey(cer);
			//System.out.println(CertUtil.verify(pubKey, sign, .));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
