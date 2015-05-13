package com.frameworkset.common.tag.html;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.frameworkset.common.tag.pager.tags.PagerDataSet;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.bean.DocRelated;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.dataloader.CMSDataLoaderManager;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.rssmanager.RssManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;


/**
 * 内容管理概览标签
 * <p>Title: CMSListTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-11 20:49:42
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSListTag extends PagerDataSet {
	protected Context context = null;
	protected CMSServletRequest cmsrequest;
	protected CMSServletResponse cmsresponse;
	protected String datatype = "default";
	protected String document;
	/**
	 * json参数串，将被解析为json数据
	 * { docId:true, docname:"aaaa"}
	 */
	protected String config;
	/**
	 * 指定文档类型
	 */
	protected String docType;
	
	
	
	/**
	 * 频道显示名称字段
	 */
	protected String channel;
	
	/**
	 * 文档显示的条目数
	 */
	private int count;
	
	/**
	 * more链接信息封装类
	 */
	private CMSMoreTag more;
	
	private CMSIndexTag indexTag;
	
	private RssTag rssTag;	
	
	/**
	 * 判断当前的概览是否需要生成rss种子
	 * 缺省为false，不生成
	 * true，生成
	 */
	private boolean rss = false;
	
	/**
	 * rss种子生成的路径
	 */
	private String rssPath;
	
	/**
	 * 一个首页上放置多个站点时，必需指定当前站点，动态页面上可以指定
	 */
	protected String site;

	
	public void setPageContext(PageContext pageContext)
	{
		org.apache.log4j.ConsoleAppender s;
		super.setPageContext(pageContext);
		cmsrequest = InternalImplConverter.getInternalRequest(this.request);
		cmsresponse = InternalImplConverter.getInternalResponse(this.response);
		if(cmsrequest != null)
		{
			this.context = (Context)this.cmsrequest.getContext();
		}
		
		if(context == null)
		{
			this.context = new DefaultContextImpl(request,response);
			
		}
		
	}
	
	public String getChannel() {
		
		return channel;
	}

	public void setChannel(String channel) {		
		this.channel = channel;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public CMSMoreTag getMoreTag() {
		return more;
	}

	public void setMoreTag(CMSMoreTag more) {
		this.more = more;
	}

	/**
	 * 判断是否已经设置了more标签
	 * @return
	 */
	public boolean moreSeted() {
		
		return this.more != null;
	}
	
	/**
	 * 判断是否已经设置了more标签
	 * @return
	 */
	public boolean indextagSeted() {
		
		return this.indexTag != null;
	}
	
	
	/**
	 * 在发布关系表中记录文档和相关文档的关系,目前只处理相关文档，不处理相关频道
	 * 
	 * @param context 
	 * @param documentid 正在发布的文档id
	 */
	private void recordRelatedDocumentRecursivePubObj(Context context,int documentid)
	{
		try {
			/**
			 * 调用文档管理器中获取文档的相关文档的接口，必须保证在发布对象关系中建立该文档与其所有相关文档的关系
			 * 否则在文档在已发状态的情况下，无法保证其相关文档发生变化后（比如撤发、回收、归档、删除、回收重发、归档重发）做到该文档的
			 * 联动发布 
			 */
			List relateddocs = CMSUtil.getCMSDriverConfiguration()
			.getCMSService().getDocumentManager().getDocRelatedList(documentid);
			
			for(int i = 0; relateddocs != null && i < relateddocs.size(); i ++)
			{
				Object object = relateddocs.get(i);
				if(object instanceof DocRelated) 
				{
					DocRelated doc = (DocRelated)object;
					CMSUtil.getCMSDriverConfiguration()
					.getCMSService()						
					.getRecursivePublishManager()
						.recordRecursivePubObj(context,doc.getRelatedDocId() + "",
								RecursivePublishManager.REFOBJECTTYPE_DOCUMENT,context.getSite().getSecondName());
				}
				else if(object instanceof Channel)
				{
					
				}
			}
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void recordkeywordRelatedDocumentRecursivePubObj(Context context) {
		try {
			/**
			 * 调用文档管理器中获取文档的相关文档的接口，必须保证在发布对象关系中建立该文档与其所有相关文档的关系
			 * 否则在文档在已发状态的情况下，无法保证其相关文档发生变化后（比如撤发、回收、归档、删除、回收重发、归档重发）做到该文档的
			 * 联动发布 
			 */
			
			
			for(int i = 0; i < this.size(); i ++)
			{	
				CMSUtil.getCMSDriverConfiguration()
				.getCMSService()						
				.getRecursivePublishManager()
					.recordRecursivePubObj(context,this.getString(i,"document_id"),
							RecursivePublishManager.REFOBJECTTYPE_DOCUMENT,context.getSite().getSecondName());
				
			}
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * 记录概览标签对应的频道的二级频道与发布对象之间的关系
	 * 
	 * @param context
	 * @param channel_ 
	 */
	private void recordSonChannelsRecursivePubObj(Context context,String channel_)
	{
		
		try{
			Channel  channel_t = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(channel_);
			if(channel_t == null)
			{
				System.out.println("频道[" + channel_ + "]不存在");
				return ;
			}
			List subchannels = channel_t.getSubchnls();
			for(int i = 0; subchannels != null && i < subchannels.size(); i ++)
			{
				Channel sub = (Channel)subchannels.get(i);
				CMSUtil.getCMSDriverConfiguration()
				.getCMSService()						
				.getRecursivePublishManager()
					.recordRecursivePubObj(context,sub.getChannelId() + "",
										RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR,
										context.getSite().getSecondName());
			}
		} catch (DriverConfigurationException e) {
			
			e.printStackTrace();
		} catch (RecursivePublishException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected Map<String,Object> params;
	public Map<String,Object> getParams()
	{
		
		return params;
	}
	public int doStartTag() throws JspException
	{   
		if(params == null)
		{
		
			if(!StringUtil.isEmpty(config))
			{
				params = StringUtil.json2Object(config,HashMap.class);
			}
		}
		int ret = -2;//这个标记是为了确保调用super.doStartTag();
		
		//处理缺省上下文发布上下文，将概览列表的站点英文名属性设置到上下文中
		if(context instanceof DefaultContextImpl)
		{
			((DefaultContextImpl)context).setSite(site);
			/*获取站点ID*/
//			siteid = ((DefaultContextImpl)context).getSiteID();
		}
		
		
		
		/**
		 * 概览标签需要处理文档与频道、站点、文档之间的引用关系
		 * 1.如果datatype对应的数据加载器定义为需要递归发布的加载器，那么当加载器对应的数据发生变化时需要执行联动发布，因此需要记录
		 *   频道与当前发布对象的关系
		 *   
		 * 2.如果发布上下文是contentcontext，并且datatype为related时，相关文档即为refobject,引用对象的类型为refdocument
		 *   当前发布的文档即为发布对象，发布对象类型为pubdocument
		 *   relate类型的数据加载器只能在文档细览模板中使用
		 *   
		 * 3.如果当前发布的概览的datatype为reference时，
		 *   这种类型的数据加载器加载指定记录的引用对象为每条文档对应的频道，
		 *   这些频道的获取方法为通过当前指定（或者上下文中）的频道名称获取该频道的下级频道，
		 *   如果下级频道中的文档发生变化则递归发布与频道相关的页面
		 *   
		 * 
		 * 对于channel属性的值的获取规则如下：
		 * 如果没有指定频道,从上下文中获取缺省的上下文:
		 * 1.当前是在发布频道，对应的频道就是正在发布的频道名称
		 * 2.当前发布的是文档，就是文档所属的频道
		 * 3.其他的情况如果没有指定频道，则为非法情况 
		 * 
		 * 
		 * 概览标签跨站点引用规则如下：
		 * 1.必须指定站点site属性，值为站点的英文名称
		 * 2.必须指定频道channel属性，值为站点的属性名称
		 */
		
		/**
		 * 文档相关文档
		 */
		if(this.channel == null ) 
		{
			if(this.context != null) 
			{
				//频道为空的情况只有两种：发布频道首页，发布文档
				if(context instanceof ChannelContext)
				{
					
					this.channel = ((ChannelContext)context).getChannel().getDisplayName();
					ret = super.doStartTag();
					if(CMSDataLoaderManager.getInstance().isRecursive(getDatatype()))
					{
						if(!datatype.equals("reference"))
						{
							try { 
								CMSUtil.getCMSDriverConfiguration()
								.getCMSService()						
								.getRecursivePublishManager()
									.recordRecursivePubObj(context,((ChannelContext)context).getChannel().getChannelId() + "",
														RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR,context.getSite().getSecondName());
							} catch (DriverConfigurationException e) {
								
								e.printStackTrace();
							} catch (RecursivePublishException e) {
								
								e.printStackTrace();
							}
						}
						else
						{
							recordSonChannelsRecursivePubObj(context,channel);
						}
					}
				}
				else if(context instanceof ContentContext)
				{
					this.channel = ((ContentContext)context).getChannel().getDisplayName();
					ret = super.doStartTag();
					if(CMSDataLoaderManager.getInstance().isRecursive(getDatatype()))
					{
						if(datatype.equals("related"))
						{
							this.recordRelatedDocumentRecursivePubObj(context,((ContentContext)context).getDocument().getDocument_id());
						}
						else if(datatype.equals("reference"))
						{
							recordSonChannelsRecursivePubObj(context,channel);
						}
						else if(datatype.equals("keywordRelated"))
						{
							recordkeywordRelatedDocumentRecursivePubObj(context);
						}
						else
						{
							try {
								CMSUtil.getCMSDriverConfiguration()
								.getCMSService()						
								.getRecursivePublishManager()
									.recordRecursivePubObj(context,((ContentContext)context).getChannel().getChannelId() + "",
											RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR,context.getSite().getSecondName());
							} catch (DriverConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RecursivePublishException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
					}
					
				}		
				/*获取站点ID*/
//				siteid = context.getSiteID();
			}			
			
		}
		else
		{
            if(this.context != null)
			{
				try
				{
					Context old_ = context;
					String site_ = context.getSite().getSecondName();
					if(site != null && !site.equals("") && !site.equals(site_))
					{
//						old_ = this.context;
						this.context = new DefaultContextImpl(request,response);
						((DefaultContextImpl)context).setSite(site);
						((DefaultContextImpl)context).setOldContext(old_);						
					}
					
					ret = super.doStartTag();
					Channel channel_ = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(this.channel);					
					if(CMSDataLoaderManager.getInstance().isRecursive(getDatatype()))
					{
						if(old_ instanceof ContentContext)
						{
							if(!datatype.equals("related"))
							{
								if(!datatype.equals("reference"))
								{
									CMSUtil.getCMSDriverConfiguration()
									.getCMSService()						
									.getRecursivePublishManager()
										.recordRecursivePubObj(context,channel_.getChannelId() + "",
												RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR,
												context.getSite().getSecondName());
								}
								else
								{
									recordSonChannelsRecursivePubObj(context,channel);
								}
							}
							else
							{
								this.recordRelatedDocumentRecursivePubObj(context,((ContentContext)context).getDocument().getDocument_id());
							}
						}
						else if(old_ instanceof CMSContext 
								|| old_ instanceof ChannelContext 
								|| old_ instanceof com.frameworkset.platform.cms.driver.context.PageContext
								)
						{
							if(!datatype.equals("reference"))
							{
								CMSUtil.getCMSDriverConfiguration()
								.getCMSService()						
								.getRecursivePublishManager()
									.recordRecursivePubObj(context,channel_.getChannelId() + "",
										RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR,context.getSite().getSecondName());
							}
							else
							{
								recordSonChannelsRecursivePubObj(context,channel);
							}
							
						}
					}
					
				}
				catch(Exception e )
				{
					
				}	
				/*获取站点ID*/
//				siteid = context.getSiteID();
			}		
		}
		
		if(ret == -2) //如果之前没有调用super.doStartTag()，在此调用
			ret = super.doStartTag();
		
		/*
		 * RSS 种子生成 指定channel
		 * add by ge.tao 2007-07-02
		 */
		if(this.isRss())
		{
			try {
				//rss 种子存放的路径
				Channel currentChl = new ChannelManagerImpl().getChannelInfoByDisplayName(context.getSiteID(),this.channel);
				String tmp = CMSUtil.getChannelPublishTempPath(context,context.getSiteID(),currentChl);
				String channelPublishPath = CMSUtil.getPublishedChannelDir(context,currentChl);
				String tmpPath = tmp + "/" + currentChl.getChannelId()+".xml";
				String rssPath = channelPublishPath + "/" + currentChl.getChannelId()+".xml";
				File file = new File(tmp);
				if(!file.exists()){
					file.mkdirs();
				}
//				System.out.println("path--------------------------------"+tmpPath);
				/* 设置RSS路径 以便RSS标签 放置在概览标签里面时 能取到该路径 */
				this.setRssPath(rssPath);
				new RssManagerImpl().createRSS(this,context,currentChl,tmpPath);
			} catch (ChannelManagerException e) {  
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		/* end */
		return ret;
		
	}
	
	

//	public PagerContext getPagerContext()
//	{
//		return this.pagerContext;
//	}

	public int doEndTag() throws JspException
	{
		/**
		 * 如果设置了more标签，则在概览的标签的底部输出more链接
		 */
		if(this.moreSeted())
		{
			try {
				out.print(this.more.generatorMoreScript());
				//清理moreTag的参数
				this.more.clearParameter();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		if(this.rssSeted())
		{
			try {
				out.print(this.rssTag.generatorRssScript());
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		if(this.indextagSeted())
		{
			try {
				out.print(this.indexTag.generatorIndexScript());
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		int ret = super.doEndTag();
		
		
		return ret;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Context getContext() {
		// TODO Auto-generated method stub
		return this.context;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public void setIndexTag(CMSIndexTag indexTag) {
		this.indexTag = indexTag;
	}

	public String getDocumentid() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public boolean isRss() {
		return rss;
	}

	public void setRss(boolean rss) {
		this.rss = rss;
	}

	public boolean rssSeted() {
		
		return this.rssTag != null;
	}

	public void setRssTag(RssTag tag) {
		
		this.rssTag = tag;
	}

	public String getRssPath() {
		return rssPath;
	}

	public void setRssPath(String rssPath) {
		this.rssPath = rssPath;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	@Override
	public void doFinally() {
		this.document = null;
		this.channel = null;
		datatype = "default";
		this.count = 0;
		this.site = null;
		this.more = null;
		this.indexTag = null;
		this.context = null;
		this.docType = null;
		this.rssTag = null;
		this.config = null;
		this.params = null;
		super.doFinally();
	}
}
