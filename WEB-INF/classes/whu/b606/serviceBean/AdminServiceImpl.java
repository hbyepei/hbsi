package whu.b606.serviceBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.Gender;
import whu.b606.entity.Admin;
import whu.b606.entity.Image;
import whu.b606.pageModel.AdminModel;
import whu.b606.service.AdminService;
import whu.b606.util.CommonUtils;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 * 
 **/
@Service(AdminService.BEAN_NAME)
public class AdminServiceImpl extends DaoSupport<Admin> implements AdminService {
	@Override
	public Admin findByName(String name) {
		return find("o.name=?1", new Object[] { name });
	}

	@Override
	public Admin findByNameAndPwd(String name, String password) {
		return find("o.name=?1 and o.password=?2", new Object[] { name, CommonUtils.makeMD5(password) });
	}

	@Override
	public Admin updatePwd(Admin admin, String OldPwd, String newPwd) {
		if (null != findByNameAndPwd(admin.getName(), OldPwd)) {
			admin.setPassword(CommonUtils.makeMD5(newPwd));
			super.update(admin);
			return admin;
		}
		return null;
	}

	@Override
	public void save(Admin a) {// 需要将密码变成密文
		String pwd = CommonUtils.makeMD5(a.getPassword());
		a.setPassword(pwd);
		super.save(a);
	}

	@Override
	public List<AdminModel> getAdminModels() {
		List<Admin> list = finds(null, null);
		List<AdminModel> l = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			for (Admin a : list) {
				l.add(new AdminModel(a));
			}
		}
		return l;
	}

	@Override
	public void update(AdminModel am) throws Exception {
		Admin a = find(am.getId());
		if (a != null) {
			try {
				if (am.getAge() != null) {
					a.setAge(am.getAge());
				}
				if (!CommonUtils.isNull(am.getName())) {
					a.setName(am.getName());
				}
				if (!CommonUtils.isNull(am.getUsername())) {
					a.setUsername(am.getUsername());
				}
				if (!CommonUtils.isNull(am.getGender()) && (am.getGender().equals("男") || am.getGender().equals("女"))) {
					a.setGender(Gender.string2Enum(am.getGender()));
				}
				update(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("未找到待修改的用户！");
		}
	}

	@Override
	public Image setImage(File upfile, String upfileFileName, String upfileContentType) throws Exception {
		if (upfileFileName != null) {// 有文件上传项
			if (CommonUtils.TotalfileSize(new File[] { upfile }) > 2097150) {// 超过2M
				throw new RuntimeException("您上传的文件过大，请重新选择！(不要超过2M)");
			}
			int index = CommonUtils.allowedFileTypes(new String[] { upfileContentType }, new String[] { upfileFileName }, "image", "config/commonData");
			if (index > -1) {
				throw new RuntimeException("文件类型错误：您上传的文件(“" + upfileFileName + "”)类型不符合要求。(只接受如下类型文件：\n“jpg,jpeg,png,gif,bmp”)");
			}
			String savePath = CommonUtils.getProjectWebRoot() + CommonUtils.getParameter("config/commonData", "adminImageSavePath") + "/";
			// 得到实际保存路径并执行上传
			Map<String, String> m = CommonUtils.fileUpload(savePath, new File[] { upfile }, new String[] { upfileFileName });// 将文件上传至指定的路径，并返回这些文件的物理名称数组;
			// 若上传无误，则创建Image实体信息
			if (m != null && !m.isEmpty()) {
				Image img = new Image();
				for (Map.Entry<String, String> me : m.entrySet()) {
					img.setPathname(me.getKey());
					img.setFilename(me.getValue());
					img.setDescription("管理员头像文件");
					break;
				}
				return img;
			}
			return null;
		} else {
			return null;
		}
	}

	@Override
	public Admin add(AdminModel am) throws Exception {
		if (am != null) {
			String name = am.getName();
			Admin a;
			if (name != null) {
				a = findByName(name);
				if (a != null) {
					throw new RuntimeException("用户已经存在！");
				} else {
					a = new Admin();
					a.setName(am.getName());
					a.setUsername(am.getUsername());
					a.setPassword(CommonUtils.makeMD5(am.getPassword()));
					a.setGender(Gender.string2Enum(am.getGender()));
					a.setAge(am.getAge());
					save(a);
					return a;
				}
			}
		}
		return null;
	}
}
