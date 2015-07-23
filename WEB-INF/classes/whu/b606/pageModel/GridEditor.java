package whu.b606.pageModel;

import java.util.HashMap;
import java.util.Map;

public class GridEditor {
	private String type;
	private Map<String, Object> options = new HashMap<String, Object>();

	public GridEditor() {
		super();
	}

	public GridEditor(String type, Map<String, Object> options) {
		super();
		this.type = type;
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}
}
