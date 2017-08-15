package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;

/**
 * 
  * <p>Title: PageContextImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class PageContextImpl extends PagineContextImpl implements PageContext {
	
	private String pagePath;
	private String pageType;
	private String pageDir;
	
	
	public PageContextImpl(String siteid,String pagePath,
						   String pageType,
						   Context parentContext,
						   PublishObject publishObject,
						   PublishMonitor monitor)
	{
		super(siteid,parentContext,publishObject,monitor);
		
		String[] pageInfo = CMSUtil.getFileInfo(pagePath);
		
		this.pagePath = pagePath;
		
		this.fileName = pageInfo[1];
		this.tempFileName = fileName;
		this.fileExt = FileUtil.getFileExtByFileName(fileName);
		this.mimeType = CMSUtil.getMimeType(fileExt);
		this.pageType = pageType;
		this.pageDir = pageInfo[0];
		this.publishPath = pageDir;
		this.setNeedRecordRefObject(false);
		this.evalFileName();
//		if(!CMSUtil.pageExist(this.parentContext.getSiteID(),pagePath))
		if(!CMSUtil.pageExist(this,siteid,pagePath))
		{
			getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_NOTEXIST);
			getPublishMonitor().addFailedMessage("忽略页面[pagePath="+ pagePath +"]：页面不存在",this.publisher);
		
			return;
		}
		
	}
	
	public PageContextImpl(String siteid,String pagePath,
			   Context parentContext,
			   PublishObject publishObject,
			   PublishMonitor monitor)
	{
		super(siteid,parentContext,publishObject,monitor);
		
		String[] pageInfo = CMSUtil.getFileInfo(pagePath);
		this.pagePath = pagePath;
		
		this.fileName = pageInfo[1];
		this.tempFileName = fileName;
		this.fileExt = FileUtil.getFileExtByFileName(fileName);
		this.mimeType = CMSUtil.getMimeType(fileExt);
		this.pageType = CMSUtil.getPageType(fileName) + "";
		this.pageDir = pageInfo[0];
		this.publishPath = pageDir;
		this.setNeedRecordRefObject(false);
		this.evalFileName();
//		if(!CMSUtil.pageExist(this.parentContext.getSiteID(),pagePath))
		if(!CMSUtil.pageExist(this,siteid,pagePath))
		{
			getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_NOTEXIST);
			getPublishMonitor().addFailedMessage("忽略页面[pagePath="+ pagePath +"]：页面不存在",this.publisher);
		
			return;
		}
		
	}

	

	public String getPagePath() {
		if(!this.isPagintion())
			return pagePath;
		else
		{
			if(this.getCurrentPageNumber() == 0)
			{
				return this.pagePath;
			}
			else
			{
				String[] fileInfo = this.fileName.split("\\.");
				String name = fileInfo[0] + "_" + this.currentPageNumber + "."  + fileInfo[1];
				return CMSUtil.getPath(this.pageDir,name);
			}
		}
	}
	/**
     * 获取分页页面跳转地址
     * @return
     */
    public String getPaginJumpPath()
    {
        return super.getPagePath();
    }
    
    

	public String getPageType() {
		return pageType;
	}
	
	
	
//	/**
//	 * 获取发布页面的地址,重载父类的方法
//	 */
//	public String getPublishedPageUrl()
//	{
//		return this.getSitePublishContext() + "/" + this.pagePath;	
//	}
//	
//	/**
//	 * 获取频道预览首页页面的地址,重载父类的方法
//	 */
//	public String getPreviewPageUrl()
//	{	
//		return this.getPreviewContextPath() + "/" + this.pagePath;
//	}


	


	
	public String getPageDir() {
		// TODO Auto-generated method stub
		return this.pageDir;
	}
	
	public String getRendURI()
	{
		return this.getPagePath();
	}
	
	
	
	public String toString()
	{
		return this.pagePath;
	}
	
	
	/**  
     * 获取发布页面的地址,重载父类的方法
     */
    public String getPublishedPageUrl() {
        //如果是分页发布则需要获取第一页的文件名（fileName）对应的地址，如果不是则通过getFileName()方法获取文件名地址
        if(!this.isPagintion())
        {
            if(this.getPublishPath() != null && !this.getPublishPath().equals(""))
                return this.getSitePublishContext() + "/" + this.getPublishPath() + "/" +getFileName();
            else
                return this.getSitePublishContext() + "/" +getFileName();
        }
        else
        {
            
            if(this.getPublishPath() != null && !this.getPublishPath().equals(""))
                return this.getSitePublishContext() + "/" + this.getPublishPath() + "/" +this.fileName;
            else
                return this.getSitePublishContext() + "/" +this.fileName;
            
            
        }
    }
    
    
    private void evalFileName()
	{		
		this.jspFileName = CMSUtil.getJspFileName(this.fileName, "");
	}

}
