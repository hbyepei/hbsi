<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="java.util.Map,java.util.HashMap,whu.b606.entity.User,whu.b606.entity.Admin,whu.b606.entity.Expert"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
	Cookie[] cookies = request.getCookies();
	if (null != cookies) {
		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie);
		}
	}
String easyuiTheme = "ui-cupertino";//指定如果用户未选择样式，那么初始化一个默认样式
String headerbg="bg_bootstrap";//需要跟随主题变化的背景图片
String headerLogo="logo_black";//Logo图片
String color_white="fff";//欢迎文字颜色
String color_black="020202";//系统标题颜色
if (cookieMap.containsKey("easyuiTheme")) {
	Cookie cookie = (Cookie) cookieMap.get("easyuiTheme");
	easyuiTheme = cookie.getValue();
}
if (cookieMap.containsKey("headerbg")) {
	Cookie cookie = (Cookie) cookieMap.get("headerbg");
	headerbg = cookie.getValue();
}
if (cookieMap.containsKey("headerLogo")) {
	Cookie cookie = (Cookie) cookieMap.get("headerLogo");
	headerLogo = cookie.getValue();
}
if (cookieMap.containsKey("color_white")) {
	Cookie cookie = (Cookie) cookieMap.get("color_white");
	color_white = cookie.getValue();
}
if (cookieMap.containsKey("color_black")) {
	Cookie cookie = (Cookie) cookieMap.get("color_black");
	color_black = cookie.getValue();
}
%>
	<script src="../../js/jquery-1.8.3.min.js"></script>
	<script src="../../js/easyui/jquery.easyui.min.js" charset="UTF-8" type="text/javascript"></script>
	<script src="../../js/easyui/locale/easyui-lang-zh_CN.js" charset="UTF-8" type="text/javascript"></script>
	<script src="../../js/jquery-form.js" charset="UTF-8" type="text/javascript"></script>
	<script src="../../js/webutils.js" charset="UTF-8" type="text/javascript"></script>
	<script src="../../js/easyuiUtil.js" charset="UTF-8" type="text/javascript"></script>
	<script src="../../js/layer/layer.min.js"></script>
	<script src="../../js/laydate/laydate.js"></script>
	<link rel="stylesheet" href= "../../js/easyui/themes/icon.css" />
	<link  id="easyuiTheme"  rel="stylesheet" href="../../js/easyui/themes/<%=easyuiTheme%>/easyui.css"/>
	<%
	User user=(User)session.getAttribute("user");
	String userCss="admin.css";
	session.setAttribute("logintype", "Admin");
	if(user==null){
		response.sendRedirect("../../index.jsp");
	}else if(user instanceof Expert){
		session.setAttribute("logintype", "Expert");
		userCss="expert.css";
	}
	%>
	
	<link rel="stylesheet" href="../../css/<%=userCss%>"/>
	
<style>
*{font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;}
	/**以下用于控制当表格内空较长时，用省略号截尾(三个属性缺一不可) **/
table tr td{text-overflow : clip;white-space:nowrap;overflow:hidden;}
</style>
<script>
$(function(){
	var theme=yp.cookie('easyuiTheme');
	if(!theme){yp.changeTheme('ui-cupertino');}
	$(window).keydown(function(event) {
		// 如果按下的是退格键
		if (event.keyCode == 8) {
			var tagName = event.target.localName;
			// 如果是在textarea内不执行任何操作
			if (tagName.toLowerCase() == "input" || tagName.toLowerCase() == "textarea" || tagName.toLowerCase() == "select") {
				event.returnValue = true;
			} else {
				event.returnValue = false;
			}
			// 如果是readOnly或者disable不执行任何操作
			var rd = $(event.target).attr("readOnly");
			var da = $(event.target).attr("disabled");
			if (rd && rd.toLowerCase == 'readonly') {
				event.returnValue = false;
			}
			if (da && da.toLowerCase == 'disabled') {
				event.returnValue = false;
			}
		}
	});
});

</script>