package com.hlb.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.hlb.utils.util.xml.DomUtil;

public class DomUtilTest {
	
	@Test
	public void test01() throws Exception{
		String xmlString ="<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:esb=\"http://esb.lzccb.com\" xmlns:tns=\"http://ws.da.sunnysec.com/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><SOAP-ENV:Header><esb:RouteInformation><esb:SourceId>ZHGM</esb:SourceId><esb:GoalId>PPWS</esb:GoalId><esb:ServiceName>ZFMMHYService</esb:ServiceName><esb:OperationName>verifyZfmm</esb:OperationName><esb:AuthCode>ESB</esb:AuthCode><esb:RouteMsgId>111111111111111111111111111111111111111111111111</esb:RouteMsgId><esb:TimeOut>60</esb:TimeOut><esb:RelatesTo></esb:RelatesTo></esb:RouteInformation></SOAP-ENV:Header><SOAP-ENV:Body><tns:verifyZfmm><tns:TraceNo>000456789001234567890123654789654321</tns:TraceNo><tns:verifyZfmmReq><tns:accNo>2010900000165345</tns:accNo><tns:amount>2500</tns:amount><tns:billDate>20161222</tns:billDate><tns:billNo>1213</tns:billNo><tns:billType>5</tns:billType><tns:payCode>313</tns:payCode><tns:recvNo /></tns:verifyZfmmReq></tns:verifyZfmm></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		Document dom = DomUtil.getDocumentByXmlString(xmlString);
		Node node = DomUtil.getNodeByReg(dom.getDocumentElement(),"Body>verifyZfmm>verifyZfmmReq>accNo");
		System.out.println(node.getTextContent());
	}
	
	@Test
	public void test02() throws Exception{
		String xmlString ="<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:esb=\"http://esb.lzccb.com\" xmlns:tns=\"http://ws.da.sunnysec.com/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><SOAP-ENV:Header><esb:RouteInformation><esb:SourceId>ZHGM</esb:SourceId><esb:GoalId>PPWS</esb:GoalId><esb:ServiceName>ZFMMHYService</esb:ServiceName><esb:OperationName>verifyZfmm</esb:OperationName><esb:AuthCode>ESB</esb:AuthCode><esb:RouteMsgId>111111111111111111111111111111111111111111111111</esb:RouteMsgId><esb:TimeOut>60</esb:TimeOut><esb:RelatesTo></esb:RelatesTo></esb:RouteInformation></SOAP-ENV:Header><SOAP-ENV:Body><tns:verifyZfmm><tns:TraceNo>000456789001234567890123654789654321</tns:TraceNo><tns:verifyZfmmReq><tns:accNo>2010900000165345</tns:accNo><tns:amount>2500</tns:amount><tns:billDate>20161222</tns:billDate><tns:billNo>1213</tns:billNo><tns:billType>5</tns:billType><tns:payCode>313</tns:payCode><tns:recvNo /></tns:verifyZfmmReq></tns:verifyZfmm></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		Document dom = DomUtil.getDocumentByXmlString(xmlString);
		Map<String,String> nsMap = new HashMap<String,String>();
		nsMap.put("a", "http://schemas.xmlsoap.org/soap/envelope/");
		nsMap.put("aa", "http://esb.lzccb.com");
		nsMap.put("aaa", "http://ws.da.sunnysec.com/");
		Node node = DomUtil.getNodeByReg(dom.getDocumentElement(),"a:Body>aaa:verifyZfmm>aaa:verifyZfmmReq>aaa:accNo",nsMap);
		System.out.println(node.getTextContent());
	}
	
	@Test
	public void test03() throws Exception{
		
		Student stu = new Student();
		stu.setSname("sname");
		stu.setSex("man");
		stu.setSno("001");

		JAXBContext context = JAXBContext.newInstance(Student.class);
		
		Marshaller marshaller = context.createMarshaller();
		
		List<YW> yws =  new ArrayList<YW>();
		
		YW yw1 = new YW();
		yw1.setAa("aa");
		yw1.setBb("bb");
		
		yws.add(yw1);
		
		YW yw2 = new YW();
		yw2.setAa("11");
		yw2.setBb("22");
		
		yws.add(yw2);
		
		stu.setYws(yws);
		
		marshaller.marshal(stu, System.out);
		
	}
	
	@Test
	public void test04() throws Exception{
		
		String headerNodeUrl = "Body>reqt>appHead";
		String bodyNodeUrl = "Body>reqt>appBody";
		String svcHeaderNodeUrl = "Body>reqt>svcHead";

		long start = System.currentTimeMillis();
		String reqXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:reqt xmlns:ns=\"http://esb.xjrccb.com/1000200003\"><ns:svcHead><ns:SvcId>1000200003</ns:SvcId><ns:OptId>01</ns:OptId><ns:VerNo>1.0.0.0</ns:VerNo><ns:CsmId>20060</ns:CsmId><ns:CsmTms>2017-01-17T16:31:01.984</ns:CsmTms><ns:CsmSeqNo>12345</ns:CsmSeqNo></ns:svcHead><ns:extHead><ns:FileFlag>0</ns:FileFlag></ns:extHead><ns:appHead><ns:TranTlr>000534</ns:TranTlr><ns:TranBrc>8010100</ns:TranBrc><ns:TranCode>805004</ns:TranCode></ns:appHead><ns:appBody><ns:channelId>ATM</ns:channelId><ns:monitorId>3509eeb6-4b92-4e31-97b9-348e2a1d6fa5</ns:monitorId><ns:txnType>00</ns:txnType><ns:AcctNo>6212873000000035</ns:AcctNo><ns:Amt>2000</ns:Amt><ns:Brc>9089</ns:Brc><ns:LkmTelNo>18213028806</ns:LkmTelNo></ns:appBody></ns:reqt></soapenv:Body></soapenv:Envelope>";
		for(int i=0;i<1;i++){
			Document dom = DomUtil.getDocumentByXmlString(reqXml);
			Node headerNode = DomUtil.getNodeByReg(dom.getDocumentElement(), headerNodeUrl);
			Node svcHeaderNode = DomUtil.getNodeByReg(dom.getDocumentElement(),svcHeaderNodeUrl);
			Node bodyNode = DomUtil.getNodeByReg(dom.getDocumentElement(),bodyNodeUrl);
			if(headerNode==null || bodyNode==null || svcHeaderNode==null){
				System.err.println("失败。。。。。");
			}
			HashMap<String, String>  xmlMaps = new HashMap<String, String>();
			DomUtil.parseXmlToMap1("svcHead", svcHeaderNode.getChildNodes(), xmlMaps);
			DomUtil.parseXmlToMap1("", headerNode.getChildNodes(), xmlMaps);
			DomUtil.parseXmlToMap1("", bodyNode.getChildNodes(), xmlMaps);
			for(String key:xmlMaps.keySet()){
				System.out.println(key +" : "+xmlMaps.get(key));
			}
		}
		//System.out.println(1000000/(System.currentTimeMillis()-start));
	}
	
	
}
