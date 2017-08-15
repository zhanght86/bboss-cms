package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.CMSBaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.documentmanager.bean.DocAggregation;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.SimpleStringUtil;

/**
 * 
 * <p>
 * Title: SiteURLTag.java
 * </p>
 * 
 * <p>
 * Description:直接输出文档、频道、首页地址
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-7-16 上午10:16:55
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SiteURLTag extends CMSBaseCellTag {
	private static Logger log = LoggerFactory.getLogger(SiteURLTag.class);
	private String link;	
	

	private void doDefault() throws JspException
	{
		Object object = dataSet.getOrigineObject();
		String href = null;
		if(object instanceof Document)
		{
			/**
			 * 获取聚合文档标识
			 */
		    int aggregation = dataSet.getInt("doctype");
			if(aggregation != Document.DOCUMENT_CHANNEL)
			{
				
				if(aggregation != 3) //非聚合文档
				{
					
					/*
					 * 标签解析时，与之相关的路径有：
					 * 当前模版所在的路径－从context中获取,这里设置为"",因为内容管理系统中维护的图片地址都是基于_template目录的根开始的
					 * 当前发布的目的地－从context中获取
					 * 标签本身的链接 － 可从标签变量获取，如果没有设置标签变量则通过本频道当前的文档对应页面地址来获取
					 * 
					 */
					if(link == null)  
					{
						 href = this.getContentPath(context);
						
					}
					else
					{
						if(this.context != null)
						{
		//					String templatePath = getTemplatePath(context);
							String templatePath = "";
							href = (CMSTagUtil.getPublishedLinkPath(context,templatePath,link));
							
							
						}	
						else
						{
							
							href = link;
						}
					}
//					out.print(href);
					
				}
				else //聚合文档处理
				{
//						if(this.dataSet.getInt("isNew") == 1 && !"".equals(this.dataSet.getString("newPicPath")))
//						{
//							IMG img = new IMG();
//							
//							String newPicPath = this.dataSet.getStringWithDefault("newPicPath","image/new.gif");
//							String src = CMSTagUtil.getPublishedLinkPath(context,"",newPicPath);
//							img.setSrc(src);
//							//out.print(linkA.toString() + img.toString());
//							outStr = "<b>" + outStr + img.toString() + "</b>&nbsp;" + getAggregationScript();
//						}
//						else
//						{
//							outStr = "<b>" + outStr + "</b>&nbsp;" + getAggregationScript();
//						}
//		//				if(this.maxlength > 0 && outStr != null && outStr.length() > maxlength)
//		//				{
//		//					outStr = outStr.substring(0,this.maxlength);
//		//					if(replace != null)
//		//						outStr += replace;
//		//				}
//						
//						out.print(outStr);
					href = getAggregationScript();
				}
					
				
			}
			else
			{
				Channel channel = (Channel)dataSet.getValue("refChannel");
				
				href = CMSUtil.getPublishedChannelPath(context,channel);
				String tagText = channel.getDisplayName();
				
//				out.print(href);
				
			}
		}
		else if(object instanceof Channel)
		{
			Channel channel = (Channel)object;
			
			 href = CMSUtil.getPublishedChannelPath(context,channel);
			
//			 out.print(href);
			
		}  
		try {
			out.print(href);
		} catch (IOException e) {
			throw new JspException("",e);
		}
	}
	
	private void doCustom() throws JspException
	{
		if(site != null)
    	{
    		if(channel == null)
    		{
	    		try {
					Site site_ = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					if (site_ != null)
					{			
						String siteid = site_.getSiteId() + "";
						String indexURL = site_.getIndexFileName();
						if(context != null)
							indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);//首页地址
						else
							indexURL = CMSTagUtil.getPublishedSitePath(siteid);
						out.print(indexURL);
					}
				} catch (JspException e) {
					 
					throw e;
					
				} catch (IOException e) {
					 
					throw new JspException("",e);
				} catch (Exception e) {
					 
					throw new JspException("",e);
				}
    		}
    		else
    		{
    			try {
    				
					Channel channel_ = CMSUtil.getChannelCacheManagerBySiteName(site).getChannelByDisplayName(channel);
					String linkPath = CMSTagUtil.getPublishedChannelPath(context,channel_);
					out.print(linkPath);
				}  catch (JspException e) {
					 
					throw e;
				} catch (IOException e) {
					 
					throw new JspException("",e);
				} catch (Exception e) {
					 
					throw new JspException("",e);
				}
    		}
    	}
    	else 
    	{
    		try {
				Site site_ = context.getSite();
				Channel channel_ = CMSUtil.getChannelCacheManager(site_.getSiteId()+"").getChannelByDisplayName(channel);
				String linkPath = CMSTagUtil.getPublishedChannelPath(context,channel_);
				out.print(linkPath);
			} catch (JspException e) {
				
				 
				throw e;
			} catch (IOException e) {
				 
				throw new JspException("生成频道channel="+channel+"访问地址出错",e);
			} catch (Exception e) {
				 
				throw new JspException("生成频道channel="+channel+"访问地址出错",e);
			}
    	}
	}
	public int doStartTag() throws JspException {
		
		    super.doStartTag();    
		    if(site == null && this.channel == null)
		    {
		    	try
		    	{
		    		doDefault();
		    	}
		    	catch(Exception e)
		    	{
		    		log.error(e.getMessage(),e);
		    		if(context != null && context.getPublishMonitor() != null)
					{
						this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
								.append(context.getRendURI())
								.append("]出错,请检查站点或者站点频道或者文档是否存在!:<br>").append(SimpleStringUtil.formatBRException(e)).toString()+",请检查站点或者站点频道或者文档是否存在!",context.getPublisher());
					}
		    	}
		    }
		    else
		    {
		    	try
				{
			    	doCustom();
			    }
				catch(Exception e)
				{
					
					log.error(e.getMessage(),e);
		    		if(context != null && context.getPublishMonitor() != null)
					{
						this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
								.append(context.getRendURI())
								.append("]出错,请检查站点或者站点频道或者文档是否存在!:<br>").append(SimpleStringUtil.formatBRException(e)).toString(),context.getPublisher());
					}
				}
		    }
		
		return SKIP_BODY;
	}

	/**
	 * 获取聚合文档脚本
	 * @return
	 */
	protected String getAggregationScript()
	{
		StringBuffer str = new StringBuffer();
		DocumentManager dm = new DocumentManagerImpl();
		
		try {
	//		str.append(dataSet.getString("subtitle") + "&nbsp;");
			String docId = String.valueOf(dataSet.getInt("document_id"));
			
			List list = dm.getPubAggrDocList(docId);
			
		
			DocAggregation docAggregation;;
			
			for(int i =0;i<list.size();i++)
			{
				
				docAggregation = (DocAggregation)list.get(i);
			
				if(str.length() > 0)
				{
					str.append("|");
				}
				str.append(getContentPath(String.valueOf(docAggregation.getIdbyaggr()),docAggregation.getChlId()));
				
				
				
				
			}
		} catch (DocumentManagerException e) {
			e.printStackTrace();
		}
		
		return str.toString();
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public int doEndTag() throws JspException {
		
		return super.doEndTag();
	}

	@Override
	public void doFinally() {
		this.link = null;
		super.doFinally();
	}

}
