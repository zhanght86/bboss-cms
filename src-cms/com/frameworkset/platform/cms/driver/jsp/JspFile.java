package com.frameworkset.platform.cms.driver.jsp;

import java.io.File;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.jsp.JspFile.java</p>
 *
 * <p>Description: 发布时候生成的模板jsp源文件文件</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-11
 * @author biaoping.yin
 * @version 1.0
 */
public class JspFile extends File {
	
	/**
	 * jsp的uri路径
	 */
	private String uri ;
	
	private boolean isPagintion = false;
	
//	private boolean containJspTag = false;
	
//	private boolean exist = false;
	
	
	

	
//	public static final int SCRIPT_TEMPLATE_EXIST = 0;
//	public static final int SCRIPT_TEMPLATENOEXIST = 1;
//	
//	public static final int SCRIPT_INITFAILED = 2;
//	public static final int SCRIPT_INITED = 3;
//	public static final int SCRIPT_UNKNOWN = -1;
//	
//	
//	private int status = SCRIPT_UNKNOWN;
	
	/**
	 * 文档附件的存放路径
	 */
	private String documentattachementPath;
	
	/**
	 * 模版附件存放路径
	 */
	private String templateAttachementPath;
	
	private String pageType = CMSLink.TYPE_TEMPLATE + "";
	
	
	/**
	 * 最终发布结果存放物理目录
	 */
	private String publishdir;
	
	private Cache cache;
	
//	private StringBuffer content; 
//	/**
//	 * 文件对应的模版
//	 */
//	private Template template;

	public JspFile(File parent, String child) {
		super(parent, child);
		this.cache = new Cache();
		
	}

	public JspFile(String pathname) {
		super(pathname);
		this.cache = new Cache();
		
	}
	
	/**
	 * jsp文件构建器，
	 * @param parent
	 * @param child
	 * @param renderuri
	 * @param publishdir
	 * @param flag boolean
	 */

	public JspFile(String parent, 
				   String child,
				   String uri,
				   String publishdir,
//				   ,String pageType
				   boolean flag
//				   ,
//				   Template template
				   ) {
		super(parent,child);
		this.uri = uri;
		this.publishdir = publishdir;
		this.cache = new Cache();
//		this.template = template;
	} 
	
	

	public JspFile(String pageRoot, String pagePath, String uri , String pageType) {
		super(pageRoot,pagePath);
		this.uri = uri;
		this.publishdir = CMSUtil.getDirectroy(pagePath);
		this.pageType = pageType;
		this.cache = new Cache();
	}
	
	public JspFile(String pageRoot, String pagePath,  String pageType) {
		super(pageRoot,pagePath);
		this.uri = pagePath;
		this.publishdir = CMSUtil.getDirectroy(pagePath);
		this.pageType = pageType;
		this.cache = new Cache();
	}
	
	/**
	 * 对于需要进行中间动态发布的普通jsp需要进行重新发布处理
	 * @param file
	 * @param jspName
	 * @param pageType
	 */
	public JspFile(JspFile originePage, String jspName, String pageType) {
		super(originePage.getParent(),jspName);
		this.uri = originePage.getPublishdir() + "/" + jspName;
		this.publishdir = originePage.getPublishdir();
		this.pageType = pageType;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3680567570569033068L;

	public String getUri() {
		return uri;
	}

//	public void setContent(StringBuffer content) {
//		this.content = content;		
//	}
//	
//	
//	
//	public String getContent()
//	{
//		return this.content.toString();
//	}
	

	/**
	 * 获取文档附件的物理存放路径
	 * @return
	 */
	public String getDocAttachementPath() {
		return documentattachementPath;
	}
	
	/**
	 * 获取模版附件的存放路径
	 * @return
	 */
	public String getTemplateAttachementPath()
	{
		return templateAttachementPath;
	}
	
//	/**
//	 * 对于一般页面的发布，不存在template对象
//	 * @param templateAttachementPath
//	 */
//	public void setTemplateAttachementPath(String templateAttachementPath)
//	{
//		if(this.template != null)
//		{
//			if(this.template.getPersistType() == Template.PERSISTINDB)
//			{
//				this.templateAttachementPath = templateAttachementPath + "/tpl_" + template.getTemplateId() + "/template_files";
//			}
//			else if(this.template.getPersistType() == Template.PERSISTINFILE)
//			{
//				this.templateAttachementPath = templateAttachementPath + "/" + template.getTemplatePath() ;
//			}
//		}
//		else
//		{
//			this.templateAttachementPath = templateAttachementPath;
//		}
//	}
	
	/**
	 * 对于一般页面的发布，不存在template对象
	 * @param templateAttachementPath
	 */
	public void setTemplateAttachementPath(String templateAttachementPath,Template template)
	{
		if(template != null)
		{
			if(template.getPersistType() == Template.PERSISTINDB)
			{
				this.templateAttachementPath = templateAttachementPath + "/tpl_" + template.getTemplateId() + "/template_files";
			}
			else if(template.getPersistType() == Template.PERSISTINFILE)
			{
				this.templateAttachementPath = templateAttachementPath + "/" + template.getTemplatePath() ;
			}
		}
		else
		{
			this.templateAttachementPath = templateAttachementPath;
		}
	}
	
	public void setTemplateAttachementPath(String templateAttachementPath)
	{
		setTemplateAttachementPath(templateAttachementPath,null);
	}


	public void setDocAttachementPath(String attachementPath) {
		this.documentattachementPath = attachementPath  + "/content_files";
	}
	
	

	public String getPublishdir() {
		return publishdir;
	}
	
	
	public void setOrigineTemplateLinkTable(CMSTemplateLinkTable table) {
		
		this.cache.setOrigineTemplateLinkTable(table);
	}

	public CMSTemplateLinkTable getOrigineTemplateLinkTable() {
		return cache.getOrigineTemplateLinkTable();
	}

	public CmsLinkTable getOrigineDynamicPageLinkTable() {
		return cache.getOrigineDynamicPageLinkTable();
	}

	public void setOrigineDynamicPageLinkTable(
			CmsLinkTable origineDynamicPageLinkTable) {
		this.cache.setOrigineDynamicPageLinkTable( origineDynamicPageLinkTable);
	}

	public CmsLinkTable getOrigineStaticPageLinkTable() {
//		return origineStaticPageLinkTable;
		return this.cache.getOrigineStaticPageLinkTable();
	}

	public void setOrigineStaticPageLinkTable(
			CmsLinkTable origineStaticPageLinkTable) {
		this.cache.setOrigineStaticPageLinkTable(origineStaticPageLinkTable);
	}
	
	/**
	 * 判断文件中是否包含jsp标签
	 * @return
	 */
	public boolean containJspTag() {
		return this.cache.containJspTag();
	}

	/**
	 * 设置文件中是否包含jsp标签
	 * @param containJspTag
	 */
	public void setContainJspTag(boolean containJspTag) {
//		this.containJspTag = containJspTag;
		this.cache.setContainJspTag(containJspTag);
		
	}
	
	public Cache getCache()
	{
		return this.cache;
	}
	
	public void setCache(Cache cache)
	{
		this.cache = cache;
	}
	
//	public boolean isTemplateExist()
//	{
//		return this.status != this.SCRIPT_TEMPLATENOEXIST;
//	}

//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}

	/**
	 * 判断文件是否是二进制文件
	 */
	public boolean isBinaryFile()
	{
		return CMSUtil.isBinaryFile(this);
	}
	
	public boolean exists()
	{
		return super.exists();
		
	}
	
	
	
	/**
	 * 记录文件对应的模板文件时间戳，系统进行发布操作时，
	 * 检查是否需要重新生成临时发布文件的依据就是看模板文件的时间戳和临时文件的时间戳是否相同，
	 * 如果不同则需要重新生成临时发布文件
	 * @author Administrator
	 *
	 */
	public static class FileTimestamp implements java.io.Serializable
	{
		private long modifytime = -1l;

		public FileTimestamp(long lastModified) {
			this.modifytime = lastModified;
			
		}

		public long getModifytime() {
			return modifytime;
		}

		public void setModifytime(long modifytime) {
			this.modifytime = modifytime;
		}
		
		
	}

	/**
	 * 判断模板文件是否被修改过，检查上次发布是模板文件的时间戳和最新的模板文件时间戳是否一致
	 * @param templateFileTimestamp
	 * @return
	 */
	public boolean beenModified(FileTimestamp templateFileTimestamp) {
		if(this.cache.oldTemplateFileTimestamp == null || templateFileTimestamp == null)
			return false;
		return (this.cache.oldTemplateFileTimestamp.getModifytime() != templateFileTimestamp.getModifytime()); 
	}
	
	/**
	 * 判断发布之前数据库存储的模板是否被修改过，检查上次发布时的模板文件和模板当前修改时间是否一致
	 * @param templateFileTimestamp
	 * @return
	 */
	public boolean beenModified(long dbtemplateTimestamp) {
		if(this.cache.oldTemplateFileTimestamp == null )
			return true;
		return  (this.cache.oldTemplateFileTimestamp.getModifytime() != dbtemplateTimestamp); 
	}
//	private  boolean modified = false;
//	public void setModified(boolean modified) {
//		this.modified = modified;
//		
//	}
	
//	public boolean isModified()
//	{
//		return this.modified;
//	}
	
	
	public static class Cache
	{
		/**
		 * 模板文件时间戳，用来记录临时文件对应的模板文件上次发布时的时间戳，系统通过该时间戳来对比系统中模板文件的最新
		 * 时间戳，根据对比的结果，如果两个时间戳相同就不重新生成临时文件，否则重新生成。 
		 */
		private FileTimestamp oldTemplateFileTimestamp;
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
	}

    public boolean isPagintion()
    {
        return this.cache.isPagintion();
    }

    public void setPagintion(boolean isPagintion)
    {
        this.cache.setPagintion(isPagintion);
    }
    private boolean _pagintion = false;
    public void _setPagintion(boolean isPagintion)
    {
        this._pagintion = isPagintion;
    }
    
    public boolean _getPagintion()
    {
    	return this._pagintion;
    }
    
	
	
	
	
}
