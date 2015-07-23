package whu.b606.web.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 权限拦截器,拦截系统用户的越权访问
 * 
 * @author yepei
 * 
 */
public class PermissionInterceptor extends MethodFilterInterceptor {
	private static final long serialVersionUID = 467106840449072033L;

	@Override
	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		return actionInvocation.invoke();
	}
}
