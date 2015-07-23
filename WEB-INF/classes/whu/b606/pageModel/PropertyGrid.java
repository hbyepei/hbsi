package whu.b606.pageModel;

public class PropertyGrid {
	private String name;
	private String value;
	private String group;
	private Object editor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGroup() {
		return group;
	}

	public Object getEditor() {
		return editor;
	}

	public void setEditor(Object editor) {
		this.editor = editor;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
