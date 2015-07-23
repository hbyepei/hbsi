package whu.b606.service;

import java.io.File;

import whu.b606.dao.BaseDao;
import whu.b606.dto.Json;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Status;
import whu.b606.dto.WhereJPQL;
import whu.b606.entity.Expert;
import whu.b606.entity.Image;
import whu.b606.entity.Task;
import whu.b606.pageModel.ExpertComboGridModel;
import whu.b606.pageModel.ExpertModel;
import whu.b606.pageModel.Grid;
import whu.b606.util.MailSend;

public interface ExpertService extends BaseDao<Expert> {
	public static String BEAN_NAME = "whu.b606.serviceBean.ExpertServiceImpl";

	/**
	 * 根据用户名查找专家
	 * 
	 * @param name
	 *            用户名，注意是登录使用的名称
	 * @return 专家对象
	 */
	public abstract Expert findByName(String name);

	/**
	 * 根据姓名查找专家
	 * 
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return 专家对象
	 */
	public abstract Expert findByNameAndPwd(String name, String password);

	/**
	 * 修改密码
	 * 
	 * @param expertid
	 *            专家id
	 * @param password
	 *            原密码
	 * @param newPwd
	 *            新密码
	 * @return 修改密码后的专家对象
	 */
	public abstract Expert updatePwd(Expert expert, String password, String newPwd);

	/**
	 * 发送业务通知邮件
	 * 
	 * @param id
	 * @param task
	 * @param status
	 * @return
	 */
	public abstract MailSend getBussinessMail(Integer id, Task task, Status status);

	/**
	 * 返回专家列表信息的表格模型
	 * 
	 * @return
	 */
	public abstract Grid<ExpertModel> getExperts(WhereJPQL wj, Pagedata pagedata);

	/**
	 * 批量导入专家数据
	 * 
	 * @param upfile
	 * @return
	 */
	public abstract Json importExperts(File upfile) throws Exception;

	/**
	 * 返回专家选择列表的combogrid数据结构
	 * 
	 * @param wj
	 * @param pd
	 * @return
	 */
	public abstract Grid<ExpertComboGridModel> getExpertsForCombogrid(WhereJPQL wj, Pagedata pd);

	/**
	 * 为用户设置图像
	 * 
	 * @param upfile
	 * @param upfileFileName
	 * @param upfileContentType
	 * @return
	 * @throws Exception
	 */
	public abstract Image setImage(File upfile, String upfileFileName, String upfileContentType) throws Exception;

	/**
	 * 根据页面传来的新数据，修改专家信息
	 * 
	 * @param old
	 * @param em
	 */
	public abstract void update(Expert old, ExpertModel em);
}
