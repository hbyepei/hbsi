package whu.b606.dto;

public enum Gender {
	MALE("男"), FEMALE("女"), NONE("保密");
	private String description;

	private Gender(String description) {
		this.description = description;
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
	public static Gender string2Enum(String description) {
		Gender[] gen = Gender.values();
		for (Gender g : gen) {
			String desc = g.getDescription();
			if (description.equalsIgnoreCase(desc.trim())) {
				return g;
			}
		}
		return Gender.NONE;
	}
}