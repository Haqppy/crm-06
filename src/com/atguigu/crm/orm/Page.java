package com.atguigu.crm.orm;

import java.util.List;

import com.sun.org.apache.regexp.internal.recompile;


/**
 * 分页:
 * 页码
 * 分页大小
 * 总记录数
 * 元素
 * @author Java
 *
 * @param <T>
 */
public class Page<T> {

	private int pageNo = 1;
	private int pageSize = 5;
	
	private int totalElements;
	
	private List<T> content;
	
	public void setPageNo(String pageNoStr) {
		
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {}
		
		if(pageNo < 1){
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}

	public int getPageNo() {
		return pageNo;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		//通过总记录数校验页数
		this.totalElements = totalElements;
		
		if(this.pageNo > this.getTotalPages()){
			this.pageNo = this.getTotalPages();
		}
	}
	
	public int getTotalPages() {
		
		return (this.getTotalElements()-1)/(this.getPageSize())+1;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	/**
	 * 判断有没有上一页 和下一页
	 */
	public boolean isHasPrev() {
		return this.pageNo > 1;
	}
	
	public boolean isHasNext() {
		return this.getTotalPages() > this.pageNo;
	}
	
	/**
	 * 获取上一页 和 下一页
	 */
	public int getPrev() {
		if (isHasPrev()) {
			return this.pageNo -1;
		}
		return this.pageNo;
	}
	
	public int getNext() {
		if (isHasNext()) {
			return this.pageNo +1;
		}
		return this.pageNo;
	}
}

