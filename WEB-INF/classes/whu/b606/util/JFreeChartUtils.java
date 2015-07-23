package whu.b606.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月10日
 * 
 **/
public class JFreeChartUtils {
	/**
	 * 将符合Json格式的字符串转换成LinkedHashMap对象且不改变参数顺序
	 * 
	 * @param param
	 * @return
	 */
	public static LinkedHashMap<String, Object> parseMap(String param) {
		try {
			if (param == null || param.trim().equals("")) { return null; }
			JSON json = JSON.parseObject(param);// 将字符串转换成Map对象，
			if (json != null) {
				LinkedHashMap<String, Object> map = new LinkedHashMap<>();
				map = JSON.parseObject(param, LinkedHashMap.class);
				return map;
			}
		} catch (Exception e) {
			System.out.println("注意：JFreeChartUtils.parseMap方法异常!");
		}
		return null;
	}

	/**
	 * 根据数据集的大小确定统计图中数值轴的取值的最大范围
	 * 
	 * @param dataSet
	 * 
	 * @param count
	 * @return
	 */
	public static int getRangeMax(int count) {
		int max;
		if (count <= 10) {
			max = 10;
		} else if (count <= 30) {
			max = 30;
		} else if (count <= 50) {
			max = 50;
		} else if (count <= 100) {
			max = 100;
		} else if (count <= 200) {
			max = 200;
		} else if (count <= 500) {
			max = 500;
		} else if (count <= 1000) {
			max = 1000;
		} else {// 如果数据相当多，则让JFreeChart自动确定，返回-1做为判断标志
			max = -1;
		}
		return max;
	}

	/**
	 * 根据统计图中数值轴取值的最大范围值计算一个合理的刻度单位
	 * 
	 * @param max
	 * @return
	 */
	public static int getUnit(int max) {
		int unit;
		if (max <= 10) {
			unit = 1;
		} else if (max <= 30) {
			unit = 2;
		} else if (max <= 60) {
			unit = 4;
		} else if (max <= 200) {
			unit = 10;
		} else {
			unit = max / 15;
		}
		return unit;
	}

	public static String paintChart(HttpServletRequest request, HttpSession session, JspWriter out, CategoryDataset dataSet, int rows, int width, int height, String title, Paint paint, String usemap)
			throws Exception {
		String filename = "";
		if (height < 0) {
			height = 300;
		}
		if (width < 0) {
			width = 500;
		}
		if (dataSet == null) {
			dataSet = new DefaultCategoryDataset();
		}
		JFreeChart chart = ChartFactory.createLineChart(//
				"",//
				"",//
				"任务次数",//
				dataSet,//
				PlotOrientation.VERTICAL,// 方向
				false,// 显示图例
				true,// 生成提示工具
				false// 不生成URL链接
				);
		chart.setTitle(new TextTitle(title, new Font("微软雅黑", Font.BOLD, 16)));
		chart.setBackgroundPaint(new Color(242, 245, 247));// 设置整张图的背景颜色
		// chart.getLegend(0).setItemFont(new Font("宋体", Font.BOLD,
		// 12));
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis h_axis = plot.getDomainAxis();// 横轴
		NumberAxis v_axis = (NumberAxis) plot.getRangeAxis();// 纵轴
		int ct = dataSet.getColumnCount();
		h_axis.setMaximumCategoryLabelLines(2);// 最多显示两行
		if (ct > 10 && ct <= 15) {// 横轴数据过多，标签要垂直显示
			h_axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		} else if (ct > 15) {
			h_axis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
		} else {
			h_axis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		}
		h_axis.setLabelFont(new Font("仿宋", Font.BOLD, 12));
		h_axis.setTickLabelFont(new Font("仿宋", Font.BOLD, 12));
		v_axis.setLabelFont(new Font("仿宋", Font.BOLD, 12));
		int max = getRangeMax(rows);
		if (max > 0) {// 设置数值轴的刻度单位
			v_axis.setRange(0, max);
			v_axis.setAutoTickUnitSelection(false);
			v_axis.setTickUnit(new NumberTickUnit(getUnit(max), NumberFormat.getInstance()));
		} else {
			v_axis.setAutoTickUnitSelection(true);// max=-1，表明数据太多，此时让其自动确定刻度
		}
		plot.setBackgroundAlpha(0.1f);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.GRAY);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		plot.setOutlineVisible(false);
		plot.setNoDataMessage("没有数据");
		// plot.setBackgroundPaint(new Color(242, 245, 247));设置图表区域的背景颜色
		LineAndShapeRenderer r = (LineAndShapeRenderer) plot.getRenderer();
		r.setBaseShapesFilled(true);
		r.setBaseShapesVisible(true);
		r.setSeriesPaint(0, paint);// 设置第0条折线的颜色
		r.setSeriesStroke(0, new BasicStroke(3f));// 设置第0条折线组细
		r.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1}：{2}次", NumberFormat.getInstance()));
		r.setBaseStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		r.setBaseItemLabelsVisible(true);
		r.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
		r.setItemLabelAnchorOffset(10);
		plot.setRenderer(r);
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		filename = ServletUtilities.saveChartAsPNG(chart, width, height, info, session);
		PrintWriter writer = new PrintWriter(out);
		ChartUtilities.writeImageMap(writer, usemap, info, false);
		String imgSrc = request.getContextPath() + "/sys/chartAction.action?filename=" + filename;
		return imgSrc;
	}

	public static Map<String, Object> getDataSetAndDimense(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<>();
		if (map != null && !map.isEmpty()) {
			DefaultCategoryDataset data = new DefaultCategoryDataset();
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			int rows = 0;
			try {
				while (it.hasNext()) {
					Map.Entry<String, Object> en = it.next();
					String key = en.getKey();
					if (key.equals("width")) {
						result.put("width", (int) Double.parseDouble(en.getValue().toString()));
					} else if (key.equals("height")) {
						result.put("height", (int) Double.parseDouble(en.getValue().toString()));
					} else if (!key.equals("total")) {
						double v = Double.parseDouble(en.getValue().toString());
						data.addValue(v, "当日", key);
						if (v > rows) {
							rows = (int) v;
						}
					}
				}
				result.put("rows", rows);
				result.put("dataSet", data);
				return result;
			} catch (Exception e) {
				System.out.println("注意：JFreeChartUtils.getDataSetAndDimense方法异常，拆线图数据集创建失败！");
				return null;
			}
		}
		return null;
	}
}
