package com.hlb.mail;
import java.io.*;    
import java.text.*;    
import java.util.*;    

import javax.mail.*;    
import javax.mail.internet.*;    
   
/**   
 * 有一封邮件就需要建立一个ReciveMail对象   
 */   
public class ReceiveMail{    
	 private MimeMessage mimeMessage = null; //邮件存储在mimeMessage中   
	 private String saveAttachPath = ""; //附件下载后的存放目录，这里为什么不直接给予初始值呢，是因为要判断它是什么系统的，这样使用的分隔符是不一致的
	 private StringBuffer bodytext = new StringBuffer();//存放邮件内容（除附件外的东西）这里你会发现有两份文本，其中一份是纯文本另一一份是html的，这主要是javaMali怕你不能查看html的所以多加了一份纯文本的，根据实际情况将其去除   
	 private String dateformat = "yyyy-MM-dd HH:mm:ss"; //默认的日前显示格式
	   
    public ReceiveMail(MimeMessage mimeMessage) {//当创建一个receiveMail对象时应该有mimeMessage，这样才有意义
        this.mimeMessage = mimeMessage;    
    }    
   
    public void setMimeMessage(MimeMessage mimeMessage) {    
        this.mimeMessage = mimeMessage;    
    }    
   
    /**   
     * 获得发件人的地址和姓名   格式是：姓名<地址>
     */   
    public String getFrom() throws Exception { 
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();    
        String from = address[0].getAddress(); 
        if (from == null)    
            from = "";    
        String personal = address[0].getPersonal(); 
        if (personal == null)    
            personal = "";
        String fromaddr = personal + "<" + from + ">";    
        return fromaddr;   
    }    
   
    /**   
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址,人以逗号分隔    
     */   
    public String getMailAddress(String type) throws Exception {    
        String mailaddr = "";    
        String addtype = type.toUpperCase();    
        InternetAddress[] address = null;    
        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {    
            if (addtype.equals("TO")) {    
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);    
            } else if (addtype.equals("CC")) {    
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);    
            } else {    
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);    
            }    
            if (address != null) {    
                for (int i = 0; i < address.length; i++) {    
                    String email = address[i].getAddress();    
                    if (email == null)    
                        email = "";    
                    String personal = address[i].getPersonal();    
                    if (personal == null)    
                        personal = "";    
                    String compositeto = personal + "<" + email + ">";    
                    mailaddr += "," + compositeto;    
                }    
               mailaddr = mailaddr.substring(1); //这样设置才会将第一个逗号给去除
            }    
        } else {   
        	//传递进来的必须是定义好的邮件类型 
            throw new Exception("Error emailaddr type!");    
        }    
        return mailaddr;    
    }    
   
    /**   
     * 获得邮件主题   
     * @throws MessagingException 
     */   
    public String getSubject(){    
        String subject = "";    
        try {
        	subject=mimeMessage.getSubject();
		} catch (Exception e) {}
        if (subject == null)    
                subject = "";    
        return subject;    
    }    
    /**   
     * 获得邮件发送日期   
     */
    public String getSentDate() throws Exception {    
        Date sentdate = mimeMessage.getSentDate();    
        SimpleDateFormat format = new SimpleDateFormat(dateformat);    
        return format.format(sentdate);    
    }    
    /**   
     * 获得邮件正文内容，使用该方法前应该首先调用getMailContent方法将邮件内容填充到bodyText中,我建议使用简单方法工厂模式
     * 或者这里面直接调用getMailContent方法，但要注意这里不能这样主要是假如你这样的话每次调用getBodyText时都需要重新加载bodyText
     * 本来已经有了又需要在加载重新运行算法，不合适，从这角度来说作者是一个高手，那么再次改进的话，我们可以这样，只要该封邮件的bodyText不为空，
     * 通过判断其长度而来，那么再次调用时就不在重新加载。在这里我更喜欢作者的做法
     */    
    public String getBodyText() {    
        return bodytext.toString();   
    }    
   
    /**   
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析   
     */   
    public void getMailContent(Part part) throws Exception {
    	/*
    	 * 这里输出contentType主要是为了以后调试方便，如在今后的开发中遇到
    	 * part.isMimeType("message/rfc822"这种类型是在说
    	 * 
    	 * */
        if (part.isMimeType("text/plain")) {    
            bodytext.append((String) part.getContent());    
        } else if (part.isMimeType("text/html")) {    
            bodytext.append((String) part.getContent());    
        } else if (part.isMimeType("multipart/*")) {    
            Multipart multipart = (Multipart) part.getContent();    
            int counts = multipart.getCount();    
            for (int i = 0; i < counts; i++) {//递归算法
                getMailContent(multipart.getBodyPart(i));    
            }    
        }
    }    
   
    /**    
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"   
     */    
    public boolean getReplySign() throws MessagingException {    
        boolean replysign = false;    
        String needreply[] = mimeMessage    
                .getHeader("Disposition-Notification-To");    
        if (needreply != null) {    
            replysign = true;    
        }    
        return replysign;    
    }    
   
    /**   
     * 获得此邮件的Message-ID   
     */   
    public String getMessageId() throws MessagingException { 
    	String messageID=mimeMessage.getMessageID();
    	messageID=messageID.substring(1, messageID.length()-1);
        return messageID;    
    }    
   
    /**   
     * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
     * 这个只有在使用的是imap服务器解析时才有用，在Pop3服务器上解析它永远是false   
     */
    public boolean isNew() throws MessagingException {    
        boolean isnew = false;    
        Flags flags = ((Message) mimeMessage).getFlags();
        
        Flags.Flag[] flag = flags.getSystemFlags();    
        System.out.println("flags's length: " + flag.length);    
        for (int i = 0; i < flag.length; i++) {    
            if (flag[i] == Flags.Flag.SEEN) {    
                isnew = true; 
                break;    
            } 
            
        }    
        return isnew;    
    }    
   
    /**   
     * 判断此邮件是否包含附件   
     */   
    public boolean isContainAttach(Part part) throws Exception {    
        boolean attachflag = false;  
        if (part.isMimeType("multipart/*")) {    
            Multipart mp = (Multipart) part.getContent();    
            for (int i = 0; i < mp.getCount(); i++) {    
                BodyPart mpart = mp.getBodyPart(i);    
                String disposition = mpart.getDisposition();    
                if ((disposition != null)    
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition    
                                .equals(Part.INLINE))))    
                    attachflag = true;    
                else if (mpart.isMimeType("multipart/*")) {    
                    attachflag = isContainAttach((Part) mpart);    
                } else {    
                    String contype = mpart.getContentType();    
                    if (contype.toLowerCase().indexOf("application") != -1)    
                        attachflag = true;    
                    if (contype.toLowerCase().indexOf("name") != -1)    
                        attachflag = true;    
                }    
            }    
        }   
        return attachflag;    
    }    
   
    /**    
     * 【保存附件】    
     */    
    public void saveAttachMent(Part part) throws Exception {    
        String fileName = "";    
        if (part.isMimeType("multipart/*")) {    
            Multipart mp = (Multipart) part.getContent();    
            for (int i = 0; i < mp.getCount(); i++) {    
                BodyPart mpart = mp.getBodyPart(i);    
                String disposition = mpart.getDisposition();    
                if ((disposition != null)    
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition    
                                .equals(Part.INLINE)))) {
                    fileName =MimeUtility.decodeText(mpart.getFileName());
                    saveFile(fileName, mpart.getInputStream());    
                } else if (mpart.isMimeType("multipart/*")) {  
                    saveAttachMent(mpart);    
                } else {    
                    fileName =mpart.getFileName(); 
                    if(fileName!=null&&(mpart.getContentType().toLowerCase().indexOf("application") != -1)){
                    	fileName=MimeUtility.decodeText(fileName);
                    	saveFile(fileName, mpart.getInputStream());
                    	if(bodytext.indexOf("cid:"+fileName)!=-1){
                    		String temp=bodytext.toString();
                    		temp=temp.replace("cid:"+fileName, getAttachPath()+"\\"+getMessageId()+"\\"+fileName);
                    		bodytext=new StringBuffer(temp);
                    	}
                    }    
                }    
            }    
        } 
    }    
   
    /**    
     * 【设置附件存放路径】    
     */    
   
    public void setAttachPath(String attachpath) {    
        this.saveAttachPath = attachpath;    
    }    
   
    /**   
     * 【设置日期显示格式】   
     */   
    public void setDateFormat(String format) throws Exception {    
        this.dateformat = format;    
    }    
   
    /**   
     * 【获得附件存放路径】   
     */   
    public String getAttachPath() {    
        return saveAttachPath;    
    }    
   
    /**   
     * 【真正的保存附件到指定目录里】   
     */   
    private void saveFile(String fileName, InputStream in) throws Exception {    
        String osName = System.getProperty("os.name");    
        String storedir = getAttachPath();
        String separator = "";    
        if (osName == null)    
            osName = "";    
        if (osName.toLowerCase().indexOf("win") != -1) {    
            separator = "\\";   
            if (storedir == null || storedir.equals(""))   
                storedir = "c:\\tmp";
        } else {   
            separator = "/";   
            storedir = "c:/tmp";   
        }
        File path=new File(storedir + separator +getMessageId());
        if(!path.exists()){ path.mkdirs(); }
        File storefile = new File(storedir + separator +getMessageId()+separator+ fileName);   
        BufferedOutputStream bos = null;   
        BufferedInputStream bis = null;   
        try {   
            bos = new BufferedOutputStream(new FileOutputStream(storefile));   
            bis = new BufferedInputStream(in);   
            int c;   
            while ((c = bis.read()) != -1) { 
                bos.write(c);   
            }  
            bos.flush();
        } catch (Exception exception) {   
            exception.printStackTrace();   
            throw new Exception("文件保存失败!"); 
        } finally {   
            bos.close();   
            bis.close();   
        }   
    } 
    
   /**
    * 设置邮件为已读
    * @throws MessagingException 
    * */
    public void setReaded() throws MessagingException {
    	this.mimeMessage.setFlag(Flags.Flag.SEEN, true);
    }
    
    
    /**   
     * PraseMimeMessage类测试   
     */   
    public static void main(String args[]) throws Exception {   
       
        Properties pros = System.getProperties();
        pros.setProperty("mail.host", "imap.qq.com");
        pros.setProperty("mail.store.protocol", "imap");
		Session mailsession = Session.getInstance(pros, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("649110954", "QQliu568925822");
			}
		});
		Store store = mailsession.getStore();
		store.connect();
        Folder folder = store.getFolder("INBOX");   
        folder.open(Folder.READ_ONLY);   
        Message message[] = folder.getMessages();   
        System.out.println("Messages's length: " + message.length);   
        ReceiveMail pmm = null;   
        for (int i = 0; i < message.length; i++) {   
            pmm = new ReceiveMail((MimeMessage) message[i]);   
            if(!pmm.isNew()){
	        	System.out.println("=begin==begin==begin==begin==begin==begin=");
	            System.out.println("Message " + i + " isNew " + pmm.isNew());
	            System.out.println("Message " + i + " subject: " + pmm.getSubject());   
	            System.out.println("Message " + i + " sentdate: "+ pmm.getSentDate());   
	            System.out.println("Message " + i + " replysign: "+ pmm.getReplySign());   
	            System.out.println("Message " + i + " hasRead: " + pmm.isNew());   
	            System.out.println("Message " + i + "  containAttachment: "+ pmm.isContainAttach((Part) message[i]));   
	            System.out.println("Message " + i + " form: " + pmm.getFrom());   
	            System.out.println("Message " + i + " to: "+ pmm.getMailAddress("to"));   
	            System.out.println("Message " + i + " cc: "+ pmm.getMailAddress("cc"));   
	            System.out.println("Message " + i + " bcc: "+ pmm.getMailAddress("bcc"));   
	            pmm.setDateFormat("yy年MM月dd日 HH:mm");   
	            System.out.println("Message " + i + " sentdate: "+ pmm.getSentDate());   
	            System.out.println("Message " + i + " Message-ID: "+ pmm.getMessageId());   
	            // 获得邮件内容===============   
	            pmm.getMailContent((Part) message[i]);   
	            pmm.setAttachPath("D:\\mail");
	            pmm.saveAttachMent((Part) message[i]);
	            System.out.println("Message " + i + " bodycontent: \r\n"   
	                    + pmm.getBodyText());
	            //把邮件标记为已读
	            pmm.setReaded();
	            System.out.println("=end==end==end==end==end==end=");
           }  
        }    
    store.close();
    }    
}   
