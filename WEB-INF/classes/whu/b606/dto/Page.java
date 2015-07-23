package whu.b606.dto;

import java.util.List;

/**
 * 实体的分页模型
 * 
 * @author Yepei,Wuhan University
 * @version 1.2
 * @date 2015年4月18日
 * @param <T>
 *            实体 类型参数
 **/
public class Page<T> {
	private static final int COUNT = 10; // 一个导航器中默认显示10个导航页码
	private int pagesize = COUNT; // 默认页面尺寸，即一页中显示多少条记录,可以自定义
	private final int linkcount = COUNT; // 一个网页中显示页面导航数量
	private int totalrecord; // 总记录数
	private int totalpage; // 记住所有记录需要占用的总页数
	private int currentpagesize; // 当前页面大小，即当前页面中显示了多少条记录，注意它可能与pagesize不同，因为最后一页的记录数可能不足一页，或者因为条件筛选导致当前页的记录数未满
	private int pagenum; // 当前页号
	private int startIndex; // 记住用户想看的页的数据从数据库中的哪条记录开始取
	private int startPage;
	private int endPage;
	private List<T> list; // 记住查询所得的完整的一页记录

	public Page(Integer pagenum, Integer pagesize, int totalrecord) {// 完成页对象中各属性间的数值关系运算
		this.totalrecord = totalrecord;
		// 注意：需要保证pagesize与pagenum的合法性：
		// 1<=pagenum<=totalpage;
		// 1<=pagesize<=totalrecord
		if (pagesize != null && pagesize >= 1 && pagesize <= totalrecord) {// 只有合法的pagesize才会被传进来，否则使用默认页面大小（10）
			this.pagesize = pagesize;
		}
		// 注：计算总页数的问题，就是计算整除的问题，
		// 如m个球放入x个袋中，每个袋中可容y个球，袋子要尽量放满，则需要x=(m+y-1)/y个袋子。
		this.totalpage = (this.totalrecord + this.pagesize - 1) / this.pagesize;
		if (pagenum != null && pagenum >= 1 && pagenum <= this.totalpage) {// 只有合法的pagenum才会被传进来，否则显示第一页
			this.pagenum = pagenum;
		} else {
			this.pagenum = 1;
		}
		// 算法用户想看的记录
		this.startIndex = (this.pagenum - 1) * this.pagesize;
		// 计算页面中要显示的首页和尾页
		if (this.totalpage < 2) {
			// 尚不超过一页
			this.startPage = 1;
			this.endPage = this.totalpage;
			this.currentpagesize = this.totalrecord;
		} else {// 页数大于一页，要保证美观，需要将当前页显示在导航链接的中间
			int middle = this.linkcount / 2;
			if (middle * 2 == this.linkcount) {// 要显示偶数个导航链接时
				this.startPage = this.pagenum - middle + 1;
			} else {// 页大小为奇数时
				this.startPage = this.pagenum - middle;
			}
			this.endPage = this.pagenum + middle;
			// 上述计算保证了endPage-startPage+1=linkcount;
			// 经过上述调整后可保证当前页是中间页，但有可能使startPage<1或endPage>totalPage，所以还需要调整：
			// 计算之前需要先计算总页数与导航数的关系：
			if (this.totalpage <= this.linkcount) {// 导航条能够全部显示所有页面链接时
				this.startPage = 1;
				this.endPage = this.totalpage;
			} else {// 导航条不能全部显示所有页面链接时
				if (this.startPage < 1) {// 保证起始页为正
					this.startPage = 1;
					this.endPage = this.linkcount;// （可能超出总页数）
				}
				if (this.endPage > this.totalpage) {// 保证不超出总页数
					this.endPage = this.totalpage;
					this.startPage = this.totalpage - this.linkcount + 1;
				}
			}
			if (this.pagenum < this.totalpage) {
				this.currentpagesize = this.pagesize;
			} else {
				this.currentpagesize = this.totalrecord - ((this.pagenum - 1) * this.pagesize);
			}
		}
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getCurrentpagesize() {
		return currentpagesize;
	}

	public void setCurrentpagesize(int currentpagesize) {
		this.currentpagesize = currentpagesize;
	}

	public int getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
}
