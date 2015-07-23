package whu.b606.entity;

import java.beans.Transient;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import whu.b606.dto.Gender;
import whu.b606.dto.Technology;
import whu.b606.dto.Usertype;
import whu.b606.pageModel.ExpertModel;
import whu.b606.util.CommonUtils;
import whu.b606.util.Zh2Pingyin;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class Expert extends User implements Serializable {
	private static final long serialVersionUID = -9222156356131871971L;
	public static final Usertype usertype = Usertype.Expert;// 用户类型
	private Integer id;
	private String username;// 姓名（非登录名）
	private String password;// 密码
	private String letterid;// 聘书编号
	private Gender gender;// 性别
	private String phone;// 电话
	private String email;// 邮件
	private Integer age;// 年龄
	private String idcard;// 身份证号不空（因equals方法需要用）
	private String area;// 所属地区
	private String department_name;// 来源单位
	private String department_category;// 来源单位
	private Technology technology;// 技术组别
	private String brand;// 现服务品牌
	private Image image;// 专家图像;
	private String introduction;// 简介
	private Set<Task> tasks = new HashSet<>();// 该专家关联的任务

	@Transient
	public String findImgAbsolutePath() {
		String imgSrc = CommonUtils.getParameter("config/commonData", "expertImageSavePath");
		String returnPath = imgSrc + "/default.png";
		if (this.image != null && !CommonUtils.isNull(this.image.getPathname())) {
			String path = CommonUtils.getProjectWebRoot() + imgSrc + "/" + this.image.getPathname();
			if (new File(path).exists()) {// 图像不空，图像路径不空，且文件存在的情况下，返回文件路径，否则返回默认路径
				returnPath = imgSrc + "/" + this.image.getPathname();
			}
		}
		return returnPath;
	}

	public Expert() {}

	public Expert(ExpertModel em) {
		String idc = em.getIdcard();
		this.username = em.getUsername();
		this.age = em.getAge();
		this.area = em.getArea();
		this.brand = em.getBrand();
		this.email = em.getEmail();
		this.gender = Gender.string2Enum(em.getGender());
		this.idcard = idc;
		this.introduction = em.getIntroduction();
		this.letterid = em.getLetterid();
		this.phone = em.getPhone();
		this.technology = Technology.string2Enum(em.getTechnology());
		this.setName(Zh2Pingyin.getPinyin(em.getUsername()) + new Random().nextInt(99));
		this.department_name = em.getDepartment_name();
		this.department_category = em.getDepartment_category();
		this.password = CommonUtils.makeMD5(idc.substring(idc.length() - 6));
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = false, length = 40)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 15)
	public String getLetterid() {
		return letterid;
	}

	public void setLetterid(String letterid) {
		this.letterid = letterid;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 6)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Column(length = 15)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(length = 40)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 3)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(length = 18, unique = true)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(length = 100)
	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	@Column(length = 100)
	public String getDepartment_category() {
		return department_category;
	}

	public void setDepartment_category(String department_category) {
		this.department_category = department_category;
	}

	@Column(length = 255)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	public Technology getTechnology() {
		return technology;
	}

	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	@Column(length = 100)
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Image.class)
	@JoinColumn(name = "expert_id")
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Lob
	@Column(columnDefinition = "TEXT")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	// 多对多双向关联，专家是主控端——有专家便希望知道其任务，而有任务时可能并不关心处理它的专家
	// 取JSON数据时，不取此字段
	// 依靠中间表，所以都不是关系维护端，因此在删除实体之前应先删除关系
	@JSONField(serialize = false)
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, targetEntity = Task.class)
	@JoinTable(name = "expert_task", joinColumns = { @JoinColumn(name = "expert_id") }, inverseJoinColumns = { @JoinColumn(name = "task_id") })
	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idcard == null) ? 0 : idcard.hashCode());
		return result;
	}

	/**
	 * id和idcard相同即认为是同一个专家
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Expert other = (Expert) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (idcard == null) {
			if (other.idcard != null) return false;
		} else if (!idcard.equals(other.idcard)) return false;
		return true;
	}
}
