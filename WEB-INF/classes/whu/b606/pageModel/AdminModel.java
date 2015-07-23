package whu.b606.pageModel;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;

import whu.b606.entity.Admin;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年12月4日
 * 
 **/
public class AdminModel {
	private Integer id;
	private String name;// 用户名
	private String username;// 姓名
	private String password;// 密码
	private Integer age;
	private String gender;
	private Date createtime;
	private String image;

	public AdminModel(Admin a) {
		super();
		try {
			BeanUtils.copyProperties(this, a);
			this.gender = a.getGender().getDescription();
			this.image = a.findImgAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AdminModel() {
	}

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

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
