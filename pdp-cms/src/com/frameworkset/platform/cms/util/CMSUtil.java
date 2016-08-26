package com.frameworkset.platform.cms.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.codehaus.swizzle.stream.StringTemplate;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.ParamsHandler;
import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.util.ParamsHandler.Params;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.documentmanager.DocClassManager;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.documentmanager.bean.DocClass;
import com.frameworkset.platform.cms.driver.config.DriverConfiguration;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.ContextException;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.dataloader.CMSBaseListData;
import com.frameworkset.platform.cms.driver.dataloader.CMSDataLoadException;
import com.frameworkset.platform.cms.driver.dataloader.CMSDataLoaderManager;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsTagLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.LinkCache;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.publish.PublishEngine;
import com.frameworkset.platform.cms.driver.publish.PublishMode;
import com.frameworkset.platform.cms.driver.publish.impl.ScriptletUtil;
import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.util.RegexUtil;
import com.frameworkset.util.VelocityUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.util.CMSUtil.java</p>
 *
 * <p>Description: 内容管理系统的帮助器</p>
 *
 * <p>Copyright (c) 2006.10 </p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-2-8 16:42:57
 * @author biaoping.yin
 * @version 1.0 
 */
public class CMSUtil{
	private static final Logger log = Logger.getLogger(CMSUtil.class);

	private static final ListResourceBundle mimeTypes = new FileMIMETypes();

	private static final ListResourceBundle binaryTypes = new BinaryFileTypes();

	public static final String PUBLISHFILEFORDER = "content_files/";
	
	
	public static final String INDEX_ENGINE_TYPE_LUCENE = "LUCENE";
	public static final String INDEX_ENGINE_TYPE_HAILIANG = "HAILIANG";
	

	/**
	 * 分页文档的每页之间的分割符
	 */
	private static final String pagintionToken = "##########$$$$$$$$$$$";
	static
	{
		BaseApplicationContext.addShutdownHook(new Runnable(){
			
			@Override
			public void run() {
				
				destroy();
			}
			
		});
	}
	
	static void destroy()
	{
		SiteCacheManager.destroy();
		if(driverConfiguration != null)
		{
			driverConfiguration.getPublishEngine().clearTasks();
			driverConfiguration = null;
		}
		ScriptletUtil.destroy();
		
	}
	
	/**
	 * da.wei,200710171648
	 * 海量全文检索的删除文档索引（用空白html文档替换）
	 * @param docId,文档ID 
	 * @param siteId,站点ID
	 * */
	public static void deleteIndexForHL(String docId, String siteId){
		try{
			CMSSearchManager sm = new CMSSearchManager();
			String destination = ConfigManager.getInstance().getConfigValue("cms.indexs.hailiang.rootpath");
			if(!(destination == null || destination.equals("")))
			{
				String pathTail = sm.getPublishedFilePathTail(docId, siteId);//得到相对路径如：\site200\zjcz\content_33925.html
				String pathAll = CMSUtil.getPath(destination,pathTail);
				if(new File(pathAll).exists()){
					String blankFilePath = CMSUtil.getPath(destination,"blankFile.html");
					FileUtil.deleteFileOnly(blankFilePath);
					FileUtil.createNewFile(blankFilePath);
					FileUtil.fileCopy(blankFilePath,pathAll);
					FileUtil.deleteFileOnly(blankFilePath);
				}							
			}
		}catch(Exception e){}		
	}

	/**
	 * 获取分页文档分隔符
	 * @return
	 */
	public static String getPagintionToken() {
		return pagintionToken;
	}

	/**
	 * 获取站点得发布模式
	 * @return
	 */
	public static PublishMode getPublishMode(Integer publishMode)
			throws ContextException

	{
		if (publishMode == null)
			return PublishMode.MODE_STATIC_UNPROTECTED;
		int _mode = publishMode.intValue();
		switch (_mode) {
		case 0:
			return PublishMode.MODE_STATIC_UNPROTECTED;
		case 1:
			return PublishMode.MODE_STATIC_PROTECTED;
		case 2:
			return PublishMode.MODE_DYNAMIC_UNPROTECTED;
		case 3:
			return PublishMode.MODE_DYNAMIC_PROTECTED;
		case 4:
			return PublishMode.MODE_NO_ACTION;
		default:
			throw new ContextException("不支持的发布模式");
		}
	}

	public static String getJspFileName(String fileName,String templateid) {
		if(fileName == null)
			return null;
		if (fileName.toLowerCase().endsWith(".jsp"))
			return fileName;
		else {
			int idx = fileName.lastIndexOf(".");
			if (idx >= 0) {
				StringBuffer st = new StringBuffer();
				if(!StringUtil.isEmpty(templateid))
				{
					st.append(fileName.substring(0, idx)).append("_").append(templateid).append(".jsp");
				}
				else
				{
					st.append(fileName.substring(0, idx)).append(".jsp");
				}
				return st.toString();
			
			} else
			{
				StringBuffer st = new StringBuffer();
				if(!StringUtil.isEmpty(templateid))
				{
					st.append(fileName).append("_").append(templateid).append(".jsp");
				}
				else
				{
					st.append(fileName).append(".jsp");
				}
				
				return st.toString();
//				return fileName.concat(".").concat("jsp");
			}

		}
	}

	/**
	 * 内容管理系统驱动器，通过系统静态初时化
	 */
	private static DriverConfiguration driverConfiguration;
	static {

		String driverClass = ConfigManager
				.getInstance()
				.getConfigValue("cms.driverconfig",
						"com.frameworkset.platform.cms.driver.config.impl.DriverConfigurationImpl");
		try {
			String engineClass = ConfigManager
					.getInstance()
					.getConfigValue("cms.publish.engine",
							"com.frameworkset.platform.cms.driver.publish.impl.PublishEngineImpl");
			PublishEngine engine = (PublishEngine) Class.forName(engineClass)
					.newInstance();
			driverConfiguration = (DriverConfiguration) Class.forName(
					driverClass).getConstructor(
					new Class[] { PublishEngine.class }).newInstance(
					new Object[] { engine });

			//				driverConfiguration.setPublish

		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DriverConfiguration getCMSDriverConfiguration() {
		return driverConfiguration;
	}

	public static SiteCacheManager getSiteCacheManager() {
		return SiteCacheManager.getInstance();
	}

	/**
	 * 获取页面类型
	 * @param fileName
	 * @return
	 */
	public static String getMimeType(String fileExt) {
		try {
			return mimeTypes.getString(fileExt);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断文件是否是二进制文件
	 * @param file
	 * @return
	 */
	public static boolean isBinaryFile(File file) {
		try {
			return binaryTypes.getString(FileUtil.getFileExtByFileName(file
					.getName())) != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断文件名是否是二进制文件名称
	 * @param fileName
	 * @return
	 */
	public static boolean isBinaryFile(String fileName) {
		try {
			return binaryTypes.getString(FileUtil
					.getFileExtByFileName(fileName)) != null;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 识别地址是否是外部地址
	 * @param path
	 * @param request
	 * @return
	 */
	public static boolean isOtherDomain(String path,HttpServletRequest request)
	{
		String contextPath = request.getContextPath();
		String domainpattern = "(http|https|ftp|tps)://([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+/?.*";//http://bbs.sany.com.cn/forums/show/23.page
		return RegexUtil.isMatch(path,domainpattern) || path.startsWith(contextPath);
		
	}

	/**
	 * 获取网站的根目录
	 * @return
	 */
	public static String getWebSiteRootPath() {
		return ConfigManager.getInstance().getConfigValue("website.root.path",
				"/siteResource");
	}

	/**
	 * 获取网站的发布根目录
	 * @return
	 */
	public static String getWebPublishRootPath() {
		String publishRootPath = ConfigManager.getInstance().getConfigValue(
				"website.publish.path");
		return publishRootPath;
	}

	/**
	 * 获取网站的临时发布目录
	 * @return
	 */
	public static String getWebPublishTempPath() {
		return ConfigManager.getInstance().getConfigValue(
				"website.publish.temp.path");
	}

	public static String getWebRendRootPath() {
		// TODO Auto-generated method stub
		return ConfigManager.getInstance().getConfigValue(
				"website.render.root.path", "/${site}/_webrender");
	}

	public static String getWebRendRootPath(String sitedir) {
		String t_rendRootPath = CMSUtil.getWebRendRootPath();
		StringTemplate stringTemplate = new StringTemplate(t_rendRootPath);
		Map context = new HashMap();
		/**
		 * 站点的命名规则待定
		 */
		context.put("site", sitedir);
		String rendRootPath = stringTemplate.apply(context);
		// TODO Auto-generated method stub
		return rendRootPath;
	}

	public static String getWebProjectRootPath() {
		// TODO Auto-generated method stub
		return ConfigManager.getInstance().getConfigValue(
				"website.project.root.path", "/${site}/_webprj");
	}

	public static String getWebTemplateRootPath() {
		// TODO Auto-generated method stub
		return ConfigManager.getInstance().getConfigValue(
				"website.template.root.path", "/${site}/_template");
	}

	public static String getWebTemplateRootPath(String sitedir) {
		String t_templateRootPath = CMSUtil.getWebTemplateRootPath();
		StringTemplate stringTemplate = new StringTemplate(t_templateRootPath);
		Map context = new HashMap();
		/**
		 * 站点的命名规则待定
		 */
		context.put("site", sitedir);
		String templateRootPath = stringTemplate.apply(context);
		// TODO Auto-generated method stub
		return templateRootPath;
	}

	public static String getWebProjectRootPath(String sitedir) {
		String t_projectRootPath = CMSUtil.getWebProjectRootPath();
		StringTemplate stringTemplate = new StringTemplate(t_projectRootPath);
		Map context = new HashMap();
		/**
		 * 站点的命名规则待定
		 */
		context.put("site", sitedir);
		String projectRootPath = stringTemplate.apply(context);
		// TODO Auto-generated method stub
		return projectRootPath;
	}

	public static String getCMSContextPath() {
		return ConfigManager.getInstance().getConfigValue("cms.context.path",
				"/cms");
	}
	
	

	public static String getAppRootPath() {
		return ConfigManager.getInstance().getConfigValue("approot",
				"d:\\workspace\\cms\\creatorcms");
	}
	
	

	/**
	 * 获取mime类型文件的相应扩展名
	 * @param mimetype
	 * @return
	 */
	public static String getFileExt(String mimetype) {
		
		Enumeration keys = mimeTypes.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			String mimeType = mimeTypes.getString(key);
//			if (mimeType == null)
//				System.out.println("mimeType:" + mimeType);
//			if (mimetype == null)
//				System.out.println("mimetype:" + mimetype);

			if (mimetype.equals(mimeType))
			{
				if(key.equals(".png"))
					return "png";
				return key;
			}

		}
		return "";
		//		return mimeTypes.;
	}

	public static String getWebStyleRootPath(String siteDir) {
		String t_path = getWebStyleRootPath();
		StringTemplate stringTemplate = new StringTemplate(t_path);
		Map context = new HashMap();
		/**
		 * 站点的命名规则待定
		 */
		context.put("site", siteDir);
		String path = stringTemplate.apply(context);
		return path;
	}

	public static String getWebStyleRootPath() {
		// TODO Auto-generated method stub
		return ConfigManager.getInstance().getConfigValue(
				"website.style.root.path", "/${site}/style");
	}

	public static String getWebImagesRootPath() {
		// TODO Auto-generated method stub
		return ConfigManager.getInstance().getConfigValue(
				"website.images.root.path", "/${site}/images");
	}

	public static String getWebImagesRootPath(String siteDir) {
		String t_path = getWebImagesRootPath();
		StringTemplate stringTemplate = new StringTemplate(t_path);
		Map context = new HashMap();
		/**
		 * 站点的命名规则待定
		 */
		context.put("site", siteDir);
		String path = stringTemplate.apply(context);
		return path;
	}

	/**
	 * 获取站点的地址,相对于应用上下文的地址，不包含上下文，例如：
	 * cms/siteResource/site200
	 * @param m_target
	 * @return
	 */
	public static String getSitePath(String site) {
		// TODO Auto-generated method stub
		return CMSUtil.getCMSContextPath() + CMSUtil.getWebSiteRootPath() + "/"
				+ site;
	}
	
	

	/**
	 * 获取站点中uri的地址，包含contextpath，例如:/creatorcms/cms/siteResource/site200/_templates/chn1/a.html
	 * 
	 * @param site
	 * @param path
	 * @param targetUri
	 * @return
	 */
	public static String getSitePath(String site, String contextpath,
			String targetUri) {
		// TODO Auto-generated method stub
		return CMSUtil.getPath(contextpath + getSitePath(site), targetUri);
	}
	
	/**
	 * 通过站点id获取站点本地发布物理绝对地址，缺省为:d:/workspace/cms/creatorcms/sitepublish/site200
	 * 用户可自定义，本地发布路径
	 * @param siteid 站点id

	 * @return
	 */
	public static String getSitePubDestinction(String siteid) {
		try
		{
			Site site = CMSUtil.getSiteCacheManager().getSite(siteid);
			String path =  site.getLocalPublishPath();
			
			
			if(path!=null && !path.equals("")) 
			{
				
				return path;
			}
			else
			{
				String sitePublishRootPath = CMSUtil.getWebPublishRootPath() ;
				if(sitePublishRootPath == null || sitePublishRootPath.trim().length() != 0)
				{
					
					
					return CMSUtil.getPath(CMSUtil.getAppRootPath(),"sitepublish/" + site.getSiteDir());
					
				}
				else
					return sitePublishRootPath;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	
	}
	
	
	/**
	 * 通过站点id,频道id获取频道发布物理地址
	 * @param siteid 站点id

	 * @return
	 */
	public static String getChannelPubDestinction(String siteid,String channelid) {
		if(siteid.trim().length()==0 || channelid.trim().length()==0) return "";
		String sitedir = getSitePubDestinction(siteid);
		try
		{
			
			String channeldir = CMSUtil.getChannelCacheManager(siteid).getChannel(channelid).getChannelPath();
			
		
			return CMSUtil.getPath(sitedir,channeldir);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	
	}
	
	/**
	 * 通过站点id获取站点的物理路径,例如:d:/workspace/cms//creatorcms/cms/siteResource/site200/
	 * @param siteid 站点id

	 * @return
	 */
	public static String getSitePathById(Context context,String siteid) {
		
//		String sitepath = getSitePath("site" + siteid);
		String sitepath = getSitePath(context.getSite().getSecondName());
		
		
		return CMSUtil.getPath(CMSUtil.getAppRootPath(), sitepath);
	}
	
	
	
	
	
	/**
	 * 连接parent和subpath拼成新的路径
	 * @param parent
	 * @param subpath
	 * @return
	 */
	public static String getPath(String parent, String subpath) {
		if (subpath == null || subpath.trim().length() == 0) {
			return parent;
		} 

		if (parent == null || parent.equals(""))
			return subpath;

		boolean p = (parent.endsWith("/") || parent.endsWith("\\"));
		boolean s = (subpath.startsWith("/") || subpath.startsWith("\\"));

		if (s != p) {
			return parent + subpath;
		} else if (p == true && s == true) {
			return parent + subpath.substring(1);
		} else {
			return parent + "/" + subpath;
		}
		//		return parent + subpath;
	}
	
	public static LinkCache getLinkCache(String siteName)
	{
		return SiteCacheManager.getInstance().getSiteLinkCache(siteName);
	}

	/**
	 * 根据文件的父路径和子路径获取文件对应的目录
	 * @param parent
	 * @param subFilepath
	 * @return
	 */
	public static String getDirectroy(String parent, String subFilepath) {
		int idx = subFilepath.lastIndexOf('/');
		idx = idx != -1 ? idx : subFilepath.lastIndexOf('\\');
		if (idx == -1)
			return parent;
		else {
			subFilepath = subFilepath.substring(0, idx);
		}
		if (parent == null || parent.equals(""))
			return subFilepath;

		if (parent.endsWith("/") || parent.endsWith("\\")
				|| subFilepath.startsWith("/") || subFilepath.startsWith("\\"))
			return parent + subFilepath;
		else
			return parent + "/" + subFilepath;
	}

	/**
	 * 根据文件路径获取文件的父路径
	 * @param subFilepath
	 * @return
	 */
	public static String getDirectroy(String subFilepath) {
		int idx = subFilepath.lastIndexOf('/');
		idx = idx != -1 ? idx : subFilepath.lastIndexOf('\\');
		if (idx == -1)
			return "";
		else {
			subFilepath = subFilepath.substring(0, idx);
			return subFilepath;
		}

	}

	/**
	 * 根据文件路径获取文件的父路径和文件名
	 * @param subFilepath
	 * @return
	 */
	public static String[] getFileInfo(String subFilepath) {
		int idx = subFilepath.lastIndexOf('/');
		idx = idx != -1 ? idx : subFilepath.lastIndexOf('\\');
		if (idx == -1)

			return new String[] { "", subFilepath };
		else {
			String fileName = "";
			if(idx != subFilepath.length() -1 )
				fileName = subFilepath.substring(idx + 1);
			subFilepath = subFilepath.substring(0, idx);
			return new String[] { subFilepath, fileName };
			

		}

	}
	
	/**
	 * 根据站点目录获取站点的工程，模板存放的目录，例如site200，获取的路径为d:/workspace/cms/creatorcms/cms/siteresource/site200
	 * @param siteDir
	 * @return
	 */
	public static String getSiteRootPath(String siteDir) {
		String rootPath = CMSUtil.getPath(CMSUtil.getAppRootPath(), CMSUtil
				.getCMSContextPath());
		rootPath = CMSUtil.getPath(rootPath, CMSUtil.getWebSiteRootPath());
		rootPath = CMSUtil.getPath(rootPath, siteDir);
		return rootPath;
	}

	/**
	 * 根据上下文parent路径技术simplesubpath的相对完整路径，例如
	 * parent:/creatorcms/cms/siteResource/_template
	 * simplesubpath:../../indexnews/index.jsp
	 * 返回值为/creatorcms/cms/indexnews/index.jsp
	 * 
	 * @param parent
	 * @param simplesubpath
	 * @return
	 */
	public static String getPathFromSimplePath(String parent,
			String simplesubpath) {
		int index = simplesubpath.lastIndexOf("./");
		if (index == -1)
			return CMSUtil.getPath(parent, simplesubpath);
		/**
		 * 截取../../
		 */
		String simple = simplesubpath.substring(0, index + 2);
		StringTokenizer tokens = new StringTokenizer(parent, "/", false);

		List parents = new ArrayList();
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token.equals(""))
				continue;
			parents.add(token);
		}
		tokens = new StringTokenizer(simple, "/", false);
		int count = 0;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token.equals("."))
				continue;
			count++;
		}
		String subpath = simplesubpath.substring(index + 2);
		StringBuffer realPath = new StringBuffer();
		int bound = parents.size() - count;
		//		if(bound == 1)
		//		{
		//			realPath.append(parents.get(1));
		//		}
		//		else 
		if (bound == 0) {
			return subpath;
		} else {
			for (int i = 0; i < bound; i++) {
				if (i < bound - 1) {

					realPath.append(parents.get(i)).append("/");

				} else {
					realPath.append(parents.get(i));
				}
			}
		}

		String retpath = CMSUtil.getPath(realPath.toString(), subpath);

		return retpath;
	}
	public static List<String> paserPathTokens(String path,boolean ignoreSpace)
	{
		if(path == null )
			return null;
		List<String> tokens = new ArrayList<String>();
		if(path.length() == 0)
		{
			if(!ignoreSpace)
				tokens.add("");
			return tokens;
		}
		StringBuffer token = new StringBuffer();
		
		int tag = 0;
		for(int i = 0; i < path.length(); i ++)
		{
			char ch = path.charAt(i);
			switch(ch)
			{
				case '/':
					if(i == 0)
					{
						if(!ignoreSpace)
							tokens.add("");
					}
					else
					{
						if(tag == 1)
						{
							int it = i + 1;
							if(it == path.length())
							{
								token.append(ch);
								tokens.add(token.toString());
								token.setLength(0);
								if(!ignoreSpace)
									tokens.add("");
								break;
							}
							char t_ch = path.charAt(it);
							if(t_ch == '>')
							{
								tag = 0;
							}
							token.append(ch);
							token.append(t_ch);
							i = it;
						}
						else
						{
							if(i < path.length() - 1)
							{
								tokens.add(token.toString());
								token.setLength(0);
							}
							else
							{
								tokens.add(token.toString());
								if(!ignoreSpace)
									tokens.add("");
								token.setLength(0);
							}
							
						}
					}
					break;
				case '<'://标记开始，忽略任何路径信息直到结束标记/>
					tag = 1;
					token.append(ch);
					break;
//				case '>':
//					
				default :
					token.append(ch);
					
			}
		}
		if(token.length() > 0)
		{
			tokens.add(token.toString());
			token.setLength(0);
			
		}
		token = null;
		return tokens;
	}
	/**
	 * 计算路径fullPath路径对应的文件相对于currentRelativePath的简化路径,例如：
	 * currentRelativePath：indexnews/sports/basketball
	 * fullPath:images/log.gif
	 * 返回的结果为：
	 * ../../../images/log.gif
	 * @param currentRelativePath
	 * @param fullPath
	 * @return
	 */
	public static String getSimplePathFromfullPathByStringTokenizer(String currentRelativePath,
			String fullPath) {

		StringTokenizer tokens = new StringTokenizer(currentRelativePath, "/",
				false);
		StringTokenizer ftokens = new StringTokenizer(fullPath, "/", false);

		StringBuffer result = new StringBuffer();
		boolean start = false;

		StringBuffer newFullPath = new StringBuffer("");
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token.equals("."))
				continue;

			if (start) {
				result.append("../");
			} else if (ftokens.hasMoreTokens()) {
				String ftoken = ftokens.nextToken();
				if (token.equals(ftoken)) {
					if (tokens.hasMoreTokens())
						continue;
					else {
						boolean flag = false;
						while (ftokens.hasMoreTokens()) {

							ftoken = ftokens.nextToken();
							if (ftoken.equals("."))
								continue;
							if (flag)
								newFullPath.append("/").append(ftoken);
							else {
								newFullPath.append(ftoken);
								flag = true;
							}
						}
						start = true;
					}
				} else {
					start = true;
					newFullPath.append(ftoken);
					while (ftokens.hasMoreTokens()) {

						ftoken = ftokens.nextToken();
						if (ftoken.equals("."))
							continue;
						newFullPath.append("/").append(ftoken);
					}
					result.append("../");
					continue;
				}
			}
		}

		if (start)
			result.append(newFullPath);
		else
			result.append(fullPath);

		//		StringBuffer result = new StringBuffer();
		//		
		//		
		//		while(tokens.hasMoreTokens())
		//		{
		//			String token = tokens.nextToken();
		//			if(token.equals("."))
		//				continue;
		//			result.append("../");
		//			
		//		}
		//		result.append(fullPath);

		return result.toString();
	}
	
	/**
	 * 计算路径fullPath路径对应的文件相对于currentRelativePath的简化路径,例如：
	 * currentRelativePath：indexnews/sports/basketball
	 * fullPath:images/log.gif
	 * 返回的结果为：
	 * ../../../images/log.gif
	 * @param currentRelativePath
	 * @param fullPath
	 * @return
	 */
	public static String getSimplePathFromfullPath(String currentRelativePath,
			String fullPath) {

		List<String> tokensList = CMSUtil.paserPathTokens(currentRelativePath,true); 
		List<String> ftokensList = CMSUtil.paserPathTokens(fullPath,false);
//		StringTokenizer tokens = new StringTokenizer(fullPath, "/",
//				false);
//		StringTokenizer ftokens = new StringTokenizer(fullPath, "/", false);
		Iterator<String> tokens = tokensList.iterator();
		Iterator<String> ftokens = ftokensList.iterator();

		StringBuffer result = new StringBuffer();
		boolean start = false;

		StringBuffer newFullPath = new StringBuffer("");
		while (tokens.hasNext()) {
			String token = tokens.next();
			if (token.equals("."))
				continue;

			if (start) {
				result.append("../");
			} else if (ftokens.hasNext()) {
				String ftoken = ftokens.next();
				if (token.equals(ftoken)) {
					if (tokens.hasNext())
						continue;
					else {
						boolean flag = false;
						while (ftokens.hasNext()) {

							ftoken = ftokens.next();
							if (ftoken.equals("."))
								continue;
							if (flag)
								newFullPath.append("/").append(ftoken);
							else {
								newFullPath.append(ftoken);
								flag = true;
							}
						}
						start = true;
					}
				} else {
					start = true;
					newFullPath.append(ftoken);
					while (ftokens.hasNext()) {

						ftoken = ftokens.next();
						if (ftoken.equals("."))
							continue;
						newFullPath.append("/").append(ftoken);
					}
					result.append("../");
					continue;
				}
			}
		}

		if (start)
		{
//			result.append(newFullPath);
			return com.frameworkset.util.StringUtil.getRealPath(result.toString(), newFullPath.toString());
		}
		else
		{
//			result.append(fullPath);
			return com.frameworkset.util.StringUtil.getRealPath(result.toString(), fullPath);
		}

		//		StringBuffer result = new StringBuffer();
		//		
		//		
		//		while(tokens.hasMoreTokens())
		//		{
		//			String token = tokens.nextToken();
		//			if(token.equals("."))
		//				continue;
		//			result.append("../");
		//			
		//		}
		//		result.append(fullPath);

//		return result.toString();
	}

	public static String getMalformedURLFromRelativePath(String parent,
			String relativePath) {
		return getPath(parent, relativePath);
	}

	public static String getTemplateRootPath(String sitedir) {
		return CMSUtil.getPath(getSiteRootPath(sitedir), "_template");

	}

	public static int getPageType(String fileName) {
		// TODO Auto-generated method stub
		if (CMSUtil.isBinaryFile(fileName)) {
			return CMSLink.TYPE_BINARY_PAGE ;
		} else if (fileName.toLowerCase().endsWith(".htm")
				|| fileName.toLowerCase().endsWith(".html")
				|| fileName.toLowerCase().endsWith(".xhtml")
				)
		{
			
			return CMSLink.TYPE_STATIC_PAGE ;
//		else if ( fileName.toLowerCase().endsWith(".css"))
//			
//			return CMSLink.TYPE_STATIC_CSS;
//		else if(fileName.toLowerCase().endsWith(".js")
//				|| fileName.toLowerCase().endsWith(".gif")
//				|| fileName.toLowerCase().endsWith(".png")
//				|| fileName.toLowerCase().endsWith(".jpg")
//				|| fileName.toLowerCase().endsWith(".swf"))
//			return CMSLink.TYPE_STATIC_RESOURCE;
		}
		else {
			return CMSLink.TYPE_DYNAMIC_PAGE ;
		}

	}

	/**
	 * 获取内容页面名称
	 * @param contentID
	 * @return
	 */
	public static String getContentFileName(String siteid,String contentID) {
		DocumentManagerImpl docImpl = new DocumentManagerImpl();
       
		try {
			 Document content = docImpl.getPartDocInfoById(contentID);
			 if(content != null)
			     return getContentFileName(siteid,content);
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
	
	
	
	
	/**
	 * 获取发布模式
	 * @param isdynamic
	 * @param isprotected
	 * @return
	 */
	public static PublishMode getPublishMode(int isdynamic,int isprotected)
	{
		
		if(isdynamic == 1 && isprotected == 1)
		{
			return PublishMode.MODE_DYNAMIC_PROTECTED;
		}
		else if(isdynamic == 1 && isprotected == 0)
		{
			return  PublishMode.MODE_DYNAMIC_UNPROTECTED;
		}
		
		else if(isdynamic == 0 && isprotected == 0)
		{
			return PublishMode.MODE_STATIC_UNPROTECTED;
		}
		
		else if(isdynamic == 0 && isprotected == 1)
		{
			return PublishMode.MODE_STATIC_PROTECTED;
		}
		
		else if( isdynamic == 2)
		{
			return PublishMode.MODE_ONLY_ATTACHMENT;
		}
		return PublishMode.MODE_STATIC_UNPROTECTED;
		
	}
	/**
	 * 判断文档是否需要动态发布
	 * @param document
	 * @return
	 */
	public static boolean isDynamic(String siteid,Document document)
	{
		Context context = null;
		Channel channel;
		try {
			channel = CMSUtil.getChannelCacheManager(siteid).getChannel(document.getChanel_id() + "");
			PublishMode publishMode = getPublishMode(channel.getDocIsDynamic(),
					channel.getDocIsProtect());
			if (publishMode == PublishMode.MODE_DYNAMIC_PROTECTED 
			|| publishMode == PublishMode.MODE_DYNAMIC_UNPROTECTED)
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	/**
	 * 获取内容页面名称
	 * @param contentID
	 * @return
	 */
	public static String getContentFileName(String siteid,Document content) {
		if(!isDynamic(siteid,content))
		{
			String contentFileName = "content_" + content.getDocument_id();
			String publishName = "";
		
		    publishName = content.getPublishfilename();
		
			if (publishName.length() > 0) {
				contentFileName = publishName;
			}
			if(!contentFileName.toLowerCase().endsWith(".html") && !contentFileName.toLowerCase().endsWith(".htm"))
				return contentFileName + ".html";
			else
				return contentFileName;
		}
		else
		{
			
			
			String fileName = content.getPublishfilename();
			if(fileName == null || fileName.equals(""))
			{
				fileName = new StringBuffer("content_" )
								.append(content.getDetailtemplate_id())
								.append(".jsp")
								.toString();
			}
			else
			{
				if (!fileName.toLowerCase().endsWith(".jsp") )
					fileName += ".jsp"; 
					
					
				
			}
			return fileName;
		}
		
	}

	public static String getTemplatePath(Context context) {
		if (context instanceof ContentContext) {
			return ((ContentContext) context).getDetailTemplate()
					.getTemplatePath();
		} else if (context instanceof com.frameworkset.platform.cms.driver.context.PageContext) {
			return ((com.frameworkset.platform.cms.driver.context.PageContext) context)
					.getPageDir();
		} else if (context instanceof CMSContext) {
			return ((CMSContext) context).getIndexTemplate().getTemplatePath();
		} else if (context instanceof ChannelContext) {
			return ((ChannelContext) context).getOutlineTemplate()
					.getTemplatePath();
		} else {
			return "";
		}

	}
	
	/**
	 * 频道发布的临时目录
	 * @param context
	 * @param siteid
	 * @param channel
	 * @return 
	 * CMSUtil.java
	 * @author: ge.tao
	 */
	public static String getChannelPublishTempPath(Context context,String siteid,Channel channel){		
//		String path = CMSUtil.getPath(context.getPublishTemppath() ,"site" + siteid);
		String path = CMSUtil.getPath(context.getPublishTemppath() ,context.getSite().getSecondName());		
		return path = CMSUtil.getPath(path,channel.getChannelPath());
	}
	
	/**
	 * 频道发布的临时目录 
	 * @param context
	 * @param siteid
	 * @param channeldir 频道发布的相对路径
	 * @return 
	 * CMSUtil.java
	 * @author: ge.tao
	 */
	public static String getChannelPublishTempPath(Context context,String siteid,String channeldir)	{
//		String path = CMSUtil.getPath(context.getPublishTemppath() ,"site" + siteid);
		String path = CMSUtil.getPath(context.getPublishTemppath() ,context.getSite().getSecondName());
		return path = CMSUtil.getPath(path,channeldir);
	}
	
	/**
	 * 站点发布的临时目录
	 * @param context
	 * @param siteid
	 * @return 
	 * CMSUtil.java
	 * @author: ge.tao
	 */
	public static String getSitePublishTempPath(Context context,String siteid)	{
//		String path = CMSUtil.getPath(context.getPublishTemppath() ,"site" + siteid);
		String path = CMSUtil.getPath(context.getPublishTemppath() ,context.getSite().getSecondName());
		return path;
	}

	/**
	 * 获取当前发布的目的地地址
	 * @return
	 */
	public static String getCurrentPublishPath(Context context) {
		if (context == null)
			return "";
		return context.getPublishPath();
	}

	/**
	 * 获取文档的原始页面路径
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getContentPath(Context context, String channeldir,
			String contentid) {
		try {
			if(context instanceof DefaultContextImpl)
			{
				Document document = CMSUtil.getCMSDriverConfiguration()
				.getCMSService()
				.getDocumentManager().getPartDocInfoById(contentid);
				String originePath = getContentPath(context, document);
				if(isExternalUrl(originePath))
					return originePath;
				
				return ((DefaultContextImpl)context).getPublishedLinkPath(originePath);
				
			}
			else
			{
				Document document = CMSUtil.getCMSDriverConfiguration()
				.getCMSService()
				.getDocumentManager().getPartDocInfoById(contentid);
				String siteid = document.getSiteid() + "";
				if(siteid.equals(context.getSiteID()))
				{
					return getContentPath(context,document);
				}
				
				else
				{
					try {
						String site = CMSUtil.getSiteCacheManager().getSite(siteid).getSecondName();
						DefaultContextImpl context_ = new DefaultContextImpl(site,context.getRequestContext().getRequest(),
								context.getRequestContext().getResponse());
						String originePath = getContentPath(context_, document);
						if(isExternalUrl(originePath))
							return originePath;
						
						return context_.getPublishedLinkPath(originePath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
					
			}
			
				
			
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
	/**
	 * 获取文档的最终访问地址，文档对应所在的站点可能和上下文中包含的站点不一至，
	 * 因此需要根据文档的对应的频道，查找文档所在的站点，这会影响系统的性能
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getContentPath(Context context, Document document) {
		try {
			Channel channel = null;
			String docsiteid = document.getSiteid() + "";
			/**
			 * 文档是否是引用其他站点文档标识
			 * true，标识是
			 * false，不是
			 * 判断的逻辑是：
			 * 文档的站点
			 */
			boolean flag = false;
			if(document.getSiteid() == -1 
					|| docsiteid.equals(context.getSiteID()))
			{
				channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(document.getChanel_id() + "");
				docsiteid = context.getSiteID();
			}
			else
			{
				channel = CMSUtil.getChannelCacheManager(docsiteid).getChannel(document.getChanel_id() + "");
				flag = true;
			}
			DefaultContextImpl defctx = null; 
			if(flag)
			{
				defctx = new DefaultContextImpl(CMSUtil.getSiteCacheManager().getSite(docsiteid).getSecondName(),context.getRequestContext());
//				defctx = new DefaultContextImpl(docsiteid); 
			}
			PublishMode publishMode = context.getPublishMode(channel.getDocIsDynamic(),
															channel.getDocIsProtect());
			if (publishMode == PublishMode.MODE_DYNAMIC_PROTECTED 
					|| publishMode == PublishMode.MODE_DYNAMIC_UNPROTECTED)
			{//动态发布
				if(document.getDoctype() == Document.DOCUMENT_NORMAL)
				{
					String fileName = document.getPublishfilename();
					if(fileName == null || fileName.equals(""))
					{
						fileName = new StringBuffer("content_" )
										.append(document.getDetailtemplate_id())
										.append(".jsp?siteid=")
										.append(docsiteid )
										.append("&channelid=" )
										.append(document.getChanel_id() )
										.append("&documentid=" )
										.append(document.getDocument_id())
										.toString();
					}
					else
					{
						if (!fileName.toLowerCase().endsWith(".jsp") )
							fileName += ".jsp"; 
						fileName = new StringBuffer(fileName)
										.append("?siteid=")
										.append(docsiteid )
										.append("&channelid=" )
										.append(document.getChanel_id() )
										.append("&documentid=" )
										.append(document.getDocument_id())
										.toString();
					}
					String retPath = CMSUtil.getPath(channel.getChannelPath(), fileName); 
					if(flag)
					{
						retPath = defctx.getPublishedLinkPath(retPath); 
					}
					return retPath;
				}
				else if(document.getDoctype() == Document.DOCUMENT_OUTLINK)
				{
					return document.getLinkfile();
				}
				else if(document.getDoctype() == Document.DOCUMENT_OUTFILE)
				{
					return document.getContent();
				}
				else 
				{
					return "";
				}
			}
			else
			{//静态发布
				if(document.getDoctype() == Document.DOCUMENT_NORMAL)
				{
					String fileName = document.getPublishfilename();
					if(fileName == null || fileName.equals(""))
						fileName = "content_" + document.getDocument_id() + ".html";
					else if(!fileName.toLowerCase().endsWith(".html") && !fileName.toLowerCase().endsWith(".htm"))
					{
						fileName += ".html";
					}
					String retPath = CMSUtil.getPath(channel.getChannelPath(), fileName); 
					if(flag)
					{
						retPath = defctx.getPublishedLinkPath(retPath); 
					}
					return retPath;
//					return CMSUtil.getPath(channel.getChannelPath(), fileName);
				}
				else if(document.getDoctype() == Document.DOCUMENT_OUTLINK)
				{
					return document.getLinkfile();
				}
				else if(document.getDoctype() == Document.DOCUMENT_OUTFILE)
				{
					return document.getContent();
				}
				else 
				{
					return "";
				}
			}
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
	
	/**
	 * 获取文档附件的最终访问地址，文档对应所在的站点可能和上下文中包含的站点不一至，
	 * 因此需要根据文档的对应的频道，查找文档所在的站点，这会影响系统的性能
	 * @param Context
	 * @param document
	 * @param attachurl
	 * @return
	 */
	public static String getContentAttachPath(Context context, Document document,String attachurl) {
//		try {
			
			
			try {
				Channel channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(document.getChanel_id() + "");
				return CMSUtil.getPath(channel.getChannelPath(), attachurl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
			
//			PublishMode publishMode = context.getPublishMode(channel.getDocIsDynamic(),
//															channel.getDocIsProtect());
//			if (publishMode == PublishMode.MODE_DYNAMIC_PROTECTED 
////					|| publishMode == PublishMode.MODE_DYNAMIC_UNPROTECTED)
//			{//动态发布
//				if(document.getDoctype() == Document.DOCUMENT_NORMAL)
//				{
//					String fileName = document.getPublishfilename();
//					if(fileName == null || fileName.equals(""))
//					{
//						fileName = new StringBuffer("content_" )
//										.append(document.getDetailtemplate_id())
//										.append(".jsp?siteid=")
//										.append(context.getSiteID() )
//										.append("&channelid=" )
//										.append(document.getChanel_id() )
//										.append("&documentid=" )
//										.append(document.getDocument_id())
//										.toString();
//					}
//					else
//					{
//						if (!fileName.toLowerCase().endsWith(".jsp") )
//							fileName += ".jsp"; 
//						fileName = new StringBuffer(fileName)
//										.append("?siteid=")
//										.append(context.getSiteID() )
//										.append("&channelid=" )
//										.append(document.getChanel_id() )
//										.append("&documentid=" )
//										.append(document.getDocument_id())
//										.toString();
//					}
//					return CMSUtil.getPath(channel.getChannelPath(), fileName);
//				}
//				else if(document.getDoctype() == Document.DOCUMENT_OUTLINK)
//				{
//					return document.getLinkfile();
//				}
//				else if(document.getDoctype() == Document.DOCUMENT_OUTFILE)
//				{
//					return document.getContent();
//				}
//				else 
//				{
//					return "";
//				}
//			}
//			else
//			{//静态发布
//				if(document.getDoctype() == Document.DOCUMENT_NORMAL)
//				{
//					String fileName = document.getPublishfilename();
//					if(fileName == null || fileName.equals(""))
//						fileName = "content_" + document.getDocument_id() + ".html";
//					else if(!fileName.toLowerCase().endsWith(".html") && !fileName.toLowerCase().endsWith(".htm"))
//					{
//						fileName += ".html";
//					}
//					return CMSUtil.getPath(channel.getChannelPath(), fileName);
//				}
//				else if(document.getDoctype() == Document.DOCUMENT_OUTLINK)
//				{
//					return document.getLinkfile();
//				}
//				else if(document.getDoctype() == Document.DOCUMENT_OUTFILE)
//				{
//					return document.getContent();
//				}
//				else 
//				{
//					return "";
//				}
//			}
//		} catch (DocumentManagerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DriverConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "";
		
	}
	
	
	/**
	 * 判断给定的url是否是外部url
	 * @param url
	 * @return
	 */
	public static boolean isExternalUrl(String url)
	{
		if(url.toLowerCase().startsWith("http://") 
				|| url.toLowerCase().startsWith("https://") 
				|| url.toLowerCase().startsWith("ftp://")
				|| url.toLowerCase().startsWith("mailto:") 
				|| url.toLowerCase().startsWith("tps://")
				|| url.startsWith("/"))
			return true;
		else
			return false;
	}

	/**
	 * 获取文档的原始路径
	 * @param channeldir 
	 * @param contentid
	 * @return
	 */
	public static String getPublishedContentPath(Context context,
			String channeldir, String contentid) {
		String originePath = getContentPath(context, channeldir, contentid);
		if(isExternalUrl(originePath))
			return originePath;
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getPublishedLinkPath(originePath);
		} else {
			String currentPublishPath = getCurrentPublishPath(context);

			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
					originePath);
		}

	}
	
	/**
	 * 获取文档的url地址
	 * @param channeldir 
	 * @param contentid
	 * @return
	 */
	public static String getPublishedContentPath(Context context,Document document) {
		String originePath = getContentPath(context, document);
		if(isExternalUrl(originePath))
			return originePath;
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getPublishedLinkPath(originePath);
		} else {
			String currentPublishPath = getCurrentPublishPath(context);

			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
					originePath);
		}

	}
	
	
	/**
	 * 获取文档的附件的发布地址
	 * @param channeldir 
	 * @param contentid
	 * @return
	 */
	public static String getPublishedContentAttachPath(Context context,Document document,String attachurl) {
		String originePath = getContentAttachPath(context, document,attachurl);
		if(isExternalUrl(originePath))
			return originePath;
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getPublishedLinkPath(originePath);
		} else {
			String currentPublishPath = getCurrentPublishPath(context);

			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
					originePath);
		}

	}
	
	/**
	 * RSS 获取发布时的全路径
	 * @param context
	 * @param channeldir
	 * @param contentid
	 * @return 
	 * CMSUtil.java
	 * @author: ge.tao
	 */
	public static String getRssPublishedContentPath(Context context,
			String channeldir, String contentid) {
		String originePath = getContentPath(context, channeldir, contentid);
		if(isExternalUrl(originePath))
			return originePath;
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getRssPublishedLinkPath(originePath);
		} else {
			String currentPublishPath = getCurrentPublishPath(context);

			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
					originePath);
		}

	}
	
	public static Channel getParentChannelWithLevel(Channel current,int level)
	{
		try {
			if(level == 0 || current == null)
				return current;
			
			Channel current_ = CMSUtil.getChannelCacheManager(current.getSiteId() + "").getChannel(current.getParentChannelId() + "");
			if(current_ == null)
				return current;
			return getParentChannelWithLevel(current_,level - 1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return current;
	}
	
	
	public static Channel getParentChannelWithLevel(String siteid,Channel current,int level)
	{
		try {
			if(level == 0 || current == null)
				return current;
			
			Channel current_ = CMSUtil.getChannelCacheManager(siteid).getChannel(current.getParentChannelId() + "");
			if(current_ == null)
				return current;
			return getParentChannelWithLevel(siteid,current_,level - 1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return current;
	}

	/**
	 * 
	 * @param site 站点英文名称
	 * @param channeldir 频道相对目录
	 * @param contentid 文档id
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getPublishContentPath(String site, String channeldir,
			String contentid, HttpServletRequest request,
			HttpServletResponse response) {
		DefaultContextImpl context = new DefaultContextImpl(site, request,
				response);
		String fileName = CMSUtil.getContentPath(context,channeldir,contentid);
		if(CMSUtil.isExternalUrl(fileName))
			return fileName;
		return context.getPublishedLinkPath(fileName);
	}

	//	/**
	//	 * 获取频道首页的路径
	//	 * @param channeldir
	//	 * @param contentid
	//	 * @return
	//	 */
	//	public static String getPublishedChannelPath(Context context,String channelpath)
	//	{
	//		if(context instanceof DefaultContextImpl)
	//		{
	//			return ((DefaultContextImpl)context).getPublishedLinkPath(channelpath);
	//		}
	//		else
	//		{
	//			String currentPublishPath = getCurrentPublishPath(context);
	//			
	//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,channelpath);
	//		}
	//	} 
	//	

	/**
	 * 获取频道首页的路径
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getPublishedChannelPath(Context context,
			Channel channel) {
		if(context.getSiteID().equals(channel.getSiteId() + ""))
		{
			String channelpath = CMSUtil.getChannelIndexPath(context,channel);
			if(CMSUtil.isExternalUrl(channelpath))
				return channelpath;
			if (channel.getPageflag() == 1 && channelpath.startsWith("/"))
				return channelpath;
			
	
			if (context instanceof DefaultContextImpl) {
				return ((DefaultContextImpl) context)
						.getPublishedLinkPath(channelpath);
			} else {
				String currentPublishPath = getCurrentPublishPath(context);
	
				return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
						channelpath);
			}
		}
		else
		{
			
			try {
				String site = CMSUtil.getSiteCacheManager().getSite(channel.getSiteId() + "").getSecondName();
				DefaultContextImpl context_ = new DefaultContextImpl(site,context.getRequestContext()) ;
				String channelpath = CMSUtil.getChannelIndexPath(context_,channel);
				if(CMSUtil.isExternalUrl(channelpath))
					return channelpath;
				else
				{
					return context_.getPublishedLinkPath(channelpath);					
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return "";
		}
	}
	
	
//	public static String 
	/**
	 * rss 获取频道发布全路径url
	 * @param context
	 * @param channel
	 * @return 
	 * CMSUtil.java
	 * @author: ge.tao
	 */
	public static String getRssPublishedChannelPath(Context context,
			Channel channel) {
		if(context.getSiteID().equals(channel.getChannelId() + ""))
		{
			String channelpath = CMSUtil.getChannelIndexPath(context,channel);
			if(CMSUtil.isExternalUrl(channelpath) )
				return channelpath;
			if (channel.getPageflag() == 1 && channelpath.startsWith("/") ){
				String siteurl = CMSUtil.getPublishedSitePath(context);
				
				
	
				return CMSUtil.getPath(siteurl,channelpath);
			}		
	
			if (context instanceof DefaultContextImpl) {
				return ((DefaultContextImpl) context)
						.getRssPublishedLinkPath(channelpath);
			} else {
				String currentPublishPath = getCurrentPublishPath(context);
	
				return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
						channelpath);
			}
		}
		else
		{
			
			try {
				String site = CMSUtil.getSiteCacheManager().getSite(channel.getSiteId() + "").getSecondName();
				DefaultContextImpl context_ = new DefaultContextImpl(site,context.getRequestContext());
				String channelpath = CMSUtil.getChannelIndexPath(context_,channel);
				if(CMSUtil.isExternalUrl(channelpath) )
					return channelpath;
				return context_.getRssPublishedLinkPath(channelpath);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
			
		}
	}

	/**
	 * 获取频道首页的路径uri,例如：http://域名/channeldir
	 * @param context
	 * @param channel
	 * @return
	 */
	public static String getPublishedChannelDir(Context context, Channel channel) {
		//		String channelpath = CMSUtil.getChannelIndexPath(channel);
		if(channel == null)
			return "";
		String channelpath = channel.getChannelPath();
		

		DefaultContextImpl _context = new DefaultContextImpl(context.getSite()
				.getSecondName(), context.getRequestContext().getRequest(),
				context.getRequestContext().getResponse());

		return _context.getPublishedLinkPath(channelpath);

	}

	/**
	 * 获取频道首页的路径 http://域名/sitePath
	 * @param context
	 * @param context
	 * @return
	 */
	public static String getPublishedSitePath(Context context, String sitePath) {
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getPublishedLinkPath(sitePath);
		} else {
			String currentPublishPath = context.getPublishPath();

			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
					sitePath);
		}
	}
	

	/**
	 * 获取站点根访问地址 如：http://域名
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getPublishedSitePath(Context context) {
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getPublishedLinkPath("");
		} else {
			String currentPublishPath = context.getPublishPath();

			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
					"");
		}
	}

	/**
	 * 获取链接文件发布后的路径
	 * @param templatePath 模板所在路径
	 * @param linkPath
	 * @return
	 */
	public static String getPublishedLinkPath(Context context,
			String templatePath, String linkPath) {
		
		if (context == null || linkPath == null)
			return linkPath;
		if(CMSUtil.isExternalUrl(linkPath))
			return linkPath;
		if (context instanceof DefaultContextImpl) {
			return ((DefaultContextImpl) context)
					.getPublishedLinkPath(linkPath);
		} else {
			CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,
					templatePath);
			processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
			CMSLink templink = processor.processHref(linkPath,CmsTagLinkProcessor.LINK_PARSER_DISTRIBUTE);
			return templink.getHref();
		}
	}
	
//	public static void  addPublishedLinkDirPath(Context context,
//			String templatePath, String linkPath) {
//		
//		if (context == null || linkPath == null)
//			return ;
//		
//		if (context instanceof DefaultContextImpl) {
////			((DefaultContextImpl) context)
////					.getPublishedLinkPath(linkPath);
//			;
//		} else {
//			CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,
//					templatePath);
//			processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
//			processor.processDir(linkPath,CmsTagLinkProcessor.LINK_PARSER_DISTRIBUTE);
//
//		}
//	}
	
	/**
	 * 获取链接文件发布后的路径
	 * @param templatePath
	 * @param linkPath
	 * @return
	 */
	public static CMSLink getPublishedLink(Context context,
			String templatePath, String linkPath) {
		if (context == null)
		{
			CmsTagLinkProcessor p = new CmsTagLinkProcessor();
			return p.createCMSLink(linkPath,linkPath);
		}
		if (context instanceof DefaultContextImpl) {
			CmsTagLinkProcessor p = new CmsTagLinkProcessor();
			return p.createCMSLink(linkPath,linkPath);
			
		} else {
			CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,
					templatePath);
			processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
			CMSLink templink = processor.processHref(linkPath,CmsTagLinkProcessor.LINK_PARSER_DISTRIBUTE);
			return templink;
		}
	}
	
	/**
	 * 添加链接文件发布到发布上下文中以便系统能够在发布过程中自动发布这些
	 * 链接文件到系统中
	 * @param templatePath
	 * @param linkPath
	 * @return
	 */
	public static void addPublishLink(Context context,
			String templatePath, String linkPath) {
		if (context == null)
		{
			
		}
		else if (context instanceof DefaultContextImpl) {
//			CmsTagLinkProcessor p = new CmsTagLinkProcessor();
//			 p.createCMSLink(linkPath,linkPath);
			
		} else {
			CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,
					templatePath);
			processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
			CMSLink templink = processor.processHref(linkPath,CmsTagLinkProcessor.LINK_PARSER_DISTRIBUTE);
		}
	}
	
	/**
	 * 添加链接文件发布到发布上下文中以便系统能够在发布过程中自动发布这些
	 * 链接文件到系统中
	 * @param templatePath
	 * @param linkPath
	 * @return
	 */
	public static void addPublishWEBPrjsLink(Context context,
			String linkPath) {
		
		if (context == null)
		{
			
		}
		else if (context instanceof DefaultContextImpl) {
//			CmsTagLinkProcessor p = new CmsTagLinkProcessor();
//			 p.createCMSLink(linkPath,linkPath);
			
		} else {
			String encoding = context.getSite().getEncoding();
			
			CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,
															  CmsLinkProcessor.REPLACE_LINKS,
															  encoding);
			processor.setHandletype(CmsTagLinkProcessor.PROCESS_CONTENT);
			CMSLink templink = processor.processHref(linkPath,CmsTagLinkProcessor.LINK_PARSER_DISTRIBUTE);
		}
	}

	/**
	 * 获取原始的频道路径链接
	 * @param context
	 * @return
	 */
	public static String getChannelPath(Context context) {
		if (context instanceof ContentContext) {
			ContentContext contentContext = (ContentContext) context;
			
			String channelindex = CMSUtil.getChannelIndexPath(context,contentContext.getChannel());
//			String dir = contentContext.getChannel().getChannelPath();
//			dir += "/" + contentContext.getChannel().getPubFileName();
			return channelindex;
		} else if (context instanceof com.frameworkset.platform.cms.driver.context.PageContext) {
			return "";
		} else if (context instanceof CMSContext) {
			return "";
		} else if (context instanceof ChannelContext) {
			String channelindex = CMSUtil.getChannelIndexPath(context,((ChannelContext) context).getChannel());
//			String dir = contentContext.getChannel().getChannelPath();
//			dir += "/" + contentContext.getChannel().getPubFileName();
			return channelindex;
			

		} else {
			return "";
		}

	}
	
	/**
	 * 获取站点根访问地址 如：http://域名
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getPublishedSitePath(String siteid) {
		DefaultContextImpl context = new DefaultContextImpl(siteid);
		return context.getPublishedLinkPath("");
//		if (context instanceof DefaultContextImpl) {
//			return ((DefaultContextImpl) context)
//					.getPublishedLinkPath("");
//		} else {
//			String currentPublishPath = context.getPublishPath();
//
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
//					"");
//		}
	}
	
	/**
	 * 获取站点根访问地址 如：http://域名
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getPublishedSitePath(HttpServletRequest request,HttpServletResponse response,String site) {
		DefaultContextImpl context = new DefaultContextImpl(site,request,response);
		return context.getPublishedLinkPath("");
//		if (context instanceof DefaultContextImpl) {
//			return ((DefaultContextImpl) context)
//					.getPublishedLinkPath("");
//		} else {
//			String currentPublishPath = context.getPublishPath();
//
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
//					"");
//		}
	}
	
	/**
	 * 获取站点首页地址
	 * @param channeldir
	 * @param contentid
	 * @return
	 */
	public static String getPublishedSitePath(HttpServletRequest request,HttpServletResponse response,Site site) {
		String domain = site.getWebHttp();		
		if(domain != null && !domain.trim().equals("") && !domain.equals("http://"))
		{
			return domain;
		}
		else
		{
			DefaultContextImpl context = new DefaultContextImpl(site.getSecondName(),request,response);
			String indexName = site.getIndexFileName();
			return context.getPublishedLinkPath(indexName);
		}
//		if (context instanceof DefaultContextImpl) {
//			return ((DefaultContextImpl) context)
//					.getPublishedLinkPath("");
//		} else {
//			String currentPublishPath = context.getPublishPath();
//
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
//					"");
//		}
	}

	/**
	 * 获取原始的频道路径链接
	 * @param context
	 * @return
	 */
	public static String getChannelDispalyName(Context context) {

		if (context instanceof ContentContext) {
			ContentContext contentContext = (ContentContext) context;

			return contentContext.getChannel().getDisplayName();
		} else if (context instanceof com.frameworkset.platform.cms.driver.context.PageContext) {
			return "";
		} else if (context instanceof CMSContext) {
			return "";
		} else if (context instanceof ChannelContext) {

			return ((ChannelContext) context).getChannel().getDisplayName();

		} else {
			return "";
		}

	}

	/**
	 * 获取链接文件发布后的路径,并且记录需要发布的链接附件，必须是相对于_template的绝对路径
	 * 如果是正对_template的相对路径，则必须指定模板的相对路径
	 * @see getPublishedLinkPath(context, templatePath, linkPath);
	 * @param linkPath
	 * @return
	 */
	public static String getPublishedLinkPath(Context context, String linkPath) {

		return getPublishedLinkPath(context, "", linkPath);
	}

	/**
	 * 获取内容管理概览缺省的数据获取接口
	 * @return
	 */
	public static CMSBaseListData getCMSBaseListData() {
		return CMSDataLoaderManager.getInstance().getDefaultDataLoader();
	}

	/**
	 * 获取内容管理概览缺省的数据获取接口
	 * @return
	 */
	public static CMSBaseListData getCMSBaseListData(String type) {
		try {
			return CMSDataLoaderManager.getInstance().getDataLoader(type);
		} catch (CMSDataLoadException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前发布任务所属的频道
	 * @param context
	 * @return
	 */
	public static Channel getCurrentChannel(Context context) {
		if (context instanceof ContentContext) {
			ContentContext contentContext = (ContentContext) context;

			return contentContext.getChannel();
		} else if (context instanceof com.frameworkset.platform.cms.driver.context.PageContext) {
			return null;
		} else if (context instanceof CMSContext) {
			return null;
		} else if (context instanceof ChannelContext) {

			return ((ChannelContext) context).getChannel();

		} else {
			return null;
		}
	}

	/**
	 * 加载模板
	 * @param templateUrl 模板路径
	 * @param vcontext
	 */
	public static String loadTemplate(String templateUrl,
			bboss.org.apache.velocity.context.Context vcontext) {
		StringWriter writer = new StringWriter();
		Template template = VelocityUtil.getTemplate(templateUrl);
		try {
			template.merge(vcontext, writer);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	
	
	/**
	 * 获取频道的首页地址
	 * @param channel
	 * @return
	 */
	public static String getChannelIndexPath(Context context,Channel channel) {
		
		if (channel.getPageflag() == 0) //模版首页模版方式
		{
			return CMSUtil.getPath(channel.getChannelPath(), channel
					.getPubFileName());
			
		} else if (channel.getPageflag() == 1) //指定页面为首页
		{
			//外部链接需要特殊处理
			
			return channel.getIndexpagepath();
		} 
		else if(channel.getPageflag() == 3) //通过其他频道首页生成当前的页面:站点id：频道id,暂时只考虑了引用同一个站点
		{
			String channelinfo = channel.getIndexpagepath();
			
			String[] infos = channelinfo.split(":");
			if(infos[0].equals(channel.getSiteId() + ""))
			{
				try {
					return CMSUtil.getChannelIndexPath(context,CMSUtil.getChannelCacheManager(infos[0]).getChannel(infos[1]));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return "";
			}
			else
			{
				try {
					
					DefaultContextImpl defaultContext = new DefaultContextImpl(CMSUtil.getSiteCacheManager().getSite(infos[0]).getSecondName(),context.getRequestContext());
					String tempPath = CMSUtil.getChannelIndexPath(defaultContext,CMSUtil.getChannelCacheManager(infos[0]).getChannel(infos[1]));
					if(CMSUtil.isExternalUrl(tempPath))
						return tempPath;
					else
					{
						return defaultContext.getPublishedLinkPath(tempPath);
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return "";
			}
			
		}
		else //指定文档细览页面为首页,如果文档对应的频道和当前发布的频道不一致时,需要另外获取文档频道
		{

			try {

				
				Channel t_channel = CMSUtil.getCMSDriverConfiguration()
						.getCMSService().getChannelManager()
						.getChannelOfDocument(channel.getIndexpagepath());
				if (t_channel != null) {
					return CMSUtil.getContentPath(context,t_channel.getChannelPath(),channel.getIndexpagepath());
					
				} else {
					log.debug("获取文档["
							+ channel.getIndexpagepath()
							+ "]类型频道首页地址:文档对应的频道不存在或者文档不存在!");
					context.getPublishMonitor().addFailedMessage("获取文档["
							+ channel.getIndexpagepath()
							+ "]类型频道首页地址:文档对应的频道不存在或者文档不存在!",context.getPublisher());
					return "";
				}

			} catch (DriverConfigurationException e) {
				try
				{
					log.debug("获取文档["
							+ channel.getIndexpagepath()
							+ "]类型频道首页地址:文档对应的频道不存在或者文档不存在!",e);
					context.getPublishMonitor().addFailedMessage("获取文档["
							+ channel.getIndexpagepath()
							+ "]类型频道首页地址:文档对应的频道不存在或者文档不存在!",context.getPublisher());
				}
				catch(Exception e_)
				{
					
				}
				return "";
			}
		}
	}

	public static ChannelCacheManager getChannelCacheManager(String siteid) {
		// TODO Auto-generated method stub
		return getSiteCacheManager().getChannelCacheManager(siteid);
	}
	
	public static ChannelCacheManager getChannelCacheManagerBySiteName(String site) {
		// TODO Auto-generated method stub
		return getSiteCacheManager().getChannelCacheManagerByEname(site);
	}

	/**
	 * 获取系统的全局唯一吗
	 * @return
	 */
	public static String getUUID() {
		
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 
	 * @param filePath 源图片路径
	 * @param watermark 水印图片路径
	 * @param waterStr 水印文字
	 * @param fontType 字体
	 * @param fontSize 字体大小
	 * @param position 水印粘贴位置
	 * @return boolean
	 * CMSUtil.java
	 * @author: 陶格
	 */
	public static boolean genWaterImage(String filePath,
			                            String watermark,
			                            String waterStr,
			                            String fontType,
			                            Color fontColor,
			                            int fontSize,
			                            int position) {
		System.setProperty("java.awt.headless", "true"); 
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage();
		ImageIcon waterIcon = new ImageIcon(watermark);
		
		int width = imgIcon.getIconWidth();
		int height = imgIcon.getIconHeight();
		BufferedImage bimage = new BufferedImage(width, height,	BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimage.createGraphics();
		g.setColor(fontColor);
		//g.setBackground(Color.white);		
		
		//添加水印图片
		if(watermark.length()>0){
			Image waterImg = waterIcon.getImage();
			//width = width-waterIcon.getIconWidth();
			//height = height-waterIcon.getIconHeight();
			g.drawImage(theImg, 0, 0, null);
			//水印图片的位置 与水印文字一起定位
			switch(position){
				case 0 ://居中
					g.drawImage(waterImg, width / 2, height / 2, null);					
					break;
				case 1 ://左上
					g.drawImage(waterImg, waterIcon.getIconWidth(), 0 , null);
					break;
				case 2 ://右上
					g.drawImage(waterImg, width-waterIcon.getIconWidth() , 0 , null);
					break;
				case 3 ://左下
					g.drawImage(waterImg, waterIcon.getIconWidth(), height-waterIcon.getIconHeight() , null);
					break;
				case 4 ://右下
					g.drawImage(waterImg, width-waterIcon.getIconWidth() , height-waterIcon.getIconHeight() , null);
					break;
				default ://居中
					g.drawImage(waterImg, width / 2, height / 2, null);		    
					break;
			}
		}
		
		//添加文字
		if(waterStr.length()>0){
			AttributedString ats = new AttributedString(waterStr);
	        Font f = new Font(fontType,Font.BOLD, fontSize);	
	        ats.addAttribute(TextAttribute.FONT, f, 0,waterStr.length() );
	        
	        AttributedCharacterIterator iter = ats.getIterator();
	        switch(position){
			case 0 ://居中
				g.drawString(iter,width / 2, height / 2);
				break;
			case 1 ://左上
				g.drawString(iter,waterStr.length(), 20);
				break;
			case 2 ://右上
				g.drawString(iter,width-waterStr.length()*15 , 20);
				break;
			case 3 ://左下
				g.drawString(iter,waterStr.length(), height );
				break;
			case 4 ://右下
				g.drawString(iter,width-waterStr.length()*15 , height);
				break;
			default ://居中
				if(watermark.length()>0){
			        g.drawString(iter,width / 2, height / 2);	
				}else{
					int t_left = waterIcon.getIconWidth();
					int t_height = waterIcon.getIconHeight();
					g.drawString(iter,(width / 2)+t_left, (height / 2)+t_height);
				}
				break;
		}
		}
		
		g.dispose();
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(1f, true);
			encoder.encode(bimage, param);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * 为图片文件添加文字水印
	 * @param filePath
	 * @param watermark
	 * @return
	 */
	public static boolean genWaterText(String filePath, 
			                           String watermark,
			                           int position) {
		//添加水印,filePath   源图片路径，   watermark   水印图片路径
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage(); 
		int width = imgIcon.getIconWidth();
		int height = imgIcon.getIconHeight();
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimage.createGraphics();
		g.setColor(Color.red);
		g.setBackground(Color.white);
		g.drawImage(theImg, 0, 0, null);
		//g.drawImage(waterImg,100,100,null);   
		g.drawString(watermark, 10, 10); //添加文字   
		g.dispose();
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(50f, true);
			encoder.encode(bimage, param);
			out.close();
		} catch (Exception e) {
			return false;
		}
		return true;

	}
	
	
	

//
//	public static void main(String[] args) {
//
//		CMSUtil.genWaterImage("D:\\workspace\\CMS\\creatorcms\\cms\\siteResource\\mixImages\\a.jpg",
//						       "D:\\workspace\\CMS\\creatorcms\\cms\\siteResource\\waterImages\\b.gif",
//						       "",
//						       "黑体",Color.RED,15,0);
//	}

	public static Site getCurrentSite(Context context) {
		return context.getSite();
		
	}

	public static Document getCurrentDocuemnt(Context context) {
		if(context instanceof ContentContext)
		{
			return ((ContentContext)context).getDocument();
		}
		return null;
		
	}
	
	/**
	 * 判断站点中是否存在给定路径的页面地址
	 * @param siteid
	 * @param pageurl
	 * @return
	 */
	public static boolean pageExist(Context context,String siteid,String pageurl)
	{
//		String sitepath = CMSUtil.getSiteRootPath("site" + siteid);
		String sitepath = CMSUtil.getSiteRootPath(context.getSite().getSecondName());
		String pagePath =  CMSUtil.getPath(CMSUtil.getPath(sitepath,"_template"),pageurl);
		File file = new File(pagePath);
		try
		{
			return (file.exists());
		}
		catch(Exception e)		
		{
			return false;
		}
	}
	
	/**
	 * 采用冒泡排序法，对整数数组按升序进行排序
	 * @param datas
	 * @return
	 */
	public static int[] sort(int[] datas)
	{
		int temp; // 交换标志
		boolean exchange;

		for (int i = 0; i < datas.length; i++) // 最多做R.Length-1趟排序
		{
			exchange = false; // 本趟排序开始前，交换标志应为假
			for (int j = datas.length - 2; j >= i; j--) {
				if (datas[j + 1] < datas[j])// 交换条件
				{
					temp = datas[j + 1];
					datas[j + 1] = datas[j];
					datas[j] = temp;
					exchange = true; // 发生了交换，故将交换标志置为真
				}
			}
			// 本趟排序未发生交换，提前终止算法
			if (exchange == false) {
				break;
			}
		}
		return datas;


	}
	
	public static Site getSite(String siteid)
	{
		try {
			return CMSUtil.getSiteCacheManager().getSite(siteid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 统计chr在str中出现的次数
	 * @param str
	 * @param chr
	 * @return 
	 * CMSUtil.java
	 * @author: ge.tao
	 */
	public static int count(String str,char chr)
	{
		int count = 0; 
		char[] strchars = str.toCharArray();
		for(int i = 0;  i < strchars.length; i ++)
		{
			if(strchars[i] == chr)
				count ++;
		}
		return count;
		
	}
	
	/**
	   * 针对HTML的特殊字符转义
	   * xinwang.jiao
	   * 2007-9-29
	   * @param value String
	   * @return String
	   */
	public static String filterStr(String value) {
	    if (value == null) 
	    {
	      return (null);
	    }
	    char content[] = new char[value.length()];
	    value.getChars(0, value.length(), content, 0);
	    StringBuffer result = new StringBuffer(content.length + 50);
	    for (int i = 0; i < content.length; i++) {
	    	switch (content[i]) {
		        case '<':
		          result.append("&lt;");
		          break;
		        case '>':
		          result.append("&gt;");
		          break;
		        case '&':
		          result.append("&amp;");
		          break;
		        case '"':
		          result.append("&quot;");
		          break;
		        case '\'':
		          result.append("&#39;");
		          break;
		        /*case '\\':
		          result.append("\\\\");
		          break;*/
		        default:
		          result.append(content[i]);
	    	}
	    }
		return (result.toString());
	}
	
	/**
	 * 获取文件字节数
	 * 最开始是出于选文件的浏览器考虑
	 * xinwang.jiao
	 * 2007-9-29
	 * @param size
	 * @return
	 */
	public static String convertFileSize (long size)
	{
		int divisor = 1024;
		String unit = "K";
		if (divisor ==1) return size /divisor + " "+unit;
		String aftercomma = ""+100*(size % divisor)/divisor;
		if (aftercomma.length() == 1) aftercomma="0"+aftercomma;
		return size /divisor + "."+aftercomma+" "+unit;
	}

	/**
	 * 获取时间输出格式(yyyy-MM-dd HH:mm:ss)
	 * 最开始是出于选文件的浏览器考虑
	 * xinwang.jiao
	 * 2007-9-29
	 * @param myDate
	 * @return
	 */
	public static String formatDate(Date myDate) 
	{
		String strFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(strFormat);
		String strDate = formatter.format(myDate);
		return strDate;
	}
	
	/**
	 * 获取时间指定输出格式(yyyy-MM-dd HH:mm:ss)
	 * 最开始是出于选文件的浏览器考虑
	 * xinwang.jiao
	 * 2007-9-29
	 * @param myDate
	 * @return
	 */
	public static String formatDate(String strFormat,Date myDate) 
	{
		//String strFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(strFormat);
		String strDate = formatter.format(myDate);
		return strDate;
	}
	
	/**
	 * 判断内容管理系统中是否允许indextype的搜索引擎
	 * @param indextype
	 * @return
	 */
	public static boolean enableIndexType(String indextype)
	{
		//默认为CMS
		String indextype_sys = ConfigManager.getInstance().getConfigValue("cms.indexs.engine.type",CMSUtil.INDEX_ENGINE_TYPE_LUCENE);
		return indextype_sys.equalsIgnoreCase(indextype);
	}
	
	/**
	 * 判断内容管理系统中是否开启索引实时更新
	 * @param indextype
	 * @return
	 */
	public static boolean enableIndex()
	{
		//默认是关闭索引实时更新
		return ConfigManager.getInstance().getConfigBooleanValue("cms.indexs.switch",false);
		
	}
	public static List<String> tokens(String path)
	{
		StringTokenizer tokens = new StringTokenizer(path, "/",
				false);
		List<String> dd = new ArrayList<String>(tokens.countTokens());
		while(tokens.hasMoreTokens())
			dd.add(tokens.nextToken());
		return dd;
	}
	public static void main (String[] args)
	{
//		int count = count("123456a",'a');
//		System.out.println(count);
//		String url = "aa/bb/";
//		List lsts = paserPathTokens(url);
//		List oldes = tokens(url);
//		url = "aa/bb/c";
//		lsts = paserPathTokens(url);
//		oldes = tokens(url);
//		url = "aa/<cms:aaaa/>";
//		lsts = paserPathTokens(url);
//		oldes = tokens(url);
//		url = "/aa/<cms:aaaa/>";
//		lsts = paserPathTokens(url);
//		oldes = tokens(url);
//		url = "/aa/<cms:aaaa/";
//		lsts = paserPathTokens(url);
//		oldes = tokens(url);
//		url = "";
//		lsts = paserPathTokens(url);
//		oldes = tokens(url);
		
		/**
		 * 计算路径fullPath路径对应的文件相对于currentRelativePath的简化路径,例如：
		 * currentRelativePath：indexnews/sports/basketball
		 * fullPath:images/log.gif
		 * 返回的结果为：
		 * ../../../images/log.gif
		 * @param currentRelativePath
		 * @param fullPath
		 * @return
		 */
		String currentRelativePath= "indexnews/sports/basketball";
		String fullPath="images/log.gif";
		String ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		
		currentRelativePath= "/indexnews/sports/basketball";
		fullPath="images/log.gif";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		
		currentRelativePath= "indexnews/sports/basketball/";
		fullPath="images/log.gif";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		currentRelativePath= "/";
		fullPath="images/log.gif";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		currentRelativePath= "";
		fullPath="images/log.gif";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		currentRelativePath= "aaaa/aaaaa/";
		fullPath="/images/log.gif/a";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		currentRelativePath= "aaaa/aaaaa/";
		fullPath="images/log.gif/a/";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
		
		currentRelativePath= "aaaa/aaaaa/";
		fullPath="/images/log.gif/a/";
		ret = getSimplePathFromfullPath(currentRelativePath,
				fullPath) ;
		System.out.println(ret);
	}
	private static String cms_charset = null;
	private static Object lock = new Object();
	public static String getCharset()
	{
		if(cms_charset == null)
		{
			synchronized(lock)
			{
				if(cms_charset == null)
				{
					cms_charset = ConfigManager.getInstance().getConfigValue("cms.charset", CmsEncoder.ENCODING_US_GBK);
				}
				
			}
		}
		
		return cms_charset; 
	}
	
	private static Boolean autoCloseHtmlTag = null;
	private static Object lock2 = new Object();
	public static boolean autoCloseHtmlTag()
	{
		if(autoCloseHtmlTag == null)
		{
			synchronized(lock2)
			{
				if(autoCloseHtmlTag == null)
				{
					boolean temp = ConfigManager.getInstance().getConfigBooleanValue("cms.autoCloseHtmlTag", true);
					autoCloseHtmlTag = new Boolean(temp);
				}
				
			}
		}
		
		return autoCloseHtmlTag.booleanValue(); 
	}
	
	public static List<String> noAutoCloseTagList ;
	public static final Object lock1 = new Object();
	
	public static List<String> getNoAutoCloseTagList()
	{
		if(noAutoCloseTagList == null)
		{
			synchronized(lock1)
			{
				if(noAutoCloseTagList == null)
				{
					noAutoCloseTagList  = new ArrayList<String>();
//					noAutoCloseTagList.add("A");
				}
			}
		}
		return noAutoCloseTagList;
	}
	private static ParamsHandler siteParamsHandler;
	public static ParamsHandler getSiteParamsHandler()
	{
		if(siteParamsHandler != null)
			return siteParamsHandler;
		ParamsHandler temp = ParamsHandler.getParamsHandler("cms.siteparamshandler");
		siteParamsHandler = temp;
		return siteParamsHandler;
		
	}
	public static final String SITE_PARAM_TYPE = "site";
	public static String getSiteParam(String site,String name)
	{
		ParamsHandler siteParamsHandler = CMSUtil.getSiteParamsHandler();
		
		Param param = siteParamsHandler.getParam(site, name, SITE_PARAM_TYPE);
		return String.valueOf(param.getValue());
	}
	
	public static Params getSiteParams(String site)
	{
		ParamsHandler siteParamsHandler = CMSUtil.getSiteParamsHandler();		
		Params param = siteParamsHandler.getParams(site, SITE_PARAM_TYPE);
		return param;
	}
	
	public static Params getSiteParams(String site,HttpServletRequest request)
	{
		Params params_ = (Params)request.getAttribute("CMS.SITE_PARAMS");
		if(params_ != null)
		{
			return params_; 
		}
		ParamsHandler siteParamsHandler = CMSUtil.getSiteParamsHandler();		
		params_ = siteParamsHandler.getParams(site, SITE_PARAM_TYPE);
		request.setAttribute("CMS.SITE_PARAMS", params_);
		return params_;
	}
	private static DocClassManager docClassManager;
	
	public static List<DocClass> getDocClassList(String currentSiteid)
	{
		if(docClassManager == null)
			docClassManager = WebApplicationContextUtils.getWebApplicationContext().getTBeanObject("document.docclassmanager", DocClassManager.class);	
		try {
			return docClassManager.queryListDocclass( currentSiteid);
		} catch (SQLException e) {
			return new ArrayList<DocClass>();
		}
	}

}
