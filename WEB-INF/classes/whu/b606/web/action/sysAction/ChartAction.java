package whu.b606.web.action.sysAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import whu.b606.web.action.BaseAction;

/**
 * 此Action专为图表服务，以使得DisplayChart.servlet能够正常被访问，而不是被Struts2拦截掉
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月8日
 * 
 **/
@Action("chartAction")
@Namespace("/sys")
@ParentPackage("common")
// 转发到org.jfree.chart.servlet.DisplayChart，由此Servlet进行对临时图片的访问
@Result(location = "/DisplayChart")
public class ChartAction extends BaseAction {
	private static final long serialVersionUID = -7027978697332057803L;

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
}
