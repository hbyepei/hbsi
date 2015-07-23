package whu.b606.dto;

public enum Usertype {
	Consumer("个人用户"), Expert("专家用户"), Admin("管理员"), Saler("商家");
	private String description;

	private Usertype(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
