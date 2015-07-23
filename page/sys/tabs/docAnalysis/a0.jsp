<%@page import="java.awt.Color"%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@page
import=" java.awt.Font"
import="java.awt.Color"
import=" java.io.IOException" 
import=" java.io.PrintWriter" 
import=" java.text.DecimalFormat" 
import=" java.text.NumberFormat" 
import="  java.util.Iterator" 
import=" java.util.Map" 
import=" java.util.LinkedHashMap" 
import="  java.util.Map.Entry" 
import="  org.jfree.chart.ChartFactory" 
import="  org.jfree.chart.ChartRenderingInfo" 
import="  org.jfree.chart.ChartUtilities" 
import="  org.jfree.chart.JFreeChart" 
import="  org.jfree.chart.entity.StandardEntityCollection" 
import="  org.jfree.chart.labels.StandardPieSectionLabelGenerator" 
import="  org.jfree.chart.labels.StandardPieToolTipGenerator" 
import="  org.jfree.chart.plot.PiePlot" 
import=" org.jfree.chart.servlet.ServletUtilities" 
import="  org.jfree.chart.title.LegendTitle" 
import="  org.jfree.chart.title.TextTitle" 
import="  org.jfree.data.general.DefaultPieDataset" 
import=" whu.b606.dto.Status" 
import="whu.b606.util.JFreeChartUtils"
import="  com.alibaba.fastjson.JSON" 
import=" org.jfree.chart.plot.PiePlot3D"
%>
<!-- 该页面接收一个以post方式传过来的名为params的已经序列化成字符串多个参数，在JS中取到后直接是一个js对象-->
<%!
String explode=null;
private  DefaultPieDataset getDataSet(Map<String,Object> map) throws Exception {
	if (map != null && !map.isEmpty()) {
		DefaultPieDataset data = new DefaultPieDataset();
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> en = it.next();
			String key = en.getKey();
			if (!key.equals("total") && !key.equals("succ")) {
				String[] val = en.getValue().toString().split("#");
				if (val.length > 1) {
					data.setValue(val[0], Double.parseDouble(val[1]));
					if(key.equals(Status.complete.toString())){
						explode=val[0];
					}
				}
			}
		}
		return data;
	}
	return null;
}
%>
<%
	String imgSrc="";
	String filename ="";
	try {
		String p=request.getParameter("p");
		LinkedHashMap<String,Object> map=JFreeChartUtils.parseMap(p);
		DefaultPieDataset dataSet = getDataSet(map);
		if (dataSet != null) {
			JFreeChart chart = ChartFactory.createPieChart("任务状态分布", // 图表标题
					dataSet, // 数据集
					true, // 是否包含图例
					true, // 是否包含提示工具
					false// 是否包含url
					);
			// 设置标题字体信息
			chart.setTitle(new TextTitle("任务状态分布", new Font("微软雅黑", Font.BOLD, 16)));
			chart.setBackgroundPaint(new Color(242, 245, 247));// 设置整张图的背景颜色
			LegendTitle legend = chart.getLegend(0);// 图例
			legend.setItemFont(new Font("宋体", Font.BOLD, 12));
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setLabelFont(new Font("仿宋", Font.PLAIN, 14));
			plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}:({1}件)", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));
			plot.setIgnoreZeroValues(true);
			plot.setIgnoreNullValues(true);
			plot.setOutlineVisible(false);
			plot.setBackgroundAlpha(0);
			plot.setNoDataMessage("无数据");
			// 设置标签样式，{0}表示选项,{1}表示数字,{2}表示比例
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));
			if(explode!=null){
				plot.setExplodePercent(explode, 0.3);// 分离圆弧，注意爆炸模式不支持3D
			}
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 300,267, info, session);//宽高比例应该和<img/>标签中定义的一致
			PrintWriter writer = new PrintWriter(out);
			ChartUtilities.writeImageMap(writer,"a0_chart" , info, false);
			imgSrc =request.getContextPath()+"/sys/chartAction.action?filename="+filename;
		} else {
			System.out.println("未能成功生成统计图");
		}
	} catch (Exception e) {
		System.out.println("构造DataSet对象产生了异常");
	}
// 	out.clear();
// 	out = pageContext.pushBody();
%>

<style>
#a0_table {
	width: 100%;
	height: 100%
}

#a0_table tr {
	line-height: 50%;
	min-height: 80px;
}

.row_1 {
	font-size: 24px;
	color: #444;
}

.row_2 {
	vertical-align: baseline;
	padding-top: 15px;
	font-size: 50px;
	font-weight: bolder;
}

.col_1 {
	text-align: right;
	width: 40%;
	height: 50%;
}

.col_2 {
	text-align: center;
	width: 60%;
	height: 50%;
}
</style>
<div style="width: 50%;float: left;">
	<table id="a0_table">
		<tr>
			<td class="col_1 row_1"><img src="../../js/easyui/themes/icons/yp_allTask_big.png" style="vertical-align: middle;margin-right:5px;" />任务总量</td>
			<td class="col_2 row_1"><img src="../../js/easyui/themes/icons/yp_ok_big.png" style="vertical-align: middle; margin-right: 5px;" />成功处理</td>
		</tr>
		<tr>
			<td class="col_1 row_2" style="color: #D08C3C;padding-right: 20px;" id="a0_total"></td>
			<td class="col_2  row_2" style="color: #14C020" id="a0_succ"></td>
		</tr>
	</table>
</div>
<div style="float: left;text-align: right">
	 <img src="<%=imgSrc %>" usemap="#a0_chart"  style="cursor: pointer;" width="90%" height="80%" />
</div>
<script>
	var p = ${param['p']};
	var unit = "<span style='font-size:16px;color:#444;margin-left:5px;'>次</span>";
	$("#a0_total").html(p.total + unit);
	$("#a0_succ").html(p.succ + unit);
</script>