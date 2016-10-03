package model;

import java.io.File;
import java.util.Map;

import javax.mail.Message.RecipientType;

/**
 * 邮件的模型类
 * */
public class MailModel {
	private String encoding = "UTF-8";
	
	private String subject;//邮件主题
	
	private String sender;//发件人如果需要添加别名，格式为别名<邮件名>，eg:胡萝卜<10210063902qq.com>
	private String password;
	
	private String date;//发间时间
	
	private Map<String, RecipientType> copyToSends;//收件人、邮件抄送集
	
	private String content;//邮件内容
	private File[] imgs;//内容图片
    private Map<String, File> enclosures;//邮件附件集
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Map<String, RecipientType> getCopyToSends() {
		return copyToSends;
	}
	public void setCopyToSends(Map<String, RecipientType> copyToSends) {
		this.copyToSends = copyToSends;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public File[] getImgs() {
		return imgs;
	}
	public void setImgs(File[] imgs) {
		this.imgs = imgs;
	}
	public Map<String, File> getEnclosures() {
		return enclosures;
	}
	public void setEnclosures(Map<String, File> enclosures) {
		this.enclosures = enclosures;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
