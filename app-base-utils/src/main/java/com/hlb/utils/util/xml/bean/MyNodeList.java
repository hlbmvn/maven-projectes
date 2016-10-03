package com.hlb.utils.util.xml.bean;

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyNodeList implements NodeList{
	List<Node> nodeList;

	public Node item(int index) {
		return nodeList.get(index);
	}

	public int getLength() {
		return nodeList.size();
	}
	
	public MyNodeList(List<Node> nodeList) {
		this.nodeList=nodeList;
	}
}