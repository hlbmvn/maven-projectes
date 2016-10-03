package com.hlb.utils.urlConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author hlb
 * @desc 该类用于后台向前台发送请求并且获得相关的响应
 * @date 2014年4月5日
 * */

public class HttpUrlClient {
	
	/**
	 * 用于仅仅是GET的发送方式
	 * */
	public static HttpUrlResponse doGet(String url,String encoding,Map<String,String> parameters ){
		return send(url,"GET",encoding,parameters);
	}

	/**
	 * 用于仅仅是POST的发送方式
	 * */
	public static HttpUrlResponse doPost(String url,String encoding,Map<String ,String > parameters){
		return send(url,"POST",encoding,parameters);
	}
	
	
	private static HttpUrlResponse send(String url,String method,String encoding,Map<String,String> parameters){
		HttpUrlResponse response= null;
			URL ur=null;
			URLConnection connection=null;
			try {
				String condition=Map2String(parameters);
				/*采取POST的方式进行数据的请求*/
				if("POST".equalsIgnoreCase(method)){
					ur=new URL(url);
					connection=ur.openConnection();
					connection.setDoOutput(true);
					connection.setUseCaches(false);
					OutputStream out = connection.getOutputStream();
					out.write(condition.getBytes());
				}

				if("GET".equalsIgnoreCase(method)){
					ur=new URL(url+"?"+condition);
					connection=ur.openConnection();
				}
				
				/**
				 * 忽略https请求的证书
				 * */
				 if ("https".equalsIgnoreCase(ur.getProtocol())) {
				      HttpsURLConnection husn = (HttpsURLConnection)connection;
				      husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
				      husn.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());
				  }
				long connectTime = connection.getConnectTimeout();
				String contentEncoding = connection.getContentEncoding();
				String connectType = connection.getContentType();
				BufferedReader content=new BufferedReader(new InputStreamReader(connection.getInputStream(),encoding));
				StringBuffer html=new StringBuffer();
				String line="";
				while((line=content.readLine())!=null){
					html.append(line);
				}
				
				response=new HttpUrlResponse();
				response.setConnectTime(connectTime);
				response.setContentEncoding(contentEncoding);
				response.setContentType(connectType);
				response.setHtmlContent(html.toString());
			} catch (IOException e) {
				System.out.println("链接url["+url+"]时失败");
				e.printStackTrace();
			}
		return response;
	}
	
	public static String Map2String( Map<String ,String > map){
		StringBuffer temp=new StringBuffer();
		Set<String> keys=map.keySet();
		Iterator<String> its=keys.iterator();
		while(its.hasNext()){
			String key=its.next();
			String value = map.get(key);
			temp.append(key+"="+value+"&");
		}
		return temp.deleteCharAt(temp.length()-1).toString();
	}
}
