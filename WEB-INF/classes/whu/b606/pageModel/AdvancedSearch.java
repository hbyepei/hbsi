/**
 * 
 */
package whu.b606.pageModel;

import java.util.Date;

import whu.b606.dto.ApplyType;
import whu.b606.dto.Status;

/**
 * @author yepei
 * 
 */
public class AdvancedSearch {
	private String adcaseid;
	private Status adstatus;
	private ApplyType adapplytype;
	private Date adstarttime;
	private Date adendtime;
	private String adname1;
	private String adname2;
	private String admodel;
	private String admatter;

	public String getAdcaseid() {
		return adcaseid;
	}

	public void setAdcaseid(String adcaseid) {
		this.adcaseid = adcaseid;
	}

	public Status getAdstatus() {
		return adstatus;
	}

	public void setAdstatus(Status adstatus) {
		this.adstatus = adstatus;
	}

	public ApplyType getAdapplytype() {
		return adapplytype;
	}

	public void setAdapplytype(ApplyType adapplytype) {
		this.adapplytype = adapplytype;
	}

	public Date getAdstarttime() {
		return adstarttime;
	}

	public void setAdstarttime(Date adstarttime) {
		this.adstarttime = adstarttime;
	}

	public Date getAdendtime() {
		return adendtime;
	}

	public void setAdendtime(Date adendtime) {
		this.adendtime = adendtime;
	}

	public String getAdname1() {
		return adname1;
	}

	public void setAdname1(String adname1) {
		this.adname1 = adname1;
	}

	public String getAdname2() {
		return adname2;
	}

	public void setAdname2(String adname2) {
		this.adname2 = adname2;
	}

	public String getAdmodel() {
		return admodel;
	}

	public void setAdmodel(String admodel) {
		this.admodel = admodel;
	}

	public String getAdmatter() {
		return admatter;
	}

	public void setAdmatter(String admatter) {
		this.admatter = admatter;
	}
}
