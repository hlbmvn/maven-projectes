package com.hlb.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpTest {
	
	@Test 
    public void testget() {  
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet("http://bc.qbao.com/tuan/13192.html");
           // HttpGet httpget = new HttpGet("http://localhost:8080/Test/RequestServlet");
            httpget.addHeader("X-Forwarded-For", "111.160.6.7");
            httpget.addHeader("REMOTE_ADDR","192.168.6.7");
            httpget.addHeader("TTP_CLIENT_IP","192.168.6.7");
            httpget.addHeader("Referer","http://bc.qbao.com/tuanShare/13192/8A4DD3A0BEE83915A1AFF91FA6D53AA3.html");
           
           // https://www.baidu.com/s?wd=http&rsv_spt=1&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&inputT=3032&rsv_pq=e633eb88001122ff&rsv_t=31b5g7nsVppMs6yIqgRVIjM%2F3heC9YDtTSckikIZhwmF%2FlpQ0jyckHJzS2g6%2FGgdTx2f&rsv_sug3=8&rsv_sug1=5&rsv_sug2=0&rsv_sug4=3032&bs=httpClient
            System.out.println("executing request " + httpget.getURI());  
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    // 打印响应内容长度    
                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
                    System.out.println("Response content: " + EntityUtils.toString(entity));  
                }  
                System.out.println("------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
	public static void main(String[] args) throws Exception {
		
		
		
	}
	
}
