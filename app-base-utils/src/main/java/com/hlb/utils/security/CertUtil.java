package com.hlb.utils.security;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

public class CertUtil {

	private static final String JKS = "JKS";
	private static final String P12 = "P12";
	private static final String PKCS12 = "PKCS12";
	private static final String JCEKS = "JCEKS";
	private static final String JCK = "JCK";
	private static final String PFX = "PFX";

	/**
	 *  获取信息摘要【获取信息的加密数据，如MD5等】
	 * @param textBytes 原信息
	 * @param algorithm 算法
	 * @return 返回该算法的信息摘要
	 * @throws Exception
	 */
	public static byte[] msgDigest(byte[] textBytes, String algorithm)
		throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.update(textBytes);
		return messageDigest.digest();
	}

	/**
	 * 通过证书获取公钥
	 * @param certPath 证书的路径
	 * @return 返回公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String certPath) throws Exception {
		InputStream streamCert = new FileInputStream(certPath);
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		Certificate cert = factory.generateCertificate(streamCert);
		return cert.getPublicKey();
	}
	/**
	 * 通过密钥文件或者证书文件获取私钥
	 * @param keyPath  密钥文件或者证书的路径
	 * @param passwd   密钥文件或者证书的密码
	 * @return 返回私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String keyPath, String passwd)
		throws Exception {

		String keySuffix = keyPath.substring(keyPath.indexOf(".") + 1);
		String keyType = JKS;
		if (keySuffix == null || keySuffix.trim().equals(""))
			keyType = JKS;
		else
			keySuffix = keySuffix.trim().toUpperCase();

		if (keySuffix.equals(P12)) {
			keyType = PKCS12;
		} else if (keySuffix.equals(PFX)) {
			keyType = PKCS12;
		} else if (keySuffix.equals(JCK)) {
			keyType = JCEKS;
		} else
			keyType = JKS;

		return getPrivateKey(keyPath, passwd, keyType);

	}

	/**
	 * 通过证书或者密钥文件获取私钥
	 * @param keyPath  证书或者密钥文件
	 * @param passwd   密钥保存密码
	 * @param keyType  密钥保存类型
	 * @return    返回私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(
		String keyPath,
		String passwd,
		String keyType)
		throws Exception {

		KeyStore ks = KeyStore.getInstance(keyType);
		char[] cPasswd=null;
		if(passwd!=null){
			cPasswd= passwd.toCharArray();
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(keyPath);		
//			if(cPasswd!=null){
				ks.load(fis, cPasswd);
			/*}else{
				ks.load();
			}*/
			fis.close();
		} finally {
			if (fis != null) {
				fis.close();
				fis = null;
			}
		}	
		@SuppressWarnings("rawtypes")
		Enumeration aliasenum = ks.aliases();
		String keyAlias = null;
		PrivateKey key = null;			
		while (aliasenum.hasMoreElements()) {
			keyAlias = (String) aliasenum.nextElement();
			key = (PrivateKey) ks.getKey(keyAlias, cPasswd);
			if (key != null)
				break;
		}		
		return key;
	}
	/**
	 * 	使用私钥签名 
	 * @param key 私钥
	 * @param b 需要签名的byte 数组
	 * @return 返回签名后的byte
	 * @throws Exception
	 */
	public static byte[] sign(PrivateKey priKey, byte[] b) throws Exception {
//		Signature sig = Signature.getInstance(priKey.getAlgorithm());
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initSign(priKey);
		sig.update(b);
		return sig.sign();
	}
	/**
	 * 	使用公钥验证 
	 * @param pubKey 公钥
	 * @param orgByte 原始数据byte 数组
	 * @param signaByte 签名后的数据byte 数组
	 * @return 是否验证结果
	 * @throws Exception
	 */
	public static boolean verify(
		PublicKey pubKey,
		byte[] orgByte,
		byte[] signaByte)
		throws Exception {
//		Signature sig = Signature.getInstance(pubKey.getAlgorithm());
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(pubKey);
		sig.update(orgByte);
		return sig.verify(signaByte);
	}
}
