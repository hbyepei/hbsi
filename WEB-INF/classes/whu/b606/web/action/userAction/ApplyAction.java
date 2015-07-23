package whu.b606.web.action.userAction;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import whu.b606.dto.ApplyType;
import whu.b606.dto.FileType;
import whu.b606.dto.Json;
import whu.b606.dto.Status;
import whu.b606.dto.Usertype;
import whu.b606.entity.Accident_part;
import whu.b606.entity.Car;
import whu.b606.entity.Consumer;
import whu.b606.entity.Saler;
import whu.b606.entity.Task;
import whu.b606.entity.UpFile;
import whu.b606.entity.User;
import whu.b606.pageModel.CarAccidentParts;
import whu.b606.pageModel.TaskDetailModel;
import whu.b606.pageModel.TaskListModel;
import whu.b606.service.ConsumerService;
import whu.b606.service.SalerService;
import whu.b606.service.TaskService;
import whu.b606.util.MailSend;
import whu.b606.web.action.BaseAction;

import com.alibaba.fastjson.JSON;

/**
 * @author yepei
 */
@Action("applyAction")
@Namespace("/user")
@Results({ @Result(name = "preshow_applydoc", location = "/page/user/preshow_applydoc.jsp"),
		@Result(name = "preshow_consultdoc", location = "/page/user/preshow_consultdoc.jsp"),
		@Result(name = "servershow_applydoc", location = "/page/sys/showdetail.jsp"),
		// 文件下载
		@Result(name = "docDownload", type = "stream", params = { "contentType", "application/msword", "inputName", "targetFile", "contentDisposition", "attachment;filename=${downloadFileName}",
				"bufferSize", "4096" }) })
public class ApplyAction extends BaseAction {
	private static final long serialVersionUID = -2302336043151977707L;
	Json j = new Json();
	private Consumer c;
	private Saler s;
	private Task t;
	private UpFile f;
	private String usertype, looker;
	private CarAccidentParts cap;
	private File[] upfile;// 使用File数组来封装多个文件上传域所上传的文件内容
	private String[] upfileContentType;// 使用字符串数组来封装多个文件上传项所对应的文件类型，请注意该字段的命名规范
	private String[] upfileFileName;// 使用字符串数组来封装多个文件上传项所对应的文件名字，请注意该字段的命名规范
	private String userEmail, queryCode, docname;
	private Integer taskid;
	@Resource
	ConsumerService cs;
	@Resource
	SalerService ss;
	@Resource
	TaskService ts;

	/**
	 * 将申请类型及token存入session
	 */
	public void setType() {
		if (null != this.usertype && !"".equals(this.usertype)) {
			session.put("usertype", this.usertype);
			session.put("token", 0);
			j.setOk(true);
		}
		WriteJson(j);
	}

	/**
	 * 将申请方或被申请方信息存入Session
	 */
	public void setApplyerInfo() {
		if (this.c != null) {
			session.put("c", this.c);
			setHasEmail(this.c);
			if (this.c.getIdcard().trim().equals("")) {
				this.c.setIdcard(null);
			}
		} else if (this.s != null) {
			session.put("s", this.s);
			setHasEmail(this.s);
			if (this.s.getCode().trim().equals("")) {
				this.s.setCode(null);
			}
		}
		j.setOk(true);
		WriteJson(j);
	}

	/**
	 * 收集争议信息、产品信息、凭证文件信息、生成时间信息，返回查询码，发送邮件
	 * 
	 */
	public void finish() {
		// 防重复提交
		if (session.get("token") != null && (int) session.get("token") > 0) {
			j.setMsg("已经提交过了，请勿重复提交！");
			j.setOk(false);
			WriteJson(j);
			return;
		}
		// 将刚刚提交的也放入session以便于用户做修改
		if (this.t != null) {
			session.put("t", this.t);
			Car car = t.getCar();
			if (car != null) {
				Set<Accident_part> adp = car.getAccident_part();
				SetAccidentParts(adp, this.cap);
				Date dt = car.getDealtime();
				if (dt != null) {
					session.put("dealtime", new SimpleDateFormat("yyyy-MM-dd").format(dt));
				}
			}
		}
		List<MailSend> ms = null;
		try {
			// 先处理文件上传
			Set<UpFile> upfiles = new HashSet<>();
			Json upload = ts.saveFile(this.upfile, this.upfileFileName, this.upfileContentType, FileType.PROOF, "[凭证文件]");
			if (!upload.isOk()) {// 上传失败，原因可能有很多
				throw new RuntimeException(upload.getMsg());
			} else {// 上传成功
				if (upload.getObject() != null) {
					upfiles = (Set<UpFile>) upload.getObject();
				}
			}
			ApplyType at = ApplyType.valueOf((String) session.get("usertype"));
			Consumer cm = (Consumer) session.get("c");
			Saler sm = (Saler) session.get("s");
			Json taskInfo = ts.addNewTask(upfiles, cm, sm, at, this.t);
			if (taskInfo.isOk()) {// 成功添加后，再从这个task中获取查询码并上传文件，然后更新此task对象，最后发送邮件
				Task task = (Task) taskInfo.getObject();
				session.put("queryCode", taskInfo.getMsg());// 将查询码存到session中去
				// 检查是否需要分配专家，若是则根据页面传来的专家的id数组分配专家。若数组为空证明不是管理员手动创建，不能马上分配，应当将这些信息展示到首页提示区，以提醒管理员早点分配
				// 若数组不空，则立即分配。
				String sucInfo = "已保存此申请。";
				// 检查消费者、单位、专家的邮件地址信息并启动线程发送邮件
				ms = ts.getMailSend(task, null, Status.toaudit, taskInfo.getMsg());
				if (ms != null && !ms.isEmpty()) {
					sucInfo += "已向用户发送相关邮件通知！";
					super.sendMail(ms);
				}
				j.setMsg(sucInfo);
				j.setOk(true);
			} else {
				throw new RuntimeException(taskInfo.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("服务器出现了点小差错，未能成功提交申请！\n错误信息：\n" + e.getCause().getMessage());
			j.setOk(false);
		} finally {
			session.put("token", ((int) session.get("token")) + 1);
			// 最后销毁session中保存的东西
			cleanSession(new String[] { "c", "s", "dealtime", "t", "usertype" });
			WriteJson(j);
		}
	}

	/**
	 * 设置电子邮件
	 */
	public void setEmail() {
		MailSend m = null;
		try {
			String urtp = (String) session.get("usertype");
			String queryCode = (String) session.get("queryCode");
			if (this.userEmail != null && urtp != null && queryCode != null && !this.userEmail.equals("") && !urtp.equals("") && !queryCode.equals("")) {
				if (urtp.equals(Usertype.Consumer.toString())) {
					Consumer csm = cs.findByQuerycode(queryCode);
					if (csm != null) {
						csm.setEmail(this.userEmail);
						cs.update(csm);
						Task curTask = ts.findByCaseid(csm.getCurrentTask_caseid());
						m = cs.getStatusMailSend(curTask.getStatus(), curTask, queryCode);
						j.setMsg("保存成功，查询编号已经发送至此邮箱！");
						j.setOk(true);
					} else {
						j.setMsg("未找到用户，请重试！");
						j.setOk(false);
					}
				} else if (urtp.equals(Usertype.Saler.toString())) {
					Saler sal = ss.findByQuerycode(queryCode);
					if (sal != null) {
						sal.setEmail(this.userEmail);
						ss.update(sal);
						Task curTask = ts.findByCaseid(sal.getCurrentTask_caseid());
						m = ss.getStatusMailSend(curTask.getStatus(), curTask, queryCode);
						j.setMsg("保存成功，查询编号已经发送至此邮箱！");
						j.setOk(true);
					} else {
						j.setMsg("未找到用户，请重试！");
						j.setOk(false);
					}
				}
				if (m != null) {
					m.setDaemon(true);
					m.start();
				}
			} else {
				j.setMsg("值为空！");
				j.setOk(false);
			}
		} catch (Exception e) {
			j.setOk(false);
			j.setMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			sendMail(m);
			WriteJson(j);
		}
	}

	/**
	 * 查询任务列表
	 */
	public void query() {
		if (this.queryCode != null && !this.queryCode.equals("")) {
			Consumer cmr = cs.findByQuerycode(this.queryCode);
			Saler sal = null;
			Set<Task> tasks = null;
			String curCaseid = null;
			Usertype queryType = null;
			if (cmr != null) {
				tasks = cmr.getTasks();
				curCaseid = cmr.getCurrentTask_caseid();
				queryType = Usertype.Consumer;
			} else {
				sal = ss.findByQuerycode(this.queryCode);
				if (sal != null) {
					tasks = sal.getTasks();
					curCaseid = sal.getCurrentTask_caseid();
					queryType = Usertype.Saler;
				}
			}
			if (tasks != null && !tasks.isEmpty()) {// 如果有数据，那么就要予以显示
				List<TaskListModel> tlist = new ArrayList<>();
				for (Task task : tasks) {// 注意，tasks中即包含自己申请的，也包含被申请的任务
					tlist.add(new TaskListModel(task, curCaseid, queryType));
				}
				Collections.sort(tlist, new Comparator<TaskListModel>() {
					@Override
					public int compare(TaskListModel tm1, TaskListModel tm2) {
						Date d1 = tm1.getApplytime();
						Date d2 = tm2.getApplytime();
						String s1 = tm1.getTime();
						String s2 = tm2.getTime();
						int d = d2.compareTo(d1);
						if (s1.equals(TaskListModel.curTask)) {// 此时tm1应当最大
							d = -1;
						} else if (s2.equals(TaskListModel.curTask)) {// 此时tm2应当最大
							d = 1;
						}
						return d;
					}
				});
				session.put("tasks", tlist);
				j.setOk(true);
			} else {
				j.setOk(false);
				j.setMsg("未找到相关数据！");
			}
			WriteJson(j);
		}
	}

	/**
	 * 查看任务详情
	 */
	public void showTask() {
		String msg = "没有找到有效的任务！";
		if (this.taskid != null && !this.taskid.equals("")) {
			Task t = ts.find(this.taskid);
			if (t != null) {
				TaskDetailModel tdm = new TaskDetailModel(t);
				j.setObject(tdm);
				j.setOk(true);
			} else {
				j.setMsg(msg);
			}
		} else {
			j.setMsg(msg);
		}
		WriteJson(j);
	}

	/**
	 * 查看进度信息
	 */
	public void showPace() {
		String msg = "没有找到有效的任务！";
		if (this.taskid != null && !this.taskid.equals("")) {
			Task t = ts.find(this.taskid);
			if (t != null) {
				String jsonString = JSON.toJSONString(t, features);
				session.put("task", jsonString);// 存入JSON格式，方便页面中JS调用
				j.setOk(true);
			} else {
				j.setMsg(msg);
			}
		} else {
			j.setMsg(msg);
		}
		WriteJson(j);
	}

	/**
	 * 删除任务，只是简单地从用户的任务集合中移除即可
	 */
	public void deleteTask() {
		String msg = "没有找到有效的任务！";
		if (this.taskid != null && !this.taskid.equals("")) {
			Task t = ts.find(this.taskid);
			ApplyType at = null;
			if (t != null) {
				at = t.getApplytype();
				Set<Task> tasks = null;
				Consumer c = null;
				Saler s = null;
				if (at.equals(ApplyType.Consumer)) {
					c = t.getConsumer();
					tasks = c != null ? c.getTasks() : null;
					if (tasks != null && tasks.contains(t)) {
						t.setConsumer(null);
						tasks.remove(t);
						ts.update(t);
						cs.update(c);
						msg = "删除成功！";
						j.setOk(true);
					}
				} else if (at.equals(ApplyType.Saler)) {
					s = t.getSaler();
					tasks = s != null ? s.getTasks() : null;
					if (tasks != null && tasks.contains(t)) {
						t.setSaler(null);
						tasks.remove(t);
						ts.update(t);
						ss.update(s);
						msg = "删除成功！";
						j.setOk(true);
					}
				}
				// 还应更新session
				List<TaskListModel> tlist = (List<TaskListModel>) session.get("tasks");
				if (tlist != null && !tlist.isEmpty()) {
					for (TaskListModel tm : tlist) {
						if (tm.getId().equals(this.taskid)) {
							tlist.remove(tm);
							break;
						}
					}
				}
				session.put("tasks", tlist);
			}
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	/**
	 * 用户撤销了状态为"处理中"或“待审核”的任务
	 */
	public void cancelTask() {
		String msg = "没有找到有效的任务！";
		List<MailSend> ms = null;
		try {
			if (this.taskid != null && !this.taskid.equals("")) {
				Task t = ts.find(this.taskid);
				if (t != null) {
					t.setStatus(Status.terminate);
					Map<Status, Date> stime = t.getStatustime();
					stime.put(Status.terminate, new Date());
					t.setStatustime(stime);
					String prefix = "[用户已撤销]-";
					if (!t.getMatter().startsWith(prefix)) {
						t.setMatter(prefix + t.getMatter());
					}
					t.setInfo("用户已经主动撤销了此申请！");
					ms = ts.getMailSend(t);
					if (ms != null && !ms.isEmpty()) {
						for (MailSend msd : ms) {
							msd.setDaemon(true);
							msd.start();
						}
					}
					ts.update(t);
					msg = "操作成功！";
					j.setOk(true);
					// 还应更新session
					List<TaskListModel> tlist = (List<TaskListModel>) session.get("tasks");
					if (tlist != null && !tlist.isEmpty()) {
						for (TaskListModel tm : tlist) {
							if (tm.getId().equals(this.taskid)) {
								tm.setStatus(Status.terminate.getDescription());
								break;
							}
						}
					}
					session.put("tasks", tlist);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "操作失败，错误：" + e.getMessage();
		} finally {
			sendMail(ms);
			j.setMsg(msg);
			WriteJson(j);
		}
	}

	/**
	 * 为用户生成Word文档并下载
	 */
	public String downloadUserDoc() {
		return "docDownload";
	}

	/**
	 * 在线预览当前任务。
	 * 注意，在执行此步之前，session中已经有一个名为task的对象了，这是在showPace中存入的，但它是一个Task对象的Json字符串，
	 * 所以在页面中取用时请小心 。
	 */
	public String onlinePrelook() {
		if (this.taskid != null && this.docname != null) {
			Task t = ts.find(this.taskid);
			String sessionTask = (String) session.get("task");
			if (null != t) {
				if (sessionTask == null || this.looker.equals("admin")) {// 如果是管理员查看，则因为其可能查看多个任务，所以session中的任务对象应该发生变化
					String js = JSON.toJSONString(t, features);
					session.put("task", js);// 存入JSON格式，方便页面中JS调用
				}
				if (this.docname.equals("applydoc")) {
					return "preshow_applydoc";
				} else if (this.docname.equals("consultdoc")) {
					return "preshow_consultdoc";
				} else if (this.docname.equals("applydoc_server")) { return "servershow_applydoc"; }
			}
		}
		return ERROR;
	}

	/**
	 * 获得下载文件流，根据taskid查询出task，然后再根据docname确定要填充哪一个文档后返回文档流
	 * 
	 * @return 下载文件输入流
	 */
	public InputStream getTargetFile() {
		return ts.getDocInputStream(this.taskid, this.docname);
	}

	/**
	 * 获取下载显示的文件名称
	 * 
	 * @return 下载文件时显示的文件名
	 */
	public String getDownloadFileName() {
		return ts.getDownloadFileName(this.docname);
	}

	private void setHasEmail(User u) {
		Consumer c = null;
		Saler s = null;
		String email = null;
		if (u instanceof Consumer) {
			c = (Consumer) u;
			email = c.getEmail();
		} else if (u instanceof Saler) {
			s = (Saler) u;
			email = s.getEmail();
		}
		if (email != null && !email.equals("")) {
			String urtp = (String) session.get("usertype");
			if (urtp != null && !urtp.equals("")) {
				session.put("hasEmail", true);
			}
		}
	}

	/**
	 * 将事故部位设置到car中去
	 * 
	 * @param adp
	 * @param cap
	 */
	public static void SetAccidentParts(Set<Accident_part> adp, CarAccidentParts cap) {
		Class c = cap.getClass();
		Field[] f = c.getDeclaredFields();
		for (int i = 0; i < f.length; i++) {
			f[i].setAccessible(true);
			String fName = f[i].getName();// 属性名
			// Class<?> type=f[i].getType();
			// String tName=type.getName();//类型名
			try {
				Object v = f[i].get(cap);
				if (v != null) {
					Accident_part ap = new Accident_part();
					ap.setTime(new Date());
					ap.setName(fName);
					adp.add(ap);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public File[] getUpfile() {
		return upfile;
	}

	public void setUpfile(File[] upfile) {
		this.upfile = upfile;
	}

	public String[] getUpfileContentType() {
		return upfileContentType;
	}

	public void setUpfileContentType(String[] upfileContentType) {
		this.upfileContentType = upfileContentType;
	}

	public String[] getUpfileFileName() {
		return upfileFileName;
	}

	public void setUpfileFileName(String[] upfileFileName) {
		this.upfileFileName = upfileFileName;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public Consumer getC() {
		return c;
	}

	public void setC(Consumer c) {
		this.c = c;
	}

	public Saler getS() {
		return s;
	}

	public void setS(Saler s) {
		this.s = s;
	}

	public Task getT() {
		return t;
	}

	public void setT(Task t) {
		this.t = t;
	}

	public UpFile getF() {
		return f;
	}

	public void setF(UpFile f) {
		this.f = f;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public Integer getTaskid() {
		return taskid;
	}

	public void setTaskid(Integer taskid) {
		this.taskid = taskid;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getLooker() {
		return looker;
	}

	public void setLooker(String looker) {
		this.looker = looker;
	}

	public CarAccidentParts getCap() {
		return cap;
	}

	public void setCap(CarAccidentParts cap) {
		this.cap = cap;
	}
}
