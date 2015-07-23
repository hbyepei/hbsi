package whu.b606.pageModel;

import java.lang.reflect.Field;
import java.util.Set;

import whu.b606.entity.Accident_part;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2015年3月6日
 * 
 **/
public class CarAccidentParts {
	private String chesheng;
	private String chuandong;
	private String dianqishebei;
	private String fadongji;
	private String chelun;
	private String qinang;
	private String xuanjiaxi;
	private String zhidongxi;
	private String zhuanxiangxi;
	private String fujiashebei;

	public CarAccidentParts() {
		super();
	}

	public CarAccidentParts(Set<Accident_part> accident_part) {
		if (accident_part != null) {
			for (Accident_part ap : accident_part) {
				String name = ap.getName();
				Field[] fds = this.getClass().getDeclaredFields();
				for (Field f : fds) {
					String n = f.getName();
					if (n.equals(name)) {
						f.setAccessible(true);
						try {
							f.set(this, "on");
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public String isChesheng() {
		return chesheng;
	}

	public void setChesheng(String chesheng) {
		this.chesheng = chesheng;
	}

	public String isChuandong() {
		return chuandong;
	}

	public void setChuandong(String chuandong) {
		this.chuandong = chuandong;
	}

	public String isDianqishebei() {
		return dianqishebei;
	}

	public void setDianqishebei(String dianqishebei) {
		this.dianqishebei = dianqishebei;
	}

	public String isFadongji() {
		return fadongji;
	}

	public void setFadongji(String fadongji) {
		this.fadongji = fadongji;
	}

	public String isChelun() {
		return chelun;
	}

	public void setChelun(String chelun) {
		this.chelun = chelun;
	}

	public String isQinang() {
		return qinang;
	}

	public void setQinang(String qinang) {
		this.qinang = qinang;
	}

	public String isXuanjiaxi() {
		return xuanjiaxi;
	}

	public void setXuanjiaxi(String xuanjiaxi) {
		this.xuanjiaxi = xuanjiaxi;
	}

	public String isZhidongxi() {
		return zhidongxi;
	}

	public void setZhidongxi(String zhidongxi) {
		this.zhidongxi = zhidongxi;
	}

	public String isZhuanxiangxi() {
		return zhuanxiangxi;
	}

	public void setZhuanxiangxi(String zhuanxiangxi) {
		this.zhuanxiangxi = zhuanxiangxi;
	}

	public String isFujiashebei() {
		return fujiashebei;
	}

	public void setFujiashebei(String fujiashebei) {
		this.fujiashebei = fujiashebei;
	}
}
