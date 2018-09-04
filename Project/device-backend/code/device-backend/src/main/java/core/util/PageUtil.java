package core.util;

public class PageUtil {
	private Integer page;//当前页数 ,从第一页开始算
	private Integer pageSize;//每页显示的数据行数
	private Integer start;//SQL查询起始页数

	public PageUtil() {
		//空构造函数
	}
	/**
	 * 构造器
	 * @param page  当前页
	 * @param pageSize  每页显示的行数
	 */
	public PageUtil(String page, String pageSize) {
		if(page==null || page.isEmpty()) {
			page="1";
		}
		if(pageSize==null||pageSize.isEmpty()){
			pageSize="10";
		}
		this.page = Integer.valueOf(page);
		this.pageSize = Integer.valueOf(pageSize);
	}
	
	public PageUtil(Integer page, Integer pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		start = (getCurrentPage() - 1) * getPageSize();
		return start;
	}

	public Integer getCurrentPage() {
		if (page == null || page < 1) {
			page = 1;
		}
		return page;
	}

	public void setCurrentPage(Integer currentPage) {
		this.page = currentPage;
	}

	public Integer getPageSize() {
		if(pageSize==null) {
			pageSize=10;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setStart(Integer start) {
		this.start = start;
	}
}
