package com.hlb.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hlb.utils.util.xml.DomUtil;

public class XmlParseTest2 {
	
	@Test
	public void testParseXml(){
		
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ebank><ENCODING>UTF-8</ENCODING><TXN_CODE>01</TXN_CODE><ORDER_NUM>0000001</ORDER_NUM><ORDERTIME>20150718123456</ORDERTIME><MER_CODE>000000000056</MER_CODE><ORDER_AMT>800</ORDER_AMT><CURRENCY>01</CURRENCY><OURL>网银支付回调地址</OURL><RURL>前台通知地址</RURL><GOODSNAME>桃子</GOODSNAME><GOODSID></GOODSID><GOODSNUM>10</GOODSNUM><REMARK></REMARK><IP>192.168.1.100</IP></ebank>";
		//将xml字符串变为 Document
		try {
			long start = System.currentTimeMillis();
			//for(int i=0;i<500000;i++){
				Document document = DomUtil.getDocumentByXmlString(xmlString);
			//}
			System.out.println("cost:"+500000/(System.currentTimeMillis()-start));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
