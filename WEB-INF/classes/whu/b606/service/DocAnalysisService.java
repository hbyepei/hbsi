package whu.b606.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import whu.b606.dao.BaseDao;
import whu.b606.dto.CarTableModel;
import whu.b606.dto.ExpertsRankInfo;
import whu.b606.dto.Pagedata;
import whu.b606.entity.Task;
import whu.b606.pageModel.AccidentInfoModel;
import whu.b606.pageModel.ComboItem;

public interface DocAnalysisService extends BaseDao<Task> {
	public static String BEAN_NAME = "whu.b606.serviceBean.DocAnalysisServiceImpl";

	public abstract Map<String, Object> getPageData(String tab, Date startTime, Date endTime);

	public abstract List<ExpertsRankInfo> getExpertsRankInfo();

	public abstract List<CarTableModel> getCarTableModelRows(Pagedata pd);

	public abstract List<ComboItem> getApplyYears(boolean containsAll);

	public abstract List<AccidentInfoModel> getAccidentInfoModelRows(Integer year);
}
