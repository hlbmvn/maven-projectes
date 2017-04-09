package com.hlb.testA;

import java.util.HashMap;
import java.util.Map;

import com.hlb.utils.reflect.ReflectUtil;

public class RefrectA {
	
	private String txnId;
	private String txnName;
	Map<String, String> params = new HashMap<String, String>();
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTxnName() {
		return txnName;
	}
	public void setTxnName(String txnName) {
		this.txnName = txnName;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public static void main(String[] args) {
		RefrectA txn = new RefrectA();
		txn.setTxnId("A00");
		txn.getParams().put("AA", "123");
		long start = System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			String ddl = "txn.getTxnId() = A00";
			boolean res = ReflectUtil.calculate(txn, ddl, "txn");
		}
		System.out.println(System.currentTimeMillis()-start);
	}
	
}
