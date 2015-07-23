<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="
java.awt.Color,
java.util.Map,
org.jfree.data.category.DefaultCategoryDataset,
whu.b606.util.JFreeChartUtils
"%>
<%
String imgSrc="";
String usemap="#b4_chart";
try { 
	Map<String,Object> map=JFreeChartUtils.getDataSetAndDimense(JFreeChartUtils.parseMap(request.getParameter("p")));
	if(map!=null){
		int width = (int)map.get("width");
		int height = (int)map.get("height");
		DefaultCategoryDataset dataSet = (DefaultCategoryDataset)map.get("dataSet");
		int rows=(int)map.get("rows");
		imgSrc=JFreeChartUtils.paintChart(request, session, out, dataSet,rows, width, height, "终止调解的申请信息", Color.MAGENTA,usemap);
	}else{
		System.out.println("集合为空！（b4.jsp-line19）");
		imgSrc=JFreeChartUtils.paintChart(request, session, out, null, 0,-1, -1, "终止调解的申请信息", Color.MAGENTA,usemap);
	}
} catch (Exception e) {
	System.out.println("构造统计图时产生了异常");
}
%>
<div>
	<img src="<%=imgSrc%>" usemap="<%=usemap%>" style="cursor: pointer;" />
</div>
