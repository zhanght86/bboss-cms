package com.frameworkset.platform.cms.driver.context;

import com.frameworkset.common.tag.pager.tags.PagerContext;

public interface PagineContext extends Context{
	
	
	/*******************************************************************
	 *                      定义分页概览方法开始                           *                     
	 *******************************************************************/
	/**
	 * 设置当前页面是否是分页页面
	 * @param pagintion
	 */
	public void setPagintion(boolean pagintion);
	
	/**
	 * 判断当前页面是否是分页页面
	 * @return
	 */
	public boolean isPagintion();
	
	/**
	 * 获取总的记录条数
	 * @return
	 */
	public long getTotalSize();
	/**
	 * 获取当前的页码currentPageNumber
	 * @return
	 */
	public int getCurrentPageNumber();
	/**
	 * 
	 * 获取当前的记录起点
	 * @return
	 */
	public long getOffset();
	
	public long getNextOffset();
	/**
	 * 获取每页可展示的分页记录数
	 * @return
	 */
	public int getMaxPageItems();

	/**
	 * 获取分页上下文
	 * @return
	 */
	public PagerContext getPagerContext();
	/**
	 * 设置分页上下文
	 * @param pagerContext
	 */
	public void setPagerContext(PagerContext pagerContext);

	/**
	 * 设置当前页码
	 * @param currentPageNumber
	 */
	public void setCurrentPageNumber(int currentPageNumber);

	/**
	 * 设置每页显示的最大记录数
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) ;

	/**
	 * 设置每页获取数据的记录起点
	 * @param offset
	 */
	public void setOffset(long offset) ;

	/**
	 * 设置总的记录数
	 * @param totalSize
	 */
	public void setTotalSize(long totalSize) ;
	
	/**
	 * 设置数据类类型
	 * @param dataType
	 */
	public void setDataType(String dataType);
	
	/**
	 * 获取数据类获取类型
	 * @return
	 */
	public String getDataType();

	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPages();
	
	/**
	 * 判断所有的页面是否发布完成
	 * @return
	 */
	public boolean isPublishAllPage();
	
	/**
	 * 自动翻到下一页
	 *
	 */
	public void next();
	

	/**
	 * 获取对应页面的地址
	 * @param pageNumber
	 * @return
	 */
	public String getPagePath(int pageNumber);
	
	/**
	 * 获取对应页面的地址
	 * @param pageNumber
	 * @return
	 */
	public String getPagePath();

    /**
     * @return
     */
    public String getPaginJumpPath();
	
	/*******************************************************************
	 *                      定义分页概览方法结束                           *                     
	 *******************************************************************/

}
