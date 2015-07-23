package whu.b606.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import whu.b606.dto.Order;
import whu.b606.dto.Page;
import whu.b606.util.CommonUtils;
import whu.b606.util.GenericsUtils;

/**
 * 数据库CRUD操作通用实现类
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 * @param <T>
 *            实体类型参数
 **/
@SuppressWarnings("unchecked")
@Transactional
public abstract class DaoSupport<T> implements BaseDao<T> {
	protected Class<T> entityClass = GenericsUtils.getSuperClassGenricType(this.getClass());// 获得当前类(DaoSupport)的父类(BaseDao)的泛型参数(T)的实际类型
	private final String entityName = CommonUtils.getEntityName(entityClass);// 获取实体的实体名(默认是其类名，但也有可能是经注解特别标注的实体名)，这个实体名对应相应的表名，用于构造select语句中的from子句以确定该查哪个表
	@PersistenceContext
	protected EntityManager em;// 设成protected类型的以允许子类访问注入的对象

	@Override
	public void clear1stCache() {
		em.clear();
	}

	@Override
	public void save(T entity) {
		em.persist(entity);
	}

	@Override
	public void update(T entity) {
		em.merge(entity);
	}

	@Override
	public void delete(Serializable... ids) {
		for (Object id : ids) {
			em.remove(em.getReference(this.entityClass, id));
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public T find(Serializable id) {
		if (id == null) { throw new RuntimeException(this.entityClass.getName() + ":传入的实体id不能为空"); }
		return em.find(this.entityClass, id);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public T find(String whereJPQL, Object[] params) {
		if (finds(whereJPQL, params) != null) {
			List<T> list = finds(whereJPQL, params);
			if (list.size() > 0) { return list.get(0); }
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<T> finds(String whereJPQL, Object[] params) {
		String jpql = "select o from " + entityName + " o " + (whereJPQL == null || whereJPQL.trim().equals("") ? "" : "where " + whereJPQL);
		Query query = em.createQuery(jpql);
		if (null != params) {
			setQueryParameters(query, params);
		}
		return query.getResultList();
	}

	@Override
	public List<T> finds(String sql) {
		Query query = em.createNativeQuery(sql, this.entityClass);
		return query.getResultList();
	}

	@Override
	public Page<T> findByPage(Integer pagenum, Integer pagesize) {
		return PageQuery(pagenum, pagesize, null, null, null);
	}

	@Override
	public Page<T> findByPage(Integer pagenum, Integer pagesize, String whereJPQL, Object[] params) {
		return PageQuery(pagenum, pagesize, whereJPQL, params, null);
	}

	@Override
	public Page<T> findByPage(Integer pagenum, Integer pagesize, LinkedHashMap<String, Order> orderBy) {
		return PageQuery(pagenum, pagesize, null, null, orderBy);
	}

	@Override
	public Page<T> findByPage(Integer pagenum, Integer pagesize, String whereJPQL, Object[] params, LinkedHashMap<String, Order> orderBy) {
		return PageQuery(pagenum, pagesize, whereJPQL, params, orderBy);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public int Count() {
		Query query = em.createQuery("select count(o) from " + entityName + " o ");
		long totalrecord = (Long) query.getSingleResult();
		return (int) totalrecord;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public int Count(String whereJPQL, Object[] params) {
		String sql = "select count(o) from " + entityName + " o " + (whereJPQL == null || whereJPQL.trim().equals("") ? "" : "where " + whereJPQL);
		Query query = em.createQuery(sql);
		setQueryParameters(query, params);
		long totalrecord = (Long) query.getSingleResult();
		return (int) totalrecord;
	}

	/**
	 * @param pagenum
	 *            用户希望看的页号
	 * @param pagesize
	 *            用户希望每页显示的记录数
	 * @param whereJPQL
	 *            条件语句，形如：“o.feild1=?1 and o.feild2=?2 and o.feild3 like ?3 and
	 *            o.feild4 in (?4,?5,…)”
	 * @param params
	 *            用于替换jpql语句中的“？”的参数数组
	 * @param orderBy
	 *            排序条件，接收一个Map，其中key为字段名称，value为排序关键字(ASC or DESC)
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	protected Page<T> PageQuery(Integer pagenum, Integer pagesize, String whereJPQL, Object[] params, LinkedHashMap<String, Order> orderBy) {
		// 算出总记录数传给Page对象
		long totalrecord = Count(whereJPQL, params);
		Page<T> page = new Page<T>(pagenum, pagesize, (int) totalrecord);
		String qry = "select o from " + entityName + " o " + (whereJPQL == null || whereJPQL.trim().equals("") ? "" : "where " + whereJPQL) + buildOrderBy(orderBy);
		Query query = em.createQuery(qry);
		setQueryParameters(query, params);
		query.setFirstResult(page.getStartIndex()).setMaxResults(pagesize);
		page.setList(query.getResultList());
		return page;
	}

	/**
	 * 组装orderBy子句，形如：" order by field1 asc, field2 desc, field3 asc, …"
	 * 
	 * @param orderBy
	 * @return
	 */
	protected String buildOrderBy(LinkedHashMap<String, Order> orderBy) {
		StringBuffer orderByJPQL = new StringBuffer("");
		if (null != orderBy && orderBy.size() > 0) {
			orderByJPQL.append(" order by ");
			for (String key : orderBy.keySet()) {
				orderByJPQL.append(key)//
						.append(" ")//
						.append(orderBy.get(key).toString())//
						.append(",");
			}
			orderByJPQL.deleteCharAt(orderByJPQL.length() - 1);
		}
		return orderByJPQL.toString();
	}

	/**
	 * 为Query对象设置查询参数
	 * 
	 * @param query
	 * @param params
	 */
	protected void setQueryParameters(Query query, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
	}
}
