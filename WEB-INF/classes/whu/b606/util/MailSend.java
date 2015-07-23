package whu.b606.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 邮件发送工具
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 */
public class MailSend extends Thread {
	// 以下项是发件环境配置属性，可以从配置文件中读取
	private String from;// 发件人
	private String username;// 发件人用户名
	private String password;// 发件人登录密码
	private String host;// 邮件服务器
	private String displayName;// 要显示的发件人名称
	private boolean needAuth; // 服务器是否要身份认证
	// 以下项须由外部传入
	private String subject;// 邮件主题
	private String[] to;// 收件人地址
	private String msgcontent;// 邮件内容
	private Map<String, String> f = new HashMap<String, String>();// 附件地址以及附件对应的名称
	private Object[] template;// 字符串替换模板
	private String[] picturePath;
	private String htmlContent;// 带有html标记的内容
	private String htmlPath;
	/**
	 * 发件类型，取值：text、simpleHtml、simpleTemplate、htmlTemplate(默认)、atext(text型带附件)、
	 * asimpleHtml( simpleHtml型带附件)、ahtmlTemplate(htmlTemplate型带附件)、
	 * asimpleTemplate (simpleTemplate型带附件)
	 */
	private String sendType = "htmlTemplate";

	/**
	 * 线程执行的主方法
	 */
	@Override
	public void run() {
		try {
			send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 纯文本邮件
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param msgcontent
	 *            邮件内容
	 */
	public MailSend(String emailSubject, String[] emailAddr, String msgcontent) {
		this.sendType = "text";
		getMailSend(emailSubject, emailAddr, msgcontent, null, null, null, null, null);
	}

	/**
	 * 带附件的纯文本邮件
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param msgcontent
	 *            邮件内容
	 * @param attachments
	 *            包含附件绝对路径和对应附件名的集合
	 */
	public MailSend(String emailSubject, String[] emailAddr, String msgcontent, Map<String, String> attachments) {
		this.sendType = "atext";
		getMailSend(emailSubject, emailAddr, msgcontent, attachments, null, null, null, null);
	}

	/**
	 * 带附件，且内容由html模板文件生成的邮件
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param attachments
	 *            包含附件绝对路径和对应附件名的集合
	 * @param htmlPath
	 *            html模板文件绝对地址
	 * @param template
	 *            用于替换html文件中占位符的对象数组
	 */
	public MailSend(String emailSubject, String[] emailAddr, Map<String, String> attachments, String htmlPath, Object[] template) {
		this.sendType = "ahtmlTemplate";
		getMailSend(emailSubject, emailAddr, null, attachments, htmlPath, template, null, null);
	}

	/**
	 * 不带附件，内容由html模板文件生成的邮件
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param htmlPath
	 *            html模板文件绝对地址
	 * @param template
	 *            用于替换html文件中占位符的对象数组,注，template参数不能为null，但可以为空数组
	 */
	public MailSend(String emailSubject, String[] emailAddr, String htmlPath, Object[] template) {
		this.sendType = "htmlTemplate";
		if (null == template) { throw new RuntimeException("template参数不能为null，但可以为空数组"); }
		getMailSend(emailSubject, emailAddr, null, null, htmlPath, template, null, null);
	}

	/**
	 * 带附件，内容由简单的包含html标签的字符串生成的邮件，可以有图片标签，如果无图，则只需要将图片参数设为null
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param attachments
	 *            包含附件绝对路径和对应附件名的集合
	 * @param htmlContent
	 *            包含html标签的字符
	 * @param picturePath
	 *            图标的实际引用路径
	 */
	public MailSend(String emailSubject, String[] emailAddr, Map<String, String> attachments, String htmlContent, String[] picturePath) {
		this.sendType = "asimpleHtml";
		getMailSend(emailSubject, emailAddr, null, attachments, null, null, htmlContent, picturePath);
	}

	/**
	 * 不带附件，内容由简单的包含html标签的字符串生成的邮件，可以有图片标签，如果无图，则只需要将图片参数设为null
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param htmlContent
	 *            包含html标签的字符
	 * @param picturePath
	 *            图标的实际引用路径
	 */
	public MailSend(String emailSubject, String[] emailAddr, String htmlContent, String[] picturePath) {
		this.sendType = "simpleHtml";
		getMailSend(emailSubject, emailAddr, null, null, null, null, htmlContent, picturePath);
	}

	/**
	 * 带附件，内容由简单的包含html标签的字符串生成的邮件，且可使用html模板
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param attachments
	 *            包含附件绝对路径和对应附件名的集合
	 * @param htmlPath
	 *            html文件路径
	 * @param template
	 *            模板参数
	 * @param picturePath
	 *            图片路径数组
	 */
	public MailSend(String emailSubject, String[] emailAddr, Map<String, String> attachments, String htmlPath, Object[] template, String[] picturePath) {
		this.sendType = "asimpleTemplate";
		getMailSend(emailSubject, emailAddr, null, attachments, htmlPath, template, null, picturePath);
	}

	/**
	 * 不带附件，内容由简单的包含html标签的字符串生成的邮件，且可使用html模板
	 * 
	 * @param emailSubject
	 *            邮件主题
	 * @param emailAddr
	 *            收件地址
	 * @param htmlPath
	 *            html文件路径
	 * @param template
	 *            模板参数
	 * @param picturePath
	 *            图片路径数组
	 */
	public MailSend(String emailSubject, String[] emailAddr, String htmlPath, Object[] template, String[] picturePath) {
		this.sendType = "simpleTemplate";
		getMailSend(emailSubject, emailAddr, null, null, htmlPath, template, null, picturePath);
	}

	/**
	 * 无参构造器，其中from、username、password、host、displayName、
	 * needAuth这几个参数可以直接调用私有方法setContextParameters进行设定，也可以手工设定
	 */
	public MailSend() {}

	/**
	 * 发送
	 * 
	 * @throws Exception
	 *             邮件发送异常
	 */
	public void send() throws Exception {
		Transport ts = null;
		try {
			Properties prop = new Properties();
			// prop.setProperty("mail.debug", "true");// 发布模式时请注释掉
			prop.setProperty("mail.host", host);
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.store.protocol", "pop3");
			Session session = null;
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};
			if (this.needAuth) {
				prop.setProperty("mail.smtp.auth", "true");
				session = Session.getInstance(prop, authenticator);
			} else {
				prop.setProperty("mail.smtp.auth", "false");
				session = Session.getInstance(prop, null);
			}
			ts = session.getTransport();
			ts.connect(host, 25, username, password);
			Message message = makeMessage(session);
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();
		} catch (AuthenticationFailedException e) {
			System.out.println("邮件发送失败！错误原因：\n" + "身份验证错误!" + e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("邮件发送失败！错误原因：\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != ts) {
					ts.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 私有的全面构造方法，为其它公有构造方法服务
	 * 
	 * @param emailSubject
	 * @param emailAddr
	 * @param msgcontent
	 * @param attachments
	 * @param htmlPath
	 * @param template
	 * @param htmlContent
	 * @param picturePath
	 */
	private void getMailSend(String emailSubject, String[] emailAddr, String msgcontent, Map<String, String> attachments, String htmlPath, Object[] template, String htmlContent, String[] picturePath) {
		setContextParameters();
		if (null != emailSubject) {
			this.subject = emailSubject;
		}
		if (null != emailAddr) {
			this.to = emailAddr;
		}
		if (null != msgcontent) {
			this.msgcontent = msgcontent;
		}
		if (null != attachments) {
			this.f = attachments;
		}
		if (null != htmlPath) {
			this.htmlPath = htmlPath;
		}
		if (null != template) {
			this.template = template;
		}
		if (null != htmlContent) {
			this.htmlContent = htmlContent;
		}
		if (null != picturePath) {
			this.picturePath = picturePath;
		}
	}

	/**
	 * 根据Session设置Message对象的文本内容、附件内容、发送日期，发送者地址、要显示的发送者名称、多个收件者地址以及邮件主题
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private Message makeMessage(Session session) throws Exception {
		MimeMessage message = new MimeMessage(session);
		MimeMultipart multipart = new MimeMultipart("mixed");// 混合型
		/********* 1.发送纯文本内容 ********/
		if (this.sendType.equals("text")) {
			multipart.addBodyPart(getTextPart(this.msgcontent));
			/********* 2. 发送简单html内容 *********/
		} else if (this.sendType.equals("simpleHtml")) {
			multipart.addBodyPart(getSimpleHtmlPart(this.htmlContent, this.picturePath));
			/********* 3.发送带附件的纯文本内容 ********/
		} else if (this.sendType.equals("atext")) {
			multipart.addBodyPart(getTextPart(this.msgcontent));
			addAttachPart(multipart, this.f);
			/********* 4.发送带附件的简单html邮件 ********/
		} else if (this.sendType.equals("asimpleHtml")) {
			multipart.addBodyPart(getSimpleHtmlPart(this.htmlContent, this.picturePath));
			addAttachPart(multipart, this.f);
			/********* 5.发送带附件的html模板邮件 ********/
		} else if (this.sendType.equals("ahtmlTemplate")) {
			multipart.addBodyPart(getHtmlPart(this.htmlPath, this.template));
			addAttachPart(multipart, this.f);
			/********* 6.发送带附件可用模板的简单邮件 ****/
		} else if (this.sendType.equals("asimpleTemplate")) {
			multipart.addBodyPart(getSimpleTemplatePart(this.htmlPath, this.template, this.picturePath));
			addAttachPart(multipart, this.f);
			/********* 7.发送可用模板的简单邮件 *******/
		} else if (this.sendType.equals("simpleTemplate")) {
			multipart.addBodyPart(getSimpleTemplatePart(this.htmlPath, this.template, this.picturePath));
			/********* 8.直接读取html模板进行发送****** */
		} else {
			multipart.addBodyPart(getHtmlPart(this.htmlPath, this.template));
		}
		message.setContent(multipart, "text/html;charset=utf-8");
		message.setSentDate(new Date()); // 设置信件头的发送日期
		message.setReplyTo(new Address[] { new InternetAddress(this.from) });// 设置回复地址
		Address from_addr = new InternetAddress(this.from, this.displayName, "UTF-8");
		message.setFrom(from_addr);
		/**
		 * =====注意，以下两句的做法很重要，即为message增加一个地址数组，======
		 * 地址内容任意，但若是中文的话需要编码，这样做的目的是为了让收件方在邮件
		 * 列表中直接把设置的displayName显示为邮件名称。如果不这样做的话，即使设
		 * 置了displayName,在收件箱中也不会显示出来。在QQ邮箱中测试时是这样的， 至于原因尚不清楚。
		 */
		Address[] changeDisplayName = { new InternetAddress(MimeUtility.encodeText("显示display名称")) };
		message.addFrom(changeDisplayName);
		int len = this.to.length;
		Address[] address = new InternetAddress[len];
		for (int i = 0; i < len; i++) {
			address[i] = new InternetAddress(to[i]);
		}
		message.addRecipients(Message.RecipientType.TO, address);
		message.setSubject(this.subject);
		message.saveChanges();
		/*** ================暂存邮件至本地================= ***/
		// OutputStream out = new FileOutputStream("D://demo.eml");
		// message.writeTo(out);// 写出到文件
		// out.close();
		// // 想发送时，读取本地文件即可
		// MimeMessage localMsg = new MimeMessage(session, new
		// FileInputStream("D://demo.eml"));
		// Transport.send(localMsg,
		// InternetAddress.parse("xxxxxx@126.com,xxxx@qq.com"));
		/*** ================暂存邮件至本地================= ***/
		return message;
	}

	/**
	 * 生成纯文件的bodyPart
	 * 
	 * @return
	 * @throws MessagingException
	 */
	private BodyPart getTextPart(String text) throws MessagingException {
		BodyPart textPart = new MimeBodyPart();
		textPart.setText(text);
		return textPart;
	}

	/**
	 * 读取本地一个html格式的模板文件来生成一个bodypart
	 * 
	 * @param htmlPath
	 *            html文件的绝对路径
	 * @param template
	 *            用以将html文件中的占位符替换掉
	 * @return BodyPart
	 * @throws MessagingException
	 * @throws IOException
	 */
	private BodyPart getHtmlPart(String htmlPath, Object[] template) throws MessagingException, IOException {
		BodyPart htmlPart = new MimeBodyPart();
		String content = CommonUtils.read(htmlPath).toString();
		if (null != template && template.length > 0) {
			// MsgFormat()方法能够将html文件中原有的大括号加上单引号标记，防止被认为是占位符
			content = MessageFormat.format(MsgFormat(content), template);// 字符串模板替换，详见我的个人微博：http://t.qq.com/yepeiweibo
		}
		htmlPart.setContent(content, "text/html;charset=utf-8");
		return htmlPart;
	}

	/**
	 * 生成带有html标签和图片的简单bodypart
	 * 
	 * @param content
	 *            带有html标记的字符串，如果有图片，则图片的src应当按顺序设置为img0,img1,img2...
	 * @param picturePath
	 *            出现在content中的图片标签的实际图片源数组，如果没有图片，请传递null
	 * @return
	 * @throws MessagingException
	 */
	private BodyPart getSimpleHtmlPart(String content, String picturePath[]) throws MessagingException {
		BodyPart simpleHtmlPart = new MimeBodyPart();
		BodyPart htmlTextPart = new MimeBodyPart(); // 邮件内容
		MimeMultipart htmlMultipart = new MimeMultipart();// html+picture
		htmlMultipart.setSubType("related");
		if (null != picturePath && picturePath.length > 0) {
			// 检查html内容中的src属性值是否是以cid:开关，否则加上,且为src按顺序赋值为img0、img1、img2。。。。
			htmlTextPart.setContent(setHtmlSrc(content), "text/html;charset=utf-8");
			for (int i = 0; i < picturePath.length; i++) {
				BodyPart htmlPicturePart = new MimeBodyPart();
				DataSource picSource = new FileDataSource(picturePath[i]);// 获得图片的数据源
				DataHandler handler = new DataHandler(picSource);
				htmlPicturePart.setDataHandler(handler);
				// htmlPicturePart.setHeader("Content-Location", imgSrc);
				// 设置html中需要引用图片的那个地址
				htmlPicturePart.setHeader("Content-ID", "img" + i);// 注意，html中img标签的src属性值须为“cid:”+imgSrc
				htmlMultipart.addBodyPart(htmlPicturePart);
			}
		} else {
			htmlTextPart.setContent(content, "text/html;charset=utf-8");
		}
		htmlMultipart.addBodyPart(htmlTextPart);
		simpleHtmlPart.setContent(htmlMultipart);
		return simpleHtmlPart;
	}

	/**
	 * 使用模板生成带有html标签和图片的简单bodypart
	 * 
	 * @param htmlPath
	 *            html文件地址
	 * @param template
	 *            模板参数
	 * @param picturePath
	 *            出现在content中的图片标签的实际图片源数组，如果没有图片，请传递null
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private BodyPart getSimpleTemplatePart(String htmlPath, Object[] template, String[] picturePath) throws MessagingException, IOException {
		String content = CommonUtils.read(htmlPath).toString();
		if (null != template && template.length > 0) {
			// MsgFormat()方法能够将html文件中原有的大括号加上单引号标记，防止被认为是占位符
			content = MessageFormat.format(MsgFormat(content), template);// 字符串模板替换，详见我的个人微博：http://t.qq.com/yepeiweibo
		}
		return getSimpleHtmlPart(content, picturePath);
	}

	/**
	 * 向一个multipart中添加附件
	 * 
	 * @param multipart
	 *            MimeMultipart类型对象
	 * @param files
	 *            包含附件绝对路径和附件名的集合
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void addAttachPart(MimeMultipart multipart, Map<String, String> files) throws MessagingException, UnsupportedEncodingException {
		if (!files.isEmpty()) {// 有附件
			for (Map.Entry<String, String> me : files.entrySet()) {// 对每一个附件都要新加一个MimeBodypart
				BodyPart fpart = new MimeBodyPart();
				String fpath = me.getKey();// 附件路径
				String fname = me.getValue();// 附件名
				int index = fpath.lastIndexOf('.');
				if (index < 0) {
					throw new RuntimeException("非法的附件类型！");
				} else {
					if (fname.lastIndexOf(".") < 0) {// 如果附件后不包含文件类型后缀，则自动加上，否则附件在下载之后是未知文件类型
						fname += fpath.substring(index);
					}
				}
				DataSource fSource = new FileDataSource(fpath);// 创建附件源
				fpart.setDataHandler(new DataHandler(fSource));
				fpart.setFileName(MimeUtility.encodeText(fname));// 设置附件名,注：需要编码以解决乱码问题
				multipart.addBodyPart(fpart);
			}
		}
	}

	/**
	 * 从配置文件中读取发件环境参数并设置给MailSend对象
	 */
	private void setContextParameters() {
		try {
			this.from = CommonUtils.getParameter("config/commonData", "MailFrom");
			this.username = CommonUtils.getParameter("config/commonData", "MailUser");
			this.password = CommonUtils.getParameter("config/commonData", "MailPassword");
			this.host = CommonUtils.getParameter("config/commonData", "MailHost");
			this.displayName = CommonUtils.getParameter("config/commonData", "MailDisplayName");
			this.needAuth = CommonUtils.getParameter("config/commonData", "MailNeedAuth") == "true" ? true : false;
		} catch (Exception e) {
			throw new RuntimeException("参数读取错误，详情：" + e.getMessage());
		}
	}

	/**
	 * 检查html内容中的src属性值是否是以cid:开头，否则加上,且为src按顺序赋值为img0、img1、img2...
	 * 
	 * @param HtmlContent
	 */
	private String setHtmlSrc(String HtmlContent) {
		// 查找以<img开头，以>结尾，内包含src="..."的内容
		return CommonUtils.replaceHtmlTag(HtmlContent, "img", "src", "cid:img", true);
	}

	/**
	 * 将字符串中非“{非纯数字}”标记的大括号两边加上单引号，这样可使MessageFormat工具顺利将html文件中的原有“{}”原样输出
	 * 
	 * @param s
	 * @return
	 */
	private String MsgFormat(String s) {
		String regex = "\\{[^\\{\\}]*?\\D[^\\{\\}]*?\\}";
		StringBuffer str = new StringBuffer().append(s);
		Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(str);
		while (m.find()) {
			str = str.replace(m.start(), m.end(), "'" + m.group() + "'");
		}
		return str.toString();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	public Map<String, String> getF() {
		return f;
	}

	public void setF(Map<String, String> f) {
		this.f = f;
	}

	public Object[] getTemplate() {
		return template;
	}

	public void setTemplate(Object[] template) {
		this.template = template;
	}

	public String[] getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String[] picturePath) {
		this.picturePath = picturePath;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
}
