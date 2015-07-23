package whu.b606.service;

import whu.b606.dao.BaseDao;
import whu.b606.entity.Accident_part;

public interface AccidentPartService extends BaseDao<Accident_part> {
	public static String BEAN_NAME = "whu.b606.serviceBean.Accident_partServiceImpl";
}
