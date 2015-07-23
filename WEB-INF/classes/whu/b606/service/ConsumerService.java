package whu.b606.service;

import whu.b606.dao.BaseDao;
import whu.b606.dto.Status;
import whu.b606.entity.Consumer;
import whu.b606.entity.Task;
import whu.b606.util.MailSend;

public interface ConsumerService extends BaseDao<Consumer> {
	public static String BEAN_NAME = "whu.b606.serviceBean.ConsumerServiceImpl";

	/**
	 * 根据姓名与身份证号查找用户
	 * 
	 * @param name
	 *            用户名
	 * @param phone
	 *            电话号码
	 * @return 消费者用户
	 */
	public abstract Consumer findByNameAndIdcard(String name, String idcard);

	/**
	 * 根据查询编码查找用户
	 * 
	 * @param querycode
	 *            查询码
	 * @return 消费者用户
	 */
	public abstract Consumer findByQuerycode(String querycode);

	/**
	 * 生成查询码重置的通知邮件
	 * 
	 * @param id
	 *            消费者用户的id
	 * @param queryCode
	 *            新的查询码
	 * @return 发送邮件的线程对象
	 */
	public abstract MailSend getCodeResetMail(Integer id, String queryCode);

	/**
	 * 根据身份证号查询用户
	 * 
	 * @param idcard
	 * @return 消费者用户
	 */
	public abstract Consumer findByIdcard(String idcard);

	/**
	 * 决定增加一个用户还是修改
	 * 
	 * @param c
	 *            来自页面的一个用户，未经保存过，无id
	 * @param queryCode
	 *            查询码
	 * @param t
	 *            关联任务
	 * @return 操作之后的用户对象
	 */
	public abstract Consumer saveOrUpdate(Consumer c, String queryCode, Task t);

	/**
	 * 发送状态修改邮件
	 * 
	 * @param queryCode
	 * 
	 * @param status
	 * @return
	 */
	public abstract MailSend getStatusMailSend(Status currentStatus, Task task, String queryCode);
}
