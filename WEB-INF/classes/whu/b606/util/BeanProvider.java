package whu.b606.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 获取Spring组件窗口中特定Bean组件的工具
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 */
public class BeanProvider {
	private static BeanProvinderCore bpc;
	static {
		bpc = new BeanProvinderCore();
		bpc.load("config/applicationContext.xml");
	}

	public static Object getBean(String beanName) {
		if (StringUtils.isBlank(beanName)) { throw new RuntimeException("您要访问的服务名称不能为空"); }
		Object bean = null;
		// 如果spring容器中包含beanName
		if (bpc.ctx.containsBean(beanName)) {
			bean = bpc.ctx.getBean(beanName);
		}
		// 如果spring容器中不包含beanName
		if (bean == null) { throw new RuntimeException("您要访问的服务名称[" + beanName + "]不存在"); }
		return bean;
	}
}

/**
 * 该类的主要作用是加载spring.xml文件
 */
class BeanProvinderCore {
	protected ApplicationContext ctx;

	/**
	 * @param filePath
	 *            spring.xml
	 */
	public void load(String filePath) {
		ctx = new ClassPathXmlApplicationContext(filePath);// 注：filePath可为类路径下的"spring.xml"或子目录下的xxx/xxx/spring.xml
		// ctx=new
		// FileSystemXmlApplicationContext(filePath);注：此时的filePath形如："src/xxx/beans.xml"
	}
}
