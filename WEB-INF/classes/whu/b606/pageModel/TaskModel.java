package whu.b606.pageModel;

import java.util.Date;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import whu.b606.dto.ApplyType;
import whu.b606.dto.FileType;
import whu.b606.entity.Consumer;
import whu.b606.entity.Expert;
import whu.b606.entity.Saler;
import whu.b606.entity.Task;
import whu.b606.entity.UpFile;
import whu.b606.util.CommonUtils;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年12月3日
 * 
 **/
public class TaskModel {
	private Integer id;
	private String caseid;
	private String status;
	private Date applytime;
	private String applytype;
	private String matter;
	private String description;
	private boolean needexpert;
	private String[][] upfile;// 存放多个凭证文件的文件名和文件路径
	private Integer id_1;// 申请方id号
	private String name_1;// 申请方姓名
	private String phone_1;// 申请方电话
	private String email_1;
	private String code_1;// 申请方身份证号或机构代码
	private String agent_1;
	private String address_1;
	private Integer id_2;
	private String name_2;// 责任方。。。
	private String phone_2;
	private String email_2;
	private String code_2;
	private String agent_2;
	private String address_2;
	private String brand;// 车辆品牌
	private String model;// 车辆型号
	private String carriage;// 车架号
	private Date dealtime;// 购车时间
	private String info;// 备注信息
	private String question;// 咨询问题;
	private String consultation;// 专家给予的咨询意见
	private Integer[] expertids;

	public TaskModel(Task t) {
		try {
			BeanUtils.copyProperties(this, t);
			BeanUtils.copyProperties(this, t.getCar());
			if (CommonUtils.isNull(t.getQuestion())) {
				this.question = t.getDescription();
			}
			Set<UpFile> set = t.getRelatedfile();
			if (set != null && !set.isEmpty()) {
				this.upfile = new String[set.size()][2];
				int count = 0;
				for (UpFile f : set) {
					if (f.getFiletype().equals(FileType.PROOF)) {
						this.upfile[count][0] = f.getPathname();
						this.upfile[count][1] = f.getFilename();
						count++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApplyType ap = t.getApplytype();
		this.status = t.getStatus().getDescription();
		this.applytype = ap.getDescription();
		Consumer c = t.getConsumer();
		Saler s = t.getSaler();
		if (ap.equals(ApplyType.Consumer)) {
			if (c != null) {
				this.id_1 = c.getId();
				this.name_1 = c.getName();
				this.phone_1 = c.getPhone();
				this.code_1 = c.getIdcard();
				this.agent_1 = c.getAgent();
				this.address_1 = c.getAddress();
			}
			if (s != null) {
				this.id_2 = s.getId();
				this.name_2 = s.getName();
				this.phone_2 = s.getPhone();
				this.code_2 = s.getCode();
				this.agent_2 = s.getAgent();
				this.address_2 = s.getAddress();
			}
		} else {
			if (s != null) {
				this.id_1 = s.getId();
				this.name_1 = s.getName();
				this.phone_1 = s.getPhone();
				this.code_1 = s.getCode();
				this.agent_1 = s.getAgent();
				this.address_1 = s.getAddress();
			}
			if (c != null) {
				this.id_2 = c.getId();
				this.name_2 = c.getName();
				this.phone_2 = c.getPhone();
				this.code_2 = c.getIdcard();
				this.agent_2 = c.getAgent();
				this.address_2 = c.getAddress();
			}
		}
		Set<Expert> eset = t.getExperts();
		if (eset != null && !eset.isEmpty()) {
			this.expertids = new Integer[eset.size()];
			int count = 0;
			for (Expert e : eset) {
				this.expertids[count] = e.getId();
				count++;
			}
		}
	}

	public TaskModel() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	public String getApplytype() {
		return applytype;
	}

	public void setApplytype(String applytype) {
		this.applytype = applytype;
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

	public boolean isNeedexpert() {
		return needexpert;
	}

	public void setNeedexpert(boolean needexpert) {
		this.needexpert = needexpert;
	}

	public String[][] getUpfile() {
		return upfile;
	}

	public void setUpfile(String[][] upfile) {
		this.upfile = upfile;
	}

	public Integer getId_1() {
		return id_1;
	}

	public void setId_1(Integer id_1) {
		this.id_1 = id_1;
	}

	public String getName_1() {
		return name_1;
	}

	public void setName_1(String name_1) {
		this.name_1 = name_1;
	}

	public String getPhone_1() {
		return phone_1;
	}

	public void setPhone_1(String phone_1) {
		this.phone_1 = phone_1;
	}

	public String getCode_1() {
		return code_1;
	}

	public void setCode_1(String code_1) {
		this.code_1 = code_1;
	}

	public Integer getId_2() {
		return id_2;
	}

	public void setId_2(Integer id_2) {
		this.id_2 = id_2;
	}

	public String getName_2() {
		return name_2;
	}

	public void setName_2(String name_2) {
		this.name_2 = name_2;
	}

	public String getPhone_2() {
		return phone_2;
	}

	public void setPhone_2(String phone_2) {
		this.phone_2 = phone_2;
	}

	public String getCode_2() {
		return code_2;
	}

	public void setCode_2(String code_2) {
		this.code_2 = code_2;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCarriage() {
		return carriage;
	}

	public void setCarriage(String carriage) {
		this.carriage = carriage;
	}

	public Date getDealtime() {
		return dealtime;
	}

	public void setDealtime(Date dealtime) {
		this.dealtime = dealtime;
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

	public Integer[] getExpertids() {
		return expertids;
	}

	public void setExpertids(Integer[] expertids) {
		this.expertids = expertids;
	}

	public String getAgent_1() {
		return agent_1;
	}

	public void setAgent_1(String agent_1) {
		this.agent_1 = agent_1;
	}

	public String getAgent_2() {
		return agent_2;
	}

	public void setAgent_2(String agent_2) {
		this.agent_2 = agent_2;
	}

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getEmail_1() {
		return email_1;
	}

	public void setEmail_1(String email_1) {
		this.email_1 = email_1;
	}

	public String getEmail_2() {
		return email_2;
	}

	public void setEmail_2(String email_2) {
		this.email_2 = email_2;
	}
}
