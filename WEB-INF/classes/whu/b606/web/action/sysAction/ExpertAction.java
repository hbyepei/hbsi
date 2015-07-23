package whu.b606.web.action.sysAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import whu.b606.dto.Json;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Technology;
import whu.b606.dto.Usertype;
import whu.b606.dto.WhereJPQL;
import whu.b606.entity.Expert;
import whu.b606.entity.Image;
import whu.b606.entity.Task;
import whu.b606.pageModel.ComboItem;
import whu.b606.pageModel.ExpertModel;
import whu.b606.pageModel.Grid;
import whu.b606.pageModel.TaskModel;
import whu.b606.service.ExpertService;
import whu.b606.service.ImageService;
import whu.b606.util.CommonUtils;
import whu.b606.web.action.BaseAction;

import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年12月6日
 * 
 **/
@Action("expertAction")
@Namespace("/sys")
@ParentPackage("common")
public class ExpertAction extends BaseAction implements ModelDriven<ExpertModel> {
	private static final long serialVersionUID = -6368587067203624338L;
	private String sort, order, loadtype, searchName, searchValue;
	private boolean onlyUsefull = false;
	private int page, rows, groupID;
	private File upfile;// 使用File数组来封装多个文件上传域所上传的文件内容
	private String upfileContentType;// 使用字符串数组来封装多个文件上传项所对应的文件类型，请注意该字段的命名规范
	private String upfileFileName;// 使用字符串数组来封装多个文件上传项所对应的文件名字，请注意该字段的命名规范
	ExpertModel em = new ExpertModel();
	Json j = new Json();
	@Resource
	ExpertService es;
	@Resource
	ImageService is;

	public void getExperts() {
		String lt = getLoadtype();
		WhereJPQL wj = loadData(lt);
		Grid g = null;
		Pagedata pd = new Pagedata(this.page, this.rows, this.sort, this.order);
		if (lt.equals("combogrid")) {
			g = es.getExpertsForCombogrid(wj, pd);
		} else {
			g = es.getExperts(wj, pd);
		}
		String json = getJson(g);
		if (lt.equals("search") && !this.searchName.equals("all") && !this.searchName.equals("id")) {
			json = CommonUtils.emphasize(json, this.searchValue);// 如果是按关键字查询的，则将查询结果中的关键字高亮显示
		}
		WriteString(json);
	}

	public void add() {
		// 保证用户名、聘书编号、身份证号要唯一
		String msg = "数据为空!";
		try {
			if (this.em != null) {
				String idcard = em.getIdcard();
				String name = em.getName();
				String letterid = em.getLetterid();
				if (idcard == null || name == null || letterid == null) { throw new RuntimeException("参数为空！"); }
				Expert e1 = es.find("o.idcard=?1", new Object[] { idcard });
				Expert e2 = es.find("o.name=?1", new Object[] { name });
				Expert e3 = es.find("o.letterid=?1", new Object[] { letterid });
				if (e1 != null) { throw new RuntimeException("此身份证号已经存在！"); }
				if (e2 != null) { throw new RuntimeException("此用户名(登录名)已经存在！"); }
				if (e3 != null) { throw new RuntimeException("此聘书编号已经存在，请检查！"); }
				String tech = em.getTechnology();
				if (tech != null) {
					int gID = Integer.parseInt(tech);
					em.setTechnology(Technology.getEnumByID(gID).getDescription());
				}
				Expert e = new Expert(em);
				e.setName(em.getName());
				if (this.upfile != null && this.upfileFileName != null && this.upfileContentType != null) {
					Image img = es.setImage(upfile, upfileFileName, upfileContentType);
					if (img != null) {
						e.setImage(img);
					}
				}
				es.save(e);
				j.setOk(true);
				msg = "添加成功，此用户的密码为其身份证号后六位！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "添加失败，错误：" + e.getMessage();
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	public void update() {
		// 保证用户名、聘书编号、身份证号要唯一
		String msg = "数据为空!";
		try {
			if (this.em != null) {
				Expert old = es.find(em.getId());
				if (old != null) {
					String idcard = em.getIdcard();
					String name = em.getName();
					String letterid = em.getLetterid();
					if (idcard == null || name == null || letterid == null) { throw new RuntimeException("参数为空！"); }
					Expert e1 = es.find("o.idcard=?1", new Object[] { idcard });
					Expert e2 = es.find("o.name=?1", new Object[] { name });
					Expert e3 = es.find("o.letterid=?1", new Object[] { letterid });
					if (e1 != null && !e1.equals(old)) { throw new RuntimeException("此身份证号已经存在！"); }
					if (e2 != null && !e2.equals(old)) { throw new RuntimeException("此用户名(登录名)已经存在！"); }
					if (e3 != null && !e3.equals(old)) { throw new RuntimeException("此聘书编号已经存在，请检查！"); }
					String tech = em.getTechnology();
					if (tech != null) {
						int gID = Integer.parseInt(tech);
						em.setTechnology(Technology.getEnumByID(gID).getDescription());
					}
					if (this.upfile != null && this.upfileFileName != null && this.upfileContentType != null) {
						Image img = es.setImage(upfile, upfileFileName, upfileContentType);
						if (img != null) {
							Image oldimg = old.getImage();
							old.setImage(img);
							is.removeImage(oldimg, Usertype.Expert);
						}
					}
					es.update(old, em);
					j.setOk(true);
					msg = "修改成功！";
				} else {
					msg = "未找到要修改的专家！";
				}
			}
		} catch (Exception e) {
			msg = "修改失败，错误：" + e.getMessage();
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	public void delete() {
		try {
			if (this.em.getId() != null) {
				Expert e = es.find(em.getId());
				if (e != null) {
					Image img = e.getImage();
					es.delete(e.getId());
					is.removeImage(img, Usertype.Expert);
					j.setMsg("删除成功！");
					j.setOk(true);
				}
			}
		} catch (Exception e) {
			j.setMsg("删除失败！错误：" + e.getMessage());
		}
		WriteJson(j);
	}

	public void getRelatedWorks() {
		String msg = "未找到数据！";
		if (this.em != null && this.em.getId() != null) {
			Expert e = es.find(em.getId());
			if (e != null) {
				Set<Task> set = e.getTasks();
				if (!set.isEmpty()) {
					List<TaskModel> list = new ArrayList<>();
					for (Task t : set) {
						TaskModel tm = new TaskModel(t);
						if (tm.getMatter() != null && tm.getMatter().length() > 15) {
							tm.setMatter(tm.getMatter().substring(0, 15));
						}
						list.add(tm);
					}
					j.setObject(list);
					msg = "";
					j.setOk(true);
				} else {
					msg = "暂无关联任务";
				}
			} else {
				msg = "未找到此专家！";
			}
		}
		j.setMsg(msg);
		WriteJson(j);
	}

	public void importExperts() {
		try {
			if (this.upfile != null && this.upfileFileName != null) {
				Json result = es.importExperts(this.upfile);
				if (result.isOk()) {
					j.setOk(true);
				}
				j.setMsg(result.getMsg());
			} else {
				j.setMsg("无文件！");
			}
		} catch (Exception e) {
			j.setMsg("导入失败，错误：" + e.getMessage());
		}
		WriteJson(j);
	}

	public void getExpertsGroupComboInfo() {
		Technology[] techs = Technology.values();
		List<ComboItem> list = new ArrayList<>();
		for (int i = 0; i < techs.length; i++) {
			ComboItem item = new ComboItem(techs[i].getGroupID(), techs[i].getDescription() + "技术组", null);
			if (!this.onlyUsefull || item.getId() != 6) {
				list.add(item);
			}
		}
		if (!this.onlyUsefull) {
			list.add(new ComboItem(0, "所有技术组", null));
		}
		WriteJson(list);
	}

	private WhereJPQL loadData(String loadType) {
		WhereJPQL wj = new WhereJPQL();
		if (loadType.equals("group") || loadType.equals("combogrid")) {// 基本加载，加载所有当前任务（状态为toaudit/processing/accepted）
			if (this.groupID != 0) {
				wj.setWhereJPQL("o.technology=?1");
				wj.setParams(new Object[] { Technology.getEnumByID(this.groupID) });
			}
		} else if (loadType.equals("search")) {
			if (!CommonUtils.isNull(this.searchValue)) {
				if (this.searchName.equals("id")) {
					wj.setWhereJPQL("o." + this.searchName + " = ?1");
					wj.setParams(new Object[] { Integer.parseInt(this.searchValue) });
				} else {
					if (!this.searchName.equals("all")) {
						wj.setWhereJPQL("o." + this.searchName + " like ?1");
					} else {
						wj.setWhereJPQL("o.name like ?1 or o.username like ?1 or o.phone like ?1 or o.email like ?1 or o.area like ?1 or o.letterid like ?1 ");
					}
					wj.setParams(new Object[] { "%" + this.searchValue + "%" });
				}
			}
		} else if (loadType.equals("all")) {// 查询所有任务
		} else {
			return null;
		}
		return wj;
	}

	@Override
	public ExpertModel getModel() {
		return em;
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

	public File getUpfile() {
		return upfile;
	}

	public void setUpfile(File upfile) {
		this.upfile = upfile;
	}

	public String getUpfileContentType() {
		return upfileContentType;
	}

	public void setUpfileContentType(String upfileContentType) {
		this.upfileContentType = upfileContentType;
	}

	public String getUpfileFileName() {
		return upfileFileName;
	}

	public void setUpfileFileName(String upfileFileName) {
		this.upfileFileName = upfileFileName;
	}

	public String getLoadtype() {
		String[] load = loadtype.split(",");
		loadtype = load[load.length - 1].trim();
		return loadtype;
	}

	public void setLoadtype(String loadtype) {
		this.loadtype = loadtype;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public boolean isOnlyUsefull() {
		return onlyUsefull;
	}

	public void setOnlyUsefull(boolean onlyUsefull) {
		this.onlyUsefull = onlyUsefull;
	}
}
