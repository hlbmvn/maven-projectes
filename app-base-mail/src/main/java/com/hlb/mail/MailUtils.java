package com.hlb.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.MailModel;

public class MailUtils {
	
	private static Logger log = LoggerFactory.getLogger(MailUtils.class);
	
	/** MailboxType 邮箱类型 */
	private static Map<String, String> MailboxTypes = null;
	
	private static void init() {
		MailboxTypes = new HashMap<String, String>();
		InputStream input = MailUtils.class.getClassLoader().getResourceAsStream("mail.properties");
		Properties properties = new Properties();
		try {
			properties.load(input);
			for(Object key:properties.keySet()){
				MailboxTypes.put(key.toString(),properties.getProperty(key.toString(), key.toString()));
			}
		} catch (IOException e) {
			log.warn("未找到mail.properties文件，将无法加载内置MailboxTypes");
		}
	}
	
	private static String getHost(String sender){
		if(MailboxTypes==null){
			init();
		}
		String temp = sender.substring(sender.lastIndexOf("@")+1);
		if(temp.lastIndexOf(">")==(temp.length()-1)){
			temp = temp.substring(0, temp.length()-1);
		}
		
		if( MailboxTypes.get(temp)!=null){
			return MailboxTypes.get(temp);
		}else{
			return "smtp."+temp;
		}
	}
	
	
	/**
	 * 发送邮件
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 * @desc 邮件的结构,发送成功返回消息，发送失败将抛出异常
	 *	MimeMessage(邮件)
	 *		MimeMultipart[邮件的载体]
	 *			属性：
	 *			fromAddr [发件人]
	 *			toAddr[收件人]
	 *			subject[主题]
	 *			[抄送信息]
	 *	
	 *			MimeMultipart[邮件信息]
	 *				一些列的附件bodyPart
	 *				一个邮件内容复杂体bodyPart
	 *					MimeMultipart
	 *						bodyPart（图片的）
	 *						bodyPart  (邮件内容的）
	 * 
	 * */
	public static Message sendMail(MailModel mail) throws MessagingException, UnsupportedEncodingException{
		//设置邮件发送属性
		Properties props = System.getProperties();
		{
			props.put("mail.smtp.auth", "true");
			props.put("mail.transport.protocol", "smtp");
		}
		//创建会话连接
		Session session = Session.getInstance(props);
		session.setDebug(true);
		//创建邮件
		Message msg = new MimeMessage(session);
		//设置发件人信息
		String nickNameAccount = getNickName(mail.getSender());
		String[] account = nickNameAccount.split(",");
		InternetAddress fromAddr;
		try {
			fromAddr = new InternetAddress(MimeUtility.encodeText(account[0])+"<" + account[1] + ">");
			msg.setFrom(fromAddr);
		} catch (UnsupportedEncodingException e) {
			log.error("发件人存在特殊符号，无法编码[{}]",account[0]);
		}
		
		msg.setText(mail.getContent());
		msg.setSubject(mail.getSubject()); // 邮件主题
		setCopyToSends(msg, mail.getCopyToSends()); // 设置抄送信息
		MimeMultipart msgMultipart = new MimeMultipart("mixed"); // 创建邮件复杂体
		msg.setContent(msgMultipart); // 将邮件复杂体添加到邮件正文中
		
		setEnclosureFile(msgMultipart, mail.getEnclosures()); // 设置附件信息
		MimeBodyPart content = new MimeBodyPart(); // 创建邮件复杂体正文信息
		msgMultipart.addBodyPart(content); // 将正文信息添加到复杂体中

		MimeMultipart bodyMultipart = new MimeMultipart("related");
		content.setContent(bodyMultipart);
		
		if(mail.getImgs()!=null){
			//正文的图片部分
			for(File imgFile:mail.getImgs()){
				MimeBodyPart jpgBody = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(imgFile);
				jpgBody.setDataHandler(new DataHandler(fds));
				jpgBody.setContentID(imgFile.getName());
				bodyMultipart.addBodyPart(jpgBody);
			}
		}
		
		MimeBodyPart htmlPart = new MimeBodyPart();  // 创建HTML文本域
		bodyMultipart.addBodyPart(htmlPart); // 将HTML文本域添加到正文组合中
		DataSource htmlDs = new ByteArrayDataSource(mail.getContent().getBytes(mail.getEncoding()),"application/octet-stream");
		DataHandler htmlDh = new DataHandler(htmlDs);
		htmlPart.setDataHandler(htmlDh);
		htmlPart.setContent(mail.getContent(), "text/html;charset="+mail.getEncoding());
		msg.saveChanges(); // 生成邮件
		
		Transport transport=session.getTransport();
		String[] temp = getHost(mail.getSender()).split(",");
		String smtpHost = temp[0];
		int port = 25;
		if(temp.length>1){
			port = Integer.parseInt(temp[1]);
		}
		String mailAcc = mail.getSender().substring(0,mail.getSender().lastIndexOf("@"));
		if(mailAcc.indexOf("<")!=-1){
			mailAcc = mailAcc.substring(mailAcc.indexOf("<")+1);
		}
		transport.connect(smtpHost,port,mailAcc, mail.getPassword());
		int size = mail.getCopyToSends().keySet().size();
		Address[] addrs = new Address[size];
		int i = 0;
		for(String key:mail.getCopyToSends().keySet()){
			addrs[i] =  new InternetAddress(key);
			i++;
		}
		transport.sendMessage(msg,addrs);
        transport.close();
		return msg;
	}
	
	// 设置邮件附件信息
	private static void setEnclosureFile(MimeMultipart msgMultipart,
			Map<String, File> enclosures) throws MessagingException,
			UnsupportedEncodingException {
		if (enclosures == null || enclosures.isEmpty()) {
			return ;
		}
		Set<String> enclosureSet = enclosures.keySet();
		Iterator<String> enclosureIter = enclosureSet.iterator();
		while (enclosureIter.hasNext()) {
			String attchFileName = enclosureIter.next();
			MimeBodyPart attch = new MimeBodyPart();
			msgMultipart.addBodyPart(attch);
			File temEnclosureFile = enclosures.get(attchFileName);
			DataSource ds = new FileDataSource(temEnclosureFile);
			DataHandler dh = new DataHandler(ds);
			attch.setDataHandler(dh);
			attch.setFileName(MimeUtility
					.encodeText(attchFileName));// 设置附件显示名称
		}
	}
	
	private static void setCopyToSends(Message msg,
			Map<String, RecipientType> copyToSends) throws AddressException,
			UnsupportedEncodingException, MessagingException {
		if (copyToSends == null) {
			return;
		}
		Set<String> copyToSendSet = copyToSends.keySet();
		Iterator<String> copyToSendIter = copyToSendSet.iterator();
		List<String> to = new ArrayList<String>(); // 定义接收者账号信息集
		List<String> cc = new ArrayList<String>(); // 定义抄送者账号信息集
		List<String> bcc = new ArrayList<String>();// 定义秘密抄送者账号信息集
		while (copyToSendIter.hasNext()) {
			String address = copyToSendIter.next(); // 获取抄送者邮箱账号信息
			RecipientType tmpRecipientType = copyToSends.get(address);
			if (tmpRecipientType == RecipientType.TO) {
				to.add(address);
			}
			if (tmpRecipientType == RecipientType.CC) {
				cc.add(address);
			}
			if (tmpRecipientType == RecipientType.BCC) {
				bcc.add(address);
			}
		}

		// 获取接收者信息集
		Address[] to_addrs = getAddress(to);
		if (to_addrs != null) {
			msg.setRecipients(MimeMessage.RecipientType.TO, to_addrs);
		}

		// 获取抄送者信息集
		Address[] cc_addrs = getAddress(cc);
		if (cc_addrs != null) {
			msg.setRecipients(MimeMessage.RecipientType.CC, cc_addrs);
		}

		// 获取秘密抄送者信息集
		Address[] bcc_addrs = getAddress(bcc);
		if (bcc_addrs != null) {
			msg.setRecipients(MimeMessage.RecipientType.BCC, bcc_addrs);
		}
	}
	
	// 获取邮件地址信息
	private static Address[] getAddress(List<String> recpType)
			throws AddressException, UnsupportedEncodingException {
		if (recpType == null || recpType.isEmpty()) {
			return null;
		}
		Address[] addrs = new Address[recpType.size()];
		for (int i = 0; i < addrs.length; i++) {
			String nickNameAccount = getNickName(recpType.get(i));
			String[] nickName_account = nickNameAccount.split(",");
			addrs[i] = new InternetAddress("\""
					+ MimeUtility.encodeText("" + nickName_account[0] + "")
					+ "\" <" + nickName_account[1] + ">");
		}
		return addrs;
	}

	// 获取邮箱账号昵称信息
	private static String getNickName(String mailAccount) {
		int index = mailAccount.lastIndexOf("<");
		if (index == -1) { // 不含有昵称信息，未找到"<>"
			// aaa@163.com
			String nickName = mailAccount.substring(0, mailAccount
					.lastIndexOf("@"));
			return nickName + "," + mailAccount;
		} else if (index == 0) { // 不含有昵称信息 但找到了"<>"
			// <aaa@163.com>
			String nickName = mailAccount.substring(index + 1, mailAccount
					.lastIndexOf("@"));
			String account = mailAccount.substring(index + 1, mailAccount
					.lastIndexOf(">"));
			return nickName + "," + account;
		} else if (index > 0) { // 含有昵称信息，并且找到了"<>"
			String nickName = mailAccount.substring(0, index);
			String account = mailAccount.substring(index + 1, mailAccount
					.lastIndexOf(">"));
			return nickName + "," + account;
		}
		return mailAccount;
	}
}
