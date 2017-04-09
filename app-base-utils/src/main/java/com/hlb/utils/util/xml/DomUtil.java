package com.hlb.utils.util.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.hlb.utils.string.StringUtils;
import com.hlb.utils.util.xml.bean.MyNodeList;

public class DomUtil {

	public static short Element = 1;
	public static short Attribute = 2;
	public static short Text = 3;
	public static short CDATA = 4;
	public static short Entity_Reference = 5;
	public static short Entity = 6;
	public static short Processing_Instrucion = 7;
	public static short Comment = 8;
	public static short Document = 9;
	public static short Document_Type = 10;
	public static short Document_Fragment = 11;
	public static short Notation = 12;
	public static DocumentBuilder docbuilder;

	static {
		DocumentBuilderFactory docbuilderfactory = DocumentBuilderFactory.newInstance();
		docbuilderfactory.setNamespaceAware(true);
		try {
			docbuilder = docbuilderfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文档的根节点
	 * 
	 * @param doc
	 *            w3c 文档
	 * @return element --文档根节点
	 */
	public static Element getDocumentElement(Document doc) {
		return doc.getDocumentElement();
	}

	/**
	 * 获取根元素内第一个元素的相关属性，不存在则返回空
	 * 
	 * @param doc
	 *            w3c文档
	 * @param attrName
	 *            --文档属性
	 */
	public static String getAttr0(Document doc, String attrName) throws Exception {
		try {
			Element element = doc.getDocumentElement();
			NodeList nodeList = element.getChildNodes();
			NamedNodeMap attrNodes = nodeList.item(0).getAttributes();
			if (attrNodes == null || attrNodes.getLength() <= 0) {
				return "";
			}
			Node attrNode = attrNodes.getNamedItem(attrName);
			if (attrNode == null) {
				return "";
			}
			return attrNode.getNodeValue();
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 将一个格式良好的xml字符串转化成一个w3c的dom对象,解析失败则抛出异常
	 * 
	 * @param xmlString
	 *            --格式良好的xml字符串
	 * @return Document --w3c Dom对象,解析失败则抛出异常
	 */
	public static synchronized Document getDocumentByXmlString(String xmlString) throws Exception {
		return docbuilder.parse(new InputSource(new StringReader(xmlString)));
	}

	/**
	 * 将dom对象转化成xmlString字符串
	 * 
	 * @param doc
	 *            w3c Document对象
	 * @return String xml的字符串[无格式的]
	 */
	public static String serializeXML(Document doc) throws Exception {
		String xmlString = null;
		try {
			DOMSource doms = new DOMSource(doc);
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(doms, sr);
			sw.close();
			xmlString = sw.getBuffer().toString();
		} catch (Exception e) {
			throw e;
		}
		return xmlString;
	}

	/**
	 * 创建一个w3c document
	 * 
	 * @return --Document
	 */
	public static Document getDocument() throws Exception {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		return doc;
	}

	/**
	 * 创建一个叫 ${elementName}的Element，并将其放入到document中
	 * 
	 * @return Element --返回刚刚创建的Element
	 */
	public static Element addElement(Document document, String elementName) throws Exception {
		Element element = document.createElement(elementName);
		document.appendChild(element);
		return element;
	}

	/**
	 * 将指定的Element元素加入到Document中
	 */
	public static void addElement(Document document, Element element) throws Exception {
		document.appendChild(element);
	}

	/**
	 * 向Element1中加入Element2
	 */
	public static void addElement(Element element1, Element element2) throws Exception {
		element1.appendChild(element2);
	}

	/**
	 * 对Element元素设定一个属性值
	 * 
	 * @param element
	 *            --设定属性的元素
	 * @param attrName
	 *            --属性name
	 * @param attrValue
	 *            --属性value
	 */
	public static void setAttr(Element element, String attrName, String attrValue) throws Exception {
		element.setAttribute(attrName, attrValue);
	}

	/**
	 * 对给定的元素设定纯文本
	 * 
	 * @param element
	 *            --给定的元素
	 * @param plainText
	 *            --纯文本
	 */
	public static void setElementText(Element element, String plainText) {
		element.setTextContent(plainText);
	}

	/**
	 * document 创建一个元素并返回
	 * 
	 * @param document
	 * @param tagName
	 *            元素名称
	 */
	public static Element createElement(Document document, String tagName) {
		return document.createElement(tagName);
	}

	/**
	 * 获取文档中指定元素名称的<b>子孙【children】</b>元素
	 * 
	 * @param document
	 *            --w3c Document
	 * @param tagname
	 *            --元素名称
	 * @return NodeList --元素的集合
	 */
	public static NodeList getElementsByTagName(Document document, String tagname) {
		NodeList list = document.getElementsByTagName(tagname);
		return list;
	}

	/**
	 * 获得指定元素且指定名称的<b>子孙【children】</b>元素，当不存在的时候返回null
	 * 
	 * @param element
	 *            --指定元素
	 * @param tagname
	 *            --要获得的元素的名称
	 */
	public static NodeList getElementsByTagName(Element element, String tagname) {
		return element.getElementsByTagName(tagname);
	}

	/**
	 * 获取文档中指定元素名称的<b>子【son】</b>元素
	 * 
	 * @param document
	 *            --w3c Document
	 * @param tagname
	 *            --元素名称
	 * @return NodeList --元素的集合
	 */
	public static NodeList getSubElementsByTagName(Document document, String tagname) {
		NodeList list = document.getChildNodes();
		return getNodeListByTagname(list, tagname);
	}

	/**
	 * 获取文档中的<b>子【son】</b>元素
	 * 
	 * @param document
	 *            --w3c Document
	 * @return NodeList --元素的集合
	 */
	public static NodeList getSubElements(Document document) {
		return document.getChildNodes();
	}

	/**
	 * 获得指定元素且指定名称的<b>子【son】</b>元素，当不存在的时候返回null
	 * 
	 * @param element
	 *            --指定元素
	 * @param tagname
	 *            --要获得的元素的名称
	 */
	public static NodeList getSubElementsByTagName(Element element, String tagname) {
		NodeList nodeList = element.getChildNodes();
		return getNodeListByTagname(nodeList, tagname);
	}

	/**
	 * 获得指定元素的<b>子【son】</b>元素
	 * 
	 * @param element，指定的元素
	 */
	public static NodeList getSubElements(Element element) {
		return element.getChildNodes();
	}

	/**
	 * 获取元素中的内容
	 * 
	 * @param element
	 *            --给定的元素
	 * @return String --元素的内容
	 */
	public static String getTextContent(Element element) {
		return element.getTextContent();
	}

	/**
	 * 获取元素的属性值
	 * 
	 * @param element
	 *            --指定元素
	 * @param attrName
	 *            --属性名称
	 * @return String --属性值
	 **/
	public static String getAttrByElement(Element element, String attrName) {
		return element.getAttribute(attrName);
	}

	/**
	 * 获取指定元素名称的元素集合
	 */
	private static NodeList getNodeListByTagname(NodeList nodeList, String tagname) {
		LinkedList<Node> list = new LinkedList<Node>();
		// 过滤
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (tagname.equals(node.getNodeName())) {
				list.add(node);
			}
		}
		return new MyNodeList(list);
	}

	/**
	 * 解析一个XML报文为Map对象
	 */
	public static Map<String, String> parseXmlToMap(String xml) {
		try {
			Document document = getDocumentByXmlString(xml);
			Map<String, String> result = new HashMap<String, String>();
			parseXmlToMap("", document.getDocumentElement().getChildNodes(), result);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	private static void parseXmlToMap(String prefix, NodeList nodes, Map<String, String> maps) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.hasChildNodes()) {
				if ("".equals(prefix)) {
					parseXmlToMap(node.getNodeName(), node.getChildNodes(), maps);
				} else {
					parseXmlToMap(prefix + "_" + node.getNodeName(), node.getChildNodes(), maps);
				}
			} else {
				maps.put(prefix, node.getNodeValue());
			}
		}
	}

	public static void parseXmlToMap1(String prefix, NodeList nodes, Map<String, String> maps) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String nodeName = node.getNodeName();
			nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
			short nodeType = node.getNodeType();
			if (nodeType == DomUtil.Text || nodeType == DomUtil.CDATA || nodeType == DomUtil.Comment) {
				continue;
			}
			if (StringUtils.isEmpty(prefix)) {
				maps.put(nodeName, node.getTextContent());
			} else {
				maps.put(prefix + "_" + nodeName, node.getTextContent());
			}
		}
	}

	/**
	 * 根据节点表达式获取节点信息（模糊搜寻） 当存在多个时，返回第一个，当不存在时候，返回null
	 */
	public static Node getNodeByReg(Node parNode, String reg) {
		String[] eles = reg.split(">");
		int len = eles.length;
		Node nextNode = parNode;
		for (int i = 0; i < len; i++) {
			nextNode = getNextNode(nextNode, eles[i]);
		}
		return nextNode;
	}

	/**
	 * 根据节点表达式找寻指定命名空间下的元素（敏感寻找） 当存在多个时，返回第一个，当不存在时候，返回null
	 */
	public static Node getNodeByReg(Node parNode, String reg, Map<String, String> xmlNs) {
		String[] eles = reg.split(">");
		int len = eles.length;
		Node nextNode = parNode;
		for (int i = 0; i < len; i++) {
			String nsFix = "";
			String nodeName = eles[i];
			if (eles[i].indexOf(":") != -1) {
				nsFix = eles[i].substring(0, eles[i].indexOf(":"));
				nsFix = xmlNs.get(nsFix);
				nodeName = eles[i].substring(eles[i].indexOf(":") + 1);
			}
			nextNode = getNextNode(nextNode, nodeName, nsFix);
		}
		return nextNode;
	}

	/**
	 * 根据节点表达式获取满足条件的所有节点信息（模糊搜寻） 当不存在时候，返回空的list
	 */
	public List<Node> getNodesByReg(Node parNode, String reg) {
		return null;
	}

	/**
	 * 根据节点表达式找寻指定命名空间下满足条件的所有元素（敏感寻找） 当不存在时候，返回空的list
	 */
	public List<Node> getNodesByReg(Node parNode, String reg, Map<String, String> xmlNs) {
		return null;
	}

	/**
	 * 将指定节点的XML节点信息转化为特定对象
	 */
	public void XmlTransferObj(Node node, Class<? extends Object> obj) {

	}

	/**
	 * 获取下一个节点
	 * 
	 * @param node
	 * @param nodeName
	 * @return
	 */
	private static Node getNextNode(Node node, String nodeName) {
		if (node != null && node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			String childNodeName;
			short nodeType;
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				nodeType = childNode.getNodeType();
				if (nodeType == DomUtil.Text || nodeType == DomUtil.CDATA || nodeType == DomUtil.Comment) {
					continue;
				}
				childNodeName = childNode.getNodeName();
				childNodeName = childNodeName.substring(childNodeName.indexOf(":") + 1);
				if (childNodeName.equals(nodeName) || ("*".equals(nodeName) && childNode.hasChildNodes())) {
					return childNode;
				}
			}
		}
		return null;
	}

	/**
	 * 获取下一个感知命名空间的节点
	 * 
	 * @param node
	 * @param nodeName
	 * @return
	 */
	private static Node getNextNode(Node node, String nodeName, String NsStr) {
		if (node != null && node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				short nodeType = childNode.getNodeType();
				if (nodeType == DomUtil.Text || nodeType == DomUtil.CDATA || nodeType == DomUtil.Comment) {
					continue;
				}
				String childNodeName = childNode.getNodeName();
				String localNs = childNode.getNamespaceURI();
				if ((localNs == null && NsStr.equals("")) || localNs.equals(NsStr) || "*".equals(NsStr)) {
					childNodeName = childNodeName.substring(childNodeName.indexOf(":") + 1);
					if (childNodeName.equals(nodeName) || ("*".equals(nodeName) && childNode.hasChildNodes())) {
						return childNode;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 解析一个结点获取子节点，并转化为MAP
	 * */
	public static void parseNodeToMap(Node node, String prefix, Map<String, String> mapStore) {
		if (prefix == null) {
			prefix = "";
		}
		if (node.hasChildNodes()) {
			parseXmlToMap1(prefix, node.getChildNodes(), mapStore);
		}
	}
	
	/**
	 * 在A节点下找B
	 * */
	public static String getNodeValue (String nodeUrls,Node node,Map<String,String> nameSpace){
		int index = nodeUrls.indexOf(">");
		String urls = nodeUrls;
		if(index!=-1){
			urls = nodeUrls.substring(0,nodeUrls.indexOf(">"));
		}

		if(node.hasChildNodes()){//存在子节点
			NodeList nodes = node.getChildNodes();
			for(int i=0;i<nodes.getLength();i++){
				Node childNode = nodes.item(i);
				short nodeType = childNode.getNodeType(); 
				if (nodeType == DomUtil.Text || nodeType == DomUtil.CDATA || nodeType == DomUtil.Comment) {
					continue;
				}
				if(checkNodeEQ(nameSpace,urls,childNode)){
					if(index!=-1){
						String nodeUrl = nodeUrls.substring(index+1);
						return getNodeValue(nodeUrl,childNode,nameSpace);
					}else{
						return childNode.getTextContent();
					}
				}
			}
		}else{
			return null;
		}
		return null;
	}
	
	
	private static boolean checkNodeEQ(Map<String,String> namespaces,String url,Node node){
		String nodeName;
		String nodeNameSpace = null;
		
		String[] tmp = url.split(":");
		if(tmp.length==2){
			nodeNameSpace = namespaces.get(tmp[0]);
			if(nodeNameSpace==null){
				nodeNameSpace = tmp[0];
			}
			nodeName = tmp[1];
		}else{
			nodeName = tmp[0];
		}
		
		if(node.getLocalName().equals(nodeName)){
			if(node.getNamespaceURI()==null && nodeNameSpace==null){//均没有命名空间
				return true;
			}else if(node.getNamespaceURI()!=null && node.getNamespaceURI().equals(nodeNameSpace)){
				return true;
			}
		}
		
		return false;
	}
	
}