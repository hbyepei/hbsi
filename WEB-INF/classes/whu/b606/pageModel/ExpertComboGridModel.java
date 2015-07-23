package whu.b606.pageModel;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import whu.b606.entity.Expert;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年12月4日
 * 
 **/
public class ExpertComboGridModel {
	private Integer id;// 组id
	private String image;
	private String username;
	private String letterid;
	private String gender;
	private int currenttasks;
	private String technology;
	private String brand;
	private String area;

	public ExpertComboGridModel(Expert e) {
		try {
			BeanUtils.copyProperties(this, e);
		} catch (IllegalAccessException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		this.image = e.findImgAbsolutePath();
		this.gender = e.getGender().getDescription();
		this.technology = e.getTechnology().getDescription();
		this.currenttasks = e.getTasks().size();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLetterid() {
		return letterid;
	}

	public void setLetterid(String letterid) {
		this.letterid = letterid;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getCurrenttasks() {
		return currenttasks;
	}

	public void setCurrenttasks(int currenttasks) {
		this.currenttasks = currenttasks;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
