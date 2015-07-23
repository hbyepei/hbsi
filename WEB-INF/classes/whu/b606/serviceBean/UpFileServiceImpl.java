package whu.b606.serviceBean;

import java.util.Set;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.entity.UpFile;
import whu.b606.exception.NoFileException;
import whu.b606.service.UpFileService;
import whu.b606.util.CommonUtils;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月28日
 * 
 **/
@Service(UpFileService.BEAN_NAME)
public class UpFileServiceImpl extends DaoSupport<UpFile> implements UpFileService {
	@Override
	public void delete(Set<UpFile> files) {
		if (files != null && !files.isEmpty()) {// 有凭证文件
			String savePath = CommonUtils.getParameter("config/commonData", "upfileSavePath");
			String realPath = CommonUtils.getProjectWebRoot() + savePath;
			for (UpFile file : files) {
				// 无论在数据库中存不存在都删除磁盘上的文件
				if (file.getPathname() != null) {
					String fullname = realPath + "/" + file.getPathname();
					try {// 注：需要对文件删除项单独的进行异常捕获和处理，因为文件不存在时抛出的异常不应当使程序终止
						CommonUtils.deleteFile(fullname, 6000l);// 通过文件的路径与实际名称将文件删除
					} catch (Exception e) {
						if (e instanceof NoFileException) {}
						e.printStackTrace();
					}
				}
				if (file.getId() != null && null != find(file.getId())) {
					delete(file.getId());
				}
			}
		}
	}

	@Override
	public UpFile findByPathName(String todelete) {
		// TODO 自动生成的方法存根
		return null;
	}
}
