package com.frameworkset.platform.cms.driver.dataloader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.frameworkset.platform.cms.driver.context.ContentContext;


/**
 * <p>Title: </p>
 *
 * <p>Description: 获取内容管理的数据，细览页面数据加载器</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class CMSDetailDataLoader implements java.io.Serializable {
	/**
	 * 缓冲内容管理系统数据
	 * Map<contentkey,List<Document>>
	 */
	private static Map contents = Collections.synchronizedMap(new HashMap());
	
	private Class dataType;
//	private static CMSDetailDataLoader instance;
	public CMSDetailDataLoader()
	{
		
	}
//	public static CMSDetailDataLoader getInstance()
//	{
//		if(instance == null)
//			instance = new CMSDetailDataLoader();
//		return instance;
//	}
	
	public Object getContent(ContentContext contentcontext) throws CMSDataLoadException
	{
		{
//			String contentkey = this.buildContentKey(contentcontext);
//			Object content = contents.get(contentkey);
//			if(content == null)
//			{
				Object content = this.doGetContent(contentcontext);
				if(content == null)
					throw new CMSDataLoadException("获取内容数据为Null：" + contentcontext);
//				contents.put(contentkey,content);
				dataType = content.getClass();
//			}
			return content;
			
			
		}
	}
	/**
	 * 
	 * @param contentcontext
	 * @return
	 * @throws CMSDataLoadException
	 */
	protected abstract Object doGetContent(ContentContext contentcontext) throws CMSDataLoadException;
	
	
	protected String buildContentKey(ContentContext contentcontext)
	{
		return contentcontext.getSiteID() + ":" + contentcontext.getContentid();

	}

}
