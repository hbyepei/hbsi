package whu.b606.service;

import whu.b606.dao.BaseDao;
import whu.b606.dto.Status;
import whu.b606.entity.Saler;
import whu.b606.entity.Task;
import whu.b606.util.MailSend;

public interface SalerService extends BaseDao<Saler> {
	public static String BEAN_NAME = "whu.b606.serviceBean.SalerServiceImpl";

	/**
	 * 根据姓名与机构代码查找单位
	 * 
	 * @param name
	 *            单位名称
	 * @param code
	 *            机构代码
	 * @return 单位对象
	 */
	public abstract Saler findByNameAndCode(String name, String code);

	/**
	 * 根据查询编码查找用户
	 * 
	 * @param querycode
	 *            查询码
	 * @return 商家对象
	 */
	public abstract Saler findByQuerycode(String querycode);

	/**
	 * 根据机构代码查询单位
	 * 
	 * @param code
	 *            组织机构代码
	 * @return 单位对象
	 */
	public abstract Saler findByCode(String code);

	/**
	 * 生成查询码重置的通知邮件
	 * 
	 * @param id
	 *            用户的id
	 * @param queryCode
	 *            新的查询码
	 * @return 发送邮件的线程对象
	 */
	public abstract MailSend getCodeResetMail(Integer id, String queryCode);

	/**
	 * 决定增加一个用户还是修改
	 * 
	 * @param s
	 *            来自页面的一个用户，未经保存过，无id
	 * @param queryCode
	 *            查询码
	 * @param t
	 *            关联任务
	 * @return 操作之后的用户对象
	 */
	public abstract Saler saveOrUpdate(Saler s, String queryCode, Task t);

	/**
	 * 发送状态更改通知
	 * 
	 * @param status
	 * @param task
	 * @param queryCode
	 * @return
	 */
	public abstract MailSend getStatusMailSend(Status status, Task task, String queryCode);
}
