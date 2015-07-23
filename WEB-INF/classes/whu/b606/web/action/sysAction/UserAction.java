/**
 * 
 */
package whu.b606.web.action.sysAction;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import whu.b606.dto.Json;
import whu.b606.entity.Admin;
import whu.b606.entity.Expert;
import whu.b606.entity.User;
import whu.b606.service.AdminService;
import whu.b606.service.ExpertService;
import whu.b606.web.action.BaseAction;

/**
 * @author yepei
 * 
 */
@Action("userAction")
@Namespace("/sys")
@ParentPackage("common")
public class UserAction extends BaseAction {
	private static final long serialVersionUID = -3600225263400512922L;
	private String password;
	private String newPwd;
	Json j = new Json();
	@Resource
	AdminService as;
	@Resource
	ExpertService es;

	/**
	 * 修改密码
	 */
	public void updatePwd() {
		User u;
		Admin a;
		try {
			u = (User) session.get("user");
			a = null;
			Expert e = null;
			if (u != null) {
				if (u instanceof Admin) {
					a = as.updatePwd((Admin) u, this.password, this.newPwd);
				} else if (u instanceof Expert) {
					e = es.updatePwd((Expert) u, this.password, this.newPwd);
				}
				if (a != null || e != null) {
					j.setMsg("修改成功，请重新登录！");
					j.setOk(true);
					WriteJson(j);
					cleanSession("user");
				} else {
					throw new RuntimeException("修改失败，请检查原密码是否正确！");
				}
			} else {
				throw new RuntimeException("未登录，不能修改密码！");
			}
		} catch (Exception e1) {
			j.setMsg("错误信息：\n" + e1.getMessage());
			j.setOk(false);
			WriteJson(j);
		}
	}

	/**
	 * 个人信息
	 */
	public void userInfo() {
		// write
		j.setMsg("此处可以使用类似于先前的专家编辑界面进行处理");
		WriteJson(j);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
