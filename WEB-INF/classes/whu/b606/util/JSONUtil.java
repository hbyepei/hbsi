package whu.b606.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

/**
 * 对象转JSON并输出
 * 
 * @author Yepei,Wuhan University
 * @version 1.0
 * @date 2015年4月1日
 **/
public class JSONUtil {
	public static void WriteJson(Object obj, HttpServletResponse response) {
		WriteString(getJson(obj), response);
	}

	private static String getJson(Object obj) {
		return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss");
	}

	private static void WriteString(String jsonString, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.write(jsonString);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
