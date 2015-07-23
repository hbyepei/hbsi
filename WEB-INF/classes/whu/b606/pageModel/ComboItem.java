package whu.b606.pageModel;

public class ComboItem {
	private Integer id;
	private String text;
	private Object info;// 携带其它可能用得着的信息，比如当表示角色时，此属性可以标识其是否为超管

	public ComboItem() {}

	public ComboItem(Integer id, String text, Object info) {
		super();
		this.id = id;
		this.text = text;
		this.info = info;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
}
