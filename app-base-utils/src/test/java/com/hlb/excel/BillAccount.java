package com.hlb.excel;

import com.hlb.utils.excel.poi.ExcelResources;

public class BillAccount {
	
	
	private String atm;//交易金额
	private String state;//对账状态
	
	@ExcelResources(title="交易金额",order=1)
	public String getAtm() {
		return atm;
	}
	public void setAtm(String atm) {
		this.atm = atm;
	}
	
	@ExcelResources(title="交易状态",order=2)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
