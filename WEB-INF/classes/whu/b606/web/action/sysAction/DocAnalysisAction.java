package whu.b606.web.action.sysAction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import whu.b606.dto.CarTableModel;
import whu.b606.dto.ExpertsRankInfo;
import whu.b606.dto.Json;
import whu.b606.dto.Pagedata;
import whu.b606.pageModel.AccidentInfoModel;
import whu.b606.pageModel.ComboItem;
import whu.b606.pageModel.Grid;
import whu.b606.service.DocAnalysisService;
import whu.b606.web.action.BaseAction;

/**
 * @author yepei
 */
@Action("docAnalysisAction")
@Namespace("/sys")
@ParentPackage("common")
public class DocAnalysisAction extends BaseAction {
	private static final long serialVersionUID = 1088916458463742610L;
	private String div;// 代表页面中不同选项卡DIV标识的参数名称，例如div=A0,则表示请求A区域第0个选项卡中所需的数据
	private String path;// 页面路径前缀
	private int page;
	private int rows;
	private Integer year;
	private Date startTime, endTime;
	private String sort;
	private String order;
	Json j = new Json();
	@Resource
	DocAnalysisService ds;

	/**
	 * 根据不同的选项卡标识生成不同的页面数据 回传一个Object数组，该数组包含两个参数，第一个是页面路径，第二个是携带的数据
	 */
	public void getData() {
		if (div != null && !div.equals("")) {
			String tab = div.toLowerCase();
			Map<String, Object> params = ds.getPageData(tab, startTime, endTime);// 回传的数据
			Object[] backData = new Object[2];
			backData[0] = path + tab + ".jsp";// 页面路径
			backData[1] = params;
			j.setObject(backData);
			j.setOk(true);
		} else {
			j.setMsg("没有显示内容的区域！");
		}
		WriteJson(j);
	}

	public void getTableModel() {
		try {
			Grid g = new Grid();
			String[] sorts = sort.split(",");
			for (int i = 0; i < sorts.length; i++) {
				sorts[i] = "car." + sorts[i];
			}
			Pagedata pd = new Pagedata(page, rows, sorts, order.split(","));
			long total = ds.Count(null, null);
			List<CarTableModel> rows = ds.getCarTableModelRows(pd);
			g.setRows(rows);
			g.setTotal(total);
			WriteJson(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取参与调解的专家排名信息对象数组，格式如下：
	 * [{排名图标:rankIcon,图像:'image',姓名:name,最近参与日期:recentTime}]
	 */
	public void getRankInfo() {
		List<ExpertsRankInfo> eri = ds.getExpertsRankInfo();
		if (eri == null || eri.isEmpty()) {
			j.setMsg("无数据！");
		} else {
			j.setOk(true);
			j.setObject(eri);
		}
		WriteJson(j);
	}

	/**
	 * 事故统计
	 */
	public void getCarAccidentPartsInfo() {
		try {
			Grid g = new Grid();
			long total = 13;// 12个月加最后一行汇总
			List<AccidentInfoModel> rows = ds.getAccidentInfoModelRows(this.year);// 根据年份进行构造，年份值可能为-1(全部加载)，或null(只加载最近的一年)，或一个具体的年份值
			g.setRows(rows);
			g.setTotal(total);
			WriteJson(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getMonthDataByYear() {// 不包含全部
		List<ComboItem> items = ds.getApplyYears(false);
		WriteJson(items);
	}

	public void getMonthDataByYear2() {// 包含全部
		List<ComboItem> items = ds.getApplyYears(true);
		WriteJson(items);
	}

	public String getDiv() {
		return div;
	}

	public void setDiv(String div) {
		this.div = div;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
