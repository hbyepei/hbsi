package whu.b606.dto;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月14日
 * 
 **/
public enum Technology {
	electrical(1, "电气"), engine(2, "发动机"), law(3, "法律政策"), carbody(4, "车身"), underpan(5, "底盘"), none(6, "未知");
	private String description;
	private int groupID;

	private Technology(int groupID, String description) {
		this.groupID = groupID;
		this.description = description;
	}

	public int getGroupID() {
		return this.groupID;
	}

	/**
	 * 获得枚举类型实例的描述信息
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 根据描述信息返回对应的枚举实例
	 * 
	 * @param description
	 * @return
	 */
	public static Technology string2Enum(String description) {
		Technology[] tech = Technology.values();
		for (Technology t : tech) {
			String desc = t.getDescription();
			if (description.equalsIgnoreCase(desc.trim())) { return t; }
		}
		return Technology.none;
	}

	public static Technology getEnumByID(int groupID) {
		Technology[] tech = Technology.values();
		for (Technology t : tech) {
			if (t.getGroupID() == groupID) { return t; }
		}
		return null;
	}
}
