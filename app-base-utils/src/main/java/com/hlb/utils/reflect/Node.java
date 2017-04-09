package com.hlb.utils.reflect;
/**
 *	二叉树定义
 * */
public class Node {
	private Node leftNode;
	private Node rightNode;

	private String cmd;
	
	public Node(String cmd) {
		this.cmd = cmd;
	}
	
	public Node getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}
	public Node getRightNode() {
		return rightNode;
	}
	public void setRightNode(Node rightNode) {
		this.rightNode = rightNode;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}