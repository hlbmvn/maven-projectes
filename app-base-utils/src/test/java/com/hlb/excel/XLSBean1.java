package com.hlb.excel;

import com.hlb.utils.excel.poi.ExcelResources;

public class XLSBean1 {
	
	private String str1;
	private String str2;
	private String str3;
	private String str4;
	private String str5;
	private String str6;
	private String str7;
	private String str8;
	private String str9;
	private String str10;

	@ExcelResources(title="服务方",order=1)
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	
	@ExcelResources(title="服务名称",order=2)
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	
	@ExcelResources(title="操作名称",order=3)
	public String getStr3() {
		return str3;
	}
	public void setStr3(String str3) {
		this.str3 = str3;
	}
	
	@ExcelResources(title="服务操作备注",order=4)
	public String getStr4() {
		return str4;
	}
	public void setStr4(String str4) {
		this.str4 = str4;
	}

	@ExcelResources(title="开发状态",order=5)
	public String getStr5() {
		return str5;
	}
	public void setStr5(String str5) {
		this.str5 = str5;
	}
	
	@ExcelResources(title="计划开发日期",order=6)
	public String getStr6() {
		return str6;
	}
	public void setStr6(String str6) {
		this.str6 = str6;
	}
	
	@ExcelResources(title="请求者",order=7)
	public String getStr7() {
		return str7;
	}
	public void setStr7(String str7) {
		this.str7 = str7;
	}

	@ExcelResources(title="测试状态",order=1)
	public String getStr8() {
		return str8;
	}
	public void setStr8(String str8) {
		this.str8 = str8;
	}
	
	@ExcelResources(title="计划测试时间",order=9)
	public String getStr9() {
		return str9;
	}
	public void setStr9(String str9) {
		this.str9 = str9;
	}
	
	@ExcelResources(title="备注",order=10)
	public String getStr10() {
		return str10;
	}
	public void setStr10(String str10) {
		this.str10 = str10;
	}
}
