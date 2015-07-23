package whu.b606.pageModel;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;

import whu.b606.dto.Usertype;
import whu.b606.entity.Task;

/**
 * 用户的个人申请列表模型
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月29日
 * 
 **/
public class TaskListModel {
	public static final String curTask = "<label style='color:red;'>当前任务</label>";
	private Integer id;
	private boolean selfapplyed;// 是否是主动申请的
	private String caseid;
	private String status;
	private String matter;
	private String description;
	private String time;
	private String info;// 备注信息
	private String question;// 咨询问题;
	private String consultation;// 专家给予的咨询意见
	private Date applytime;// 排序依据

	/**
	 * 
	 * @param t
	 *            任务集中的某个任务
	 * @param curCaseid
	 *            当前的任务编号
	 * @param applyer
	 *            查询者
	 */
	public TaskListModel(Task t, String curCaseid, Usertype queryer) {
		DateFormat df = new SimpleDateFormat("yyyy/M/d-H:mm");
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		// this.id = t.getId();
		// this.caseid = t.getCaseid();
		// this.matter = t.getMatter();
		// this.description = t.getDescription();
		// this.applytime = t.getApplytime();
		// this.info = t.getInfo();
		// this.question = t.getQuestion();
		// this.consultation = t.getConsultation();
		this.status = t.getStatus().getDescription();
		// 如果查询者的身份和此任务的申请类型一致，则表明此任务是自主申请
		this.selfapplyed = t.getApplytype().toString().equals(queryer.toString());
		if (curCaseid != null && curCaseid.equals(t.getCaseid()) && this.selfapplyed) {
			this.time = TaskListModel.curTask;
		} else {
			this.time = df.format(t.getApplytime());
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isSelfapplyed() {
		return selfapplyed;
	}

	public void setSelfapplyed(boolean selfapplyed) {
		this.selfapplyed = selfapplyed;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMatter() {
		return matter;
	}

	public void setMatter(String matter) {
		this.matter = matter;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getConsultation() {
		return consultation;
	}

	public void setConsultation(String consultation) {
		this.consultation = consultation;
	}

	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}
}
