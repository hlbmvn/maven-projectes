package com.hlb.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.Test;

import model.MailModel;

public class TestMail {

	@Test
	public void testSendMail() throws UnsupportedEncodingException, MessagingException{
		
		MailModel mail = new MailModel();
		//发件人信息
		//mail.setSender("刘朋波<liupengbo@chinasofti.com>");
		//mail.setPassword("QQliu5689258");
		
		mail.setSender("1021006390@qq.com");
		mail.setPassword("vvlqasciuteobcgj");
		//mail.setPassword("vvlqasciuteobcgj");
		
		//邮件信息
		mail.setSubject("邮件主题");
		mail.setContent("<div style=\"height:900px;width:300px;color:red\">"
				+ "何厚铧"
				+ "<img src=\"http://b.hiphotos.baidu.com/image/h%3D200/sign=18dbdb76ad014c08063b2fa53a79025b/023b5bb5c9ea15cec72cb6d6b2003af33b87b22b.jpg\" />"
				+ "<img src=\"cid:yy.png\" /></div>");
		File[] files = new File[]{new File("/Users/liupengbo/Documents/temp/logs/yy.png")};
		mail.setImgs(files);
		
		//附件信息
		Map<String, File> enclosures = new HashMap<String, File>();
		enclosures.put("李四.txt", new File("/Users/liupengbo/Documents/temp/logs/riskA.log"));
		enclosures.put("test.txt", new File("/Users/liupengbo/Documents/temp/logs/riskA.log"));
		mail.setEnclosures(enclosures);
		
		//收件人信息
		Map<String,RecipientType> copyToSends = new HashMap<String, Message.RecipientType>();
		copyToSends.put("李四<1021006390@qq.com>", RecipientType.TO);//收件人
		//copyToSends.put("liupengbo@chinasofti.com", RecipientType.CC);//抄送人
		mail.setCopyToSends(copyToSends);

		Message msg = MailUtils.sendMail(mail);
		
		/*文件存储归档*/
        String filePath = "/Users/liupengbo/Downloads/demo1.eml";
        OutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			msg.writeTo(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {}
			}
		}
		
		System.out.println("success");
		
	}
}
