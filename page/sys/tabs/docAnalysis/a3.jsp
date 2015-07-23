<%@ page language="java" pageEncoding="UTF-8"%>
<%@page
	import="
java.util.LinkedHashMap,
org.jfree.chart.labels.StandardCategoryItemLabelGenerator,
org.jfree.chart.labels.ItemLabelPosition,
org.jfree.chart.labels.ItemLabelAnchor,
org.jfree.ui.TextAnchor,
org.jfree.chart.axis.NumberTickUnit,
org.jfree.chart.plot.DatasetRenderingOrder,
 java.io.PrintWriter,
 java.awt.Font,
 java.awt.Color,
 java.util.Map,
 org.jfree.chart.axis.CategoryAxis,
org.jfree.chart.axis.NumberAxis,
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
"
%>
<%!
 int width = 300;
 int height = 260;
 String filename = "";
 String imgSrc = "";
 private static CategoryDataset getDataSet(Map<String,Object> map) {
		try {
			if (map != null && !map.isEmpty()) {
				DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
				for(Map.Entry<String,Object> me:map.entrySet()){//月/数量
					dataSet.addValue(Integer.parseInt(me.getValue().toString()), me.getKey(), me.getKey() );//第二个参数表示按什么分组
				}
				return dataSet;
			}
		} catch (Exception e) {
			System.out.println("数据据构造失败!");
		}
		return null;
	}
%>
<%
try {
	LinkedHashMap<String,Object> lhm=JFreeChartUtils.parseMap( request.getParameter("p"));
	if(lhm.containsKey("height")){
		height=(int)Double.parseDouble(lhm.get("height").toString());
		lhm.remove("height");
	}
	if(lhm.containsKey("width")){
		width=(int)Double.parseDouble(lhm.get("width").toString());
		lhm.remove("width");
	}
	CategoryDataset dataSet = getDataSet(lhm);
	JFreeChart chart = ChartFactory.createBarChart3D(//
			"调解次数月度统计",// 标题
			"月份",// 目录轴的显示标签
			"次数",// 数值轴的显示标签
			dataSet, // 数据集
			PlotOrientation.VERTICAL, // 图表方向
			false, // 显示图例
			true, // 生成提示
			false// 生成URL链接
			);
	chart.setTitle(new TextTitle("调解次数统计", new Font("微软雅黑", Font.PLAIN, 16)));
	chart.setBackgroundPaint(new Color(242, 245, 247));// 设置整张图的背景颜色
	CategoryPlot plot = chart.getCategoryPlot();// 图表区域对象
	CategoryAxis h_axis = plot.getDomainAxis();// 横轴
	NumberAxis v_axis = (NumberAxis) plot.getRangeAxis();// 纵轴

	v_axis.setLabelFont(new Font("宋体", Font.BOLD, 14));
	v_axis.setAutoRange(true);
	v_axis.setLowerBound(1);//要显示的开始处的最小值
    v_axis.setUpperMargin(0.2);//柱子离上方的距离 
    v_axis.setAutoTickUnitSelection(true);
    v_axis.setTickUnit(new NumberTickUnit(1)); 
    
    
    
	h_axis.setLabelFont(new Font("宋体", Font.BOLD, 12));
	h_axis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
	h_axis.setLowerMargin(0.05);//设置距离图片左端距离此时为10%
	h_axis.setUpperMargin(0.05);//设置距离图片右端距离此时为百分之10
	h_axis.setCategoryLabelPositionOffset(1);//图表横轴与标签的距离(10像素)
     //设置柱的透明度 
 	plot.setForegroundAlpha(1.0f); 
	plot.setOutlineVisible(false);
	plot.setNoDataMessage("没有数据");
	plot.setBackgroundAlpha(0.5f);
	
	BarRenderer3D renderer= new BarRenderer3D();
	renderer.setMaximumBarWidth(0.025f);//柱子宽度
	renderer.setShadowVisible(true);
	renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
	renderer.setBaseItemLabelsVisible(true); 
	renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT)); 
	renderer.setItemLabelAnchorOffset(10);
 //   renderer.setItemMargin(0.1);//组内柱子间隔为组宽的10%，注，此例中未分组
    plot.setRenderer(renderer);//使用我们设计的效果
    plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD); 
	ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
	filename = ServletUtilities.saveChartAsPNG(chart, width, height, info, session);
	PrintWriter writer = new PrintWriter(out);
	ChartUtilities.writeImageMap(writer, "a3_chart", info, false);
	imgSrc = request.getContextPath() + "/sys/chartAction.action?filename=" + filename;
} catch (Exception e) {
	System.out.println("构造统计图时产生了异常");
}
%>
<div style="float:right;margin-top: -10px; ">年份:
<input  id="applytime_year" name="year" style="width: 80px;"></input>
		
</div>
<img src="<%=imgSrc%>" usemap="#a3_chart" style="cursor: pointer;" width="100%" height="75%" />
<script>
	$('#applytime_year').combobox({
		editable:false,
		valueField:'id',
		textField:'text',
		panelHeight:'auto',
		url:yp.bp()+"/sys/docAnalysisAction!getMonthDataByYear.action",
		onSelect:function(record){//选择某一项后，重新加载当前页面
			year=record.id;
			var yearTime=new Date(year,0,1);
			loadTabsData('docAnalysis_tabsA3',yearTime,yearTime);
		},
		onLoadSuccess:function(){
			if(!year){
				var t=new Date();
				year=t.getFullYear();
			}
			$(this).combobox('setValue',year);
		}
	});
</script>