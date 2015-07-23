<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="
 java.io.PrintWriter,
 java.awt.Font,
 java.awt.Color,
 java.util.Map,
 org.jfree.chart.axis.CategoryAxis,
org.jfree.chart.axis.NumberAxis,
org.jfree.chart.axis.NumberTickUnit,
 org.jfree.chart.ChartFactory,
 org.jfree.chart.ChartRenderingInfo,
 org.jfree.chart.ChartUtilities,
 org.jfree.chart.JFreeChart,
 org.jfree.chart.entity.StandardEntityCollection,
 org.jfree.chart.plot.CategoryPlot,
 org.jfree.chart.plot.PlotOrientation,
 org.jfree.chart.servlet.ServletUtilities,
 org.jfree.chart.title.TextTitle,
 org.jfree.data.category.CategoryDataset,
 org.jfree.data.category.DefaultCategoryDataset,
 org.jfree.chart.renderer.category.BarRenderer3D,
 whu.b606.util.JFreeChartUtils
" %>
<%!
 int width = 300;
 int height = 260;
 String filename = "";
 String imgSrc = "";
 private static CategoryDataset getDataSet(Map<String,Object> map) {
		try {
			if (map != null && !map.isEmpty()) {
				DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
				Object ccount=map.get("ccount");
				Object sscout=map.get("scount");
				dataSet.addValue(Double.parseDouble(ccount.toString()), "消费者","消费者");
				dataSet.addValue(Double.parseDouble(sscout.toString()), "商家","商家");
				return dataSet;
			}
		} catch (Exception e) {
			System.out.println("数据构造失败!");
		}
		return null;
	}
%>
<%
try {
	CategoryDataset dataSet = getDataSet(JFreeChartUtils.parseMap( request.getParameter("p")));
	JFreeChart chart = ChartFactory.createBarChart3D(//
			"用户类别统计",// 标题
			"",// 目录轴的显示标签
			"次数",// 数值轴的显示标签
			dataSet, // 数据集
			PlotOrientation.VERTICAL, // 图表方向
			false, // 显示图例
			true, // 生成提示
			false// 生成URL链接
			);
	chart.setTitle(new TextTitle("用户类别信息", new Font("微软雅黑", Font.PLAIN, 16)));
	chart.setBackgroundPaint(new Color(242, 245, 247));// 设置整张图的背景颜色
	CategoryPlot plot = chart.getCategoryPlot();// 图表区域对象
	plot.setOutlineVisible(false);
	plot.setNoDataMessage("没有数据");
	plot.setBackgroundAlpha(0);
	CategoryAxis h_axis = plot.getDomainAxis();// 横轴
	NumberAxis v_axis = (NumberAxis) plot.getRangeAxis();// 纵轴
	v_axis.setLabelFont(new Font("宋体", Font.BOLD, 14));
	v_axis.setLowerBound(0);//要显示的开始处的最小值
	 v_axis.setAutoTickUnitSelection(true);
	v_axis.setTickUnit(new NumberTickUnit(1));
	h_axis.setLabelFont(new Font("宋体", Font.BOLD, 12));
	h_axis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
	h_axis.setLowerMargin(0.2);//设置距离图片左端距离此时为10%
	h_axis.setUpperMargin(0.2);//设置距离图片右端距离此时为百分之10
	h_axis.setCategoryLabelPositionOffset(10);//图表横轴与标签的距离(10像素)
	BarRenderer3D renderer= new BarRenderer3D();
    renderer.setItemMargin(0.1);//组内柱子间隔为组宽的10%
    plot.setRenderer(renderer);//使用我们设计的效果
	ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
	filename = ServletUtilities.saveChartAsPNG(chart, width, height, info, session);
	PrintWriter writer = new PrintWriter(out);
	ChartUtilities.writeImageMap(writer, "a1_chart", info, false);
	imgSrc = request.getContextPath() + "/sys/chartAction.action?filename=" + filename;
} catch (Exception e) {
	System.out.println("构造统计图时产生了异常");
}
%>
<style>
#a1_div{float: left;text-align: left;padding:8% 0 0 10%;}
#a1_div img{vertical-align: middle;}
#a1_div span{vertical-align: middle;}
</style>
<div style="width: 50%;height:80%;float: left;">
	 <img src="<%=imgSrc%>" usemap="#a1_chart"  style="cursor: pointer;" width="100%" height="100%" />
</div>
<div id="a1_div">
	<div>
		<img  src="../../js/easyui/themes/icons/yp_consumer_big.png"/>
		<span >&nbsp;消费者(家用车车主):</span>
		<label id="a1_ccount"></label>
	</div>
	<div style="padding-top: 20px;">
		<img  src="../../js/easyui/themes/icons/yp_saler_big.png"/>
		<span >&nbsp;修理商/销售商/制造商:</span>
		<label id="a1_scount"></label>
	</div>
<!-- 	<div style="padding-top: 20px;">
	<img  src="../../../ui/easyui/themes/icons/yp_ratio_big.png" />
		<span  >&nbsp;C/S比例：</span>
		<label id="a1_csratio"></label>
	</div> -->
</div>
<script>
	var p = ${param['p']};
	var ccount=p.ccount;
	var scount=p.scount;
	var p1=(ccount/(ccount+scount)*100).toFixed(2);
	var p2=(scount/(ccount+scount)*100).toFixed(2);
	var unit = "<span style='font-size:16px;color:#444;margin-left:5px;'>次（";
	if(ccount&&scount){
		$("#a1_ccount").html(p.ccount + unit+p1+"%）</span>");
		$("#a1_scount").html(p.scount + unit+p2+"%）</span>");
	}
</script>