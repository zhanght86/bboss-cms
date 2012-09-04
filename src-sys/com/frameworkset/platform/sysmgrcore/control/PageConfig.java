package com.frameworkset.platform.sysmgrcore.control;

import java.io.Serializable;

/**
 * 项目：SysMgrCore <br>
 * 描述：数据分页配置 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public class PageConfig implements Serializable {
	/**
	 * 当前页面的起始索引值
	 */
	private int startIndex = -1;

	/**
	 * 每页所显示的数据记录总数
	 */
	private int pageSize = 0;

	/**
	 * 返回给客户程序的记录总数
	 */
	private int totalSize = -1;

	/**
	 * 返回每一个页面中需要显示的数据的大小
	 * 
	 * @return 返回 pageSize。
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每一个页面中需要显示的数据的大小
	 * 
	 * @param pageSize
	 *            要设置的 pageSize。
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回当前页面中的数据起始位置
	 * 
	 * @return 返回 startIndex。
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * 设置当前页面中的数据起始位置
	 * 
	 * @param startIndex
	 *            要设置的 startIndex。
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * 设置数据的总数
	 * @param totalSize 需要设置的总数
	 */
	protected void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * 返回数据的总数　
	 * @return 返回的总数
	 */
	public int getTotalSize() {
		return totalSize;
	}

}