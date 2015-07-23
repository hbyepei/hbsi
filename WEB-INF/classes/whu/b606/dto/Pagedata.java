package whu.b606.dto;

public class Pagedata {
	/**
	 * 接收页面传递过来的排序方案数组
	 */
	private String[] order;
	/**
	 * 接收页面传递过来的页号
	 */
	private int page;
	/**
	 * 接收页面传递过来的页面尺寸（每页容纳的记录数）
	 */
	private int rows;
	/**
	 * 接收页面传递过来的排序字段名数组
	 */
	private String[] sort;

	public Pagedata() {
		super();
	}

	public Pagedata(int page, int rows, String[] sort, String[] order) {
		super();
		this.order = order;
		this.page = page;
		this.rows = rows;
		this.sort = sort;
	}

	public Pagedata(int page, int rows, String sort, String order) {
		this.page = page;
		this.rows = rows;
		this.sort = sort.split(",");
		this.order = order.split(",");
	}

	public String[] getOrder() {
		return order;
	}

	public void setOrder(String[] order) {
		this.order = order;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String[] getSort() {
		return sort;
	}

	public void setSort(String[] sort) {
		this.sort = sort;
	}
}
