package whu.b606.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import whu.b606.util.MailSend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	// 为子类Action提供request和response对象
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, Object> session = ActionContext.getContext().getSession();
	protected static SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};

	/**
	 * 设置request
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 设置response
	 * 
	 * @param response
	 *            HttpServletResponse对象
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		response.setCharacterEncoding("UTF-8");
	}

	/**
	 * 将对象以json字符串格式通过response写回
	 * 注：当加上了禁止循环引用的检测机制时，则会自动对循环引用进行检查，如果有循环引用则会报堆栈溢出的错误
	 * 对多对多关联的实体，如果无须持久化某一属性，则可以在get方法上加上@JSONField(serialize=false)的注解，以避免循环引用
	 * 请注意：@Transient注解或字段上的transient注解虽然也可保存不持久化此属性，但这表示此属性是短暂的，不会被保存到数据库中，
	 * 而@JSONField则指在取数据构造json串时，不包括此属性。因此在页面端做数据显示时，这将非常有用！
	 * 
	 * @param obj
	 *            对象
	 */
	public void WriteJson(Object obj) {
		WriteString(getJson(obj));
	}

	public String getJson(Object obj) {
		return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss");
	}

	public void WriteString(String jsonString) {
		try {
			PrintWriter out = response.getWriter();
			out.write(jsonString);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMail(List<MailSend> ms) {
		if (ms != null && !ms.isEmpty()) {
			for (MailSend m : ms) {
				sendMail(m);
			}
		}
	}

	public void sendMail(MailSend m) {
		if (m != null) {
			m.setDaemon(true);
			m.start();
		}
		// int time = 0;
		// while ((null != m && m.isAlive()) && time < 5000) {//
		// 如果邮件线程还在运行，则暂缓此方法的结束，否则此方法的过早结束会导致邮件线程的死掉
		// try {
		// Thread.sleep(100);// 每0.1秒检查一次邮件是否发送完成
		// time += 100;// 主线程等待时间不超过5秒
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}

	/**
	 * 清除session中无用的数据
	 * 
	 * @param params
	 *            session中的参数名称数组
	 */
	public void cleanSession(String... params) {
		if (params.length > 0) {
			for (String s : params) {
				if (null != session.get(s)) {
					session.remove(s);
				}
			}
		}
	}
}
