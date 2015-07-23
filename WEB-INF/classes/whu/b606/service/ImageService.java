package whu.b606.service;

import whu.b606.dao.BaseDao;
import whu.b606.dto.Usertype;
import whu.b606.entity.Image;

public interface ImageService extends BaseDao<Image> {
	public static String BEAN_NAME = "whu.b606.serviceBean.ImageServiceImpl";

	/**
	 * 删除头像文件
	 */
	public void removeImage(Image img, Usertype usertype);
}
