package whu.b606.pageModel;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import whu.b606.entity.Accident_part;
import whu.b606.service.AccidentPartService;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2015年3月6日
 * 
 **/
public class AccidentInfoModel {
	private int month;
	private int chesheng;
	private int chuandong;
	private int dianqishebei;
	private int fadongji;
	private int chelun;
	private int qinang;
	private int xuanjiaxi;
	private int zhidongxi;
	private int zhuanxiangxi;
	private int fujiashebei;
	private int all;// 单月汇总

	public AccidentInfoModel(AccidentPartService aps, Integer year, int month) {// year取值：-1,null,20XX.
		int y = -1;
		if (year == null) {
			Calendar cd = new GregorianCalendar();
			cd.setTime(new Date());
			y = cd.get(Calendar.YEAR);
		} else if (year > 0) {
			y = year;
		}
		String sql = "DATE_FORMAT( o.time, '%Y-%m-%d %h:%i:%s') like ?1";// 查询条件（查询特定月份的记录集合）
		Object[] params = null;
		String monthFlag = month > 9 ? "" + month : "0" + month;
		if (y > 0) {
			params = new Object[] { y + "-" + monthFlag + "-%" };
		} else {
			params = new Object[] { "%-" + monthFlag + "-%" };
		}
		List<Accident_part> alist = aps.finds(sql, params);
		this.month = month;
		for (Accident_part ap : alist) {
			String name = ap.getName();
			Class cls = this.getClass();
			Field[] fds = cls.getDeclaredFields();
			for (Field f : fds) {
				f.setAccessible(true);
				if (f.getName().equals(name)) {
					try {
						int value = f.getInt(this);
						f.set(this, ++value);
						break;
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.all = alist.size();
	}

	public AccidentInfoModel() {}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getChesheng() {
		return chesheng;
	}

	public void setChesheng(int chesheng) {
		this.chesheng = chesheng;
	}

	public int getChuandong() {
		return chuandong;
	}

	public void setChuandong(int chuandong) {
		this.chuandong = chuandong;
	}

	public int getDianqishebei() {
		return dianqishebei;
	}

	public void setDianqishebei(int dianqishebei) {
		this.dianqishebei = dianqishebei;
	}

	public int getFadongji() {
		return fadongji;
	}

	public void setFadongji(int fadongji) {
		this.fadongji = fadongji;
	}

	public int getChelun() {
		return chelun;
	}

	public void setChelun(int chelun) {
		this.chelun = chelun;
	}

	public int getQinang() {
		return qinang;
	}

	public void setQinang(int qinang) {
		this.qinang = qinang;
	}

	public int getXuanjiaxi() {
		return xuanjiaxi;
	}

	public void setXuanjiaxi(int xuanjiaxi) {
		this.xuanjiaxi = xuanjiaxi;
	}

	public int getZhidongxi() {
		return zhidongxi;
	}

	public void setZhidongxi(int zhidongxi) {
		this.zhidongxi = zhidongxi;
	}

	public int getZhuanxiangxi() {
		return zhuanxiangxi;
	}

	public void setZhuanxiangxi(int zhuanxiangxi) {
		this.zhuanxiangxi = zhuanxiangxi;
	}

	public int getFujiashebei() {
		return fujiashebei;
	}

	public void setFujiashebei(int fujiashebei) {
		this.fujiashebei = fujiashebei;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
}
