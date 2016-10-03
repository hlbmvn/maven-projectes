package com.hlb.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hlb.utils.util.xml.DomUtil;

public class XmlParseTest {
	
	@Test
	public void testParseXml(){
		
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ebank><ENCODING>UTF-8</ENCODING><TXN_CODE>01</TXN_CODE><ORDER_NUM>0000001</ORDER_NUM><ORDERTIME>20150718123456</ORDERTIME><MER_CODE>000000000056</MER_CODE><ORDER_AMT>800</ORDER_AMT><CURRENCY>01</CURRENCY><OURL>网银支付回调地址</OURL><RURL>前台通知地址</RURL><GOODSNAME>桃子</GOODSNAME><GOODSID></GOODSID><GOODSNUM>10</GOODSNUM><REMARK></REMARK><IP>192.168.1.100</IP></ebank>";
		//将xml字符串变为 Document
		try {
			Document document = DomUtil.getDocumentByXmlString(xmlString);
			//获取根节点
			Element ele = DomUtil.getDocumentElement(document);
			//获取根节点后的所有子节点
			NodeList nodes = DomUtil.getSubElements(ele);
			int length = nodes.getLength();
			for(int i=0;i<length;i++){
				Node node = nodes.item(i);
				System.out.println(node.getNodeName()+":"+node.getTextContent());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
}
