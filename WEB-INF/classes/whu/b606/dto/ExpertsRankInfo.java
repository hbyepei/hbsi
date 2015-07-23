package whu.b606.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import whu.b606.entity.Expert;
import whu.b606.entity.Image;
import whu.b606.entity.Task;
import whu.b606.util.CommonUtils;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月5日
 * 
 **/
public class ExpertsRankInfo {
	// {排名图标:rankIcon,图像:'image',姓名:name,最近参与日期:recentTime}
	private int id;
	private int rank;
	private String rankIcon;
	private String image;
	private String name;
	private String recentTime;
	private int count;

	public ExpertsRankInfo() {}

	/**
	 * 通过给定专家对象构造专家信息对象，需要为id、rank、 rankIcon、image、name、recentTime、count赋值
	 * 
	 * @param e
	 */
	public ExpertsRankInfo(Expert e, int count, int rank) {
		if (null != e) {
			this.id = e.getId();
			this.count = count;
			this.rank = rank;
			this.name = getShowName(e);
			Date recent = getRecentTime(e);
			if (recent == null) {
				this.recentTime = "未知时间";
			} else {
				this.recentTime = new SimpleDateFormat("yyyy/MM/dd").format(recent);
			}
			this.rankIcon = makeRankIcon(rank);
			String prefix = "/page/sys/tabs/docAnalysis/expertRankIcon/expertImg.jsp?imagePath=";
			String url = "";
			Image img = e.getImage();
			if (img != null && img.getPathname() != null) {
				url = prefix + img.getPathname();
			} else {
				url = prefix + "null";
			}
			this.image = url;
		}
	}

	public static String getShowName(Expert e) {
		if (e != null) { return CommonUtils.isNull(e.getUsername()) ? e.getName() : e.getUsername(); }
		return null;
	}

	public static Date getRecentTime(Expert e) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		if (e != null) {
			Set<Task> tasks = e.getTasks();
			if (null != tasks && !tasks.isEmpty()) {
				try {
					Date dt = df.parse("2010/12/31");
					for (Task t : tasks) {
						Date d = t.getApplytime();
						if (d.after(dt)) {
							dt = d;
						}
					}
					return dt;
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 提供排名图标的路径，注路径须从项目根目录开始，且前带“/”
	 */
	private String makeRankIcon(int rank) {
		if (rank < 1) {
			return "/images/expert/unknow.png";
		} else {
			return "/page/sys/tabs/docAnalysis/expertRankIcon/rank.jsp?rank=" + rank;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getRankIcon() {
		return rankIcon;
	}

	public void setRankIcon(String rankIcon) {
		this.rankIcon = rankIcon;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecentTime() {
		return recentTime;
	}

	public void setRecentTime(String recentTime) {
		this.recentTime = recentTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
