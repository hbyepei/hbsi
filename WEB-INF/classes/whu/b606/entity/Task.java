package whu.b606.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import whu.b606.dto.ApplyType;
import whu.b606.dto.Status;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月14日
 * 
 **/
@Entity
public class Task implements Serializable {
	private static final long serialVersionUID = -8366052061721754371L;
	private Integer id;
	private String caseid;// 编号
	private Status status;// 状态
	// 记录当前任务已经经历的状态集合，以供调解查询时使用，可能保存的值为：accepted,processing,cancel,refused,complete
	private Map<Status, Date> statustime = new LinkedHashMap<>();
	private Date applytime;// 申请单的生成日期
	private String matter;// 申请事项
	private String description;// 问题描述
	private ApplyType applytype;// 申请类型
	private boolean needexpert;// 是否需要专家
	private Consumer consumer;
	private Saler saler;
	private Set<Expert> experts = new HashSet<>();// 关联的专家，可能有多个
	private Car car;// 关系的产品
	private String info;// 备注信息，例如不予受理的理由，调解终止的理由（用户撤销，专家无法调解，无法达成一致），调解完成的理由（双方已经达成一致）
	private String question;// 咨询问题，源问题描述来自于用户填写的三包责任争议简易情况描述，可基于此进行修改。
	private String consultation;// 专家填写的咨询意见。
	private Set<UpFile> relatedfile = new HashSet<>();// 相关文件(凭证文件、调解申请信息文件、专家咨询结果文件，图像文件等)

	// 注意：关于Task与Consumer(Saler)的多对一双向关联的级联问题。目前想要的效果是：
	// 1、对Task的插入、更新、修改及删除均不要对其关联的唯一Consumer级联执行。而是单独新增Consumer或者修改现有Consumer
	// 2、对Consumer的插入是Task的插入时有选择的进行，Consumer的其它操作（更新、修改、删除）不级联到Task
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(length = 18, nullable = false, unique = true)
	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 12)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ElementCollection
	public Map<Status, Date> getStatustime() {
		return statustime;
	}

	public void setStatustime(Map<Status, Date> statustime) {
		this.statustime = statustime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	@Column(length = 255)
	public String getMatter() {
		return matter;
	}

	public void setMatter(String matter) {
		this.matter = matter;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 8)
	public ApplyType getApplytype() {
		return applytype;
	}

	public void setApplytype(ApplyType applytype) {
		this.applytype = applytype;
	}

	@Basic
	public boolean isNeedexpert() {
		return needexpert;
	}

	public void setNeedexpert(boolean needexpert) {
		this.needexpert = needexpert;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "consumer_id")
	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "saler_id")
	public Saler getSaler() {
		return saler;
	}

	public void setSaler(Saler saler) {
		this.saler = saler;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, targetEntity = Expert.class)
	@JoinTable(name = "expert_task", schema = "", joinColumns = { @JoinColumn(name = "task_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "expert_id", nullable = false, updatable = false) })
	public Set<Expert> getExperts() {
		return experts;
	}

	public void setExperts(Set<Expert> experts) {
		this.experts = experts;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	// 关联的实体都共享同样的主键
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "TEXT")
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "TEXT")
	public String getConsultation() {
		return consultation;
	}

	public void setConsultation(String consultation) {
		this.consultation = consultation;
	}

	// 一对多单向关联
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY)
	public Set<UpFile> getRelatedfile() {
		return relatedfile;
	}

	public void setRelatedfile(Set<UpFile> relatedfile) {
		this.relatedfile = relatedfile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseid == null) ? 0 : caseid.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Task other = (Task) obj;
		if (caseid == null) {
			if (other.caseid != null) return false;
		} else if (!caseid.equals(other.caseid)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (status != other.status) return false;
		return true;
	}
}
