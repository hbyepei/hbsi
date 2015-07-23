package whu.b606.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import whu.b606.exception.NoFileException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 通用工具类，包涵读取属性文件工具、生成UUID工具、文件批量上传工具、文件批量删除工具、MD5码工具、对象转JSON字符串工具、
 * 修改html标签中指定属性值工具.......
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 */
public class CommonUtils {
	/**
	 * 生成一个唯一的标号UUID
	 * 
	 * @return uuid号
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 读取配置文件
	 * 
	 * @param parameterName
	 *            配置文件中的参数名称
	 * @param filename
	 *            类路径下的配置文件名，不包括后缀
	 * @return 参数值
	 */
	public static String getParameter(String filename, String parameterName) {
		try {
			Properties props = new Properties();
			if (!filename.endsWith(".properties")) {
				filename += ".properties";
			}
			InputStream in = CommonUtils.class.getClassLoader().getResourceAsStream(filename);
			try {
				props.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String result = props.getProperty(parameterName);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("deprecation")
	/**
	 * 生成案例编号
	 * @return 案例编号
	 */
	public static String makeCaseId() {
		Date d = new Date();
		StringBuffer buf = new StringBuffer();
		buf.append(d.getYear() + 1900);
		buf.append((d.getMonth() + 1) < 10 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1));
		buf.append(d.getDate());
		buf.append(d.getHours());
		buf.append(d.getMinutes());
		int radom = new Random().nextInt(900) + 100;
		return "AP." + buf.toString() + radom;
	}

	/**
	 * 生成一个随机查询码
	 * 
	 * @return 8位数随机查询码
	 */
	public static String makeQueryCode() {
		// 生成一个8位数随机查询码告诉用户
		String code = "";
		for (int i = 0; i < 8; i++) {
			code += RandomUtils.nextInt(10);
		}
		return code;
	}

	/**
	 * 将文件数组上传至指定目录
	 * 
	 * @param savePath
	 *            保存路径
	 * @param files
	 *            文件数组
	 * @param upfileFileName
	 *            与文件数组中的文件名对应的文件名数组
	 * @return 返回一个Map集合，其中的key代表文件的随机实际名称，value代表文件的自然名称
	 * @throws IOException
	 *             IO异常
	 */
	public static Map<String, String> fileUpload(String savePath, File[] files, String[] upfileFileName) throws IOException {
		if (files != null && files.length > 0) {
			int length = files.length;
			String[] realFileName = new String[length];
			Map<String, String> fm = new HashMap<String, String>();
			File dir = new File(savePath);
			if (!dir.exists()) {// 如果上传目录不存在，则创建
				dir.mkdir();
			}
			for (int i = 0; i < length; i++) {
				int index = upfileFileName[i].lastIndexOf('.');
				if (index != -1) {// 如果文件有扩展名，则以时间值作为文件名
					realFileName[i] = UUID.randomUUID().toString() + upfileFileName[i].substring(index);
				} else {// 如果没有扩展名，则以随机以随机码作为文件名
					realFileName[i] = UUID.randomUUID().toString();
				}
				fm.put(realFileName[i], upfileFileName[i]);// 将自然文件名和实际文件名保存起来
				FileOutputStream fos = null;
				FileInputStream fis = null;
				// 读取保存在临时目录下的文件，写入新的文件中
				try {
					fos = new FileOutputStream(savePath + realFileName[i]);
					fis = new FileInputStream(files[i]);
					byte[] buffer = new byte[2048];
					int len = 0;
					while ((len = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
					fis.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fis) {
							fis.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (null != fos) {
							fos.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return fm;
		}
		return null;
	}

	/**
	 * 将指定目录下的文件删除
	 * 
	 * @param fullFilename
	 *            待删除文件的完整路径或目录
	 * @param delay
	 *            延迟删除的时间，默认是6000毫秒，若给-1，则使用默认时间
	 * @throws NoFileException
	 *             文件删除异常
	 */
	public static void deleteFile(final String fullFilename, final long delay) {// 注，此filename是包含文件类型后缀的
		long t = delay;
		if (t < 0) {
			t = 6000l;
		}
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {// 立即删除的话，可能会因为文件占用而无法成功删除，所以可在一段时间后再删除
			@Override
			public void run() {
				File dir = new File(fullFilename);// 注，这一句不能在run方法之外，否则在run执行时，文件将一直被占用
				if (!dir.exists()) {// 如果文件不存在
					try {
						throw new NoFileException();
					} catch (NoFileException e) {
						System.out.println(e.getMessage());
					}
				} else if (dir.isFile()) {
					boolean succ = dir.delete();
					if (!succ) {
						CommonUtils.deleteFile(fullFilename, delay);
					}
				} else if (dir.isDirectory()) {// 如果是目录
					String[] children = dir.list();// 递归删除目录中的子目录下
					if (children.length > 0) {
						for (int i = 0; i < children.length; i++) {
							String subName = fullFilename;
							if (fullFilename.endsWith("/") || fullFilename.endsWith("\\")) {
								subName += children[i];
							} else {
								subName += System.getProperty("line.separator") + children[i];
							}
							CommonUtils.deleteFile(subName, delay);
						}
					} else {
						boolean succ = dir.delete();
						if (!succ) {
							CommonUtils.deleteFile(fullFilename, delay);
						}
					}
				}
			}
		};
		timer.schedule(tt, t);
	}

	/**
	 * 计算一组文件的大小
	 * 
	 * @param upfile
	 *            文件数组
	 * @return 字节数(B)
	 */
	public static int TotalfileSize(File[] upfile) {
		int len = 0;
		for (int i = 0; i < upfile.length; i++) {
			len += upfile[i].length();
		}
		return len;
	}

	/**
	 * 检验给出的文件类型是否是允许的文件类型：doc/docx/xls/xlsx/ppt/pptx/wps/et/dps/txt/pdf/jpg/
	 * jpeg/png/gif/bmp/
	 * 
	 * @param upfileContentType
	 *            待上传文件的类型数组
	 * @param filenames
	 *            文件名数组
	 * @param fileType
	 *            文件所属的大类型，例如："common"表示通用类型，可以包括常用的各种文档类型，"image"表示图片类型，只包括jpg/
	 *            png/gif/bmp等代表图片的类型
	 * @param configFile
	 *            类路径下的配置文件的文件名，不包括后缀，如“config/commonData”
	 * @return 不合法的类型的文件索引值
	 */
	public static int allowedFileTypes(String[] upfileContentType, String[] filenames, String fileType, String configFile) {// common,image
		String s = getParameter(configFile, fileType + "_fileType");
		String ext = getParameter(configFile, fileType + "_fileExt");
		String t = "";
		for (int i = 0; i < upfileContentType.length; i++) {
			t = filenames[i].substring(filenames[i].lastIndexOf(".") + 1);
			if ((!s.contains(upfileContentType[i])) || (!ext.contains(t))) { return i; }
		}
		return -1;
	}

	/**
	 * 生成字符串的MD5码
	 * 
	 * @param password
	 *            待编码的字符串
	 * @return 对应MD5值
	 */
	public static String makeMD5(String password) {
		MessageDigest md;
		try {
			// 生成一个MD5加密计算摘要
			md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(password.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String pwd = new BigInteger(1, md.digest()).toString(16);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	/**
	 * 获得实体的默认简单类名， 如果类名被注解更改过了，则返回更改过后的类名
	 * 
	 * @param <T>
	 *            代表对象的实际类型的泛型参数
	 * @param entityClass
	 *            类型
	 * @return 实体的实际名称
	 */
	public static <T> String getEntityName(Class<T> entityClass) {
		String entityName = entityClass.getSimpleName();// 得到的是实体的默认简单类名
		Entity entity = entityClass.getAnnotation(Entity.class);// 如果类名被注解更改过了，则返回更改过后的类名
		if (entity.name() != null && !"".equals(entity.name())) {
			entityName = entity.name();
		}
		return entityName;
	}

	/**
	 * 将对象转换成JSON字符串
	 * 
	 * @param o
	 *            对象
	 * @return 对象转换成JSON格式后的字符串
	 */
	public static String getJsonString(Object o) {
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator jg = new JsonFactory().createGenerator(sw);
			om.writeValue(jg, o);
			jg.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * 添加Cookie
	 * 
	 * @param params
	 *            包含需要保存到cookie中的键值对
	 * @param response
	 *            HttpServletResponse对象
	 * @param request
	 *            HttpServletRequest对象
	 * @throws UnsupportedEncodingException
	 *             不支持的编码格式异常
	 */
	public static void addCookie(Map<String, Object> params, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
		if (params.size() < 1) {
			return;
		} else {
			for (Map.Entry<String, Object> me : params.entrySet()) {
				Cookie cookie = new Cookie(me.getKey(), java.net.URLEncoder.encode(me.getValue().toString(), "utf-8"));
				cookie.setPath(request.getContextPath() + "/");
				cookie.setMaxAge(0);
				cookie.setMaxAge(7 * 24 * 60 * 60);
				// 加入Cookie到响应头
				response.addCookie(cookie);
			}
		}
	}

	/**
	 * 将日期格式化成指定模式的字符串格式。 SimpleDateFormat函数语法如下： 常见的模式如： "yyyy-MM-dd",
	 * "yyyy-MM-dd HH:mm","yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd",
	 * "yyyy/MM/dd-HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd",
	 * "yyyy.MM.dd-HH:mm","yyyy.MM.dd HH:mm:ss", "yyyy年MM月dd日 HH时mm分ss秒",
	 * "yyyy年MM月dd日 HH时mm分ss秒 E", "yyyy年MM月dd日 HH时mm分", "yyyy年MM月dd日" 。 【说明：G
	 * 年代标志符， y 年， M 月， d 日， h 时 在上午或下午 (1~12)， H 时 在一天中 (0~23)， m 分， s 秒， S 毫秒，
	 * E 星期， D 一年中的第几天， F 一月中第几个星期几， w 一年中第几个星期， W 一月中第几个星期， a 上午 / 下午 标记符 ， k 时
	 * 在一天中 (1~24)， K 时 在上午或下午 (0~11)， z 时区】
	 * 
	 * @param date
	 *            待转换的日期对象
	 * @param pattern
	 *            目标格式的模式 ，如："yyyy-MM-dd HH:mm:ss"
	 * @return 符合特定格式的日期字符串
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat Fmt = new SimpleDateFormat(pattern);
		return Fmt.format(date);
	}

	/**
	 * 读取一个html文件
	 * 
	 * @param path
	 *            html文件的存放路径
	 * @return StringBuffer对象
	 * @throws IOException
	 *             IO异常
	 */
	public static StringBuffer read(String path) throws IOException {
		StringBuffer str = new StringBuffer("");
		File file = new File(path);
		String temp = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((temp = br.readLine()) != null) {
				temp.replaceAll("  ", " ");// 不留多余空格
				str.append(temp + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				br.close();
			}
		}
		return str;
	}

	/**
	 * 获得当前工程的根路径:D:\projWorkSpace\proj
	 * 注意，当此方法在web环境下被调用时，得到的结果可能不一样，此时路径中的空格会被替换成%20
	 * 而这样的路径在本地是无效的，所以还需要检查路径中是否包含“%20”，若有则替换成空格
	 * 
	 * @return 当前工程的根路径
	 */
	public static String getProjectURI() {
		String path = new File("").getAbsolutePath();
		if (path.contains("%20")) {
			path = path.replaceAll("%20", " ");
		}
		return path;
	}

	/**
	 * 当前工程的classpath路径:D:/projWorkSpace/proj/WebRoot/WEB-INF/classes
	 * 
	 * @return 当前工程的classpath路径
	 */
	public static String getProjectClassPath() {
		String path = CommonUtils.class.getResource("/").getPath();
		if (path.contains("%20")) {
			path = path.replaceAll("%20", " ");
		}
		return path.substring(1, path.length() - 1);
	}

	/**
	 * 当前工程的WEB-INF路径:D:/projWorkSpace/proj/WebRoot/WEB-INF
	 * 
	 * @return 当前工程的WEB-INF路径
	 */
	public static String getProjectWEBINF() {
		String path = CommonUtils.class.getResource("/").getPath();
		if (path.contains("%20")) {
			path = path.replaceAll("%20", " ");
		}
		return path.substring(1, path.length() - 9);
	}

	/**
	 * 当前工程的Web-Root路径:D:/projWorkSpace/proj/WebRoot
	 * 
	 * @return 当前工程的Web-Root路径
	 */
	public static String getProjectWebRoot() {
		String path = CommonUtils.class.getResource("/").getPath();
		if (path.contains("%20")) {
			path = path.replaceAll("%20", " ");
		}
		return path.substring(1, path.length() - 17);
	}

	/**
	 * 替换指定的标签的属性值
	 * 
	 * @author yepei
	 * @param str
	 *            待替换字符串
	 * @param tagName
	 *            要替换的标签
	 * @param tagAttrib
	 *            要替换的标签属性
	 * @param attribValue
	 *            新的属性值
	 * @param auto
	 *            是否自动为每一个新属性添加计数后缀：img0、img1、img2……
	 * @return 替换后的html内容字符串
	 */
	public static String replaceHtmlTag(String str, String tagName, String tagAttrib, String attribValue, boolean auto) {
		// String regxpForTag = "<//s*" + beforeTag + "//s+([^>]*)//s*>" ;
		String regxpForTag = "<\\s*" + tagName + "\\s+([^>]*)\\s*";
		String regxpForTagAttrib = tagAttrib + "=\\s*\"([^\"]*)\"";
		// Pattern.CASE_INSENSITIVE 忽略大小写
		Pattern patternForTag = Pattern.compile(regxpForTag, Pattern.CASE_INSENSITIVE);
		Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib, Pattern.CASE_INSENSITIVE);
		Matcher matcherForTag = patternForTag.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherForTag.find();
		int count = 0;
		// 循环找出每个 img 标签
		while (result) {
			String ext = "";
			if (auto) {
				ext += count++;
			}
			StringBuffer sbreplace = new StringBuffer("<" + tagName + " ");
			Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
			if (matcherForAttrib.find()) {
				matcherForAttrib.appendReplacement(sbreplace, tagAttrib + "=\"" + attribValue + ext + "\"");
			}
			matcherForAttrib.appendTail(sbreplace);
			matcherForTag.appendReplacement(sb, sbreplace.toString());
			result = matcherForTag.find();
		}
		matcherForTag.appendTail(sb);
		return sb.toString().trim();
	}

	/**
	 * 对象判空
	 * 
	 * @param obj
	 *            待判断的对象
	 * @return 若对象为空，或者当对象的字符序列对象且为空字符时返回真，否则返回假
	 */
	public static boolean isNull(Object obj) {
		return null == obj || "".equals(obj);
	}

	/**
	 * 高亮显示json字符串中的特定文本
	 * 
	 * @param json
	 * @param searchValue
	 * @return
	 */
	public static String emphasize(String json, String searchValue) {
		if (!CommonUtils.isNull(searchValue)) {
			String newStr = "<label class='emphasize'>" + searchValue + "</label>";
			json = json.replace(searchValue, newStr);
		}
		return json;
	}

	/**
	 * 替换Word模板中的文字，返回输入流
	 * 
	 * @param filepath
	 *            文件路径
	 * @param newValues
	 *            oldValue-newValue键值对
	 * @return
	 * @throws IOException
	 */
	public static InputStream getDocInputStream(String filepath, Map<String, String> newValues) throws IOException {
		InputStream ins;
		ByteArrayOutputStream out = null;
		try {
			FileInputStream in = new FileInputStream(filepath);
			HWPFDocument hdt = new HWPFDocument(in);
			Range range = hdt.getRange();
			for (Map.Entry<String, String> me : newValues.entrySet()) {
				String key = me.getKey();
				String value = me.getValue();
				if (value == null) {
					value = "";
				}
				range.replaceText(key, value);
			}
			out = new ByteArrayOutputStream();
			hdt.write(out);
			ins = new ByteArrayInputStream(out.toByteArray());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ins;
	}
}
