package whu.b606.serviceBean;

import org.springframework.stereotype.Service;

import whu.b606.dao.DaoSupport;
import whu.b606.entity.Accident_part;
import whu.b606.service.AccidentPartService;

/**
 * @author yepei
 */
@Service(AccidentPartService.BEAN_NAME)
public class Accident_partServiceImpl extends DaoSupport<Accident_part> implements AccidentPartService {}
