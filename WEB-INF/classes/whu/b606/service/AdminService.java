package whu.b606.service;

import java.io.File;
import java.util.List;

import whu.b606.dao.BaseDao;
import whu.b606.entity.Admin;
import whu.b606.entity.Image;
import whu.b606.pageModel.AdminModel;

public interface AdminService extends BaseDao<Admin> {
	public static String BEAN_NAME = "whu.b606.serviceBean.AdminServiceImpl";

	/**
	 * 根据姓名查找管理员
	 * 
	 * @param name
	 *            用户名
	 * @return 管理员
	 */
	public abstract Admin findByName(String name);

	/**
	 * 根据姓名和密码查找管理员
	 * 
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return 管理员
	 */
	public abstract Admin findByNameAndPwd(String name, String password);

	/**
	 * 修改密码
	 * 
	 * @param admin
	 *            待修改密码的管理员
	 * @param OldPwd
	 *            原密码
	 * @param newPwd
	 *            新密码
	 * @return 修改密码后的管理员
	 */
	public abstract Admin updatePwd(Admin admin, String OldPwd, String newPwd);

	/**
	 * 返回管理员对象的模型列表对象
	 * 
	 * @return
	 */
	public abstract List<AdminModel> getAdminModels();

	/**
	 * 修改管理员信息
	 * 
	 * @param am
	 */
	public abstract void update(AdminModel am) throws Exception;

	/**
	 * 更改管理员图像
	 * 
	 * @param upfile
	 * @param upfileFileName
	 * @param upfileContentType
	 * @return
	 * @throws Exception
	 */
	public abstract Image setImage(File upfile, String upfileFileName, String upfileContentType) throws Exception;

	/**
	 * 添加用户
	 * 
	 * @param am
	 * @return
	 */
	public abstract Admin add(AdminModel am) throws Exception;
}
