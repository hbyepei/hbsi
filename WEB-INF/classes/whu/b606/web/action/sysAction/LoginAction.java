package whu.b606.web.action.sysAction;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import whu.b606.dto.Gender;
import whu.b606.dto.Json;
import whu.b606.entity.Admin;
import whu.b606.entity.Expert;
import whu.b606.entity.Image;
import whu.b606.entity.User;
import whu.b606.pageModel.LoginInfo;
import whu.b606.service.AdminService;
import whu.b606.service.ExpertService;
import whu.b606.util.CommonUtils;
import whu.b606.web.action.BaseAction;

import com.opensymphony.xwork2.ModelDriven;

/**
 * @author yepei
 */
@Action("loginAction")
@Namespace("/")
public class LoginAction extends BaseAction implements ModelDriven<LoginInfo> {
	private static final long serialVersionUID = 5475208176958287272L;
	LoginInfo l = new LoginInfo();
	@Resource
	AdminService as;
	@Resource
	ExpertService es;
	User u;
	Json j = new Json();

	// 要接收的数据：name/password/usertype
	// 要返回的信息：json
	@Override
	public LoginInfo getModel() {
		return l;
	}

	public void updatePwd() {
		if (null != session.get("user")) {
			User u = (User) session.get("user");
			Admin a = null;
			Expert e = null;
			if (u instanceof Admin) {
				a = as.updatePwd((Admin) u, l.getPassword(), l.getNewPwd());
			} else if (u instanceof Expert) {
				e = es.updatePwd((Expert) u, l.getPassword(), l.getNewPwd());
			} else {
				j.setMsg("用户类型有误！");
			}
			if (a != null || e != null) {
				j.setOk(true);
				j.setMsg("密码修改成功！");
			} else {
				j.setMsg("密码错误！");
			}
		} else {
			j.setMsg("请先登录！");
		}
		WriteJson(j);
	}

	/**
	 * 用户登录
	 */
	public void login() {
		boolean succ = false;
		String msg = "";
		try {
			boolean first = init();// 若是第一次登录，则初始化一个管理员账户
			User u = null;
			if (!first) {// 非首次
				String usertype = l.getUsertype();
				if (null != usertype && !usertype.trim().equals("")) {
					switch (usertype.toLowerCase()) {
						case "admin":
							Admin a = as.findByNameAndPwd(l.getName(), l.getPassword());
							if (a != null) {
								u = a;
								u.setInfo(a.findImgAbsolutePath());
							}
							break;
						case "expert":
							String logintype = request.getParameter("logintype");
							Expert e = null;
							if (logintype == null) {
								e = es.findByNameAndPwd(l.getName(), l.getPassword());
							} else if (logintype.equals("remote")) {// 远程链接登录
								String p = request.getParameter("p");// md5加密的密码
								String id = request.getParameter("id");// 专家ID号
								String time = request.getParameter("t");// 链接有效期的起始时间
								if (p != null && id != null && time != null) {
									Integer eid = Integer.parseInt(id);
									long t = Long.parseLong(time);
									long now = new Date().getTime();
									if (now - t > 15 * 24 * 3600 * 1000) {// 链接超过了有效期
										msg = "链接已经过期！";
										throw new RuntimeException(msg);
									}
									e = es.find("o.id=?1 and o.password=?2", new Object[] { eid, p });// 根据id号和加密的密码查询专家实体
								}
							} else if (logintype.equals("null")) {}// 根据链接中的id号无法找到专家
							if (e != null) {
								u = e;
								u.setInfo(e.findImgAbsolutePath());
							}
							break;
						default:
							msg = "未知的用户类型！";
							break;
					}
					if (u != null) {
						session.put("user", u);// 如果用户找到了，则执行登录，登录成功
						succ = true;
						msg = "登录成功";
					} else {
						if (msg.equals("")) {
							msg = "用户名或密码错误！";
						}
					}
				} else {
					msg = "未知的用户类型！";
				}
			} else {// 首次
				msg = "第一次使用系统，系统已经为您创建了一个默认帐户，用户名和密码分别为：root和root，请登录成功后创建必要的管理账户。";
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		j.setMsg(msg);
		j.setOk(succ);
		WriteJson(j);
	}

	/**
	 * 检查是否是初次登录，若是，数据库中尚无数据，但创建一个新的管理员，并返回true，否则返回false
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean init() {
		List<Admin> admins = as.finds(null, null);
		if (admins != null && !admins.isEmpty()) {
			return false;
		} else {
			try {
				Admin a = new Admin();
				a.setGender(Gender.MALE);
				a.setUsername("管理员");
				a.setName("root");
				a.setAge(25);
				a.setPassword("root");
				// 将此文件存到指定路径
				String filename = "rootAdmin.png";
				File f = new File(CommonUtils.getProjectWebRoot() + "/images/admin/" + filename);
				if (f != null && f.exists()) {
					String savePath = request.getRealPath(CommonUtils.getParameter("config/commonData", "adminImageSavePath"));
					Map<String, String> fileinfo = CommonUtils.fileUpload(savePath, new File[] { f }, new String[] { filename });// key代表随机名，value代表自然名
					if (fileinfo != null && !fileinfo.isEmpty()) {
						Iterator<Entry<String, String>> it = fileinfo.entrySet().iterator();
						while (it.hasNext()) {
							Entry<String, String> en = it.next();
							Image img = new Image(en.getValue(), en.getKey(), "系统默认管理员");
							a.setImage(img);
							break;
						}
					}
				}
				as.save(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	/**
	 * 注销
	 */
	public void logout() {
		if (null != session.get("user")) {
			session.remove("user");
		}
		j.setOk(true);
		WriteJson(j);
	}
}
