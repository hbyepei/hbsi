package whu.b606.serviceBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.ApplyType;
import whu.b606.dto.FileType;
import whu.b606.dto.Json;
import whu.b606.dto.Order;
import whu.b606.dto.Page;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Status;
import whu.b606.dto.WhereJPQL;
import whu.b606.entity.Accident_part;
import whu.b606.entity.Car;
import whu.b606.entity.Consumer;
import whu.b606.entity.Expert;
import whu.b606.entity.Saler;
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
import whu.b606.service.UpFileService;
import whu.b606.util.CommonUtils;
import whu.b606.util.MailSend;
import whu.b606.util.ZipCompressor;
import whu.b606.web.action.userAction.ApplyAction;

/**
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 **/
@Service(TaskService.BEAN_NAME)
public class TaskServiceImpl extends DaoSupport<Task> implements TaskService {
	@Resource
	UpFileService fs;
	@Resource
	ConsumerService cs;
	@Resource
	SalerService ss;
	@Resource
	ExpertService es;

	@Override
	public void deleteTask(Integer... caseids) {
		String whereJPQL = "o.caseid=?1";
		for (int i = 0; i < caseids.length - 1; i++) {
			whereJPQL += " or o.caseid=?" + (i + 2);
		}
		List<Task> l = this.finds(whereJPQL, caseids);
		if (null != l && l.size() > 0) {
			for (int i = 0; i < l.size(); i++) {
				Task t = l.get(i);
				delete(t.getId());
				// 接下来删除文件
				fs.delete(t.getRelatedfile());
			}
		}
	}

	@Override
	public Task findByCaseid(String caseid) {
		return find("o.caseid=?1", new Object[] { caseid });
	}

	/**
	 * 将申请信息提取并封装存储，新建任务。要考虑的方面非常多。罗列如下：
	 * 1、收集争议信息、产品信息、凭证文件信息、消费者信息、单位信息、申请时间信息、申请类型。 2、生成Task对象，并保存。
	 * 3、查询用户或单位（根据身份证或机构代码）是否存在，若是，则更新，否则添加。 4、返回查询码
	 * 
	 * @param upfiles
	 *            上传的文件信息
	 * @param c
	 *            消费者
	 * @param s
	 *            机构用户
	 * @param tp
	 *            申请类型
	 * @param t
	 *            任务信息
	 * @return Json格式的操作结果信息 若操作成功，则ok=true，msg=查询码(未加密的)， object=task（成功保存的对象）
	 *         若操作失败，则ok=false，msg=提示信息，object=null
	 */
	@Override
	public Json addNewTask(Set<UpFile> upfiles, Consumer c, Saler s, ApplyType tp, Task t) {
		Json r = new Json();
		if (upfiles == null || c == null || s == null || tp == null || t == null) {
			r.setMsg("参数不能为null");
			return r;
		}
		t.setApplytime(new Date());
		t.setApplytype(tp);
		t.setCaseid(CommonUtils.makeCaseId());
		t.setRelatedfile(upfiles);
		t.setStatus(Status.toaudit);
		t.setInfo("申请已经提交，等待审核...");
		Map<Status, Date> times = new LinkedHashMap<>();
		times.put(t.getStatus(), t.getApplytime());
		t.setStatustime(times);
		String queryCode = "";
		boolean flag = true;
		while (flag) {
			queryCode = CommonUtils.makeQueryCode();
			flag = cs.findByQuerycode(queryCode) != null || ss.findByQuerycode(queryCode) != null;
		}
		try {
			// 需要单独保存task并保存或更新c、s（不能靠级联）
			Consumer cmr = cs.saveOrUpdate(c, queryCode, t);
			Saler slr = ss.saveOrUpdate(s, queryCode, t);
			if (cmr != null) {
				t.setConsumer(cmr);
			}
			if (slr != null) {
				t.setSaler(slr);
			}
			super.save(t);
			if (t.getId() != null) {// 证明保存成功
				r.setObject(t);
				r.setMsg(queryCode);
				r.setOk(true);
			} else {
				r.setMsg("出了点差错！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			r.setMsg(e.getMessage());
		}
		return r;
	}

	/**
	 * 文件上传
	 */
	@Override
	public Json saveFile(File[] upfile, String[] upfileFileName, String[] upfileContentType, FileType ft, String description) {
		Json filesInfo = new Json();
		if (upfileFileName != null && upfileFileName.length > 0) {// 有文件上传项
			try {
				if (CommonUtils.TotalfileSize(upfile) > 10485760) {// 超过10M
					throw new RuntimeException("您上传的文件过大，请重新选择！(不要超过10M)");
				}
				int index = CommonUtils.allowedFileTypes(upfileContentType, upfileFileName, "common", "config/commonData");
				if (index > -1) { throw new RuntimeException("文件类型错误：您上传的第" + (index + 1) + "个文件(“" + upfileFileName[index] + "”)类型不符合要求。(只接受如下类型文件：\n“doc,docx,xls,xlsx,ppt,pptx,wps,et,dps,txt,pdf,jpg\n,jpeg,png,gif,bmp”)"); }
				String savePath = CommonUtils.getProjectWebRoot() + CommonUtils.getParameter("config/commonData", "upfileSavePath") + "/";
				// 得到实际保存路径并执行上传
				Map<String, String> m = CommonUtils.fileUpload(savePath, upfile, upfileFileName);// 将文件上传至指定的路径，并返回这些文件的物理名称数组;
				// 若上传无误，则创建File实体信息
				if (m != null && !m.isEmpty()) {
					Set<UpFile> sets = new HashSet<>();
					for (Map.Entry<String, String> me : m.entrySet()) {
						UpFile uf = new UpFile();
						uf.setPathname(me.getKey());
						uf.setFilename(me.getValue());
						uf.setFiletype(ft);
						uf.setDescription(description);
						sets.add(uf);
					}
					filesInfo.setObject(sets);
					filesInfo.setOk(true);
				} else {
					filesInfo.setMsg("上传过程中发生了意外！");
				}
			} catch (Exception e) {
				filesInfo.setMsg(e.getMessage());
			}
		} else {
			filesInfo.setOk(true);
		}
		return filesInfo;
	}

	/**
	 * 为Task分配若干专家
	 */
	@Override
	public String setExperts(Task task, Integer[] expertids) {
		Task t = find(task.getId());
		String msg = "";
		if (t != null) {
			if (expertids != null && expertids.length > 0) {
				Set<Expert> epts = new HashSet<>();
				for (Integer id : expertids) {
					Expert e = es.find(id);
					if (e != null) {
						epts.add(e);
					}
				}
				if (epts.isEmpty()) {
					msg = "指定的专家未找到！";
				} else {
					if (epts.size() == expertids.length) {
						msg = "已经成功分配专家！";
					} else {
						msg = "只成功分配了" + epts.size() + "个专家！";
					}
					t.setExperts(epts);
					update(t);
				}
			} else {
				msg = "未指定专家";
			}
		} else {
			msg = "任务在数据库中不存在，无法分配专家！";
		}
		return msg;
	}

	/**
	 * 根据任务状态提供有关各方的邮件发送线程对象
	 */
	@Override
	public List<MailSend> getMailSend(Task task, Integer[] expertids, Status status, String queryCode) {
		List<MailSend> list = new ArrayList<>();
		if (task != null) {
			MailSend mc = cs.getStatusMailSend(status, task, queryCode);
			MailSend ms = ss.getStatusMailSend(status, task, queryCode);
			if (mc != null) {
				list.add(mc);
			}
			if (ms != null) {
				list.add(ms);
			}
			if (expertids != null && expertids.length > 0 && task.isNeedexpert()) {
				for (Integer id : expertids) {
					MailSend me = es.getBussinessMail(id, task, status);
					if (me != null) {
						list.add(me);
					}
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public List<MailSend> getMailSend(Task t) {
		if (t == null) { return null; }
		Set<Expert> ets = t.getExperts();
		Integer[] eids = null;
		if (ets != null && !ets.isEmpty()) {
			eids = new Integer[ets.size()];
			int c = 0;
			for (Expert e : ets) {
				eids[c] = e.getId();
				c++;
			}
		}
		Status st = t.getStatus();
		return getMailSend(t, eids, st, null);
	}

	@Override
	public InputStream getDocInputStream(String docname) {
		InputStream in = null;
		if (!CommonUtils.isNull(docname)) {
			try {
				String path = CommonUtils.getProjectWEBINF() + "/file/docs/" + docname + ".doc";
				String pt = CommonUtils.getProjectWEBINF() + "/file/" + docname;
				File f = new File(path);
				File other = new File(pt);
				if (f.exists()) {// 文件在docs目录下不存在
					in = new FileInputStream(f);
				} else if (other.exists()) {
					in = new FileInputStream(other);
				} else {
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in;
	}

	/**
	 * 根据docname选择一个文件模板，将task中的数据填充进去后返回输入流
	 * 
	 * @throws Exception
	 */
	@Override
	public InputStream getDocInputStream(Integer taskid, String docname) {
		Task t = find(taskid);
		if (t != null && docname != null) {
			switch (docname) {
				case "applydoc":
					return getApplyDocInputStream(t, docname);
				case "consultdoc":
					return getConsultInputStream(t, docname);
				case "consult_resultdoc":
					return getConsultResultInputStream(t, docname);
				case "terminatedoc":
					return getTerminateInputStream(t, docname);
				default:
					return null;
			}
		}
		return null;
	}

	@Override
	public String getDownloadFileName(String doc) {
		String downFileName = "";
		if (!CommonUtils.isNull(doc)) {
			switch (doc) {
				case "applydoc":
					downFileName = "家用汽车产品三包责任争议第三方调解申请书.doc";
					break;
				case "consultdoc":
					downFileName = "家用汽车疑难问题技术咨询申请书.doc";
					break;
				case "consult_resultdoc":
					downFileName = "家用汽车疑难问题技术咨询结果.doc";
					break;
				case "terminatedoc":
					downFileName = "家用汽车产品三包责任争议第三方调解终结通知书.doc";
					break;
				default:
					downFileName = "未知文件名";
					break;
			}
			try {
				downFileName = new String(downFileName.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return downFileName;
	}

	@Override
	public Grid<TaskModel> getTasks(String loadtype, Object[] params, Pagedata pagedata, User u) {
		WhereJPQL wj = makeJPQL(loadtype, params, u);
		List rows = getTaskModels(pagedata, wj.getWhereJPQL(), wj.getParams());
		Long total = (long) Count(wj.getWhereJPQL(), wj.getParams());
		Grid<TaskModel> grid = new Grid();
		grid.setTotal(total);
		grid.setRows(rows);
		return grid;
	}

	/**
	 * 分配专家，然后返回分配了专家之后的任务。注意，无须修改任务状态且分配专家后不持久化到数据库，持久化操作放在Action中统一执行
	 * 若至少分配成功一个专家，则j.setOk(true)，j.setMsg("成功分配几位专家，未能分配几位专家。")，j.setObject(
	 * task) 若全部分配失败，则j.setOk(false)，j.setMsg("失败原因")
	 */
	@Override
	public Json allocateExperts(Task t, Integer[] expertids) {
		int count = 0;
		Json result = new Json();
		Set<Expert> set = t.getExperts();
		for (Integer id : expertids) {
			Expert e = es.find(id);
			if (e != null) {
				set.add(e);
				count++;
			}
		}
		String msg = "";
		if (count > 0) {
			result.setOk(true);
			msg = count < expertids.length ? "成功分配" + count + "位专家，失败" + (expertids.length - count) + "位！" : "专家分配成功";
			result.setObject(t);
		} else {
			msg = "专家未分配成功！";
		}
		result.setMsg(msg);
		return result;
	}

	@Override
	public List<MailSend> getMailSend(Task t, Status s) {
		return getMailSend(t, null, s, null);
	}

	/**
	 * 根据文件后缀获得文件的MIME类型
	 */
	@Override
	public String getFileContentType(String doc) {
		String t = "";
		if (!CommonUtils.isNull(doc)) {
			String ext = doc.substring(doc.lastIndexOf('.') + 1);
			switch (ext) {
				case "doc":
					t = "application/msword";
					break;
				case "docx":
					t = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
					break;
				case "xls":
					t = "application/vnd.ms-excel";
					break;
				case "xlsx":
					t = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
					break;
				case "ppt":
					t = "application/vnd.ms-powerpoint";
					break;
				case "pptx":
					t = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
					break;
				case "wps":
					t = "application/vnd.ms-works";
					break;
				case "et":
					t = "application/vnd.ms-set";
					break;
				case "dps":
					t = "application/vnd.ms-sdps";
					break;
				case "txt":
					t = "text/plain";
					break;
				case "pdf":
					t = "application/pdf";
					break;
				case "jpg":
					t = "image/jpeg";
					break;
				case "jpeg":
					t = "image/jpeg";
					break;
				case "png":
					t = "image/png";
					break;
				case "gif":
					t = "image/gif";
					break;
				case "bmp":
					t = "image/bmp";
					break;
				case "zip":
					t = "application/vnd.zul";
					break;
				default:
					return null;
			}
		}
		return t;
	}

	/**
	 * 根据页面模型修改任务属性(只修改tm中出现的属性)。若修改成功，根据需要选择是否发送邮件
	 * 
	 * @param tm
	 *            保存新新属性的实体
	 * @param deletedFiles
	 *            要删除的文件
	 * @param upfiles
	 *            要重新上传的文件
	 * @return 返回修改结果
	 */
	@Override
	public Json update(TaskModel tm, List<String> deletedFiles, Object[] upfiles, CarAccidentParts cap) {
		String nullmsg = "数据为空！";
		String msg = nullmsg;
		boolean ok = false;
		List<MailSend> ms = new ArrayList<>();
		Object[] obj = null;
		try {
			if (tm != null && tm.getId() != null) {
				Task t = find(tm.getId());
				if (t != null) {
					Car car = t.getCar();
					if (car != null) {
						Set<Accident_part> adp = new HashSet<>();
						ApplyAction.SetAccidentParts(adp, cap);
						car.setAccident_part(adp);
					}
					Json upresult = updateTask(t, tm, deletedFiles);
					obj = (Object[]) upresult.getObject();
					t = (Task) obj[0];
				}
				if (t == null) { throw new RuntimeException("未找到待修改的实体！"); }
				if (upfiles != null && upfiles.length == 3) {
					File[] files = (File[]) upfiles[0];
					String[] filenames = (String[]) upfiles[1];
					String[] filetypes = (String[]) upfiles[2];
					// 处理文件上传
					Set<UpFile> fileset = t.getRelatedfile();
					Json upload = saveFile(files, filenames, filetypes, FileType.PROOF, "[凭证文件]");
					if (upload.isOk() && upload.getObject() != null) {// 上传成功
						fileset.addAll((Set<UpFile>) upload.getObject());
					}
					msg = upload.getMsg();
					update(t);
					if (!msg.equals(nullmsg) && msg.length() > 0) {
						msg += ",";
					}
					// t更新到数据库中之后执行文件的实际删除
					Set<UpFile> tobeDeleted = (Set<UpFile>) obj[6];
					if (tobeDeleted != null && !tobeDeleted.isEmpty()) {
						fs.delete(tobeDeleted);
					}
					msg += "修改成功！";
					ok = true;
					// 最后，在返回之前，获取发送邮件的线程集合
					String code1 = (String) obj[3], code2 = (String) obj[4];
					boolean n1 = (boolean) obj[1], n2 = (boolean) obj[2];
					List<Integer> eneedEmail = (List<Integer>) obj[5];
					if (n1) {
						ms.add(cs.getStatusMailSend(t.getStatus(), t, code1));
					}
					if (n2) {
						ms.add(ss.getStatusMailSend(t.getStatus(), t, code2));
					}
					if (!eneedEmail.isEmpty()) {
						for (Integer id : eneedEmail) {
							ms.add(es.getBussinessMail(id, t, t.getStatus()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return new Json(ok, msg, ms);
	}

	/**
	 * 建立新任务，分配专家，发送邮件等
	 */
	@Override
	public Json addTask(TaskModel tm, Object[] upfiles, CarAccidentParts cap) {
		String nullmsg = "数据为空！";
		String msg = nullmsg;
		boolean ok = false;
		List<MailSend> ms = new ArrayList<>();
		String queryCode = "";
		try {
			if (tm != null) {
				Task t = new Task();
				// ========为t设置基本属性=========
				t.setApplytime(new Date());
				t.setCaseid(CommonUtils.makeCaseId());
				t.setApplytype(ApplyType.string2Enum(tm.getApplytype()));
				t.setConsultation(tm.getConsultation());
				t.setDescription(tm.getDescription());
				t.setMatter(tm.getMatter());
				t.setNeedexpert(tm.isNeedexpert());
				t.setQuestion(tm.getQuestion());
				t.setInfo("管理员新建的任务");
				t.setStatus(Status.processing);
				Map<Status, Date> st = t.getStatustime();
				st.put(Status.toaudit, new Date());
				st.put(Status.processing, new Date());
				// ========设置汽车=========
				Car car = new Car();
				car.setBrand(tm.getBrand());
				car.setCarriage(tm.getCarriage());
				car.setDealtime(tm.getDealtime());
				car.setModel(tm.getModel());
				ApplyAction.SetAccidentParts(car.getAccident_part(), cap);
				t.setCar(car);
				// ========设置消费者用户=========
				Consumer c = new Consumer();
				c.setName(tm.getName_1());
				c.setAgent(tm.getAgent_1());
				c.setPhone(tm.getPhone_1());
				c.setIdcard(tm.getCode_1());
				c.setEmail(tm.getEmail_1());
				c.setCurrentTask_caseid(t.getCaseid());
				c.setAddress(tm.getAddress_1());
				Set<Task> cset = c.getTasks();
				cset.add(t);
				// ========设置机构用户=========
				Saler s = new Saler();
				s.setName(tm.getName_2());
				s.setAddress(tm.getAddress_2());
				s.setAgent(tm.getAgent_2());
				s.setPhone(tm.getPhone_2());
				s.setCode(tm.getCode_2());
				s.setEmail(tm.getEmail_2());
				s.setCurrentTask_caseid(t.getCaseid());
				Set<Task> sset = s.getTasks();
				sset.add(t);
				// ========设置申请方的查询码=========
				boolean flag = true;
				while (flag) {
					queryCode = CommonUtils.makeQueryCode();
					flag = cs.findByQuerycode(queryCode) != null || ss.findByQuerycode(queryCode) != null;
				}
				if (t.getApplytype().equals(ApplyType.Consumer)) {
					c.setQuerycode(CommonUtils.makeMD5(queryCode));
				} else if (t.getApplytype().equals(ApplyType.Saler)) {
					s.setQuerycode(CommonUtils.makeMD5(queryCode));
				}
				// ========保存申请方与被申请方=========
				// cs.save(c);此方法无法保证对同一用户新建多个任务
				// ss.save(s);
				t.setConsumer(cs.saveOrUpdate(c, queryCode, t));
				t.setSaler(ss.saveOrUpdate(s, queryCode, t));
				// ==========分配专家==========
				Integer[] eids = tm.getExpertids();
				if (eids != null) {
					Json jsn = allocateExperts(t, eids);
					msg = jsn.getMsg();
				}
				// =============== 处理文件上传=================
				if (upfiles != null && upfiles.length == 3) {
					Json jsn = saveFile((File[]) upfiles[0], (String[]) upfiles[1], (String[]) upfiles[2], FileType.PROOF, "[凭证文件]");
					if (jsn.isOk()) {
						Set<UpFile> set = (Set<UpFile>) jsn.getObject();
						if (set != null && !set.isEmpty()) {
							Set<UpFile> rf = t.getRelatedfile();
							rf.addAll(set);
						}
					} else {
						if (!msg.equals(nullmsg) && msg.length() > 0) {
							msg += ",";
						}
						msg += jsn.getMsg();
					}
				}
				// ===========持久化=============
				save(t);
				ok = true;
				if (!msg.equals(nullmsg) && msg.length() > 0) {
					msg += ",";
				}
				msg += "保存成功！";
				ms = getMailSend(t, tm.getExpertids(), Status.processing, queryCode);
			}
		} catch (Exception e) {
			throw e;
		}
		return new Json(ok, msg, ms);
	}

	@Override
	public String fillNumbersForSql(String sql) {
		int k = 1;
		while (sql.indexOf("?@") > -1) {
			sql = sql.replaceFirst("@", "" + k);
			k++;
		}
		return sql;
	}

	@Override
	public InputStream generateExcelInputStream(boolean exportAll) throws Exception {
		String whereJPQL = null;
		Object[] params = null;
		if (!exportAll) {
			whereJPQL = "o.status=?1 or o.status=?2 or o.status=?3";// 除了待审核、处理中、已出咨询意见的任务之外
			params = new Object[] { Status.refused, Status.complete, Status.terminate };
		}
		try {
			List<Task> list = finds(whereJPQL, params);
			if (list != null && !list.isEmpty()) {
				String template = CommonUtils.getProjectWEBINF() + "/file/docs/templates/alltasks.xls";
				File tempFile = new File(template);// 1.得到Excel的模板文件
				if (!tempFile.exists() || !tempFile.isFile()) {
					throw new RuntimeException("模板文件:[" + tempFile + "]不存在！");
				} else {
					Workbook wb = Workbook.getWorkbook(tempFile);// 2.创建只读的Excel工作薄的对象
					ByteArrayOutputStream out = new ByteArrayOutputStream();// 3.创建输出流
					WritableWorkbook workbook = Workbook.createWorkbook(out, wb);// 4.获得可写的工作薄
					WritableSheet sheet = workbook.getSheet(0); // 5.读取工作薄中的第一张工作表
					int k = 2;
					DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
					for (Task t : list) {
						TaskModel tm = new TaskModel(t);
						int j = 0;
						setCellValue(sheet, k, j++, tm.getCaseid());// 第0列
						setCellValue(sheet, k, j++, df1.format(tm.getApplytime()));// 1
						setCellValue(sheet, k, j++, tm.getStatus());
						setCellValue(sheet, k, j++, tm.isNeedexpert() ? "是" : "否");// 3
						setCellValue(sheet, k, j++, tm.getMatter());
						setCellValue(sheet, k, j++, tm.getDescription());
						setCellValue(sheet, k, j++, tm.getConsultation());
						setCellValue(sheet, k, j++, tm.getBrand());
						setCellValue(sheet, k, j++, tm.getModel());
						setCellValue(sheet, k, j++, tm.getCarriage());
						setCellValue(sheet, k, j++, tm.getDealtime() == null ? "" : df2.format(tm.getDealtime()));// 10
						setCellValue(sheet, k, j++, tm.getInfo());
						setCellValue(sheet, k, j++, tm.getName_1());
						setCellValue(sheet, k, j++, tm.getCode_1());// 13
						setCellValue(sheet, k, j++, tm.getAgent_1());// 14
						setCellValue(sheet, k, j++, tm.getPhone_1());// 15
						setCellValue(sheet, k, j++, tm.getAddress_1());
						setCellValue(sheet, k, j++, tm.getName_2());
						setCellValue(sheet, k, j++, tm.getCode_2());// 18
						setCellValue(sheet, k, j++, tm.getAgent_2());// 19
						setCellValue(sheet, k, j++, tm.getPhone_2());// 20
						setCellValue(sheet, k, j++, tm.getAddress_2());
						k++;
					}
					workbook.write();
					workbook.close();
					wb.close();
					return new ByteArrayInputStream(out.toByteArray());
				}
			} else {
				throw new RuntimeException("数据库中无数据！");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public InputStream generateWordZipInputStream(Integer[] ids) throws Exception {
		if (ids == null || ids.length < 1) {
			return null;
		} else {
			FileOutputStream out = null;
			InputStream in = null;
			try {
				String whereJPQL = "";
				for (int i = 0; i < ids.length; i++) {
					whereJPQL += "o.id=?@ or ";
				}
				whereJPQL = whereJPQL.substring(0, whereJPQL.length() - 3);
				List<Task> list = finds(fillNumbersForSql(whereJPQL), ids);
				if (list != null && !list.isEmpty()) {
					int count = 0;
					String tempPath = CommonUtils.getProjectWebRoot() + CommonUtils.getParameter("config/commonData", "tempFilePath");
					File f = new File(tempPath);
					if (!f.exists()) {
						f.mkdir();
					}
					String template = CommonUtils.getProjectWEBINF() + "/file/docs/templates/taskinfo.doc";
					DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
					byte[] buffer = new byte[1024];
					for (Task t : list) {
						try {
							Map<String, String> m = new HashMap<>();
							TaskModel tm = new TaskModel(t);
							m.put("status", tm.getStatus());
							m.put("caseid", tm.getCaseid());
							m.put("applytime", df1.format(tm.getApplytime()));
							m.put("name_1", tm.getName_1());
							m.put("code_1", tm.getCode_1());
							m.put("agent_1", tm.getAgent_1());
							m.put("phone_1", tm.getPhone_1());
							m.put("address_1", tm.getAddress_1());
							m.put("name_2", tm.getName_2());
							m.put("code_2", tm.getCode_2());
							m.put("agent_2", tm.getAgent_2());
							m.put("phone_2", tm.getPhone_2());
							m.put("address_2", tm.getAddress_2());
							m.put("brand", tm.getBrand());
							m.put("model", tm.getModel());
							m.put("carriage", tm.getCarriage());
							m.put("dealtime", tm.getDealtime() == null ? "" : df2.format(tm.getDealtime()));
							m.put("description", tm.getDescription());
							m.put("matter", tm.getMatter());
							m.put("consultation", tm.getConsultation());
							m.put("info", tm.getInfo());
							String fname = tempPath + tm.getName_1() + "_" + tm.getName_1() + "_" + tm.getCaseid() + ".doc";
							in = CommonUtils.getDocInputStream(template, m);
							out = new FileOutputStream(fname);
							int len = 0;
							while ((len = in.read(buffer)) != -1) {
								out.write(buffer, 0, len);
							}
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
							count++;
						}
					}
					if (list.size() < ids.length) {
						System.out.println(ids.length - list.size() + "个任务未找到！");
					}
					if (count > 0) {
						System.out.println(count + "个任务未导出成功！");
					}
					ZipCompressor zc = new ZipCompressor();
					InputStream ins = zc.compressIO(tempPath);
					CommonUtils.deleteFile(tempPath, -1);
					return ins;
				} else {
					throw new RuntimeException("未找到有效的任务数据！");
				}
			} catch (Exception e) {
				throw e;
			} finally {
				try {
					if (out != null) out.close();
					if (in != null) in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 只修改tm中出现的属性。同时，若新增了邮件地址，若改变了专家，则发送邮件。
	 * 
	 * @param t
	 * @param tm
	 * @param deletedFiles
	 * @return
	 */
	private Json updateTask(Task t, TaskModel tm, List<String> deletedFiles) {
		Json jsn = new Json();
		Object[] obj = new Object[7];
		String code1 = null, code2 = null;
		boolean n1 = false, n2 = false;
		List<Integer> eneedEmail = new ArrayList<>();
		Set<UpFile> tobeDeleted = null;
		try {
			Consumer c = t.getConsumer();
			Saler s = t.getSaler();
			Car car = t.getCar();
			if (needEmail(c.getEmail(), tm.getEmail_1())) {
				n1 = true;
				if (t.getApplytype().equals(ApplyType.Consumer)) {
					code1 = CommonUtils.makeQueryCode();
					c.setQuerycode(CommonUtils.makeMD5(code1));
				}
			}
			if (needEmail(s.getEmail(), tm.getEmail_2())) {
				n2 = true;
				if (t.getApplytype().equals(ApplyType.Saler)) {
					code2 = CommonUtils.makeQueryCode();
					s.setQuerycode(CommonUtils.makeMD5(code2));
				}
			}
			c.setName(tm.getName_1());
			c.setAgent(tm.getAgent_1());
			c.setPhone(tm.getPhone_1());
			c.setIdcard(tm.getCode_1());
			c.setEmail(tm.getEmail_1());
			c.setAddress(tm.getAddress_1());
			cs.update(c);
			s.setName(tm.getName_2());
			s.setAgent(tm.getAgent_2());
			s.setPhone(tm.getPhone_2());
			s.setCode(tm.getCode_2());
			s.setEmail(tm.getEmail_2());
			s.setAddress(tm.getAddress_2());
			ss.update(s);
			BeanUtils.copyProperties(car, tm);
			t.setMatter(tm.getMatter());
			t.setDescription(tm.getDescription());
			boolean neede = tm.isNeedexpert();
			if (neede) {
				Set<Expert> old = t.getExperts();
				if (tm.getExpertids() != null && tm.getExpertids().length > 0) {
					for (Integer id : tm.getExpertids()) {
						if (old == null || old.isEmpty()) {
							eneedEmail.add(id);
						} else {
							boolean se = false;
							for (Expert e : old) {
								if (e.getId().equals(id)) {
									se = true;
									break;
								}
							}
							if (!se) {
								eneedEmail.add(id);
							}
						}
					}
				}
			}
			t.setNeedexpert(neede);
			t.getExperts().clear();
			if (neede && tm.getExpertids() != null) {// 需要专家
				allocateExperts(t, tm.getExpertids());
			}
			if (deletedFiles != null && !deletedFiles.isEmpty()) {
				tobeDeleted = new HashSet<>();
				Set<UpFile> thas = t.getRelatedfile();
				boolean has = thas != null && !thas.isEmpty();
				for (String todelete : deletedFiles) {// 若t中有待删文件，则将t中的待删文件加入到fls中
					boolean exist = false;
					if (has) {
						Iterator<UpFile> it = thas.iterator();
						while (it.hasNext()) {
							UpFile temp = it.next();
							if (temp.getPathname().equals(todelete)) {
								exist = true;
								tobeDeleted.add(temp);// 加入实际待删除列表中去
								it.remove();
								break;
							}
						}
					}
					if (!exist) {// 数据库中没有，则只删除物理文件，也将其加入到待删除列表中去
						UpFile uf = new UpFile();
						uf.setPathname(todelete);
						tobeDeleted.add(uf);
					}
				}
				// fs.delete(tobeDeleted);现在还不能调用此方法删除文件，因为t还没有更新
			}
			jsn.setOk(true);
		} catch (IllegalAccessException | InvocationTargetException e) {
			t = null;
			jsn.setOk(false);
			e.printStackTrace();
			jsn.setMsg(e.getMessage());
		}
		obj[0] = t;
		obj[1] = n1;
		obj[2] = n2;
		obj[3] = code1;
		obj[4] = code2;
		obj[5] = eneedEmail;
		obj[6] = tobeDeleted;
		jsn.setObject(obj);
		return jsn;
	}

	private List<TaskModel> getTaskModels(Pagedata pd, String whereJPQL, Object[] params) {
		// order是排序方案数组
		// 返回的Object数据实际上是TaskModel的数组
		LinkedHashMap<String, Order> m = new LinkedHashMap<>();
		List<TaskModel> l = new ArrayList<>();
		if (null != pd.getSort() && pd.getSort().length > 0) {
			String[] sort = pd.getSort();
			for (int i = 0; i < sort.length; i++) {
				m.put(sort[i], Order.valueOf(pd.getOrder()[i].toUpperCase(Locale.ENGLISH)));
			}
		}
		Page<Task> p = this.findByPage(pd.getPage(), pd.getRows(), whereJPQL, params, m);
		List<Task> list = p.getList();
		for (Task t : list) {
			TaskModel tm = new TaskModel(t);
			l.add(tm);
		}
		return l;
	}

	private boolean needEmail(String email, String email_new) {
		if (!CommonUtils.isNull(email_new)) { return !email_new.equals(email); }
		return false;
	}

	private InputStream getApplyDocInputStream(Task t, String docname) {
		InputStream in = null;
		if (t != null) {
			String template = CommonUtils.getProjectWEBINF() + "/file/docs/templates/" + docname + ".doc";
			DateFormat df = new SimpleDateFormat("yyyy年/MM月/dd日-HH:mm");
			TaskModel tm = new TaskModel(t);
			Map<String, String> m = new HashMap<>();
			m.put("applytime", df.format(tm.getApplytime()));
			m.put("name_1", tm.getName_1());
			m.put("code_1", tm.getCode_1());
			m.put("agent_1", tm.getAgent_1());
			m.put("phone_1", tm.getPhone_1());
			m.put("address_1", tm.getAddress_1());
			m.put("name_2", tm.getName_2());
			m.put("code_2", tm.getCode_2());
			m.put("agent_2", tm.getAgent_2());
			m.put("phone_2", tm.getPhone_2());
			m.put("address_2", tm.getAddress_2());
			m.put("description", tm.getDescription());
			m.put("matter", tm.getMatter());
			try {
				in = CommonUtils.getDocInputStream(template, m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in;
	}

	private InputStream getConsultInputStream(Task t, String docname) {
		InputStream in = null;
		if (t != null) {
			String template = CommonUtils.getProjectWEBINF() + "/file/docs/templates/" + docname + ".doc";
			DateFormat df = new SimpleDateFormat("yyyy年/MM月/dd日-HH:mm");
			TaskModel tm = new TaskModel(t);
			Map<String, String> m = new HashMap<>();
			m.put("carriage", tm.getCarriage());
			m.put("dealtime", tm.getDealtime() == null ? "" : df.format(tm.getDealtime()));
			m.put("consultpeople", "");
			m.put("consulttime", "");
			m.put("description", tm.getDescription());
			Set<Expert> eset = t.getExperts();
			int k = eset.size();
			for (int i = 5; i > k; i--) {
				m.put("e_name_" + i, "");
				m.put("e_letterid_" + i, "");
			}
			if (k > 0) {
				List<Expert> list = new ArrayList<>(eset);
				for (int i = 1; i <= k; i++) {
					m.put("e_name_" + i, list.get(i - 1).getName());
					m.put("e_letterid_" + i, list.get(i - 1).getLetterid());
				}
			}
			try {
				in = CommonUtils.getDocInputStream(template, m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in;
	}

	private InputStream getTerminateInputStream(Task t, String docname) {
		InputStream in = null;
		if (t != null) {
			String template = CommonUtils.getProjectWEBINF() + "/file/docs/templates" + docname + ".doc";
			DateFormat df = new SimpleDateFormat("yyyy年/MM月/dd日-HH:mm");
			Map<String, String> m = new HashMap<>();
			ApplyType at = t.getApplytype();
			String name1 = t.getConsumer().getName();
			String name2 = t.getSaler().getName();
			if (at.equals(ApplyType.Consumer)) {
				m.put("name_1", name1);
				m.put("name_2", name2);
			} else if (at.equals(ApplyType.Saler)) {
				m.put("name_1", name2);
				m.put("name_2", name1);
			}
			m.put("matter", t.getMatter());
			m.put("info", t.getInfo());
			m.put("date", df.format(t.getStatustime().get(Status.terminate)));
			String ename = "";
			if (t.isNeedexpert()) {
				Set<Expert> eset = t.getExperts();
				if (!eset.isEmpty()) {
					for (Expert e : eset) {
						String n = e.getUsername();
						ename += (CommonUtils.isNull(n) ? e.getName() : n) + ",";
					}
					ename = ename.substring(0, ename.length() - 1) + "等技术咨询专家的调解后";
				}
			} else {
				ename = "我方调解后";
			}
			m.put("name", ename);
			try {
				in = CommonUtils.getDocInputStream(template, m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in;
	}

	private InputStream getConsultResultInputStream(Task t, String docname) {
		InputStream in = null;
		if (t != null) {
			String template = CommonUtils.getProjectWEBINF() + "/file/docs/templates/" + docname + ".doc";
			DateFormat df = new SimpleDateFormat("yyyy年/MM月/dd日-HH:mm");
			Map<String, String> m = new HashMap<>();
			m.put("question", t.getQuestion());
			m.put("consultation", t.getConsultation());
			m.put("name", "");
			Date d = t.getStatustime().get(Status.consulted);
			m.put("date", d == null ? df.format(t.getApplytime()) : df.format(d));
			try {
				in = CommonUtils.getDocInputStream(template, m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in;
	}

	private WhereJPQL makeJPQL(String loadType, Object[] params, User u) {
		LinkedList<Object> olist = new LinkedList<>();
		String sql = "";
		switch (loadType) {
			case "toaudit":
				sql = "o.status=?@ ";
				olist.add(Status.toaudit);
				break;// 只加载待审核的任务
			case "processing":
				sql = "o.status=?@ or o.status=?@ ";
				olist.add(Status.processing);
				olist.add(Status.consulted);
				break;// 只加载处理中和已出咨询意见的任务
			case "all":
				break;// 加载所有任务
			case "toaudit_bytime":
				olist.add(Status.toaudit);
				sql = "o.status=?@ " + getTimeSql((Date) params[0], (Date) params[1], olist);
				break;// 加上时间参数
			case "processing_bytime":
				olist.add(Status.processing);
				olist.add(Status.consulted);
				sql = "(o.status=?@ or o.status=?@) " + getTimeSql((Date) params[0], (Date) params[1], olist);
				break;// 加上时间参数
			case "all_bytime":
				sql = getTimeSql((Date) params[0], (Date) params[1], olist);
				break;// 加上时间参数
			case "toaudit_advanced":
				olist.add(Status.toaudit);
				sql = "o.status=?@ and " + getAdvancedLoadSql((AdvancedSearch) params[2], olist);
				break;// 加上高级查询条件参数
			case "processing_advanced":
				olist.add(Status.processing);
				olist.add(Status.consulted);
				sql = "(o.status=?@ or o.status=?@) and " + getAdvancedLoadSql((AdvancedSearch) params[2], olist);
				break;// 加上高级查询条件参数
			case "all_advanced":
				sql = getAdvancedLoadSql((AdvancedSearch) params[2], olist);
				break;// 加上高级查询条件参数
			default:
				return null;
		}
		// 若执行查询的用户是专家，则进行过滤
		if (u != null && (u instanceof Expert)) {
			Expert e = (Expert) u;
			Set<Task> tasks = null;
			try {
				tasks = e.getTasks();
				if (tasks != null && !tasks.isEmpty()) {
					if (sql.length() > 0) {
						sql += " and (";
					}
					for (Task t : tasks) {
						olist.add(t.getId());
						sql += "o.id=?@ or ";
					}
					sql = sql.trim();
					if (sql.endsWith("or")) {
						sql = sql.substring(0, sql.length() - 2).trim();
						sql += ")";
					}
				} else {// 当前专家还没有任务，则不应该显示任何任务信息
					if (!sql.trim().equals("")) {
						sql = "o.id=?@ and (" + sql + ")";
					} else {
						sql = "o.id=?@";
					}
					olist.addFirst(-1);// 这样一来，当专家没有任何任务时，将不会查到任何信息
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		WhereJPQL wj = new WhereJPQL(fillNumbersForSql(sql), olist.toArray());
		return wj;
	}

	private String getTimeSql(Date stime, Date etime, LinkedList<Object> olist) {
		if (olist == null) {
			throw new RuntimeException("请传入查找参数的容器！");
		} else {
			String sql = "";
			if (null == etime) {
				etime = new Date();
			}
			if (null == stime || stime.after(etime)) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -30);
				stime = c.getTime();// 若起始时间大于截止时间或为空，则将起始时间置为30天之前
			}
			sql = "and o.applytime between ?@ and ?@ ";
			olist.add(stime);
			olist.add(etime);
			return sql;
		}
	}

	private String getAdvancedLoadSql(AdvancedSearch ad, LinkedList<Object> olist) {
		if (olist == null) {
			throw new RuntimeException("请传入查找参数的容器！");
		} else {
			StringBuffer buff = new StringBuffer("");
			if (!CommonUtils.isNull(ad.getAdcaseid())) {
				buff.append("o.caseid=?@ ");
				olist.add(ad.getAdcaseid().trim());
			}
			if (!CommonUtils.isNull(ad.getAdstatus())) {
				buff.append("and o.status=?@ ");
				olist.add(ad.getAdstatus());
			}
			if (CommonUtils.isNull(ad.getAdendtime())) {
				ad.setAdendtime(new Date());
			}
			if (CommonUtils.isNull(ad.getAdstarttime()) || ad.getAdstarttime().after(ad.getAdendtime())) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -30);
				ad.setAdstarttime(c.getTime());// 若起始时间大于截止时间或为空，则将起始时间置为30天之前
			}
			olist.add(ad.getAdstarttime());
			olist.add(ad.getAdendtime());
			buff.append("and (o.applytime  between ?@ and ?@) ");
			if (!CommonUtils.isNull(ad.getAdapplytype())) {// 申请类型不空时
				buff.append("and o.applytype=?@ ");
				olist.add(ad.getAdapplytype());
				if (!CommonUtils.isNull(ad.getAdname1()) && !CommonUtils.isNull(ad.getAdname2())) {// 申请方和责任方均不空时
					buff.append("and o.consumer.name like ?@ and o.saler.name like ?@ ");
					if (ad.getAdapplytype().equals(ApplyType.Consumer)) {// 若是消费者申请
						olist.add("%" + ad.getAdname1() + "%");
						olist.add("%" + ad.getAdname2() + "%");
					} else {// 若是商家申请
						olist.add("%" + ad.getAdname2() + "%");
						olist.add("%" + ad.getAdname1() + "%");
					}
				} else if (!CommonUtils.isNull(ad.getAdname1()) && CommonUtils.isNull(ad.getAdname2())) {// 申请方不空，但责任方为空
					olist.add("%" + ad.getAdname1() + "%");
					if (ad.getAdapplytype().equals(ApplyType.Consumer)) {
						buff.append("and o.consumer.name like ?@ ");
					} else {
						buff.append("and o.saler.name like ?@ ");
					}
				} else if (CommonUtils.isNull(ad.getAdname1()) && !CommonUtils.isNull(ad.getAdname2())) {// 申请方空，但责任方不空
					olist.add("%" + ad.getAdname2() + "%");
					if (ad.getAdapplytype().equals(ApplyType.Consumer)) {
						buff.append("and o.saler.name like ?@ ");
					} else {
						buff.append("and o.consumer.name like ?@ ");
					}
				} else {// 申请方空，且责任方为空，什么也不做
				}
			}//
			if (!CommonUtils.isNull(ad.getAdmodel())) {
				buff.append("and o.car.model like ?@ ");
				olist.add("%" + ad.getAdmodel() + "%");
			}
			if (!CommonUtils.isNull(ad.getAdmatter())) {
				buff.append("and o.matter like ?@");
				olist.add("%" + ad.getAdmatter() + "%");
			}
			String sql = buff.toString();
			if (sql.startsWith("and ")) {// caseid字段为空的话，str就会以“and
				// ”开头，这样会导致JPQL语句非法，因此需要判断
				sql = sql.substring(4, sql.length() - 1);
			}
			return sql;
		}
	}

	/**
	 * 为指定的单元格赋值
	 * 
	 * @param sheet
	 *            工作表对象
	 * @param row
	 *            行号（下标从0开始）
	 * @param col
	 *            列号（下标从0开始）
	 * @param value
	 *            值
	 */
	private void setCellValue(WritableSheet sheet, int row, int col, Object val) {
		if (null == val) {
			val = "";
		}
		WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD);
		WritableCellFormat fmt = new WritableCellFormat(font);
		try {
			if (col == 3 || col == 1 || col == 10) {
				fmt.setAlignment(Alignment.CENTRE);
			} else {
				fmt.setAlignment(Alignment.LEFT);
			}
			Label l = new Label(col, row, (String) val, fmt);
			sheet.addCell(l);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
}
