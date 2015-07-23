package whu.b606.service;

import java.util.Set;

import whu.b606.dao.BaseDao;
import whu.b606.entity.UpFile;

public interface UpFileService extends BaseDao<UpFile> {
	public static String BEAN_NAME = "whu.b606.serviceBean.UpFileServiceImpl";

	/**
	 * 删除文件实体的同时删除磁盘文件
	 */
	public void delete(Set<UpFile> files);

	/**
	 * 根据文件名查找文件
	 * 
	 * @param todelete
	 * @return
	 */
	public UpFile findByPathName(String todelete);
}
