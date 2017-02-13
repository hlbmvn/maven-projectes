package com.hlb.image;

import com.hlb.utils.image.VerifyCodeUtils;
  
public class CreateImage    
{   
    public static void main(String[] args) throws Exception   
    {
    	
    	//(1) 先获取图片验证码
    	String imgCode = VerifyCodeUtils.generateVerifyCode(4);
    	
    	//（2）获取图片
    	String imgName = VerifyCodeUtils.genVerifyImage(120, 60, "/Users/liupengbo/Documents/temp/imgs/", imgCode);
    	//VerifyCodeUtils.outputImage(100,50,new File("/Users/liupengbo/Documents/temp/imgs/test.jpg"),"AAXZ");
    	System.out.println(imgCode+"  "+imgName);
    }       

}   


