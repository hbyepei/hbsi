package whu.b606.pageModel;

import org.apache.commons.beanutils.BeanUtils;

import whu.b606.entity.Expert;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年12月4日
 * 
 **/
public class ExpertModel {
	private Integer id;
	private String name;// 用户名
	private String username;// 姓名
	private String password;// 密码
	private Integer age;
	private String gender;
	private String image;
	private String letterid;// 聘书编号
	private String idcard;
	private String phone;
	private String email;
	private String area;
	private String department_name;
	private String department_category;
	private String brand;
	private String technology;// 技术组别
	private String introduction;

	public ExpertModel(Expert e) {
		super();
		try {
			BeanUtils.copyProperties(this, e);
			this.gender = e.getGender().getDescription();
			this.technology = e.getTechnology().getDescription();
			this.image = e.findImgAbsolutePath();
		} catch (Exception e0) {
			e0.printStackTrace();
		}
	}

	public ExpertModel() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLetterid() {
		return letterid;
	}

	public void setLetterid(String letterid) {
		this.letterid = letterid;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getDepartment_category() {
		return department_category;
	}

	public void setDepartment_category(String department_category) {
		this.department_category = department_category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}
