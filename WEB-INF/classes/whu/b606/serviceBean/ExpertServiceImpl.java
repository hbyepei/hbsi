package whu.b606.serviceBean;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.ApplyType;
import whu.b606.dto.Gender;
import whu.b606.dto.Json;
import whu.b606.dto.Order;
import whu.b606.dto.Page;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Status;
import whu.b606.dto.Technology;
import whu.b606.dto.WhereJPQL;
import whu.b606.entity.Expert;
import whu.b606.entity.Image;
import whu.b606.entity.Task;
import whu.b606.pageModel.ExpertComboGridModel;
import whu.b606.pageModel.ExpertModel;
import whu.b606.pageModel.Grid;
import whu.b606.service.ExpertService;
import whu.b606.util.CommonUtils;
import whu.b606.util.MailSend;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 * 
 **/
@Service(ExpertService.BEAN_NAME)
public class ExpertServiceImpl extends DaoSupport<Expert> implements ExpertService {
	@Override
	public Expert findByName(String name) {
		return find("o.name=?1", new Object[] { name });
	}

	@Override
	public Expert findByNameAndPwd(String name, String password) {
		return find("o.name=?1 and o.password=?2", new Object[] { name, CommonUtils.makeMD5(password) });
	}

	@Override
	public Expert updatePwd(Expert expert, String OldPwd, String newPwd) {
		if (null != findByNameAndPwd(expert.getName(), OldPwd)) {
			expert.setPassword(CommonUtils.makeMD5(newPwd));
			super.update(expert);
			return expert;
		}
		return null;
	}

	@Override
	public Image setImage(File upfile, String upfileFileName, String upfileContentType) throws Exception {
		if (upfileFileName != null) {// 有文件上传项
			if (CommonUtils.TotalfileSize(new File[] { upfile }) > 2097150) {// 超过2M
				throw new RuntimeException("您上传的文件过大，请重新选择！(不要超过2M)");
			}
			int index = CommonUtils.allowedFileTypes(new String[] { upfileContentType }, new String[] { upfileFileName }, "image", "config/commonData");
			if (index > -1) { throw new RuntimeException("文件类型错误：您上传的文件(“" + upfileFileName + "”)类型不符合要求。(只接受如下类型文件：\n“jpg,jpeg,png,gif,bmp”)"); }
			String savePath = CommonUtils.getProjectWebRoot() + CommonUtils.getParameter("config/commonData", "expertImageSavePath") + "/";
			// 得到实际保存路径并执行上传
			Map<String, String> m = CommonUtils.fileUpload(savePath, new File[] { upfile }, new String[] { upfileFileName });// 将文件上传至指定的路径，并返回这些文件的物理名称数组;
			// 若上传无误，则创建Image实体信息
			if (m != null && !m.isEmpty()) {
				Image img = new Image();
				for (Map.Entry<String, String> me : m.entrySet()) {
					img.setPathname(me.getKey());
					img.setFilename(me.getValue());
					img.setDescription("专家头像文件");
					break;
				}
				return img;
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 状态修改通知邮件，对专家用户来讲，当分配专家时、终止调解时，完成调解时需要发送邮件。
	 * 注意，只有当task.getExperts()方法返回的Set集合中包含id号为expertid的专家时，才可能成功发送邮件。所以，调用此方法之前
	 * ，应当保证专家已经加入到了task集合
	 */
	@Override
	public MailSend getBussinessMail(Integer expertid, Task task, Status currentStatus) {
		Expert e = find(expertid);
		// Set<Expert> sets = task.getExperts(); sets != null &&
		// sets.contains(e) &&
		if (e != null && e.getEmail() != null && !e.getEmail().equals("")
				&& (currentStatus.equals(Status.terminate) || currentStatus.equals(Status.processing) || currentStatus.equals(Status.complete))) {// 专家存在且与任务相关，且处于特定阶段
			/** =====固定的公共参数====== **/
			DateFormat df = new SimpleDateFormat("yyyy年M月d日");
			String name = ((e.getUsername() != null && !e.getUsername().trim().equals("")) ? e.getUsername() : e.getName()) + "(先生/女士)";
			String brand = task.getCar().getBrand();
			String model = task.getCar().getModel();
			String brandModel = (brand != null && !brand.equals("") ? brand : "未知品牌") + (model != null && !model.equals("") ? model : "未知型号") + "汽车";
			String status = currentStatus.getDescription();
			String caseid = task.getCaseid();
			String applyer = task.getApplytype().equals(ApplyType.Consumer) ? (task.getConsumer().getName() != null ? task.getConsumer().getName() : "未知") : (task.getSaler().getName() != null ? task
					.getSaler().getName() : "未知");
			String applytime = df.format(task.getApplytime());
			String matter = task.getMatter();
			String carriage = task.getCar().getCarriage() != null ? task.getCar().getCarriage() : "未知";
			String dealtime = task.getCar().getDealtime() != null ? df.format(task.getCar().getDealtime()) : "未知";
			String description = task.getDescription();
			String ip = CommonUtils.getParameter("config/commonData", "IP");
			String safeLink = getSafeLink(expertid);
			String tel = CommonUtils.getParameter("config/commonData", "Tel");
			/** =====固定的公共参数====== **/
			/** =====发件参数======= **/
			String emailSubject = "家用汽车三包争议调解通知";
			String[] emailAddr = new String[] { e.getEmail() };
			String htmlPath = CommonUtils.getProjectWEBINF() + "/emailFile/expertEmail.html";
			Object[] template = new Object[] { name, brandModel, status, caseid, applyer, applytime, matter, carriage, dealtime, description, ip, safeLink, tel };
			String[] picturePath = new String[] { CommonUtils.getProjectWebRoot() + "/images/logo2.png" };
			/** =====发件参数======= **/
			return new MailSend(emailSubject, emailAddr, htmlPath, template, picturePath);
		}
		return null;
	}

	/**
	 * 为专家生成安全的登入后台的链接地址
	 * 
	 * @param expertid
	 * @return
	 */
	private String getSafeLink(Integer expertid) {
		String ip = CommonUtils.getParameter("config/commonData", "IP");// IP地址
		Expert e = find(expertid);
		if (e != null) {
			Date t = new Date();
			return ip + "/loginAction!login.action?usertype=Expert&logintype=remote&p=" + e.getPassword() + "&id=" + expertid + "&t=" + t.getTime();
		} else {
			return ip + "/loginAction!login.action?usertype=Expert&logintype=null";
		}
	}

	@Override
	public Grid<ExpertModel> getExperts(WhereJPQL wj, Pagedata pagedata) {
		List<Expert> epts = getExpertList(pagedata, wj);
		List<ExpertModel> rows = new ArrayList<>();
		for (Expert e : epts) {
			ExpertModel em = new ExpertModel(e);
			rows.add(em);
		}
		Long total = (long) Count(wj.getWhereJPQL(), wj.getParams());
		Grid<ExpertModel> grid = new Grid();
		grid.setTotal(total);
		grid.setRows(rows);
		return grid;
	}

	private List<Expert> getExpertList(Pagedata pd, WhereJPQL wj) {
		// order是排序方案数组
		// 返回的Object数据实际上是TaskModel的数组
		LinkedHashMap<String, Order> m = new LinkedHashMap<>();
		if (null != pd.getSort() && pd.getSort().length > 0) {
			String[] sort = pd.getSort();
			for (int i = 0; i < sort.length; i++) {
				m.put(sort[i], Order.valueOf(pd.getOrder()[i].toUpperCase(Locale.ENGLISH)));
			}
		}
		Page<Expert> p = this.findByPage(pd.getPage(), pd.getRows(), wj.getWhereJPQL(), wj.getParams(), m);
		return p.getList();
	}

	@Override
	public Json importExperts(File excel) throws Exception {
		Json json = new Json();
		int count = 0;
		int total = 0;
		if (excel != null) {
			Workbook book = Workbook.getWorkbook(excel);
			if (book != null) {
				Sheet[] sheets = book.getSheets();
				if (sheets != null && sheets.length > 0) {
					for (int k = 0; k < sheets.length; k++) {
						Sheet sheet = sheets[k];// 获得第一个工作表对象
						int rows = sheet.getRows();
						int cols = sheet.getColumns();
						total += (rows - 1);
						for (int i = 1; i < rows; i++) {// 从第一行开始，因为第0行是标题
							ExpertModel em = new ExpertModel();
							for (int j = 0; j < cols; j++) {
								Cell cell = sheet.getCell(j, i);// 得到第i行j列的单元格
								String column = sheet.getCell(j, 0).getContents();
								String result = cell.getContents();
								switch (column) {
									case "专家姓名":
										em.setUsername(result);
										break;
									case "聘书编号":
										em.setLetterid(result);
										break;
									case "性别":
										em.setGender(result);
										break;
									case "年龄":
										em.setAge(Integer.parseInt(result));
										break;
									case "手机号":
										em.setPhone(result);
										break;
									case "身份证号":
										em.setIdcard(result);
										break;
									case "E-mail":
										em.setEmail(result);
										break;
									case "来源单位":
										em.setDepartment_name(result);
										break;
									case "所属地区":
										em.setArea(result);
										break;
									case "单位类别":
										em.setDepartment_category(result);
										break;
									case "技术组别":
										em.setTechnology(result);
										break;
									case "现服务品牌":
										em.setBrand(result);
										break;
									case "简介":
										em.setIntroduction(result);
										break;
									default:
										break;
								}
							}
							try {
								if (!CommonUtils.isNull(em.getIdcard()) && !CommonUtils.isNull(em.getUsername())) {
									Expert ept = save(em);
									if (ept != null) {
										count++;// 成功导入的专家数量
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					json.setMsg("成功导入" + count + "个,失败" + (total - count) + "个，登录密码是身份证号后六位!");
					json.setOk(true);
				} else {
					json.setMsg("无工作表！");
				}
				book.close();
			} else {
				json.setMsg("无工作薄！");
			}
		} else {
			json.setMsg("无有效的Excel文件！");
		}
		return json;
	}

	private Expert save(ExpertModel em) {
		Expert e = new Expert(em);
		save(e);
		if (e.getId() != null) { return e; }
		return null;
	}

	@Override
	public Grid<ExpertComboGridModel> getExpertsForCombogrid(WhereJPQL wj, Pagedata pd) {
		List<Expert> epts = getExpertList(pd, wj);
		List<ExpertComboGridModel> rows = new ArrayList<>();
		for (Expert e : epts) {
			ExpertComboGridModel em = new ExpertComboGridModel(e);
			rows.add(em);
		}
		Long total = (long) Count(wj.getWhereJPQL(), wj.getParams());
		Grid<ExpertComboGridModel> grid = new Grid();
		grid.setTotal(total);
		grid.setRows(rows);
		return grid;
	}

	@Override
	public void update(Expert old, ExpertModel em) {
		if (em.getAge() != null) old.setAge(em.getAge());
		if (em.getArea() != null) old.setArea(em.getArea());
		if (em.getBrand() != null) old.setBrand(em.getBrand());
		if (em.getDepartment_category() != null) old.setDepartment_category(em.getDepartment_category());
		if (em.getDepartment_name() != null) old.setDepartment_name(em.getDepartment_name());
		if (em.getEmail() != null) old.setEmail(em.getEmail());
		if (em.getGender() != null) old.setGender(Gender.string2Enum(em.getGender()));
		if (em.getIdcard() != null) old.setIdcard(em.getIdcard());
		if (em.getIntroduction() != null) old.setIntroduction(em.getIntroduction());
		if (em.getLetterid() != null) old.setLetterid(em.getLetterid());
		if (em.getName() != null) old.setName(em.getName());
		if (em.getPhone() != null) old.setPhone(em.getPhone());
		if (em.getTechnology() != null) old.setTechnology(Technology.string2Enum(em.getTechnology()));
		if (em.getUsername() != null) old.setUsername(em.getUsername());
		update(old);
	}
}
