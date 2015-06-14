package com.frameworkset.platform.cms.driver.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;
import org.htmlparser.util.ParserException;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.context.impl.PagineContextImpl;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.jsp.JspFile;
import com.frameworkset.platform.cms.driver.jsp.JspFile.Cache;
import com.frameworkset.platform.cms.driver.jsp.JspFile.FileTimestamp;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.driver.publish.impl.ScriptletUtil;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.util.VelocityUtil;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.publish.Scriptlet.java</p>
 *
 * <p>Description: 构造模版片段,通过模版片段生成临时的jsp页面，
 * 				   然后运行，对于运行结果可以是jsp页面形式存放，也可以是
 * 				   
 * 				   
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-11
 * @author biaoping.yin
 * @version 1.0
 */
public class Scriptlet {
	/**
	 * jsp定义头部
	 */
	public static final String[] JSP_HEADER = new String[]{"<%@ page language=\"java\" contentType=\"",
															"; charset=",
															"\"%>\r\n<%@ taglib uri=\"/WEB-INF/pager-taglib.tld\" prefix=\"cms\"%>"};
	public static final String SECURITY_HEADER = "<%@page import=\"com.frameworkset.platform.security.AccessControl\"%><%"
    +"AccessControl accesscontroler = AccessControl.getInstance();"+"accesscontroler.checkAccess(request,response);" + "%>";
	public static final String NO_SECURITY_HEADER = "<%@page import=\"com.frameworkset.platform.security.AccessControl\"%><%"
	    +"AccessControl accesscontroler = AccessControl.getInstance();"+"accesscontroler.checkAccess(request,response,false);" + "%>";
	/**
	 * List<GrammarToken> tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
	 * 模板中可以通过#include(head.html)导入一些通用的脚本块
	 */
	public static final String SCRIPT_TPL_DEFINE_PRE ="#include(";
	public static final char SCRIPT_TPL_DEFINE_END =')';
	
	
	
	
	private JspFile jspFile; 
	
	private Context context;
	
//	private String output;
	
	private com.frameworkset.platform.cms.container.Template template;
	
	
	/*************************************************************************************
	 * 以下3个变量用来存放模板、普通页面、相关的link信息
	 *************************************************************************************/
	
	 /**
     * 记录模版原始得链接表，以便分发发布结果时分发模版得附件
     * 内部得文件需要分发，外部得链接附件无需分发
     */
    private CMSTemplateLinkTable origineTemplateLinkTable;
    
	/**
	 * 存放待发布的静态页面地址，已经处理过的静态页面无需再次处理
	 */
	private CmsLinkTable origineStaticPageLinkTable;
	
	/**
	 * 存放待发布的动态页面地址，已经处理过的动态页面无需再次处理
	 */
	private CmsLinkTable origineDynamicPageLinkTable;
	

	/**
	 * 避免多次分析同一个模板文件，第一次分析时设置为true，后续判断本变量是否为true，如果为true时，initFile()方法直接返回。
	 */
	boolean inited = false;
	/**
	 * 初始化页面脚本和文件脚本对应的文件
	 */
	private void initFile()
	{
		if(inited )
			return;
		inited = true;
		if(!(context instanceof PageContext))/*初始化模版脚本文件*/
		{
			try { 
				if(context.getTempFileName() == null)
				{
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
					this.context.getPublishMonitor().addFailedMessage(" Not set  template for document or channel or site. publish context is "+context  , new Date(),
																this.context.getPublisher());
					return;
				}
//				String jspFileName = CMSUtil.getJspFileName(context.getTempFileName()); //如果模板文件名称为空，则说明相应的模板不存在，直接跳转倒异常处理块
				String jspFileName = context.getJspFileName(); //如果模板文件名称为空，则说明相应的模板不存在，直接跳转倒异常处理块
				ContentContext contentctx = null;
				if(context instanceof ContentContext)
					contentctx = (ContentContext)context;
				
				/**
				 * 如果是动态发布，则在直接产生发布临时文件
				 */
				if (contentctx != null && (context.getPublishMode() == PublishMode.MODE_DYNAMIC_PROTECTED || 
						context.getPublishMode() == PublishMode.MODE_DYNAMIC_UNPROTECTED)) {
					String path = CMSUtil.getPath(context.getSitePublishTemppath() ,context.getPublishPath());
//					jspFile = new JspFile(path,
//							  jspFileName,
//							  CMSUtil.getPath(context.getAbsoluteProjectPath(),jspFileName),
//							  context.getPublishAbsolutePath(),
//							  template);
					jspFile = new JspFile(path,
							  jspFileName,
							  CMSUtil.getPath(context.getAbsoluteProjectPath(),jspFileName),
							  context.getPublishAbsolutePath(),true);
				}
				else
				{
//					jspFile = new JspFile(context.getRealProjectPath(),
//							  jspFileName,
//							  CMSUtil.getPath(context.getAbsoluteProjectPath(),jspFileName),
//							  context.getPublishAbsolutePath(),
//							  template);
					jspFile = new JspFile(context.getRealProjectPath(),
							  jspFileName,
							  CMSUtil.getPath(context.getAbsoluteProjectPath(),jspFileName),
							  context.getPublishAbsolutePath(),
							  true);
				}
			
			/*
			 * 
			 */
			
//				try { move to line 98
				
//				jspFile.setTemplateAttachementPath(CMSUtil.getAppRootPath() 
//						+ context.getAbsoluteTemplateRootPath());
				jspFile.setTemplateAttachementPath(CMSUtil.getAppRootPath() 
						+ context.getAbsoluteTemplateRootPath(),this.template);
				/*
				 * 判断同一频道下文档细览模版临时文件是否已经生成
				 * 如果已经生成则直接返回，否则创建临时文件
				 */
				if(contentctx != null)
				{
					jspFile.setDocAttachementPath(context.getRealProjectPath());
					//注销 因为178-183行的代码已经被注释
//					if(contentctx.hasDetailTemplate() && 
//							context.getPublishMonitor()
//								   .containTempFileOfPublishObject(contentctx.getDetailTemplate().getTemplateId() + "",
//										   							"channeldetail:" + contentctx.getChannelid(),false))
						return;
				}
				
				/**
				 * 这里需要进行特殊处理，看看怎么加锁，消除同步处理存在的问题
				 * ,这段代码不能放在这里，否则将导致系统不正常的行为，因此注销掉，2009.01.15
				 */
//				if(jspFile.exists())
//					jspFile.delete();
//				File dir = jspFile.getParentFile();
//				if(!dir.exists())			
//					dir.mkdirs();
//				jspFile.createNewFile();
			}
			catch(Exception e)
			{
				context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
				if(jspFile != null)
				{
					context.getPublishMonitor().addFailedMessage("模版初始化失败：创建发布文件[" + jspFile.getAbsolutePath() + "]失败。",
																new Date(),
																this.context.getPublisher());
				}
				else
				{
					context.getPublishMonitor().addFailedMessage("模版初始化失败[" + context + "]：发布文件不存在，可能相应的模板不存在。",
							new Date(),
							this.context.getPublisher());
				}
				
//				jspFile.setStatus(JspFile.SCRIPT_INITFAILED);
				e.printStackTrace();
			}
		}
		else/*初始化普通页面脚本文件*/
		{
			PageContext pageContext = (PageContext)context;
			this.jspFile = new JspFile(pageContext.getRealTemplateRootPath(),
					   pageContext.getPagePath(),
					   
					   pageContext.getPageType());
			
			jspFile.setTemplateAttachementPath(pageContext.getRealTemplateRootPath());
			if(!jspFile.exists())
			{
				context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATEFILENOEXIST);
				context.getPublishMonitor().addFailedMessage("模版初始化失败：模版文件[" + jspFile.getAbsolutePath() + "]不存在。",
						new Date(),
						this.context.getPublisher());
			}
			
		}
	}
	
	
	private StringBuffer content; 
	
	public StringBuffer getContent()
	{
		return content;
	}
	
	public void setContent(StringBuffer content) {
		this.content = content;		
	}
	
	
	
	public void addJspHeader(Writer fileWriter)
	{
		Template template = VelocityUtil.getTemplate("publish/jsp_header.vm");
		bboss.org.apache.velocity.context.Context vcontext = new VelocityContext();
		vcontext.put("mimetype",context.getMimeType());
		vcontext.put("charset",context.getCharset());
		try {
			template.merge(vcontext,fileWriter);
		} catch (ResourceNotFoundException e) {
			
			e.printStackTrace();
		} catch (ParseErrorException e) {
			
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void addSecurityHeader(Writer fileWriter)
	{
//		Template template = VelocityUtil.getTemplate("publish/security_header.vm");
//		org.apache.velocity.context.Context vcontext = new VelocityContext();
////		vcontext.put("mimetype",context.getMimetype());
////		vcontext.put("charset",context.getCharset());
//		try {
//			template.merge(vcontext,fileWriter);
//		} catch (ResourceNotFoundException e) {
//			
//			e.printStackTrace();
//		} catch (ParseErrorException e) {
//			
//			e.printStackTrace();
//		} catch (MethodInvocationException e) {
//			
//			e.printStackTrace();
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
		
	}
	
	private String replaceTPLMacro(String content,String templatePath)
	{
		 List<GrammarToken> tokens = TextGrammarParser.parser(content, SCRIPT_TPL_DEFINE_PRE, SCRIPT_TPL_DEFINE_END);
		 return content;
		 
	}
	public void addJspBody(Writer fileWriter,String header,
												 String pre,
												 String content,
												 String after)
	{
		Template template = VelocityUtil.getTemplate("publish/jsp_generator.vm");
		bboss.org.apache.velocity.context.Context vcontext = this.buildVelocityContext( header,pre ,content,after);
		try {
			template.merge(vcontext,fileWriter);
		} catch (ResourceNotFoundException e) {
			
			e.printStackTrace();
		} catch (ParseErrorException e) {
			
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
//	public void addJspBody(FileWriter fileWriter,Reader reader)
//	{
////		this.prehandle(fileWriter);
////		Template template = VelocityUtil.getTemplate("publish/jsp_generator.vm");
////		org.apache.velocity.context.Context vcontext = this.buildVelocityContext();
////		try {
////			template.merge(vcontext,fileWriter);
////		} catch (ResourceNotFoundException e) {
////			
////			e.printStackTrace();
////		} catch (ParseErrorException e) {
////			
////			e.printStackTrace();
////		} catch (MethodInvocationException e) {
////			
////			e.printStackTrace();
////		} catch (Exception e) {
////			
////			e.printStackTrace();
////		}
//	}
	
	private boolean needAction()
	{
		if(context instanceof ContentContext)
		{
			if(!((ContentContext)context).hasDetailTemplate() 
					&& context.getPublishMode() != PublishMode.MODE_NO_ACTION 
					&& context.getPublishMode() != PublishMode.MODE_ONLY_ATTACHMENT)
			{
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
				this.context.getPublishMonitor().addFailedMessage("文档细览模版未设置",new Date(),context.getPublisher());
				
				return false;
			}
			
		}
		if(context instanceof ChannelContext)
		{
			ChannelContext _context = (ChannelContext)context;
			if(_context.isTemplateType())
			{
				if(!_context.haveOutlineTemplate() && context.getPublishMode() != PublishMode.MODE_NO_ACTION 
						&& context.getPublishMode() != PublishMode.MODE_ONLY_ATTACHMENT)
				{
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
					this.context.getPublishMonitor().addFailedMessage("频道概览模板未设置",new Date(),context.getPublisher());
					return false;
				}
			}
			else if(_context.isCustomPageType() || _context.isDocDetailPageType())
			{
				if((_context.getChannel().getIndexpagepath() == null 
						|| !_context.getChannel().getIndexpagepath().equals(""))
						& context.getPublishMode() != PublishMode.MODE_NO_ACTION 
						&& context.getPublishMode() != PublishMode.MODE_ONLY_ATTACHMENT
						)
				{
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
					this.context.getPublishMonitor().addFailedMessage("频道首页未设置",new Date(),context.getPublisher());
					return false;
				}
			}
				
			
			
			
		}
		if(context instanceof CMSContext)
		{
			if(!((CMSContext)context).haveIndexTemplate() && context.getPublishMode() != PublishMode.MODE_NO_ACTION 
					&& context.getPublishMode() != PublishMode.MODE_ONLY_ATTACHMENT)
			{
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
				this.context.getPublishMonitor().addFailedMessage("站点首页未设置",new Date(),context.getPublisher());
				
				return false;
			}
			
		}
		
		if(context instanceof PageContext)
		{
			return !this.context.getPublishMonitor().isTemplateFileNoexist();
		}
		
//		if(!this.context.getPublishMonitor().isScriptInited())
//			return false;
		
		return true;
	}
	/**
	 * 根据脚本获取jsp文件
	 * @return JspFile
	 */
	private JspFile getFile()
	{
		if(this.inited )
			return this.jspFile;
		initFile();
		boolean a = !needAction();
		boolean b = this.context.getPublishMonitor().isScriptInitFailed() ;
		boolean c = this.context.getPublishMonitor().isTemplateFileNoexist();
		boolean d = this.context.getPublishMonitor().isTemplateNoexist();
		if(a || b || c || d)
		{	
			return jspFile;
		}
		
		if(!(context instanceof PageContext))
		{
			boolean flag = false;
			if(jspFile.exists())
			{
//				jspFile.delete();
				flag = true;
				
			}
			else
			{
				File dir = jspFile.getParentFile();
				if(!dir.exists())			
					dir.mkdirs();
				try {
					jspFile.createNewFile();
				} catch (IOException e) {
					context.getPublishMonitor().addFailedMessage("创建临时文件失败:" + jspFile.getAbsolutePath(),new Date(),context.getPublisher());
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATEFILENOEXIST);
					return this.jspFile;
				}
			}
			
			/*
			 * 判断同一频道下文档细览模版临时文件是否已经生成
			 * 如果已经生成则直接返回，否则将模版文件中的内容填充到临时文件中
			 */
			ContentContext contentctx = null;
			if(context instanceof ContentContext)
			{
				contentctx = (ContentContext)context;
				if(contentctx.hasDetailTemplate() && 
						context.getPublishMonitor()
							   .containTempFileOfPublishObject(contentctx.getDetailTemplate().getTemplateId() + "",
									   							"channeldetail:" + contentctx.getChannelid(),false))
				{
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
					return jspFile;
				}
			}
			
			

			Writer fileWriter = null;
//			Reader reader = null;
//			StringWriter swriter = null;
			long lastModified = -2l;
			try {
				
				
				switch(template.getPersistType())
				{
					/*数据库存储的模版处理*/
					case com.frameworkset.platform.cms.container.Template.PERSISTINDB:
						/**
						 * 判断临时文件是否已经存在，如果存在，就需要判断模板文件或者普通页面的时间戳是否发生了变化，如果发生了变化，则需要重新生成临时文件
						 * 否则不重新生成临时文件
						 */
						lastModified = template.getModifiedTime();
						
						if(flag) 
						{
							
							Cache cache = ScriptletUtil.getCache(jspFile,lastModified);
							if(cache != null)								
							{
								if(context instanceof PagineContextImpl)
								{
									((PagineContextImpl)context).setPagintion(cache.isPagintion()); 
								}
								this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
								break;								
							}
							
						}
//						fileWriter =  new FileWriter(this.jspFile);
						fileWriter = new OutputStreamWriter( 
									new FileOutputStream(jspFile),context.getCharset());
						this.addJspHeader(fileWriter);
						if(context.getPublishMode() == PublishMode.MODE_DYNAMIC_PROTECTED)
						{
							this.addSecurityHeader(fileWriter);
						}
						this.addJspBody(fileWriter,template.getHeader(),"",template.getText(),"");
						fileWriter.flush();
						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
						this.jspFile.setCache(new Cache());
						this.jspFile.setOrigineTemplateLinkTable(this.origineTemplateLinkTable);
						this.jspFile.setOrigineDynamicPageLinkTable(this.origineDynamicPageLinkTable);
						this.jspFile.setOrigineStaticPageLinkTable(this.origineStaticPageLinkTable);
						this.jspFile.getCache().setOldTemplateFileTimestamp(new FileTimestamp(lastModified));
						this.jspFile.setPagintion(jspFile._getPagintion());
						ScriptletUtil.addCache(jspFile);					
						break;
				
				    /*文件类型模版存储的处理*/
					case com.frameworkset.platform.cms.container.Template.PERSISTINFILE:
						String templateFile = CMSUtil.getPath(this.jspFile.getTemplateAttachementPath() ,this.template.getTemplateFileName());
						File template = new File(templateFile);
						if(!template.exists())
						{
							context.getPublishMonitor().addFailedMessage("没有找到模版文件:" + templateFile,new Date(),context.getPublisher());
							this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATEFILENOEXIST);
							return this.jspFile;
						}
//						long lastModified = -2l;
						lastModified = template.lastModified();
						if(flag) 
						{				
							
							Cache cache = ScriptletUtil.getCache(jspFile,lastModified);
							if(cache != null)
							{
								if(context instanceof PagineContextImpl)
								{
									((PagineContextImpl)context).setPagintion(cache.isPagintion()); 
								}
								this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
								break;
							}
						}
						
						
//							String templateFile = CMSUtil.getPath(this.jspFile.getTemplateAttachementPath() ,this.template.getTemplateFileName());
//							File template = new File(templateFile);
						
						fileWriter =  fileWriter = new OutputStreamWriter( 
								new FileOutputStream(jspFile),context.getCharset());
						addJspHeader(fileWriter);
						if(context.getPublishMode() == PublishMode.MODE_DYNAMIC_PROTECTED)
						{
							this.addSecurityHeader(fileWriter);
						}
						
						
//							reader =  new FileReader(templateFile);
//							VelocityContext fcontext = new VelocityContext();
//							swriter = new StringWriter();
						String templatecontent = FileUtil.getFileContent(template,context.getCharset()); 
//							Velocity.evaluate(fcontext,swriter,"",reader);
//							this.template.setText(templatecontent);
////							this.template.setTextReader(reader);
//							this.template.setHeader("");
						this.addJspBody(fileWriter,"","",templatecontent,"");
						fileWriter.flush();
						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
						this.jspFile.setCache(new Cache());
						this.jspFile.setOrigineTemplateLinkTable(this.origineTemplateLinkTable);
						this.jspFile.setOrigineDynamicPageLinkTable(this.origineDynamicPageLinkTable);
						this.jspFile.setOrigineStaticPageLinkTable(this.origineStaticPageLinkTable);
						this.jspFile.getCache().setOldTemplateFileTimestamp(new FileTimestamp(lastModified));
						this.jspFile.setPagintion(jspFile._getPagintion());
						ScriptletUtil.addCache(jspFile);
						
//						fileWriter =  new FileWriter(this.jspFile);
//						addJspHeader(fileWriter);
//						if(context.getPublishMode() == PublishMode.MODE_DYNAMIC_PROTECTED)
//						{
//							this.addSecurityHeader(fileWriter);
//						}
//						String templateFile = CMSUtil.getPath(this.jspFile.getTemplateAttachementPath() ,this.template.getTemplateFileName());
//						File template = new File(templateFile);
//						if(!template.exists())
//						{
//							context.getPublishMonitor().addFailedMessage("没有找到模版文件:" + templateFile,new Date(),context.getPublisher());
//							this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATEFILENOEXIST);
//							return this.jspFile;
//						}
//						
//						reader =  new FileReader(templateFile);
//						VelocityContext fcontext = new VelocityContext();
//						swriter = new StringWriter();
//						Velocity.evaluate(fcontext,swriter,"",reader);
//						this.template.setText(swriter.toString());
//						this.template.setHeader("");
//						this.addJspBody(fileWriter);
//						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
//						this.jspFile.setOrigineTemplateLinkTable(this.origineTemplateLinkTable);
//						this.jspFile.setOrigineDynamicPageLinkTable(this.origineDynamicPageLinkTable);
//						this.jspFile.setOrigineStaticPageLinkTable(this.origineStaticPageLinkTable);
						break;
					default:
						
					
				}
//				this.jspFile.setOrigineTemplateLinkTable(this.origineTemplateLinkTable);
//				this.jspFile.setOrigineDynamicPageLinkTable(this.origineDynamicPageLinkTable);
//				this.jspFile.setOrigineStaticPageLinkTable(this.origineStaticPageLinkTable);
				
lable1:			if(contentctx != null)
				{
//					ContentContext contentctx = (ContentContext)context;
					//记录初始化模板好的模板临时jsp文件到监控器中，以避免多次在同一个频道下生成同一个细览模板临时jsp文件的
					context.getPublishMonitor()
						   .putTempFileOfPublishObject(contentctx.getDetailTemplate().getTemplateId() + "",
								   							"channeldetail:" + contentctx.getChannelid());
						
				}
				return jspFile;
			
			} catch (IOException e) {
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
				e.printStackTrace();
			} catch (Exception e) {
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
				e.printStackTrace();
			}
			finally
			{
				try {
					if(fileWriter != null)
						fileWriter.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				try {
//					if(reader != null)
//						reader.close();
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				try {
//					if(swriter != null)
//						swriter.close();
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
			}
		}
		else 
		{
			
			PageContext pagecontext = (PageContext)context;
			if(!jspFile.exists() || jspFile.isDirectory())
			{
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATEFILENOEXIST);
				return jspFile;
			}
			String endcoder = context.getSite().getEncoding();
			
			if(pagecontext.getPageType().equals(CMSLink.TYPE_STATIC_PAGE + ""))//静态页面
			{
//				String jspName = CMSUtil.getJspFileName(context.getTempFileName());
				String jspName = context.getJspFileName();
				JspFile t_jspFile = new JspFile(context.getRealProjectPath(),
//															CMSUtil.getPath(context.getRendPath() 
//																	,jspName),
												jspName,
												CMSUtil.getPath(context.getAbsoluteProjectPath(),jspName),
												pagecontext.getPageType());
				boolean flag = t_jspFile.exists();
				long lastModified = jspFile.lastModified();
				if(flag) //如果临时文件已经生成一次
				{
					//jspFile 对应于模板文件
					Cache cache = ScriptletUtil.getCache(t_jspFile, lastModified);
					if(cache != null)
					{
						this.jspFile = t_jspFile;
						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
						pagecontext.setPagintion(jspFile.isPagintion());
						
						if(jspFile.containJspTag())
							pagecontext.setNeedRecordRefObject(true);
						return jspFile;
					}
					
				}
				else
					/**
					 *	如果临时文件不存在，则可能有两种情况：
					 *  1.页面没有对应相应的临时文件，就是说是不包含标签的普通页面，不要做发布，只需拷贝和分析其中的附件即可
					 *  2.页面还没有被分析过，需要重新分析
					 */
				{
					Cache cache = ScriptletUtil.getCache(jspFile, lastModified);
					if(cache != null)
					{
						
						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
						pagecontext.setPagintion(jspFile.isPagintion());
//						if(jspFile.containJspTag())
//							pagecontext.setNeedRecordRefObject(true);
						return jspFile;
					}
					else
					{
						
					}
				}
				
				String pagePath = pagecontext.getPageDir();
				CmsLinkProcessor converter = new CmsLinkProcessor(this.context,
																  CmsLinkProcessor.REPLACE_LINKS,
																  endcoder,
																  pagePath);
				converter.setHandletype(CmsLinkProcessor.PROCESS_TEMPLATE);
				
				try {
					
					
					String output = converter.process(jspFile,endcoder);
					//设置文件是否包含jsp标签
					
					origineTemplateLinkTable = converter.getOrigineTemplateLinkTable();
					origineDynamicPageLinkTable = converter.getOrigineDynamicPageLinkTable();
					origineStaticPageLinkTable = converter.getOrigineStaticPageLinkTable();
					if(converter.containJspTag())
					{
						Writer fileWriter =  null;
						//context.getAbsoluteProjectPath()
						try {
//								String jspName = CMSUtil.getJspFileName(context.getTempFileName());
//								JspFile t_jspFile = new JspFile(context.getRealProjectPath(),
//	//															CMSUtil.getPath(context.getRendPath() 
//	//																	,jspName),
//																jspName,
//																CMSUtil.getPath(context.getAbsoluteProjectPath(),jspName),
//																pagecontext.getPageType());
//								if(t_jspFile.exists())
//									t_jspFile.delete();
							if(!flag)
							{
								File dir = t_jspFile.getParentFile();
								if(!dir.exists())			
									dir.mkdirs();
								t_jspFile.createNewFile();
							}
							//cache未初始化，导致containJspTag()方法报空指针（第一次分析页面时报nullpointexception）
							//将以下两行代码放到760行即可							
//							if(jspFile.containJspTag())
//								pagecontext.setNeedRecordRefObject(true);
							t_jspFile.setCache(new Cache());
							
							t_jspFile.setContainJspTag(converter.containJspTag());
							t_jspFile.setPagintion(!converter.isList());
							
							pagecontext.setPagintion(!converter.isList());
							t_jspFile.setTemplateAttachementPath(pagecontext.getRealTemplateRootPath());
							fileWriter = new OutputStreamWriter( 
									new FileOutputStream(t_jspFile),context.getCharset());
							addJspHeader(fileWriter);
							if(context.getPublishMode() == PublishMode.MODE_DYNAMIC_PROTECTED)
							{
								this.addSecurityHeader(fileWriter);
							}
							
							
							this.addJspBody(fileWriter,"","",output,"");
							fileWriter.flush();
							this.jspFile = t_jspFile;
//							this.jspFile.setCache(new Cache());
							//FIXME by biaoping.yin 2010-03-29
							if(jspFile.containJspTag())
                                pagecontext.setNeedRecordRefObject(true);
							jspFile.setOrigineTemplateLinkTable(origineTemplateLinkTable);
							jspFile.setOrigineDynamicPageLinkTable(origineDynamicPageLinkTable);
							jspFile.setOrigineStaticPageLinkTable(origineStaticPageLinkTable);
							jspFile.getCache().setOldTemplateFileTimestamp(new FileTimestamp(lastModified));
							this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
							ScriptletUtil.addCache(jspFile);
						}
						catch(Exception e)
						{
							this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
							this.context.getPublishMonitor().addFailedMessage("页面初始化失败：" + context + ",error message is " + e.getMessage(),new Date(),context.getPublisher());
							e.printStackTrace();
						}
						finally
						{
							try {
								if(fileWriter != null)
									fileWriter.close();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}						
					}
					else//不包含标签的普通页面，无需考虑分页的效果
					{
						jspFile.setCache(new Cache());
						jspFile.setOrigineTemplateLinkTable(origineTemplateLinkTable);
						jspFile.setOrigineDynamicPageLinkTable(origineDynamicPageLinkTable);
						jspFile.setOrigineStaticPageLinkTable(origineStaticPageLinkTable);
//						boolean ispage = true;
						StringBuffer temp = new StringBuffer(output); 
						this.setContent(temp);
						jspFile.getCache().setPageContent(temp);
						jspFile.getCache().setOldTemplateFileTimestamp(new FileTimestamp(lastModified));
						ScriptletUtil.addCache(jspFile);
						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);						
					}				
				
					return this.jspFile;
				} catch (ParserException e) {
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
					e.printStackTrace();
				}
				catch(Exception e)
				{
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
					e.printStackTrace();
				}
				
			}
			else
			{
				//动态页面暂时不做处理，后续再扩展
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);
			}
			
		}

		return jspFile;
	}
	
	private void writerString(Writer writer,String content)
	{
		if(content == null || content.equals(""))
		{
			return ;
		}
		StringReader reader = new StringReader(content);
		this.writerString(writer, reader);
		
	}
	
	private void writerString(Writer writer,Reader reader)
	{
		if(reader == null )
		{
			return ;
		}
		char[] datas = new char[1024];
		int i = 0;
		try {
//			this.buildVelocityContext();
			while((i = reader.read(datas)) > 0)
			{
				writer.write(datas,0,i);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	private bboss.org.apache.velocity.context.Context buildVelocityContext(String header,String pre,String content,String after)
	{
		bboss.org.apache.velocity.context.Context vcontext = new VelocityContext();
		String endcoder = context.getSite().getEncoding();
		
		if(!(this.context instanceof PageContext))
		{
			CmsLinkProcessor converter = new CmsLinkProcessor(this.context,CmsLinkProcessor.REPLACE_LINKS,
																endcoder,
																this.template.getTemplatePath());
			converter.setHandletype(CmsLinkProcessor.PROCESS_TEMPLATE);
			converter.setNoAutoCloseTags(CMSUtil.getNoAutoCloseTagList());
			try {
				if(header != null && !header.trim().equals(""))
				{
					vcontext.put("template_header",converter.process(header,endcoder));
				}
				else
				{
					vcontext.put("template_header","");
				}
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(pre != null && !pre.trim().equals(""))
				{
					vcontext.put("template_pre",converter.process(pre,endcoder));
				}
				else
				{
					vcontext.put("template_pre","");
				}
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(content != null && !content.trim().equals(""))
				{
					String text_ = converter.process(content,endcoder);
					vcontext.put("template_text",text_);
					/**
					 * 频道的概览发布时需要判断是否包含分页标签
					 */
					if(context instanceof ChannelContext)
					{
						((ChannelContext)context).setPagintion(!converter.isList());
						
					}
					this.jspFile._setPagintion(!converter.isList());
					/**
					 * 设置记录模板引用元素的标记为true
					 */
					context.setNeedRecordRefObject(true);
				}
				else
				{
					
					vcontext.put("template_text","");
//					/**
//					 * 频道的概览发布时需要判断是否包含分页标签
//					 */
//					if(context instanceof ChannelContext)
//					{
//						((ChannelContext)context).setPagintion(converter.isList());
//					}
					/**
					 * 设置记录模板引用元素的标记为true
					 */
					context.setNeedRecordRefObject(true);
				}
				
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(after != null && !after.trim().equals(""))
				{
					vcontext.put("template_after",converter.process(after,endcoder));
				}
				else
				{
					vcontext.put("template_after","");
				}
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			origineTemplateLinkTable = converter.getOrigineTemplateLinkTable();
			origineDynamicPageLinkTable = converter.getOrigineDynamicPageLinkTable();
			origineStaticPageLinkTable = converter.getOrigineStaticPageLinkTable();
		}
		else
		{
			
			
			vcontext.put("template_header","");
			vcontext.put("template_pre","");
			vcontext.put("template_text",content);
			vcontext.put("template_after","");
			context.setNeedRecordRefObject(true);
			
		}
		
		return vcontext;
	}
	
	public String toString()
	{
//		Template template = VelocityUtil.getTemplate("publish/jsp_generator.vm");
//		org.apache.velocity.context.Context vcontext = buildVelocityContext();
//		StringWriter stringWriter =  new StringWriter();
//		try {
//			template.merge(vcontext,stringWriter);
//			return stringWriter.getBuffer().toString();
//		} catch (ResourceNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseErrorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MethodInvocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return "";
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public JspFile getJspFile() {
		/**
		 * 如果页面已经产生则直接返回jspFile,
		 * 如果jspFile为null，是否需要根据模板初始化jspFile
		 * 待考虑，目前为空的情况只有在发布的页面已经产生（只发布附件的，或者不执行发布动作时）
		 */
		if(this.context.getPublishMonitor().isPageGenerated())
			return jspFile;
		return jspFile == null?(jspFile = this.getFile()):jspFile;
	}

	public void setJspFile(JspFile jspFile) {
		this.jspFile = jspFile;
	}
	
	public static void main(String[] args)
	{
		File f = new File("d:/dd/dd.txt");
		File parent = f.getParentFile();
		parent.mkdirs();
		
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setTemplate(com.frameworkset.platform.cms.container.Template template) {
		this.template = template;
		
	}

//	public String getOutput() {
//		return output;
//	}
	
	
}
