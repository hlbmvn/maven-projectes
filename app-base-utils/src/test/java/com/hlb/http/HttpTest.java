package com.hlb.http;

import java.util.HashMap;
import java.util.Map;

import com.hlb.utils.urlConnection.HttpUrlClient;
import com.hlb.utils.urlConnection.HttpUrlResponse;

public class HttpTest {
	
	public static void main(String[] args) throws Exception {
		int i=0;
		while(i<10000){
			String url = "http://localhost:8080/base.monitor/MonitorServlet?type=01";
			Map<String, String> map = new HashMap<String, String>();
			map.put("aaa", "bbb");
			HttpUrlResponse rsp = HttpUrlClient.doPost(url, "UTF-8", map);
			System.out.println(rsp.getHtmlContent());
			Thread.sleep(100);
			i++;
		}
	}
}
