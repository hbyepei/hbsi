package whu.b606.dto;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 * 
 **/
public enum ApplyType {
	Saler("单位用户"), Consumer("消费者"), None("未知");
	private String description;

	private ApplyType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * 根据描述信息返回对应的枚举实例
	 * 
	 * @param description
	 * @return
	 */
	public static ApplyType string2Enum(String description) {
		ApplyType[] at = ApplyType.values();
		for (ApplyType a : at) {
			String desc = a.getDescription();
			if (description.equalsIgnoreCase(desc.trim())) { return a; }
		}
		return ApplyType.None;
	}
}
