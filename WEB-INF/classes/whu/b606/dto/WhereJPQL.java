package whu.b606.dto;

public class WhereJPQL {
	private String whereJPQL;
	private Object[] params;

	public WhereJPQL() {}

	public WhereJPQL(String whereJPQL, Object[] params) {
		super();
		this.whereJPQL = whereJPQL;
		this.params = params;
	}

	public String getWhereJPQL() {
		return whereJPQL;
	}

	public void setWhereJPQL(String whereJPQL) {
		this.whereJPQL = whereJPQL;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
}
