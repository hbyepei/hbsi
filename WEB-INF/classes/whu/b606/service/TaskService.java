/**
 * 
 */
package whu.b606.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import whu.b606.dao.BaseDao;
import whu.b606.dto.ApplyType;
import whu.b606.dto.FileType;
import whu.b606.dto.Json;
import whu.b606.dto.Pagedata;
import whu.b606.dto.Status;
import whu.b606.entity.Consumer;
import whu.b606.entity.Saler;
import whu.b606.entity.Task;
import whu.b606.entity.UpFile;
import whu.b606.entity.User;
import whu.b606.pageModel.CarAccidentParts;
import whu.b606.pageModel.Grid;
import whu.b606.pageModel.TaskModel;
import whu.b606.util.MailSend;

/**
 * @author yepei
 * 
 */
public interface TaskService extends BaseDao<Task> {
	public static String BEAN_NAME = "whu.b606.serviceBean.TaskServiceImpl";

	/**
	 * 注意，在删除Form时应该将其对象的凭证文件、结果档案一并删除，所以需要使用io操作删除磁盘上的凭证文件
	 * 
	 * @param caseids
	 *            申请表的案例编号
	 */
	public abstract void deleteTask(Integer... caseids);

	/**
	 * 根据caseid查找一个表单
	 * 
	 * @param id
	 *            申请表的id号
	 * @return 申请表
	 */
	public abstract Task findByCaseid(String caseid);

	/**
	 * 将申请信息提取并封装存储
	 * 
	 * @param upfiles
	 *            上传的文件信息
	 * 
	 * @param c
	 *            消费者
	 * @param s
	 *            机构用户
	 * @param tp
	 *            申请类型
	 * @param t
	 *            任务信息
	 * @return Json格式的操作结果信息，其中ok封装操作成功与否的信息，msg封装提示信息，object封装成功处理后的任务对象
	 */
	public abstract Json addNewTask(Set<UpFile> upfiles, Consumer c, Saler s, ApplyType tp, Task t);

	/**
	 * 上传文件，返回上传结果信息
	 * 
	 * @param upfile
	 * @param upfileFileName
	 * @param upfileContentType
	 * @param ft
	 *            文件类型
	 * @param description
	 *            文件描述信息
	 * @return Json格式的结果，其中ok封装上传成功与否的信息，msg封装出错信息，object封装上传成功后的Set<UpFile>对象
	 */
	public abstract Json saveFile(File[] upfile, String[] upfileFileName, String[] upfileContentType, FileType ft, String description);

	/**
	 * 为task分配专家
	 * 
	 * @param task
	 * @param expertids
	 * @return
	 */
	public abstract String setExperts(Task task, Integer[] expertids);

	/**
	 * 向有关各方发送邮件
	 * 
	 * @param task
	 *            当前任务
	 * @param expertids
	 *            需要给相关专家发送邮件的专家id号
	 *            专家ID
	 * @param status
	 *            当前任务状态
	 * @param queryCode
	 *            查询码
	 * @return 邮件线程
	 */
	public abstract List<MailSend> getMailSend(Task task, Integer[] expertids, Status status, String queryCode);

	/**
	 * 返回文档下载流
	 * 
	 * @param doc
	 * @return
	 */
	public abstract InputStream getDocInputStream(String docname);

	public abstract String getDownloadFileName(String doc);

	/**
	 * 获得过滤好的分页数据
	 * 
	 * @param loadtype
	 *            加载类型
	 * 
	 * @param params
	 *            额外的条件参数，比如查询时间/高级查询条件等
	 * @param pagedata
	 *            分页参数
	 * @param u
	 *            执行查询的用户
	 */
	public abstract Grid<TaskModel> getTasks(String loadtype, Object[] params, Pagedata pagedata, User u);

	/**
	 * 发送邮件
	 * 
	 * @param t
	 */
	public abstract List<MailSend> getMailSend(Task t);

	/**
	 * 分配专家，然后返回分配了专家之后的任务。注意，无须修改任务状态且分配专家后不持久化到数据库，持久化操作放在Action中统一执行
	 * 若至少分配成功一个专家，则j.setOk(true)，j.setMsg("成功分配几位专家，未能分配几位专家。")，j.setObject(
	 * task)
	 * 若全部分配失败，则j.setOk(false)，j.setMsg("失败原因")
	 * 
	 * @param t
	 * @param expertids
	 * @return
	 */
	public abstract Json allocateExperts(Task t, Integer[] expertids);

	/**
	 * 发送状态更改的邮件
	 * 
	 * @param t
	 * @param refused
	 * @return
	 */
	public abstract List<MailSend> getMailSend(Task t, Status s);

	/**
	 * 获得下载文件流，根据taskid查询出task，然后填充文档后返回文档流
	 * 
	 * @param taskid
	 * @return
	 */
	public abstract InputStream getDocInputStream(Integer taskid, String docname);

	/**
	 * 根据文件扩展名获取其MIME类型
	 * 
	 * @param doc
	 * @return
	 */
	public abstract String getFileContentType(String doc);

	/**
	 * 根据页面模型修改任务属性(只修改tm中出现的属性)
	 * 
	 * @param tm
	 *            保存新新属性的实体
	 * @param deletedFiles
	 *            要删除的文件
	 * @param upfiles
	 *            要重新上传的文件
	 * @param cap
	 * @return 返回修改结果
	 */
	public abstract Json update(TaskModel tm, List<String> deletedFiles, Object[] upfiles, CarAccidentParts cap);

	/**
	 * 建立新任务
	 * 
	 * @param tm
	 * @param upfiles
	 * @param cap
	 * @return
	 */
	public abstract Json addTask(TaskModel tm, Object[] upfiles, CarAccidentParts cap);

	public abstract String fillNumbersForSql(String sql);

	/**
	 * 将所有任务导出到Excel文件中，此方法生成文件流
	 * 
	 * @param exportAll
	 *            是否将正在进行中的任务也一并导出
	 * @return
	 */
	public abstract InputStream generateExcelInputStream(boolean exportAll) throws Exception;

	/**
	 * 将选中的任务批量生成Word文件并压缩成zip格式后返回Zip文件流
	 * 
	 * @param ids
	 *            任务的ID数组
	 * @return
	 */
	public abstract InputStream generateWordZipInputStream(Integer[] ids) throws Exception;
}
