package com.frameworkset.platform.cms.driver.dataloader;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

/**
 * 概览模版数据获取接口
 * <p>Title: CMSBaseListData</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-4-13 9:47:46
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class CMSBaseListData extends DataInfoImpl  {
	protected Context context;
	protected CMSServletRequest cmsrequest;
	protected String channel;
	protected int count = -1;
	/**
	 * 外部指定的站点名称，适合于多站点的情况
	 */
	protected String site;
	protected String documentid;
	protected String doctype;
	protected Map<String,Object> params;
//	protected CMSServletResponse cmsresponse;
	/**
     * 初始化获取分页/列表数据的必要参数
     * @param sortKey 排序字段
     * @param desc 排序顺序，true表示降序，false表示升序
     * @param offSet 获取分页数据时，用该参数设置获取数据的起点
     * @param pageItemsize 获取分页数据时，用该参数设置获取数据的条数
     */
	public void initial(String sortKey,
						boolean desc,
						long offSet,
						int pageItemsize,
						boolean listMode,
						HttpServletRequest request)
	{
		super.initial(sortKey,
						desc,
						offSet,
						pageItemsize,
						listMode,
						request);
		cmsrequest = InternalImplConverter.getInternalRequest(this.request);
//		cmsresponse = InternalImplConverter.getInternalResponse(this.response);
		if(cmsrequest != null)
		{
			this.context =  (Context)this.cmsrequest.getContext();
		}
		
		
	}
	public String getStringParam(String key)
	{
		if(this.params != null)
			return (String)params.get(key);
		return null;
	}
	public String getStringParam(String key,String defaultValue)
	{
		if(this.params != null)
		{
			String value = (String)params.get(key);
			if(value == null)
				return defaultValue;
			return value;
		}
		return defaultValue;
	}
	public boolean getBooleanParam(String key)
	{
		if(this.params != null)
		{
			Boolean value = (Boolean)params.get(key);
			if(value != null)
				return value.booleanValue();
		}
		return false;
	}
	public boolean getBooleanParam(String key,boolean defaultValue)
	{
		if(this.params != null)
		{
			Boolean value = (Boolean)params.get(key);
			if(value != null)
				return value.booleanValue();
			return defaultValue;
		}
		return defaultValue;
	}
	/**
	 * 初时化频道信息
	 * @param channel
	 * @param count
	 */
	public void setOutlineInfo(String channel,int count)
	{
		this.channel = channel;
		this.count = count;
	}
	
	/**
	 * 初时化频道信息
	 * @param site
	 * @param channel
	 * @param count
	 */
	public void setOutlineInfo(String site,String channel,int count)
	{
		this.site = site;
		this.channel = channel;
		this.count = count;
	}
	
	/**
	 * 初时化频道信息
	 * @param site
	 * @param channel
	 * @param count
	 */
	public void setOutlineInfo(String site,String channel,int count,String doctype)
	{
		this.site = site;
		this.channel = channel;
		this.count = count;
		this.doctype = doctype;
	}
	
	/**
	 * 初时化频道信息
	 * @param site
	 * @param channel
	 * @param count
	 */
	public void setOutlineInfo(String site,String channel,int count,Map<String,Object> params)
	{
		this.site = site;
		this.channel = channel;
		this.count = count;
		this.params = params;
		if(params != null)
			this.doctype = (String)params.get("doctype");
	}
	
	/**
	 * 初时化文档信息
	 * @param site
	 * @param channel
	 * @param count
	 */
	public void setDocumentid(String documentid)
	{
		this.documentid = documentid;
	}
	
	
	/**
	 * 获取频道信息
	 * @return
	 */
	public String getChannel()
	{
		if(this.channel != null)
			return channel;
		else if(this.context != null)
		{
			return CMSUtil.getChannelDispalyName(context);
			
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获取当前的频道的文档显示条目数
	 * @return
	 */
	public int getCount()
	{
		return this.count;
	}
	
//	/**
//	 * 获取原始的频道路径链接
//	 * @param context
//	 * @return
//	 */
//	public static String getChannelDispalyName(Context context)
//	{
//
//		if(context instanceof ContentContext)
//		{
//			ContentContext contentContext = (ContentContext)context;
//
//			return contentContext.getChannel().getDisplayName();
//		}
//		else if(context instanceof com.frameworkset.platform.cms.driver.context.PageContext)
//		{
//			return "";
//		}
//		else if(context instanceof CMSContext)
//		{
//			return "";
//		}
//		else if(context instanceof ChannelContext)
//		{
//			
//			return ((ChannelContext)context).getChannel().getDisplayName();
//			
//		}
//		else
//		{
//			return "";
//		}
//			
//	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Override
	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ListInfo getDataList(String sortKey, boolean desc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		this.params = null;
		
		super.finalize();
	}
	
	
	

	
	
}
