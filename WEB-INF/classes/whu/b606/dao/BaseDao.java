package whu.b606.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import whu.b606.dto.Order;
import whu.b606.dto.Page;

/**
 * 数据库CRUD操作接口
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 * @param <T>
 *            实体类型参数
 **/
public interface BaseDao<T> {
	/**
	 * 清除一级缓存的数据
	 */
	public void clear1stCache();

	/**
	 * 保存实体
	 * 
	 * @param entity
	 *            待保存的实体
	 */
	public abstract void save(T entity);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 *            待保存的实体
	 */
	public abstract void update(T entity);

	/**
	 * 删除多个实体
	 * 
	 * @param ids
	 *            实体的id数组
	 */
	public abstract void delete(Serializable... ids);

	/**
	 * 根据id查询实体
	 * 
	 * @param id
	 *            实体的id
	 * @return 实体对象
	 */
	public abstract T find(Serializable id);

	/**
	 * 根据多个条件查询实体
	 * 
	 * @param whereJPQL
	 *            条件语句，形如：“o.feild1=?1 and o.feild2=?2 and o.feild3 like ?3 and
	 *            o.feild4 in (?4,?5,…)”
	 * @param params
	 *            用来填补whereJPQL语句的参数数组
	 * @return 实体对象
	 */
	public abstract T find(String whereJPQL, Object[] params);

	/**
	 * 根据多个条件查询实体集合，如果whereJPQL和params为null，则查询所有实体并返回List集合
	 * 
	 * @param whereJPQL
	 *            条件语句，形如：“o.feild1=?1 and o.feild2=?2 and o.feild3 like ?3 and
	 *            o.feild4 in (?4,?5,…)”
	 * @param params
	 *            用来填补whereJPQL语句的参数数组
	 * @return 实体的List集合
	 */
	public abstract List<T> finds(String whereJPQL, Object[] params);

	/**
	 * 原生查询
	 * 
	 * @param sql
	 * @return 实体集合
	 */
	public abstract List<T> finds(String sql);

	/**
	 * 查询实体的分页数据
	 * 
	 * @param pagenum
	 *            用户希望看的页号
	 * @param pagesize
	 *            用户希望每页显示的记录条数
	 * @return 实体的分页数据
	 */
	public abstract Page<T> findByPage(Integer pagenum, Integer pagesize);

	/**
	 * 查询实体的分页数据，并按条件过滤
	 * 
	 * @param pagenum
	 *            用户希望看的页号
	 * @param pagesize
	 *            用户希望每页显示的记录条数
	 * @param whereJPQL
	 *            条件语句，形如：“o.feild1=?1 and o.feild2=?2 and o.feild3 like ?3 and
	 *            o.feild4 in (?4,?5,…)”
	 * @param params
	 *            用于替换jpql语句中的？的参数
	 * @return 实体的分页数据
	 */
	public abstract Page<T> findByPage(Integer pagenum, Integer pagesize, String whereJPQL, Object[] params);

	/**
	 * 查询实体的分页数据，并排序
	 * 
	 * @param pagenum
	 *            用户希望看的页号
	 * @param pagesize
	 *            用户希望每页显示的记录条数
	 * @param orderBy
	 *            排序条件，接收一个Map，其中key为字段名称(o.name或name)，value为排序关键字(ASC or DESC)
	 * @return 实体的分页数据
	 */
	public abstract Page<T> findByPage(Integer pagenum, Integer pagesize, LinkedHashMap<String, Order> orderBy);

	/**
	 * 查询实体的分页数据，并进行条件过滤，并排序
	 * 
	 * @param pagenum
	 *            用户希望看的页号
	 * @param pagesize
	 *            用户希望每页显示的记录条数
	 * @param whereJPQL
	 *            条件语句，形如：“o.feild1=?1 and o.feild2=?2 and o.feild3 like ?3 and
	 *            o.feild4 in (?4,?5,…)”
	 * @param params
	 *            用于替换jpql语句中的？的参数数组
	 * @param orderBy
	 *            排序条件，接收一个Map，其中key为字段名称(o.name或name)，value为排序关键字(ASC or DESC)
	 * @return 实体的分页数据
	 */
	public abstract Page<T> findByPage(Integer pagenum, Integer pagesize, String whereJPQL, Object[] params, LinkedHashMap<String, Order> orderBy);

	/**
	 * 查询某类实体的总记录数
	 * 
	 * @return 总记录数
	 */
	public abstract int Count();

	/**
	 * 根据条件查询某类实体的总记录数
	 * 
	 * @param whereJPQL
	 *            条件语句，形如：“o.feild1=?1 and o.feild2=?2 and o.feild3 like ?3 and
	 *            o.feild4 in (?4,?5,…)”
	 * @param params
	 *            用来填补whereJPQL语句的参数数组
	 * @return 当前条件下的总记录数
	 */
	public abstract int Count(String whereJPQL, Object[] params);
}
