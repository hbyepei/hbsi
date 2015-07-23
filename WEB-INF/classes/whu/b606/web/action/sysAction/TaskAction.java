package whu.b606.web.action.sysAction;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.exception.ConstraintViolationException;

import whu.b606.dto.ApplyType;
import whu.b606.dto.FileType;
import whu.b606.dto.Json;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Status;
import whu.b606.entity.Car;
import whu.b606.entity.Expert;
import whu.b606.entity.Task;
import whu.b606.entity.UpFile;
import whu.b606.entity.User;
import whu.b606.pageModel.AdvancedSearch;
import whu.b606.pageModel.CarAccidentParts;
import whu.b606.pageModel.Grid;
import whu.b606.pageModel.TaskModel;
import whu.b606.service.ConsumerService;
import whu.b606.service.ExpertService;
import whu.b606.service.SalerService;
import whu.b606.service.TaskService;
import whu.b606.util.CommonUtils;
import whu.b606.util.MailSend;
import whu.b606.web.action.BaseAction;

@Action("taskAction")
@Namespace("/sys")
@ParentPackage("common")
@Results({
		// @Result(name = "print", location = "/page/print.jsp"),
		@Result(name = "docDownload", type = "stream", params = { "contentType", "application/msword", "inputName", "targetFileInputStream", "contentDisposition",
				"attachment;filename=${downloadFileName}", "bufferSize", "4096" }),// 文件下载
		@Result(name = "json", type = "json", params = { "root", "j", "excludeNullProperties", "false" }),
		@Result(name = "showUpfile", type = "stream", params = { "contentType", "${contentType}", "bufferSize", "4096", "inputName", "targetFileInputStream", "contentDisposition",
				"attachment;filename=${proofFileName}" }),
		@Result(name = "export2File", type = "stream", params = { "contentType", "${contentType}", "inputName", "export2FileInputStream", "contentDisposition",
				"attachment;filename=${export2FileName}", "bufferSize", "4096" }) })
// 文件下载
// 注，以上注解中，"root"参数表示JSON对象中的根对象，"excludeNullProperties"参数若为true，则空值将不会写入json对象中，若为false,则会写入json对象中，但只是写入一个null
// 另外，若加入“includeProperties”参数，则可指定其它需要被写入JSON对象中的数据
// ，若加入“excludeProperties”参数，则表示要从root所对应的参数对象中除去的属性
public class TaskAction extends BaseAction {
	private static final long serialVersionUID = 8944326225004510371L;
	private Integer id;
	private String doc, loadtype, sort, order, usertype, reason, docids;
	private boolean needexpert, exportAll;
	private int page, rows;
	private Date starttime, endtime;
	private AdvancedSearch ad;
	private TaskModel tm;
	private CarAccidentParts cap;
	private File[] upfile;// 使用File数组来封装多个文件上传域所上传的文件内容
	private String[] upfileContentType;// 使用字符串数组来封装多个文件上传项所对应的文件类型，请注意该字段的命名规范
	private String[] upfileFileName;// 使用字符串数组来封装多个文件上传项所对应的文件名字，请注意该字段的命名规范
	Json j = new Json();
	@Resource
	TaskService ts;
	@Resource
	SalerService ss;
	@Resource
	ConsumerService cs;
	@Resource
	ExpertService es;

	/**
	 * 通过审核一项任务，改变状态，发送邮件
	 */
	public void audit() {
		String msg = "";
		List<MailSend> ms = null;
		try {
			if (this.id != null) {
				Task t = ts.find(this.id);
				if (t != null) {
					t.setStatus(Status.processing);
					Map<Status, Date> statustime = t.getStatustime();
					statustime.put(Status.processing, new Date());
					if (this.needexpert) {// 进入此处应当将task的needexpert属性置为true
						t.setNeedexpert(true);
						Integer[] expertids = getIds(this.tm.getInfo());
						if (expertids != null && expertids.length > 0) {// 进入此处应当执行专家分配工作
							if (expertids.length > 5) {
								expertids = new Integer[] { expertids[0], expertids[1], expertids[2], expertids[3], expertids[4] };
							}
							Json result = ts.allocateExperts(t, expertids);
							msg = result.getMsg();
							if (result.isOk()) {
								msg = "已经通过审核，" + result.getMsg();
								t = (Task) result.getObject();
							}
						}
					} else {// 进入此处，应当将task的needexpert属性置为false
						t.setNeedexpert(false);
						msg = "审核通过！";
					}
					ms = ts.getMailSend(t);
					ts.update(t);
					j.setOk(true);
				} else {
					msg = "未找到申请数据！";
				}
			} else {
				msg = "ID为空！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "出现了错误：" + e.getMessage();
		} finally {
			j.setMsg(msg);
			sendMail(ms);
			WriteJson(j);
		}
	}

	/**
	 * 不予受理此任务，改变状态，发送邮件
	 */
	public void refuse() {
		String msg = "";
		List<MailSend> ms = null;
		try {
			if (this.id != null && this.reason != null) {
				Task t = ts.find(this.id);
				if (t != null) {
					t.setInfo(this.reason);
					t.setStatus(Status.refused);
					Map<Status, Date> statusmap = t.getStatustime();
					statusmap.put(Status.refused, new Date());
					ms = ts.getMailSend(t, Status.refused);
					ts.update(t);
					j.setOk(true);
				} else {
					msg = "未找到申请数据！";
				}
			} else {
				msg = "ID或不予受理的理由为空！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "出现了错误：" + e.getMessage();
		} finally {
			j.setMsg(msg);
			sendMail(ms);
			WriteJson(j);
		}
	}

	/**
	 * 完成调解任务
	 */
	public void complete() {
		completeOrTerminate(Status.complete);
	}

	/**
	 * 终止调解任务
	 */
	public void terminate() {
		completeOrTerminate(Status.terminate);
	}

	/**
	 * 专家结束调解，修改状态，修改任务属性，上传文件，发送邮件
	 */
	public void over() {
		String msg = "未找到数据！";
		try {
			if (this.tm != null && this.tm.getId() != null) {
				Task t = ts.find(tm.getId());
				if (t != null) {
					t.setInfo(tm.getInfo());
					t.setQuestion(tm.getQuestion());
					t.setConsultation(tm.getConsultation());
					Json fileUpload = ts.saveFile(upfile, upfileFileName, upfileContentType, FileType.CONSULT, "[咨询结果文件]");
					Set<UpFile> s = t.getRelatedfile();
					if (fileUpload.isOk()) {// 未出意外，也有可能没有文件
						Set<UpFile> sets = (Set<UpFile>) fileUpload.getObject();
						if (sets != null) {
							s.addAll(sets);
						}
					}
					ts.update(t);
					List<MailSend> ms = ts.getMailSend(t);
					sendMail(ms);
					msg = "操作成功！";
					j.setOk(true);
				}
			}
		} catch (Exception e) {
			msg = "错误:" + e.getMessage();
			e.printStackTrace();
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	/**
	 * 专家修改咨询意见，修改任务属性，上传文件
	 */
	public void modify() {
		String msg = "未找到数据！";
		try {
			if (this.tm != null && this.tm.getId() != null) {
				Task t = ts.find(tm.getId());
				if (t != null) {
					t.setQuestion(tm.getQuestion());
					t.setConsultation(tm.getConsultation());
					ts.update(t);
					msg = "操作成功！";
					j.setOk(true);
				}
			}
		} catch (Exception e) {
			msg = "错误:" + e.getMessage();
			e.printStackTrace();
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	/**
	 * 建立新任务
	 */
	public void addTask() {
		Json editResult = new Json();
		try {
			Object[] upfiles = new Object[] { this.upfile, this.upfileFileName, this.upfileContentType };
			if (tm != null && tm.isNeedexpert()) {
				tm.setExpertids(getIds(this.tm.getInfo()));
			}
			editResult = ts.addTask(tm, upfiles, this.cap);
			List<MailSend> ms = (List<MailSend>) editResult.getObject();
			super.sendMail(ms);
		} catch (Exception e) {
			String error = e.getMessage();
			if (e instanceof ConstraintViolationException || (e.getCause() != null && e.getCause() instanceof ConstraintViolationException)) {
				error = "数据存在重复！";
			}
			editResult.setMsg("错误:" + error);
		}
		WriteJson(editResult);
	}

	/**
	 * 加载待审核任务列表
	 */
	public void loadTasks() {
		User u = (User) session.get("user");
		Grid<TaskModel> g = ts.getTasks(getLoadtype(), new Object[] { this.starttime, this.endtime, this.ad }, new Pagedata(this.page, this.rows, this.sort, this.order), u);
		WriteJson(g);
	}

	/**
	 * 统计某种状态下的任务数量
	 */
	public void taskCount() {
		try {
			String[] str = this.loadtype.split(",");
			int[] count;
			if (str != null && str.length > 1) {
				count = new int[str.length + 1];
			} else {
				count = new int[] { 0, 0, 0 };
			}
			Status[] status = new Status[str.length];
			for (int i = 0; i < str.length; i++) {
				status[i] = Status.valueOf(str[i]);
			}
			String sql = "o.status=?@";
			String sqlForExpert = null;
			LinkedList<Object> olist = new LinkedList<>();
			// 若执行查询的用户是专家，则进行过滤
			User u = (User) session.get("user");
			if (u != null && (u instanceof Expert)) {
				Expert e = (Expert) u;
				Set<Task> tasks = e.getTasks();
				if (tasks != null && !tasks.isEmpty()) {
					sqlForExpert = " and (";
					for (Task t : tasks) {
						olist.add(t.getId());
						sqlForExpert += "o.id=?@ or ";
					}
					sqlForExpert = sql.trim();
					if (sqlForExpert.endsWith("or")) {
						sqlForExpert = sqlForExpert.substring(0, sqlForExpert.length() - 2).trim();
						sqlForExpert += ")";
					}
				} else {// 当前专家还没有任务，则不应该显示任何任务信息
					sqlForExpert = " and o.id=?@";
					olist.add(-1);// 这样一来，当专家没有任何任务时，将不会查到任何信息
				}
				sql += sqlForExpert;
			}
			int k = 1;
			while (sql.indexOf("?@") > -1) {
				sql = sql.replaceFirst("@", "" + k);
				k++;
			}
			for (int i = 0; i < str.length; i++) {
				olist.addFirst(status[i]);// 加入状态条件参数
				count[i] = ts.Count(sql, olist.toArray());
				olist.removeFirst();
			}
			if (sqlForExpert != null) {
				sqlForExpert = sqlForExpert.trim();
				if (sqlForExpert.startsWith("and")) {
					sqlForExpert = sqlForExpert.substring(3, sqlForExpert.length());
				}
				sqlForExpert = sqlForExpert.trim();
				if (sqlForExpert.startsWith("(") && sqlForExpert.endsWith(")")) {
					sqlForExpert = sqlForExpert.substring(1, sqlForExpert.length() - 1);
				}
				count[str.length] = ts.Count(ts.fillNumbersForSql(sqlForExpert), olist.toArray());
			} else {
				count[str.length] = ts.Count();
			}
			j.setObject(count);
			j.setOk(true);
			WriteJson(j);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获取用户详情
	 */
	public void getUserDetailInfo() {
		if (this.id != null && !CommonUtils.isNull(this.usertype)) {
			ApplyType ap = ApplyType.valueOf(this.usertype);
			User u = null;
			if (ap.equals(ApplyType.Consumer)) {
				u = cs.find(this.id);
			} else {
				u = ss.find(this.id);
			}
			if (u != null) {
				j.setOk(true);
				j.setObject(u);
			} else {
				j.setOk(false);
				j.setMsg("未找到用户！");
			}
			WriteJson(j);
		}
	}

	/**
	 * 获得下载文件流
	 * 
	 * @return 下载文件输入流
	 */
	public InputStream getTargetFileInputStream() {
		return ts.getDocInputStream(this.doc);
	}

	/**
	 * 获取文件的MIME类型
	 * 
	 * @return
	 */
	public String getContentType() {
		String tp = ts.getFileContentType(this.doc);
		return tp;
	}

	/**
	 * 返回文件下载的视图名
	 * 
	 * @return 执行文件下载的物理视图名
	 */
	public String download() {
		return "docDownload";
	}

	/**
	 * 获取下载显示的文件名称
	 * 
	 * @return 下载文件时显示的文件名
	 */
	public String getDownloadFileName() {
		return ts.getDownloadFileName(this.doc);
	}

	/**
	 * 获取凭证文件名称
	 * 
	 * @return
	 */
	public String getProofFileName() {
		String name = this.sort;
		try {
			name = new String(this.sort.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 显示文件名
	 * 
	 * @return
	 */
	public String showFile() {
		return "showUpfile";
	}

	/**
	 * 此方法是使用了json-plugin的插件的示例
	 * 
	 * @return
	 */
	public String fileExist() {
		String msg = "文件存在";
		if (!CommonUtils.isNull(this.doc)) {
			String path = CommonUtils.getProjectWebRoot() + CommonUtils.getParameter("config/commonData", "upfileSavePath") + "/" + this.doc;
			File f = new File(path);
			if (f.exists()) {
				j.setOk(true);
			} else {
				msg = "文件已经不存在了！";
			}
		} else {
			msg = "文件名为空！";
		}
		j.setMsg(msg);
		return "json";// 注意，当使用json类型的物理视图时，需要在Result中指定json数据中的根对象，并且要为此根对象提供get方法。否则前台收不到。
	}

	/**
	 * 返回一个任务的详细信息模型
	 */
	public void getTaskInfo() {
		String msg = "未找到任务信息！";
		if (this.id != null) {
			Task t = ts.find(id);
			if (t != null) {
				this.tm = new TaskModel(t);
				Integer[] ids = tm.getExpertids();
				Object[] objs = new Object[2];
				if (ids != null && ids.length > 0) {
					String str = "";
					for (int i = 0; i < ids.length; i++) {
						str += (i + ",");
					}
					this.tm.setInfo(str.substring(0, str.length() - 1));
				}
				Car car = t.getCar();
				CarAccidentParts cp = null;
				if (car != null) {
					cp = new CarAccidentParts(car.getAccident_part());
				}
				objs[0] = tm;
				objs[1] = cp;
				j.setObject(objs);
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
	 * 修改任务属性
	 */
	public void edit() {
		Json editResult = new Json();
		try {
			List<String> deletedFiles = (List<String>) session.get("deletedFiles");
			Object[] upfiles = new Object[] { this.upfile, this.upfileFileName, this.upfileContentType };
			if (tm != null && tm.isNeedexpert()) {
				tm.setExpertids(getIds(this.tm.getInfo()));
			}
			editResult = ts.update(tm, deletedFiles, upfiles, this.cap);
			List<MailSend> ms = (List<MailSend>) editResult.getObject();
			super.sendMail(ms);
			super.cleanSession("deletedFiles");
		} catch (Exception e) {
			String error = e.getMessage();
			if (e instanceof ConstraintViolationException || (e.getCause() != null && e.getCause() instanceof ConstraintViolationException)) {
				error = "数据存在重复！";
			}
			editResult.setMsg("错误:" + error);
		}
		WriteJson(editResult);
	}

	/**
	 * 记录要删除的凭证文件
	 */
	public void deleteFile() {
		List<String> deletedFiles = null;
		if (this.doc != null && this.id != null) {
			deletedFiles = (List<String>) session.get("deletedFiles");
			if (deletedFiles == null) {
				deletedFiles = new ArrayList<>();
			}
			deletedFiles.add(this.doc);
			session.put("deletedFiles", deletedFiles);
			j.setOk(true);
			WriteJson(j);
		}
	}

	/**
	 * 生成文件的InputStream流暂存Session中
	 */
	public void generateExportInputStream() {
		try {
			if (this.doc != null) {
				InputStream fileInputStream = null;
				if (doc.equals("excel")) {
					fileInputStream = ts.generateExcelInputStream(this.exportAll);
				} else if (doc.equals("word")) {
					fileInputStream = ts.generateWordZipInputStream(getIds(this.docids));
				}
				if (fileInputStream != null) {
					session.put("FileInputStream", fileInputStream);
					j.setOk(true);
				} else {
					j.setMsg("数据为空！");
				}
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		WriteJson(j);
	}

	/**
	 * 下载Excel文件，从Session中取出文件流
	 */
	public String export2File() {
		if (this.doc != null) {
			if (this.doc.equals("word")) {
				this.doc = "word.zip";// 这样加个后缀是为了在getContentType方法中引用此变量后能够返回正确的文件的MIME类型
			} else if (this.doc.equals("excel")) {
				this.doc = "excel.xls";
			} else {
				return ERROR;
			}
		}
		return "export2File";
	}

	/**
	 * 从Session中提取文件流返回给客户端
	 * 
	 * @return
	 */
	public InputStream getExport2FileInputStream() {
		InputStream in = (InputStream) session.get("FileInputStream");
		if (in == null) {
			System.out.println("流为空！");
		}
		cleanSession("FileInputStream");
		return in;
	}

	/**
	 * 下载的文件名
	 * 
	 * @return
	 */
	public String getExport2FileName() {
		String name = "文件";
		if (doc.equals("excel.xls")) {
			name = "所有申请数据.xls";
		} else if (doc.equals("word.zip")) {
			name = "任务档案.zip";
		}
		try {
			name = new String(name.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}

	public Json getJ() {// 此方法方便Struts2返回JOSN格式时查找对象之用
		return j;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getLoadtype() {
		String[] load = loadtype.split(",");
		loadtype = load[load.length - 1].trim();
		return loadtype;
	}

	public void setLoadtype(String loadtype) {
		this.loadtype = loadtype;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDocids() {
		return docids;
	}

	public void setDocids(String docids) {
		this.docids = docids;
	}

	public boolean isNeedexpert() {
		return needexpert;
	}

	public void setNeedexpert(boolean needexpert) {
		this.needexpert = needexpert;
	}

	public boolean isExportAll() {
		return exportAll;
	}

	public void setExportAll(boolean exportAll) {
		this.exportAll = exportAll;
	}

	public Integer getId() {
		return id;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AdvancedSearch getAd() {
		return ad;
	}

	public void setAd(AdvancedSearch ad) {
		this.ad = ad;
	}

	private void completeOrTerminate(Status sts) {
		String msg = "数据为空！";
		if (this.tm != null) {
			Integer id = tm.getId();
			String info = tm.getInfo();
			if (id != null) {
				try {
					Task t = ts.find(id);
					if (t != null) {
						t.setInfo(info);
						t.setStatus(sts);
						Map<Status, Date> st = t.getStatustime();
						st.put(sts, new Date());
						List<MailSend> ms = ts.getMailSend(t);
						j.setOk(true);
						msg = "操作成功！";
						sendMail(ms);
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg = "出了点小问题：" + e.getMessage();
				}
			}
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	private Integer[] getIds(String fromFeild) {
		if (fromFeild != null) {
			try {
				String[] str = fromFeild.split(",");
				Integer[] ids = new Integer[str.length];
				for (int i = 0; i < str.length; i++) {
					ids[i] = Integer.parseInt(str[i]);
				}
				return ids;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
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

	public TaskModel getTm() {
		return tm;
	}

	public void setTm(TaskModel tm) {
		this.tm = tm;
	}

	public CarAccidentParts getCap() {
		return cap;
	}

	public void setCap(CarAccidentParts cap) {
		this.cap = cap;
	}
}
