package com.hlb.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.hlb.utils.excel.poi.ExcelUtil;

public class ExcelTest {
	
	public static void main(String[] args) throws Exception {
		File file = new File("1.xls");
		//System.out.println(file.getAbsolutePath());
		List<Object> beans = ExcelUtil.getInstance().readExcel2ObjsByInputStream(new FileInputStream(file),
				XLSBean.class, 0, 0);
		
		List<XLSBean> bean0 = new ArrayList<XLSBean>();//服务方排序
		
		List<XLSBean> bean1 = new ArrayList<XLSBean>();//服务方排序
		
		List<XLSBean> bean2 = new ArrayList<XLSBean>();//服务排序
		
		List<XLSBean> bean3 = new ArrayList<XLSBean>();//操作排序
		
		List<XLSBean1> beans1 = new ArrayList<XLSBean1>();
		
		String providerNm = "";
		String serviceNm = "";
		String operationNm = "";
		
		
		for(int i = 0 ;i<beans.size();i++){
			
			for(int j = i;j<beans.size();j++){
				XLSBean  bean = (XLSBean) beans.get(j);
				if(providerNm.equals("")){
					providerNm = bean.getStr6();
				}
				if(providerNm.equals(bean.getStr6())){
					bean1.add(bean);
				}else{
					continue;
				}
				
				
				
			}
			
		}
		
		
		
	}
	
	
}
