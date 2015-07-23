package whu.b606.serviceBean;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.ApplyType;
import whu.b606.dto.CarTableModel;
import whu.b606.dto.ExpertsRankInfo;
import whu.b606.dto.Order;
import whu.b606.dto.Page;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Status;
import whu.b606.entity.Expert;
import whu.b606.entity.Task;
import whu.b606.pageModel.AccidentInfoModel;
import whu.b606.pageModel.ComboItem;
import whu.b606.service.AccidentPartService;
import whu.b606.service.DocAnalysisService;
import whu.b606.service.ExpertService;
import whu.b606.service.TaskService;

/**
 * @author yepei
 */
@Service(DocAnalysisService.BEAN_NAME)
public class DocAnalysisServiceImpl extends DaoSupport<Task> implements DocAnalysisService {
	Map<String, Object> map = new LinkedHashMap<>();
	@Resource
	TaskService ts;
	@Resource
	ExpertService es;
	@Resource
	AccidentPartService aps;

	@Override
	public Map<String, Object> getPageData(String tab, Date startTime, Date endTime) {// 需要对参数判空和纠正
		Date today = new Date();
		if (endTime == null || endTime.getTime() > today.getTime()) {
			endTime = today;
		}
		if (startTime == null || startTime.getTime() >= endTime.getTime()) {
			startTime = new Date(endTime.getTime() - 15 * 24 * 3600 * 1000);
		}
		map.clear();
		Calendar cd = new GregorianCalendar();
		switch (tab) {
			case "a0":
				return getPageData_a0();
			case "a1":
				return getPageData_a1();
			case "a2":
				return getPageData_a2();
			case "a3":
				cd.setTime(startTime);
				return getPageData_a3(cd.get(Calendar.YEAR));
			case "a4"://
				return getPageData_a2();
			case "b0":
				return getPageData_b0(startTime, endTime);
			case "b1":
				return getPageData_b1(startTime, endTime);
			case "b2":
				return getPageData_b2(startTime, endTime);
			case "b3":
				return getPageData_b3(startTime, endTime);
			case "b4":
				return getPageData_b4(startTime, endTime);
			case "b5":
				return getPageData_b5(startTime, endTime);
			default:
				return null;
		}
	}

	private Map<String, Object> getPageData_a0() {
		int total = ts.Count();
		int succ = ts.Count("o.status=?1", new Object[] { Status.complete });
		map.put("total", total);
		map.put("succ", succ);
		// 还要提供绘制饼图的必要数据给a0.jsp传过去，具体的绘制过程将在a0.jsp中完成并输出
		Status[] status = Status.values();
		for (int i = 0; i < status.length; i++) {
			int c = ts.Count("o.status=?1", new Object[] { status[i] });
			String s = status[i].toString();
			map.put(s, status[i].getDescription() + "#" + c);
		}
		return map;
	}

	private Map<String, Object> getPageData_a1() {
		int ccount = ts.Count("o.applytype=?1", new Object[] { ApplyType.Consumer });
		int scount = ts.Count("o.applytype=?1", new Object[] { ApplyType.Saler });
		map.put("ccount", ccount);
		map.put("scount", scount);
		return map;
	}

	/**
	 * 返回一个表格数据模型第一次请求服务器所需的数据，这些数据包括{int page,int rows,Stringorder,String
	 * sort}，它们并非直接返回给EasyUI框架，而是作为原你页面的参数，以使父页面中在用JS加载远程数据是再拿这些参数去访问数据库
	 * 
	 * 
	 * @param params
	 *            查询参数，包括：
	 * @return 一个Map，里边只有一条数据，其中key为"grid"，value为Grid实例
	 */
	private Map<String, Object> getPageData_a2() {
		return null;
	}

	/**
	 * 返回某一年中的每个月的调解次数集合
	 * 
	 * @return
	 */
	private Map<String, Object> getPageData_a3(int year) {
		int total = 0;
		for (int i = 1; i <= 12; i++) {
			String condition = "DATE_FORMAT( o.applytime, '%Y-%m-%d %h:%i:%s') like ?1";
			Object[] params;
			if (i < 10) {
				params = new Object[] { year + "-0" + i + "%" };
			} else {
				params = new Object[] { year + "-" + i + "%" };
			}
			int n = ts.Count(condition, params);
			total += n;
			map.put(i + "月", n);
		}
		map.put(" 合计", total);
		return map;
	}

	private Map<String, Object> getPageData_b0(Date startTime, Date endTime) {
		// 提供统计图数据的同时还应提供近半月时间内的任务总量值
		map = getPageDataByCondition(null, null, startTime, endTime);
		map.put("total", ts.Count("o.applytime  between ?1 and ?2", new Object[] { startTime, endTime }));
		return map;
	}

	private Map<String, Object> getPageData_b1(Date startTime, Date endTime) {// 返回消费者申请的15日数据
		return getPageDataByCondition("applytype", ApplyType.Consumer, startTime, endTime);
	}

	private Map<String, Object> getPageData_b2(Date startTime, Date endTime) {
		return getPageDataByCondition("applytype", ApplyType.Saler, startTime, endTime);
	}

	private Map<String, Object> getPageData_b3(Date startTime, Date endTime) {
		return getPageDataByCondition("status", Status.complete, startTime, endTime);
	}

	private Map<String, Object> getPageData_b4(Date startTime, Date endTime) {
		return getPageDataByCondition("status", Status.terminate, startTime, endTime);
	}

	private Map<String, Object> getPageData_b5(Date startTime, Date endTime) {
		return getPageDataByCondition("status", Status.refused, startTime, endTime);
	}

	/**
	 * 提供一段时间内的有关Task的信息
	 * 
	 * @param wherJPQL
	 * @return
	 */
	private Map<String, Object> getPageDataByCondition(String columnName, Object param, Date startTime, Date endTime) {
		long et = endTime.getTime();
		long st = startTime.getTime();
		Calendar cd1 = new GregorianCalendar();
		Calendar cd2 = new GregorianCalendar();
		int days = (int) ((et - st) / (24 * 3600 * 1000));// 相差的天数
		// 注意：在生成拆线图数据集时，若数据量过多，则应分时段只存入关键时间点的数据
		int step = 1;
		if (days > 20) {// 数据过多，横轴上会显示不下，此时应分时段显示，而不是将每一天的数据都显示出来
			step = days / 20 + 1;// 保证横轴上最多只显示30个数据点
		}
		Date d1 = new Date(st);
		Date d2 = new Date(st);
		for (int i = 0; i < days; i += step) {// 计算此时间段中，每天申请的个数
			d1 = d2;// 第1天
			d2 = new Date(d1.getTime() + (step * 24 * 3600 * 1000));// 第n~(1+step)天
			if (d2.getTime() > et) {
				d2 = endTime;
			}
			String whereClause = "o.applytime  between ?1 and ?2 ";
			Object[] params = new Object[] { d1, d2 };
			if (columnName != null && !columnName.equals("")) {
				whereClause += "and o." + columnName + "=?3";
			}
			if (param != null) {
				params = new Object[] { d1, d2, param };
			}
			int c = ts.Count(whereClause, params);
			cd1.setTime(d1);
			cd2.setTime(d2);
			int y1 = cd1.get(Calendar.YEAR);
			int y2 = cd2.get(Calendar.YEAR);
			int m1 = cd1.get(Calendar.MONTH) + 1;
			int m2 = cd2.get(Calendar.MONTH) + 1;
			int day1 = cd1.get(Calendar.DAY_OF_MONTH);
			int day2 = cd2.get(Calendar.DAY_OF_MONTH);
			String key = "" + m1 + "月" + day1 + "日";
			if (y1 == y2) {
				key = "" + m1 + "." + day1 + "-" + m2 + "." + day2;
			} else {// 跨年
				key = "" + (y1 + "/") + m1 + "/" + day1 + "-" + (y2 + "/") + m2 + "/" + day2;
			}
			map.put(key, c);
		}
		return map;
	}

	@Override
	public List<ExpertsRankInfo> getExpertsRankInfo() {
		List<Task> tasks = ts.finds(null, null);
		if (tasks != null && tasks.size() > 0) {
			Map<Expert, Integer> eRanks = new HashMap<>();
			// 保存参与专家的排名信息，第个个泛型参数是该专家参与的次数
			for (Task t : tasks) {
				Set<Expert> eset = t.getExperts();
				if (eset != null && !eset.isEmpty()) {
					int k = 0;
					for (Expert e : eset) {
						if (e != null) {
							int count = 1;
							if (eRanks.containsKey(e)) {
								count = eRanks.get(e);
								count++;
							}
							eRanks.put(e, count);
						}
						if (++k > 49) break;// 最多显示50条
					}
				}
			}
			return getRankedExpertList(eRanks);
		}
		return null;
	}

	@Override
	public List<CarTableModel> getCarTableModelRows(Pagedata pd) {
		try {
			if (pd != null) {
				LinkedHashMap<String, Order> orderBy = new LinkedHashMap<>();
				String[] sort = pd.getSort();
				String[] order = pd.getOrder();
				if (null != sort && null != order && sort.length == order.length) {
					for (int i = 0; i < sort.length; i++) {
						orderBy.put(sort[i], Order.valueOf(order[i].toUpperCase(Locale.ENGLISH)));
					}
				}
				Page<Task> p = ts.findByPage(pd.getPage(), pd.getRows(), orderBy);
				List<Task> list = p.getList();
				List<CarTableModel> rows = new ArrayList<>();
				for (Task t : list) {
					rows.add(new CarTableModel(t));
				}
				return rows;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ComboItem> getApplyYears(boolean containsAll) {
		String sqlMin = "o.applytime = (select min(o.applytime) from o)";
		String sqlMax = "o.applytime = (select max(o.applytime) from o)";
		Task earlyTask = ts.find(sqlMin, null);
		Task lateTask = ts.find(sqlMax, null);
		Date d1, d2;
		if (earlyTask != null) {
			d1 = earlyTask.getApplytime();
		} else {
			d1 = new Date();
		}
		if (lateTask != null) {
			d2 = lateTask.getApplytime();
		} else {
			d2 = new Date();
		}
		Calendar cd1 = new GregorianCalendar();
		cd1.setTime(d1);
		int y1 = cd1.get(Calendar.YEAR);
		cd1.setTime(d2);
		int y2 = cd1.get(Calendar.YEAR);
		List<ComboItem> items = new LinkedList<>();
		for (int i = 0; i < y2 - y1 + 1; i++) {
			ComboItem ci = new ComboItem(y2 - i, y2 - i + "年", null);
			items.add(ci);
		}
		if (containsAll) {
			ComboItem all = new ComboItem(-1, "全部", null);
			items.add(all);
		}
		return items;
	}

	/**
	 * 返回按参与次数降序排序的专家信息对象列表
	 * 
	 * @param eRanks
	 * @return
	 */
	private List<ExpertsRankInfo> getRankedExpertList(Map<Expert, Integer> rk) {
		if (rk == null || rk.isEmpty()) {
			return null;
		} else {
			List<Map.Entry<Expert, Integer>> rlist = new LinkedList<>(rk.entrySet());
			// 对此集合进行排序，首先依count进行降序，再依recentTime进行降序，再以name进行升序
			Collections.sort(rlist, new Comparator<Map.Entry<Expert, Integer>>() {
				@Override
				public int compare(Map.Entry<Expert, Integer> et1, Map.Entry<Expert, Integer> et2) {
					Expert e1 = et1.getKey();
					Expert e2 = et2.getKey();
					Integer c1 = et1.getValue();
					Integer c2 = et2.getValue();
					int r1 = c2.compareTo(c1);// 如果c1<c2则返回负值
					if (r1 == 0) {// count不能比较出来，则用第二字段继续比较
						Date d1 = ExpertsRankInfo.getRecentTime(e1);
						Date d2 = ExpertsRankInfo.getRecentTime(e2);
						DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
						int r2 = 0;
						try {
							if (d1 == null) {
								d1 = df.parse("2010/12/31");
							}
							if (d2 == null) {
								d2 = df.parse("2010/12/31");
							}
							r2 = d2.compareTo(d1);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (r2 == 0) {// recentTime比较不出来则再以name进行比较，但这次是降序
							return ExpertsRankInfo.getShowName(e1).compareTo(ExpertsRankInfo.getShowName(e2));
						} else {
							return r2;
						}
					}
					return r1;
				}
			});
			List<ExpertsRankInfo> ranks = new LinkedList<>();
			Iterator<Map.Entry<Expert, Integer>> it = rlist.iterator();
			int count = 0;
			while (it.hasNext()) {
				Map.Entry<Expert, Integer> me = it.next();
				ExpertsRankInfo eri = new ExpertsRankInfo(me.getKey(), me.getValue(), ++count);
				ranks.add(eri);
			}
			return ranks;
		}
	}

	/**
	 * 根据年份获取该年中12个月(及汇总)中发生故障的部位频率，同时每月都要单独汇总，年份值可能为-1(全部加载)，或null(只加载最近的一年)，
	 * 或一个具体的年份值
	 */
	@Override
	public List<AccidentInfoModel> getAccidentInfoModelRows(Integer year) {
		List<AccidentInfoModel> list = new ArrayList<>();
		for (int i = 0; i < 12; i++) {// 12个月
			AccidentInfoModel alm = new AccidentInfoModel(aps, year, i + 1);// 第y年i+1月份中发生的故障信息。其中aps是负责访问数据库的Bean
			list.add(alm);
		}
		Map<String, Integer> m = new HashMap<>();
		for (AccidentInfoModel a : list) {// 循环所有对象，将属性值累加
			if (a.getAll() > 0) {// 只有总计大于0时，其它值才有可能大于0，此判断可以避免大量不必要的循环
				Field[] fs = a.getClass().getDeclaredFields();
				for (Field f : fs) {
					f.setAccessible(true);
					String n = f.getName();// 属性名，即键
					if (!n.equals("month")) {
						try {
							int value = f.getInt(a);// 得到当前属性的值，要累加到集合中去
							if (m.containsKey(n)) {// 集合中已经存在此属性，则取其值+1
								m.put(n, m.get(n) + value);
							} else {
								m.put(n, value);
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		// 再加上汇总行从map中，把值赋给alm2
		AccidentInfoModel alm2 = new AccidentInfoModel();
		alm2.setMonth(-1);// 汇总行的月份值记为-1
		if (m != null && m.size() > 0) {
			Field[] fds = alm2.getClass().getDeclaredFields();
			for (Field f : fds) {// 初始化字段频率集合
				f.setAccessible(true);
				String name = f.getName();
				if (!name.equals("month")) {
					int value = m.get(name);
					try {
						f.set(alm2, value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		list.add(alm2);
		return list;
	}
}
