package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PagineContext;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.pager.tags.PagerContext;

/**
 * 分页文档、分页概览、分页页面发布上下文，用于存储所有分页上下文信息
 * 可作为PageContext,chancontext,pagecontext的公共上下文
 * <p>Title: PagineContext</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-5-25 16:21:28
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class PagineContextImpl extends BaseContextImpl implements PagineContext{
	private String dataType;
	/**
	 * 分页发布时设置将分页标签的上下文设置到当前发布的环境变量中，
	 * 本变量保存该上下文
	 */
	private PagerContext pagerContext;
	
	private boolean isPagintion = false;
	
	public PagineContextImpl()
	{
		super();
	}

	public PagineContextImpl(String siteid,Context parentContext, PublishObject publishObject, PublishMonitor monitor) {
		super(siteid,parentContext,publishObject,monitor);
	}

	public void setPagintion(boolean pagintion) {
		this.isPagintion = pagintion;		
	}

	/**
	 * 判断是否需要分页处理数据
	 */
	public boolean isPagintion() {		
		return this.isPagintion && this.getTotalSize() > this.getMaxPageItems();
	}
	protected long totalSize;
	
	/**
	 * 获取频道概览的总记录数
	 * @return
	 */
	public long getTotalSize()
	{
		return this.totalSize;
	}
	protected int currentPageNumber;
	/**
	 * 获取当前的页码currentPageNumber
	 * @return
	 */
	public int getCurrentPageNumber()
	{
		return currentPageNumber;
	}
	
	protected long offset;
	/**
	 * 
	 * 获取当前的记录起点
	 * @return
	 */
	public long getOffset()
	{
		return offset;
	}
	
	/**
	 * 每页最大记录数
	 */
	protected int maxPageItems;
	/**
	 * 获取每页可展示的分页记录数
	 * @return
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	public PagerContext getPagerContext() {
		return pagerContext;
	}

	public void setPagerContext(PagerContext pagerContext) {
		this.pagerContext = pagerContext;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public long getNextOffset() {
		// TODO Auto-generated method stub
		return this.offset + this.maxPageItems;
	}
	
	public void next()
	{
		this.offset = this.offset + this.maxPageItems;
	}
	
	
	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPages()
	{
		if(maxPageItems == 0)
		{
			System.out.println("没有设置概览标签的isList和maxPageItems属性。");
			return 0;
		}
		return (int)(this.totalSize / this.maxPageItems +  (totalSize % maxPageItems == 0 ? 0 : 1));
	}
	
	/**
	 * 获取当前页面的名称，发布概览分页时使用
	 * @return
	 */
	public String getCurrentPageName()
	{
		return getPageName(this.currentPageNumber);
	}
	
	private String getPageName(int pageNumber)
	{
		String[] info = this.fileName.split("\\.");
//		StringBuffer ret = new StringBuffer();
//		for(int i = 0; i < info.length; i ++)
//		{
//			if(i < info.length - 1)
//			{
//				ret.append(info[i]).append(".");
//			}
//			
//		}
		if(info.length == 2)
		{
			if(pageNumber > 0)
				return info[0] + "_" + pageNumber + "." + info[1];
			else
				return fileName;
		}
		else
		{
			if(pageNumber > 0)
				return info[0] + "_" + pageNumber;
			else
				return fileName;
		}
	}
	
	
	/**
	 * 获取对应页面的地址 如:content_34279_1.html
	 * @param pageNumber
	 * @return
	 */
	public String getPagePath(int pageNumber)
	{
		return CMSUtil.getPath(this.getPublishPath(),this.getPageName(pageNumber) );
	}
	
	/**
	 * 获取分页页面跳转地址
	 * @return
	 */
	public String getPaginJumpPath()
	{
	    return this.getPagePath();
	}
	
	/**
	 * 获取对应页面的地址的前面一部分 如:content_34279.html
	 * @return
	 */
	public String getPagePath(){
		
		return CMSUtil.getPath("",this.fileName );
	}
	
	public String getFileName() {
		if(!this.isPagintion())
			return this.fileName;
		else
		{
			return this.getCurrentPageName();
		}
	}
	
	public String getCurrentPagePath()
	{
		return CMSUtil.getPath(this.getPublishPath(),getCurrentPageName());
	}
	
	public boolean isPublishAllPage()
	{
		return this.currentPageNumber == this.getTotalPages() - 1;
	}

}
