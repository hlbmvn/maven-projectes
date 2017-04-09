package com.hlb.utils.reflect;

import java.lang.reflect.Method;
import java.util.Map;

public class ReflectUtil {
	/**
	 * 进行表达式的解析的判断
	 * */
	public static boolean calculate(Object reqData, String ddl, String objFlag){
		//将表达式和值进行取舍
		String transferDdlStr = getTransferDDL(reqData,ddl,objFlag);
		String[] tmp = transferDdlStr.split(" ");
		Node node = createTree(tmp);
		Object result = calcute(node);
		if(result instanceof Boolean){
			return (Boolean)result;
		}else{
			return false;
		}
	}
	
	/**
	 * 将现有的DDL解析成一个表达式语言
	 */
	public static String getTransferDDL(Object reqData, String ddl, String objFlag) {
		String tempDDL = ddl;
		// 替换传输的数据
		String[] params = ddl.split(objFlag + ".");
		for (String param : params) {
			String params1 = param.split(" ")[0];// (报错
			if (!params1.equals("")) {
				String expression = objFlag + "." + params1;// transRequest.getTblMerMonthTrans().getTransCount()
				String[] methods = params1.split("\\.");
				Object tempObj = reqData;
				for (String method : methods) {
					int offset = method.lastIndexOf("(");
					String methodName = method.substring(0, offset);
					String methodParams = method.substring(offset + 1, method.length() - 1);
					tempObj = getVal(tempObj, methodName, methodParams.split(","));
					if (tempObj == null) {
						tempObj = "0";// 这里如果是空则不能给空字符串，否则表达式截取会出错
					}
				}
				tempDDL = tempDDL.replace(expression, tempObj.toString());
			}
		}
		return tempDDL;
	}

	private static Object getVal(Object obj, String methodName, String[] params) {
		try {
			Method method = null;
			Object val = null;
			if (methodName.equals("substring")) {// 判断是java的某些特定的方法，及参数不是String类型
				if (params.length == 1) {// 参数是一个
					method = obj.getClass().getDeclaredMethod(methodName, int.class);
					val = method.invoke(obj, Integer.parseInt(params[0]));
				} else {
					method = obj.getClass().getDeclaredMethod(methodName, int.class, int.class);
					val = method.invoke(obj, Integer.parseInt(params[0]), Integer.parseInt(params[1]));
				}
			} else {
				if (params.length == 1) {// 参数是一个
					if ("".equals(params[0])) {
						method = obj.getClass().getDeclaredMethod(methodName, null);
						val = method.invoke(obj, null);
					} else {
						if (obj instanceof Map) {
							method = obj.getClass().getDeclaredMethod(methodName, Object.class);
						} else {
							method = obj.getClass().getDeclaredMethod(methodName, String.class);
						}
						val = method.invoke(obj, params[0]);
					}
				} else {
					method = obj.getClass().getDeclaredMethod(methodName, String[].class);
					val = method.invoke(obj, params);
				}
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据二叉树使用前序遍历方式得到结果
	 */
	public static Object calcute(Node node) {
		Object result = 0.0;
		String cmd = node.getCmd();
		if ("+-*/%".contains(cmd)) {// 数学运算符
			Node leftNode = node.getLeftNode();
			Node rightNode = node.getRightNode();
			Double result1 = (Double) calcute(leftNode);
			Double result2 = (Double) calcute(rightNode);
			if (cmd.equals("+")) {
				result = result1 + result2;
			} else if (cmd.equals("-")) {
				result = result1 - result2;
			} else if (cmd.equals("*")) {
				result = result1 * result2;
			} else if (cmd.equals("/")) {
				result = result1 / result2;
			} else if (cmd.equals("%")) {
				result = result1 % result2;
			}
		} else if ("= !=".contains(cmd)) {// 字符串和数学逻辑运算，当取出的数据为
			Node leftNode = node.getLeftNode();
			Node rightNode = node.getRightNode();
			Object result1 = calcute(leftNode);
			Object result2 = calcute(rightNode);
			if ("=".equals(cmd)) {
				if (result1 instanceof Double && result2 instanceof Double) {
					result = Double.parseDouble(result1.toString()) == Double.parseDouble(result2.toString());
				} else {
					result = result1.equals(result2);
				}
			} else if ("!=".equals(cmd)) {
				if (result1 instanceof Double && result2 instanceof Double) {
					result = (result1 != result2);
				} else {
					result = !result1.equals(result2);
				}
			}
		} else if ("> < >= <=".contains(cmd)) {// 数学逻辑运算符号
			Node leftNode = node.getLeftNode();
			Node rightNode = node.getRightNode();
			Double result1 = (Double) calcute(leftNode);
			Double result2 = (Double) calcute(rightNode);
			if (">".equals(cmd)) {
				result = result1 > result2;
			} else if ("<".equals(cmd)) {
				result = result1 < result2;
			} else if (">=".equals(cmd)) {
				result = result1 >= result2;
			} else if ("<=".equals(cmd)) {
				result = result1 <= result2;
			}
		} else if ("&& ||".contains(cmd)) {// 和 或 表达式的创建
			Node leftNode = node.getLeftNode();// >
			Node rightNode = node.getRightNode();// =
			Boolean result1 = (Boolean) calcute(leftNode);
			Boolean result2 = (Boolean) calcute(rightNode);
			if ("&&".equals(cmd)) {
				result = result1 && result2;
			} else if ("||".equals(cmd)) {
				result = result1 || result2;
			} else {
				result = false;
			}
		} else {
			try {
				result = Double.parseDouble(cmd);
			} catch (NumberFormatException exception) {
				result = cmd;
			}
		}
		return result;
	}

	/**
	 * 根据栈中的表达式创建一个二叉树
	 */
	// 进行二叉树的构建
	public static Node createTree(String[] opers) {
		// (1)遇到括号算字表达式
		// (2)遇到符号算优先级
		Node rootNode = null;
		Node leftNode = null;
		Node rightNode = null;
		int len = opers.length;
		int offset = 0;
		String opr = opers[offset++];
		if (opr.equals("(")) {// 获取子串表达式子
			int count = 1;
			String tempStr = "(";
			while (count != 0) {
				String temp = opers[offset++];
				if (temp.equals("(")) {
					count++;
				} else if (temp.equals(")")) {
					count--;
				}
				tempStr = tempStr + " " + temp;
			}
			tempStr = tempStr.substring(1, tempStr.length() - 1);
			tempStr = tempStr.trim();
			rootNode = createTree(tempStr.split(" "));
		} else {
			leftNode = new Node(opr);
			rootNode = new Node(opers[offset++]);
			String cmd = opers[offset++];
			if (cmd.equals("(")) {
				int count = 1;
				String tempStr = "(";
				while (count != 0) {
					String temp = opers[offset++];
					if (temp.equals("(")) {
						count++;
					} else if (temp.equals(")")) {
						count--;
					}
					tempStr = tempStr + " " + temp;
				}
				tempStr = tempStr.substring(1, tempStr.length() - 1);
				tempStr = tempStr.trim();
				rightNode = createTree(tempStr.split(" "));
			} else {
				rightNode = new Node(cmd);
			}
			rootNode.setLeftNode(leftNode);
			rootNode.setRightNode(rightNode);
		}

		for (; offset < len;) {
			String cmd = opers[offset++];
			Node node = new Node(cmd);
			if (getPriority(node.getCmd()) > getPriority(rootNode.getCmd())) {
				// 如果以前的root节点的操作符优先级小于当前节点的优先级，
				// 那么将以前节点仍然作为跟节点，但是其右节点变为当前节点，原来的右节点变为当前节点的左节点
				node.setLeftNode(rootNode.getRightNode());
				rootNode.setRightNode(node);
				String childCmd = opers[offset++];
				if (childCmd.equals("(")) {
					int count = 1;
					String tempStr = "(";
					while (count != 0) {
						String temp = opers[offset++];
						if (temp.equals("(")) {
							count++;
						} else if (temp.equals(")")) {
							count--;
						}
						tempStr = tempStr + " " + temp;
					}
					tempStr = tempStr.substring(1, tempStr.length() - 1);
					tempStr = tempStr.trim();
					node.setRightNode(createTree(tempStr.split(" ")));
				} else {
					node.setRightNode(new Node(childCmd));
				}
			} else {// 运算符的优先级不高则，原来的树作为现在节点的左子树，当前节点变为根节点
				node.setLeftNode(rootNode);
				String childCmd = opers[offset++];
				if (childCmd.equals("(")) {
					int count = 1;
					String tempStr = "(";
					while (count != 0) {
						String temp = opers[offset++];
						if (temp.equals("(")) {
							count++;
						} else if (temp.equals(")")) {
							count--;
						}
						tempStr = tempStr + " " + temp;
					}
					tempStr = tempStr.substring(1, tempStr.length() - 1);
					tempStr = tempStr.trim();
					node.setRightNode(createTree(tempStr.split(" ")));
				} else {
					node.setRightNode(new Node(childCmd));
				}
				rootNode = node;
			}

		}
		return rootNode;
	}

	private static int getPriority(String cmd) {
		int res = -1;
		if ("&& ||".contains(cmd)) {
			res = 0;
		} else if ("+ - > < >= <=".contains(cmd)) {
			res = 1;
		} else if ("*/%".contains(cmd)) {
			res = 2;
		}
		return res;
	}
}
