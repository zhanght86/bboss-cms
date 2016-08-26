package com.frameworkset.platform.cms.driver.jsp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;

public class Cache {

	public Cache() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 模板文件时间戳，用来记录临时文件对应的模板文件上次发布时的时间戳，系统通过该时间戳来对比系统中模板文件的最新
	 * 时间戳，根据对比的结果，如果两个时间戳相同就不重新生成临时文件，否则重新生成。 
	 */
	private FileTimestamp oldTemplateFileTimestamp;
	private Map<String,FileTimestamp> includeTPLTimestamps;
	private CMSTemplateLinkTable origineTemplateLinkTable;
	/**
	 * 存放待发布的静态页面地址，已经处理过的静态页面无需再次处理
	 */
	private CmsLinkTable origineStaticPageLinkTable;
	
	/**
	 * 存放待发布的动态页面地址，已经处理过的动态页面无需再次处理
	 */
	
	private CmsLinkTable origineDynamicPageLinkTable;
	
	private boolean isPagintion;
	
	/**
	 * 普通页面内数据
	 */
	private StringBuffer pageContent;

	public CMSTemplateLinkTable getOrigineTemplateLinkTable() {
		return origineTemplateLinkTable;
	}

	public void setOrigineTemplateLinkTable(CMSTemplateLinkTable origineTemplateLinkTable) {
		this.origineTemplateLinkTable = origineTemplateLinkTable;
	}

	public CmsLinkTable getOrigineStaticPageLinkTable() {
		return origineStaticPageLinkTable;
	}

	public void setOrigineStaticPageLinkTable(
			CmsLinkTable origineStaticPageLinkTable) {
		this.origineStaticPageLinkTable = origineStaticPageLinkTable;
	}

	public CmsLinkTable getOrigineDynamicPageLinkTable() {
		return origineDynamicPageLinkTable;
	}

	public void setOrigineDynamicPageLinkTable(
			CmsLinkTable origineDynamicPageLinkTable) {
		this.origineDynamicPageLinkTable = origineDynamicPageLinkTable;
	}
	private boolean containJspTag = false;
	/**
	 * 判断文件中是否包含jsp标签
	 * @return
	 */
	public boolean containJspTag() {
		return containJspTag;
	}

	/**
	 * 设置文件中是否包含jsp标签
	 * @param containJspTag
	 */
	public void setContainJspTag(boolean containJspTag) {
		this.containJspTag = containJspTag;
	}

	public StringBuffer getPageContent() {
		return pageContent;
	}

	public void setPageContent(StringBuffer pageContent) {
		this.pageContent = pageContent;
	}

	public FileTimestamp getOldTemplateFileTimestamp() {
		return oldTemplateFileTimestamp;
	}

	public void setOldTemplateFileTimestamp(FileTimestamp oldTemplateFileTimestamp) {
		this.oldTemplateFileTimestamp = oldTemplateFileTimestamp;
	}
	public boolean isPagintion()
    {
        return isPagintion;
    }

    public void setPagintion(boolean isPagintion)
    {
        this.isPagintion = isPagintion;
    }

	public void addTPL(String text, File temp, long m) {
		if(includeTPLTimestamps == null)
		{
			includeTPLTimestamps = new HashMap<String,FileTimestamp>();
			
		}
		
		FileTimestamp stamp = new FileTimestamp(m); 
		stamp.setFile(temp);
		try {
			synchronized(includeTPLTimestamps)
			{
				includeTPLTimestamps.put(temp.getCanonicalPath(), stamp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean beenModified(long dbtemplateTimestamp) {
		if(getOldTemplateFileTimestamp() == null )
			return true;
		boolean beemodify = getOldTemplateFileTimestamp().getModifytime() != dbtemplateTimestamp;
		if(beemodify)
			return true;
		if(this.includeTPLTimestamps == null || this.includeTPLTimestamps.size() == 0)
			return false;
		
		synchronized(includeTPLTimestamps)
		{
			
			Iterator<Entry<String, FileTimestamp>> fs = includeTPLTimestamps.entrySet().iterator();
		 
			while(fs.hasNext())
			{
				Entry<String, FileTimestamp> e = fs.next();
				FileTimestamp fileTimestamp = e.getValue();
				if(fileTimestamp.modified())
				{
					 
					beemodify =  true;
					break;
				}
				
				
			}
		}
		return beemodify;
	}

}
