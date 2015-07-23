package whu.b606.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import whu.b606.dto.Usertype;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月14日
 * 
 **/
@Entity
public class Saler extends User implements Serializable {
	private static final long serialVersionUID = 8092092099582765552L;
	public static final Usertype usertype = Usertype.Saler;// 用户类型
	private Integer id;
	private String code;// 机构代码
	private String agent;// 代理人，联系人
	private String phone;// 电话
	private String address;// 通讯地址
	private String querycode;// 查询编号
	private String email;// 电子邮件
	private String currentTask_caseid;// 当前任务的ID
	private Set<Task> tasks = new HashSet<>();// 自己申请的任务
	private int taskcount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(length = 12)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 40)
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@Column(length = 15)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length = 40, unique = true)
	public String getQuerycode() {
		return querycode;
	}

	public void setQuerycode(String querycode) {
		this.querycode = querycode;
	}

	@Column(length = 20)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 18)
	public String getCurrentTask_caseid() {
		return currentTask_caseid;
	}

	public void setCurrentTask_caseid(String currentTask_caseid) {
		this.currentTask_caseid = currentTask_caseid;
	}

	// 1、对Task的插入、更新、修改及删除均不要对其关联的唯一Consumer级联执行。而是单独新增Consumer或者修改现有Consumer
	// 2、对Consumer的插入是Task的插入时有选择的进行，Consumer的其它操作（更新、修改、删除）不级联到Task
	@OneToMany(// (不定义级联属性时，就不会有任何级联)
	mappedBy = "saler", fetch = FetchType.LAZY)
	@JSONField(serialize = false)
	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public int getTaskcount() {
		return this.tasks.size();
	}

	public void setTaskcount(int taskcount) {
		this.taskcount = taskcount;
	}

	/**
	 * 通过机构代码检查两个消费者是否是同一个人
	 * 
	 * @param c
	 * @return
	 */
	public boolean sameOne(Saler s) {
		return this.code.equalsIgnoreCase(s.getCode());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Saler other = (Saler) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
}
