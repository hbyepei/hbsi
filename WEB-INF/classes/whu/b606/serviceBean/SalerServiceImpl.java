package whu.b606.serviceBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.ApplyType;
import whu.b606.dto.Status;
import whu.b606.entity.Saler;
import whu.b606.entity.Task;
import whu.b606.service.SalerService;
import whu.b606.util.CommonUtils;
import whu.b606.util.MailSend;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 * 
 **/
@Service(SalerService.BEAN_NAME)
public class SalerServiceImpl extends DaoSupport<Saler> implements SalerService {
	@Override
	public Saler findByNameAndCode(String name, String code) {
		return find("o.name=?1 and o.code=?2", new Object[] { name, code });
	}

	@Override
	public Saler findByQuerycode(String querycode) {
		return find("o.querycode=?1", new Object[] { CommonUtils.makeMD5(querycode) });
	}

	@Override
	public Saler findByCode(String code) {
		return find("o.code=?1", new Object[] { code });
	}

	@Override
	public MailSend getCodeResetMail(Integer id, String queryCode) {
		String emailSubject = "查询码重置通知";
		String htmlPath = "";
		String[] picturePath = new String[] { CommonUtils.getProjectWebRoot() + "/images/logo2.png" };
		String[] emailAddr = null;
		Object[] template = null;
		if (null != id) {
			Saler s = find(id);
			if (null != s && null != s.getEmail() && !"".equals(s.getEmail().trim())) {
				emailAddr = new String[] { s.getEmail() };
				htmlPath = CommonUtils.getProjectWEBINF() + "/emailFile/resetQueryCodeEmail.html";
				template = new Object[] { queryCode, CommonUtils.getParameter("config/commonData", "Tel") };
			}
		}
		return new MailSend(emailSubject, emailAddr, htmlPath, template, picturePath);
	}

	/**
	 * 根据身份证号判断用户是否存在，进而决定是新增用户，还是更新用户。同时需要设定查询码和任务集合
	 */
	@Override
	public Saler saveOrUpdate(Saler s, String queryCode, Task t) {
		String code = s.getCode();
		boolean shouldUpdate = false;
		Saler slr = null;
		if (!CommonUtils.isNull(code)) {
			slr = findByCode(code);
		}
		if (slr != null) {// 存在此用户，应当直接更新。只有当此用户是申请方时才更新查询码（先前是责任方的话，不存在查询码）
			shouldUpdate = true;
		} else {// 不存在此用户
			slr = s;
		}
		if (t.getApplytype().equals(ApplyType.Saler)) {// 当前是申请方，则更新查询码
			slr.setQuerycode(CommonUtils.makeMD5(queryCode));// 应当更新此查询码，因为新生成的查询码与前面的可能不一样
			slr.setCurrentTask_caseid(t.getCaseid());
		} else {}
		Set<Task> tasks = slr.getTasks();
		tasks.add(t);
		// slr.setTasks(tasks);// 这一句是否多余？
		if (shouldUpdate) {
			update(slr);
		} else {
			save(slr);
		}
		return slr;
	}

	/**
	 * 状态修改通知邮件，每一个状态修改都要发送邮件。 首先看是否存在邮件地址
	 * 其次查看当前用户是不是申请方，若是则选择申请方的邮件模板，否则选择责任方的邮件模板
	 * 
	 */
	@Override
	public MailSend getStatusMailSend(Status currentStatus, Task task, String queryCode) {
		Saler s = task.getSaler();
		if (s != null && s.getEmail() != null && !s.getEmail().trim().equals("")) {
			/** =====发件参数======= **/
			String emailSubject = "武汉市家用汽车三包争议调解通知";
			String[] emailAddr = new String[] { s.getEmail() };
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
			if (task.getApplytype().equals(ApplyType.Saler)) {// 当前用户是申请方
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
				String applyer = task.getConsumer().getName();
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
