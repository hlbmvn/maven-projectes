import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.huateng.esb.adapter.util.DomUtil;

/**
 * 解析SOAP报文
 * */
public class ParstFileUtil {
	
	public static int count = 0;
	
	public static int count1 = 0;
	
	public static int count2 = 0;
	
	public static Set<String> methods = new HashSet<String>() ;
	
	public static Set<String> methods1 = new HashSet<String>() ;
	
	
	public static Map<String, String> pacs = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception {
		//报文解析
		//parseFils();
		//方法计算
		//calcuterFiles();
		getPacs();
		

	}
	
	
	//根据获得的方法名获取报文
	public static void getPacs() throws Exception{ 
		File result =  new File ("/Users/liupengbo/Downloads/result");
		
		File[] files = result.listFiles();
		for(int i = 0; i<files.length;i++){
			File tempFile  = files[i];
			if(tempFile.getName().contains("webc")){
				getPac(tempFile);
			}
			
		}
		
		Set<String> keys = pacs.keySet();
		
		Set<String> temp = new HashSet<String>();
		
		for(String key:keys){
			if(temp.contains(key)){
				continue;
			}
			String xml = pacs.get(key);
			if(key.contains("Response")){
				System.out.println("rsp_"+key+"***"+xml);
				if(pacs.get(key.substring(0, key.length()-8))!=null){
					temp.add(key.substring(0, key.length()-8));
					System.out.println("req_"+key+"***"+pacs.get(key.substring(0, key.length()-8)));
				}
			}else{
				System.out.println("req_"+key+"***"+xml);
				if(pacs.get(key+"Response")!=null){
					temp.add(key+"Response");
					System.out.println("rsp_"+key+"***"+pacs.get(key+"Response"));
				}
			}
			temp.add(key);
			
			
		}
		
	}
	
	public static void getPac(File file) throws Exception{ 
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = "";
		while((temp=br.readLine())!=null){
			if(!temp.equals("")){
				 Document dom = DomUtil.getDocumentByXmlString(temp.trim());
				 Node bodyNode = getNodeByReg(dom, "Envelope>Body");
				 NodeList nodes = bodyNode.getChildNodes();
				 for(int i=0;i<nodes.getLength();i++){
					 Node node = nodes.item(i);
					 if(node.getNodeType()==1){
						 
						 String methodName = node.getNodeName();
						 if(!checkPac(methodName)){//该报文不存在，直接添加
							 pacs.put(methodName, temp);
							 
						 }
						 
						 break;
					 }
				 }
			}
		}
		
		br.close();
		
	}
	
	public static boolean checkPac(String methodName){
		Set<String> keys = pacs.keySet();
		if(keys.contains(methodName)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static void calcuterFiles() throws Exception{
		
		File result =  new File ("/Users/liupengbo/Downloads/result");
		
		File[] files = result.listFiles();
		for(int i = 0; i<files.length;i++){
			File tempFile  = files[i];
			if(tempFile.getName().contains("webc")){
				calcuter(tempFile);
			}
			
		}
		System.out.println(methods.size());
		
		for(String methodName :methods ){
			if(methodName.endsWith("Response") || methodName.contains("Fault")){
				continue;
			}
			System.out.println(methodName);
			methods1.add(methodName);
		}
		
	}
	
	
	public static void calcuter(File file) throws Exception{
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String temp = "";
		
		while((temp=br.readLine())!=null){
			if(!temp.equals("")){
				 Document dom = DomUtil.getDocumentByXmlString(temp.trim());
				 Node bodyNode = getNodeByReg(dom, "Envelope>Body");
				 NodeList nodes = bodyNode.getChildNodes();
				 for(int i=0;i<nodes.getLength();i++){
					 Node node = nodes.item(i);
					 if(node.getNodeType()==1){
						 methods.add(node.getNodeName());
						 break;
					 }
				 }
			}
		}
		
		br.close();
	}
	
	
	public static void parseFils() throws Exception{
		
		File file = new File ("/Users/liupengbo/Downloads/java项目及日志/webc.bak.201609");
		File result =  new File ("/Users/liupengbo/Downloads/result");
		if(!result.exists()){
			result.mkdir();
		}
		

		File[] files = file.listFiles();
		for(int i = 0; i<files.length;i++){
			File tempFile  = files[i];
			if(tempFile.getName().contains("webc")){
				File targetFile = new File(result, tempFile.getName()+".txt");
				parseFile(tempFile,targetFile);
			}
			
		}
	}
	
	public static void parseFile(File sourceFile,File targetFile) throws Exception{
		
		BufferedReader br = new BufferedReader(new FileReader(sourceFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile));
		String temp = null;
		boolean isEnd = true;
		String header = "";
		String pacTemp = "";
		
		
		while((temp=br.readLine())!=null){
			
			int len = temp.split("Envelope").length;
			
			
			if(temp.contains("Envelope")){
				count = count+len-1;
			}
			
			if(temp.contains("Envelope") && isEnd){
				String pacHead = temp.split(":Envelope")[0];
				String[] tt = pacHead.split("<");
				pacHead = tt[tt.length-1];
				header = pacHead+":Envelope";
				temp = temp.substring(temp.indexOf(header)-1);
				if(temp.contains("</"+header)){
					int index = temp.indexOf("</"+header);
					int le = header.length();
					pacTemp = temp.substring(0, index+le+3);
					isEnd = true;
					header = "";
					bw.append("\r\n"+pacTemp);
				//	System.err.println("一次性符合要求："+pacTemp);
					count1++;
					pacTemp = "";
				}else{
					pacTemp = pacTemp+" "+temp;
					isEnd = false;
				}
			}else if(!isEnd){
				if(temp.contains("</"+header)){
					int index = temp.indexOf("</"+header);
					int le = header.length();
					pacTemp = pacTemp+" "+temp.substring(0, index+le+3);
					isEnd = true;
					header = "";
					bw.append("\r\n"+pacTemp);
				//	System.err.println("符合要求："+pacTemp);
					count2++;
					pacTemp = "";
				}else{
					pacTemp = pacTemp+" "+temp;
					isEnd = false;
				}
			}
		}
		
		br.close();
		bw.close();
		
	}
	
	
	/**
	 * 获取指定元素
	 * @param reg
	 * */
	public static Node getNodeByReg(Node node,String reg){
		String[] eles = reg.split(">");
		int len = eles.length;
		Node nextNode = node;
		for(int i = 0;i<len;i++){
			nextNode = getNextNode(nextNode, eles[i]);
		}
		return nextNode;
	}
	
	/**
	 * 获取下一个节点
	 * @param node
	 * @param nodeName
	 * @return
	 */
	private static Node getNextNode(Node node, String nodeName) {
		if(node!=null && node.hasChildNodes()){
			NodeList childNodes =node.getChildNodes();
			for(int i=0; i<childNodes.getLength() ;i++){
				Node childNode = childNodes.item(i);
				String childNodeName = childNode.getNodeName();
				childNodeName = childNodeName.substring(childNodeName.indexOf(":")+1);
				if(childNodeName.equals(nodeName) || ("*".equals(nodeName) && childNode.hasChildNodes())){
					return childNode;
				}
			}
		}
		return null;
	}
	
	
}


