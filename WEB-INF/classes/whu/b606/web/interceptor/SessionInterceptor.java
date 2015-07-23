package whu.b606.web.interceptor;

import org.apache.struts2.ServletActionContext;

import whu.b606.entity.User;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 对需要Session的Action访问进行拦截
 * 
 * @author yepei
 */
public class SessionInterceptor extends MethodFilterInterceptor {
	private static final long serialVersionUID = 77375108194894301L;

	@Override
	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		User u = (User) ServletActionContext.getRequest().getSession().getAttribute("user");
		if (null == u) {
			String errMsg = "对不起，连接已失效，可能是由于未登录或因长时间未操作！";
			ServletActionContext.getRequest().setAttribute("msg", errMsg);
			return "noSession";
		}
		return actionInvocation.invoke();
	}
}
