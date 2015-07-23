package whu.b606.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import whu.b606.dto.Gender;
import whu.b606.dto.Usertype;
import whu.b606.util.CommonUtils;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月14日
 * 
 **/
@Entity
public class Admin extends User implements Serializable {
	private static final long serialVersionUID = 6957897310666258439L;
	public static final Usertype usertype = Usertype.Admin;// 用户类型
	private Integer id;
	private String username;// 姓名
	private String password;
	private Integer age;// 年龄
	private Gender gender;// 性别
	private Date createtime;// 创建时间
	private Image image;// 图像

	public Admin() {
		this.createtime = new Date();
	}

	public String findImgAbsolutePath() {
		String imgSrc = CommonUtils.getParameter("config/commonData", "adminImageSavePath");
		String returnPath = imgSrc + "/default.png";
		if (this.image != null && !CommonUtils.isNull(this.image.getPathname())) {
			String path = CommonUtils.getProjectWebRoot() + imgSrc + "/" + this.image.getPathname();
			if (new File(path).exists()) {// 图像不空，图像路径不空，且文件存在的情况下，返回文件路径，否则返回默认路径
				returnPath = imgSrc + "/" + this.image.getPathname();
			}
		}
		return returnPath;
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

	@Column(length = 3)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 6)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getCreatetime() {
		return createtime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	// CascadeType.PERSIST属性会导致级联保存，但如果被级联保存的对象已经存在的话，会报错，而MERGE则是更新或保存
	// FetchType.LAZY表示懒加载，而FetchType.EAGER表示急加载
	@OneToOne(cascade = CascadeType.ALL, targetEntity = Image.class)
	@JoinColumn(name = "admin_id")
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	/**
	 * id和username相同即认为是同一个管理员
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Admin other = (Admin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
