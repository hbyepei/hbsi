package whu.b606.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import whu.b606.entity.User;

/**
 * 需要session才能访问相应的JSP文件
 * 
 * @author yepei
 * 
 */
public class SessionFilter implements Filter {
	private List<String> list = new ArrayList<String>();

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String servletPath = request.getServletPath();
		for (String url : list) {
			if (servletPath.indexOf(url) > -1) {// 需要过滤
				User u = (User) request.getSession().getAttribute("user");// 系统用户的session
				if (null == u) {// session不存在需要拦截
					// request.setAttribute("msg",
					// "对不起，连接已失效，可能是由于未登录或因长时间未操作！");
					String path = request.getContextPath();
					response.sendRedirect(path + "/index.jsp");
					return;
				}
				break;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化需要拦截的文件夹
		String include = filterConfig.getInitParameter("include");
		if (!StringUtils.isBlank(include)) {
			StringTokenizer st = new StringTokenizer(include, ",");
			list.clear();
			while (st.hasMoreTokens()) {
				list.add(st.nextToken());
			}
		}
	}
}
