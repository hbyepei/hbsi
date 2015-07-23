package whu.b606.serviceBean;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.dto.Usertype;
import whu.b606.entity.Image;
import whu.b606.service.ImageService;
import whu.b606.util.CommonUtils;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月28日
 * 
 **/
@Service(ImageService.BEAN_NAME)
public class ImageServiceImpl extends DaoSupport<Image> implements ImageService {
	@Override
	public void removeImage(Image img, Usertype usertype) {
		if (img == null || img.getPathname() == null) { return; }
		String rootPng = "";
		String savePath = "";
		if (usertype.equals(Usertype.Admin)) {
			rootPng = "rootAdmin.png";
			savePath = "adminImageSavePath";
		} else if (usertype.equals(Usertype.Expert)) {
			rootPng = "rootExpert.png";
			savePath = "expertImageSavePath";
		}
		if (img.getId() != null && find(img.getId()) != null) {
			delete(img.getId());// 删除数据库中的数据
		}
		String s = img.getPathname();
		if (!s.equals("default.png") && !s.equals(rootPng)) {
			final String path = CommonUtils.getProjectWebRoot() + CommonUtils.getParameter("config/commonData", savePath) + "/" + img.getPathname();
			CommonUtils.deleteFile(path, 6000l);
		}
	}
}
