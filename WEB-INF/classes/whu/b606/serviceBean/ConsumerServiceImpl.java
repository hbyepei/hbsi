package whu.b606.serviceBean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.ApplyType;
import whu.b606.dto.Status;
import whu.b606.entity.Consumer;
import whu.b606.entity.Task;
import whu.b606.service.ConsumerService;
import whu.b606.util.CommonUtils;
import whu.b606.util.MailSend;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 * 
 **/
@Service(ConsumerService.BEAN_NAME)
public class ConsumerServiceImpl extends DaoSupport<Consumer> implements ConsumerService {
	@Override
	public void delete(Serializable... ids) {// 由于关联关系，在删除用户之前应当先解除该与用户关联的所有任务信息
		super.delete(ids);
	}

	@Override
	public Consumer findByNameAndIdcard(String name, String idcard) {
		return find("o.querycode=?1 and name=?2", new Object[] { name, idcard });
	}

	@Override
	public Consumer findByQuerycode(String querycode) {
		return find("o.querycode=?1", new Object[] { CommonUtils.makeMD5(querycode) });
	}

	@Override
	public MailSend getCodeResetMail(Integer id, String queryCode) {
		String emailSubject = "查询码重置通知";
		String htmlPath = "";
		String[] picturePath = new String[] { CommonUtils.getProjectWebRoot() + "/images/logo2.png" };
		String[] emailAddr = null;
		Object[] template = null;
		if (null != id) {
			Consumer c = find(id);
			if (null != c && null != c.getEmail() && !"".equals(c.getEmail().trim())) {
				emailAddr = new String[] { c.getEmail() };
				htmlPath = CommonUtils.getProjectWEBINF() + "/emailFile/resetQueryCodeEmail.html";
				template = new Object[] { queryCode, CommonUtils.getParameter("config/commonData", "Tel") };
			}
		}
		return new MailSend(emailSubject, emailAddr, htmlPath, template, picturePath);
	}

	@Override
	public Consumer findByIdcard(String idcard) {
		return find("o.idcard=?1", new Object[] { idcard });
	}

	/**
	 * 根据身份证号判断用户是否存在，进而再根据其是否是申请方来决定是新增用户，还是更新用户。同时需要设定查询码和任务集合
	 */
	@Override
	public Consumer saveOrUpdate(Consumer c, String queryCode, Task t) {
		String idcard = c.getIdcard();
		boolean shouldUpdate = false;
		Consumer cmr = null;
		if (!CommonUtils.isNull(idcard)) {
			cmr = findByIdcard(idcard);
		}
		if (cmr != null) {// 存在此用户，应当直接更新。只有当此用户是申请方时才更新查询码（先前是责任方的话，不存在查询码）
			shouldUpdate = true;
		} else {// 不存在此用户
			cmr = c;
		}
		if (t.getApplytype().equals(ApplyType.Consumer)) {// 当前是申请方，则更新查询码
			cmr.setQuerycode(CommonUtils.makeMD5(queryCode));// 应当更新此查询码，因为新生成的查询码与前面的可能不一样
			cmr.setCurrentTask_caseid(t.getCaseid());
		}
		Set<Task> tasks = cmr.getTasks();
		tasks.add(t);
		// cmr.setTasks(tasks);// 这一句是否多余？
		if (shouldUpdate) {
			update(cmr);
		} else {
			save(cmr);
		}
		return cmr;
	}

	/**
	 * 状态修改通知邮件，每一个状态修改都要发送邮件。 首先看是否存在邮件地址
	 * 其次查看当前用户是不是申请方，若是则选择申请方的邮件模板，否则选择责任方的邮件模板
	 * 
	 */
	@Override
	public MailSend getStatusMailSend(Status currentStatus, Task task, String queryCode) {
		Consumer c = task.getConsumer();
		if (c != null && c.getEmail() != null && !c.getEmail().trim().equals("")) {
			/** =====发件参数======= **/
			String emailSubject = "武汉市家用汽车三包争议调解通知";
			String[] emailAddr = new String[] { c.getEmail() };
			String htmlPath = CommonUtils.getProjectWEBINF() + "/emailFile/";
			Object[] template = null;
			String[] picturePath = new String[] { CommonUtils.getProjectWebRoot() + "/images/logo2.png" };
			/** =====发件参数======= **/
			/** =====固定的公共参数====== **/
			DateFormat df = new SimpleDateFormat("yyyy年M月d日");
			String status = currentStatus.getDescription();
			String ip = CommonUtils.getParameter("config/commonData", "IP");
			String tel = CommonUtils.getParameter("config/commonData", "Tel");
			String applytime = df.format(task.getApplytime());
			if (CommonUtils.isNull(queryCode)) {
				queryCode = "********";
			}
			/** =====固定的公共参数====== **/
			if (task.getApplytype().equals(ApplyType.Consumer)) {// 当前用户是申请方
				if (currentStatus.equals(Status.toaudit)) {// 首次申请的邮件
					htmlPath += "applyEmail.html";
					template = new Object[] { applytime, status, ip, queryCode, tel };
				} else {// 非首次
					htmlPath += "aEmail.html";
					template = new Object[] { status, ip, queryCode, tel };
				}
			} else {// 非申请方
				String brand = task.getCar().getBrand();// 品牌
				String caseid = task.getCaseid();
				String applyer = task.getSaler().getName();
				String matter = task.getMatter();
				String description = task.getDescription();
				String model = "未知";// 型号
				String m = task.getCar().getModel();
				String carriage = "未知";// 车架号
				String cage = task.getCar().getCarriage();
				String dealtime = "未知";// 购车时间
				Date dt = task.getCar().getDealtime();
				if (m != null && !m.trim().equals("")) {
					model = m;
				}
				if (cage != null && !cage.trim().equals("")) {
					carriage = cage;
				}
				if (dt != null) {
					dealtime = df.format(dt);
				}
				if (currentStatus.equals(Status.toaudit)) {// 首次接收到申请的邮件
					htmlPath += "dutyEmail.html";
					template = new Object[] { brand, status, caseid, applyer, applytime, matter, model, carriage, dealtime, description, ip, tel };
				} else {// 非首次
					htmlPath += "bEmail.html";
					template = new Object[] { brand, model, status, caseid, applyer, applytime, matter, carriage, dealtime, description, ip, tel };
				}
			}
			MailSend ms = new MailSend(emailSubject, emailAddr, htmlPath, template, picturePath);
			return ms;
		}
		return null;
	}
}
