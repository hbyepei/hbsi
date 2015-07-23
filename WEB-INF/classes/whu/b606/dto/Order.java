package whu.b606.dto;

/**
 * 排序枚举
 * 
 * @author Mystery
 */
public enum Order {
	ASC("升序"), DESC("降序");
	private String description;

	private Order(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * 因为需要从页面接收排序条件，所以，需要提供直接将String类型的asc或ASC转换成enum类型
	 * 
	 * @param order
	 *            代表排序关键字的字符串，如：“ASC”或“asc”，或“DESC”或“desc”
	 * @return 若order不空且为正确的字符串（如asc或ASC或desc或DESC）则返回枚举类型值，否则返回null
	 */
	public static Order string2Enum(String order) {
		Order[] od = Order.values();
		for (Order o : od) {
			String desc = o.getDescription();
			if (order.equalsIgnoreCase(desc.trim())) { return o; }
		}
		return null;
	}
}