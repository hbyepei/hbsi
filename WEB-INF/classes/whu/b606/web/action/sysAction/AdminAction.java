/**
 * 
 */
package whu.b606.web.action.sysAction;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import whu.b606.dto.Json;
import whu.b606.dto.Usertype;
import whu.b606.entity.Admin;
import whu.b606.entity.Image;
import whu.b606.pageModel.AdminModel;
import whu.b606.service.AdminService;
import whu.b606.service.ImageService;
import whu.b606.web.action.BaseAction;

import com.opensymphony.xwork2.ModelDriven;

/**
 * @author yepei
 * 
 */
@Action("adminAction")
@Namespace("/sys")
@ParentPackage("common")
public class AdminAction extends BaseAction implements ModelDriven<AdminModel> {
	private static final long serialVersionUID = -3600225263400512922L;
	private File upfile;// 使用File数组来封装多个文件上传域所上传的文件内容
	private String upfileContentType;// 使用字符串数组来封装多个文件上传项所对应的文件类型，请注意该字段的命名规范
	private String upfileFileName;// 使用字符串数组来封装多个文件上传项所对应的文件名字，请注意该字段的命名规范
	AdminModel am = new AdminModel();
	Json j = new Json();
	@Resource
	AdminService as;
	@Resource
	ImageService is;

	public void getAdmins() {
		List<AdminModel> al = as.getAdminModels();
		WriteJson(al);
	}

	public void edit() {
		try {
			if (this.am.getName() != null) {
				as.update(am);
				if (this.upfileFileName != null) {// 有文件上传项
					Image img = as.setImage(this.upfile, this.upfileFileName, this.upfileContentType);
					if (img != null) {
						Admin a = as.find(am.getId());
						Image old = a.getImage();
						a.setImage(img);
						as.update(a);
						is.removeImage(old, Usertype.Admin);// 这一句应当放在a更新之后，否则由于约束关系，将无法删除图像文件的数据库记录
					}
				}
			}
			j.setMsg("修改成功！");
			j.setOk(true);
		} catch (Exception e) {
			j.setMsg("修改失败！错误：" + e.getMessage());
		}
		WriteJson(j);
	}

	public void add() {
		try {
			if (this.am != null) {
				Admin a = as.add(am);
				if (a != null) {
					j.setOk(true);
					j.setMsg("添加成功");
				}
				if (this.upfileFileName != null) {// 有文件上传项
					Image img = as.setImage(this.upfile, this.upfileFileName, this.upfileContentType);
					if (img != null) {
						Image old = a.getImage();
						a.setImage(img);
						as.update(a);
						is.removeImage(old, Usertype.Admin);// 这一句应当放在a更新之后，否则由于约束关系，将无法删除图像文件的数据库记录
					}
				}
			} else {
				j.setMsg("无参数！");
			}
		} catch (Exception e) {
			j.setMsg("添加失败，错误：" + e.getMessage());
		}
		WriteJson(j);
	}

	public void delete() {
		try {
			if (this.am.getId() != null) {
				Admin a = as.find(am.getId());
				if (a != null) {
					Image img = a.getImage();
					as.delete(a.getId());
					is.removeImage(img, Usertype.Admin);
					j.setMsg("删除成功！");
					j.setOk(true);
				}
			}
		} catch (Exception e) {
			j.setMsg("删除失败！");
		}
		WriteJson(j);
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

	@Override
	public AdminModel getModel() {
		return am;
	}
}
