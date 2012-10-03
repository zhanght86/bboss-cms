package com.frameworkset.platform.cms.driver.htmlconverter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.JspTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ObjectTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.ParserException;
import org.w3c.tidy.Tidy;

import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.util.CmsStringUtil;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.util.RegexUtil;
import com.frameworkset.util.StringUtil;

/**
 * Implements the HTML parser node visitor pattern to exchange all links on the
 * page.
 * <p>
 * 
 * @author biaoping.yin
 * 
 * @version $Revision: 1.0 $
 * 
 * @since 6.0.0
 */
public class CmsLinkProcessor extends CmsHtmlParser {

	protected CmsLinkTable m_linkTable;
	
	
	/**
	 * 判断文档正文是否包含分页符,如果包含则需要进行分页处理,如果模版中没有分页标签,则需要
	 * 将文档作为单独的文档进行发布处理,否则
	 */
	protected boolean containSeparatorToken = false;
	
	/**
	 * 判断细览模版中是否包含文档分页标签,发布分页文档时需要进行判断,
	 * 如果细览模版中没有没有分页标签则将文档作为一个文档发布
	 */
	protected boolean containSeparatorIndexTag = false;
	

	/** HTML end. */
	public static final String HTML_END = "</body></html>";

	/** HTML start. */
	public static final String HTML_START = "<html><body>";
	
	/**
	 * jsp代码段<%.....%>的正则表达式匹配模式
	 */
	public static final String PATTERN_JSPCODE_BLOCK = "\\s*<%.*%>\\s*";
	
	/**
	 * JSP自定义标签带标签体匹配模式,例如<cms:cell></cms:cell>
	 */
	public static final String PATTAERN_JSPTAG_HASBODY = "\\s*<[^>]+>.*</[^>]+>\\s*";
	
	
	/**
	 * JSP自定义标签不带标签体匹配模式,例如<cms:cell/>
	 */
	public static final String PATTAERN_JSPTAG_NOBODY = "\\s*<[^/]+/\\s*>\\s*";
	
	
	/**
	 * 分页符标识
	 */
	public static final String PAGE_TOKEN = "CMS_PAGE_SEPARATOR";
	
	/**
	 * 分页符标签
	 */
	public static final String PAGE_TAG = "<CMS_PAGE_SEPARATOR></CMS_PAGE_SEPARATOR>";
	
	
	

	/** The selected encoding to use for parsing the HTML. */
	private String m_encoding;

	/** Processing mode "process links". */
	public static final int PROCESS_LINKS = 1;

	/** Processing mode "replace links". */
	public static final int REPLACE_LINKS = 0;

	
	/**
	 * 定义处理内容类型
	 */
	/** Processing TEMPLATE links". */
	public static final int PROCESS_TEMPLATE = 0;

	/** Processing CONTENT links". */
	public static final int PROCESS_CONTENT = 1;

	/** Processing PUBLISH RESULT links.
	 * 目前没有处理发布结果，所有的链接在发布之前进行处理
	 * 
     */
	public static final int PROCESS_PUBLISHRESULT = 2;
	
	/**
	 * 处理文档保存时的外部链接和内部链接
	 * 对外部链接进行记录
	 * 修改内部链接为相对地址
	 */
	public static final int PROCESS_EDITCONTENT = 3;
	
	/**
	 * 处理模版
	 */
	public static final int PROCESS_EDITTEMPLATE = 4;
	
	/**
	 * 处理读取文档外部链接和内部链接
	 * 对外部链接进行记录
	 * 修改内部链接为相对地址
	 */
	public static final int PROCESS_READCONTENT = 5;
	
	
	/**
	 * 处理读取文档外部链接和内部链接
	 * 对外部链接进行记录
	 * 修改内部链接为相对地址
	 */
	public static final int PROCESS_READTEMPLATE = 6;
	
	
	/**
	 * 备份文档时附件的提取
	 */
	public static final int PROCESS_BACKUPCONTENT = 7;
	
	/**
	 * 备份模版时附件的处理
	 */
	public static final int PROCESS_BACKUPTEMPLATE = 8;

	/**
	 * 处理的脚本类型： 
	 * 0：发布时模版脚本处理（模版分析，动静态页面处理） 
	 * 1：发布时文档正文处理 
	 * 2: 发布结果分析 
	 * 3：提交保存的文档正文处理
	 * 4：提交保存的模版正文处理  
	 * 5: 已读取待编辑的文档正文处理，以便编辑器可以
	 * 6: 已读取待编辑的文档正文处理，以便编辑器可以
	 * 7：备份文档正文处理，以便获取文档相关的附件 
	 * 8: 备份模版时模版正文的处理，以便获取需要同时备份的附件
	 */
	private int handletype = -1;
	
	/**
	 * 模版，文档备份时可以重用这部分origineTemplateLinkTable,origineStaticPageLinkTable,origineDynamicPageLinkTable
	 * 这3个变量来存储要备份的模版和文档附件地址
	 */

	/**
	 * 
	 * 记录模版原始得链接表，以便分发发布结果时分发模版得附件 内部得文件需要分发，外部得链接附件无需分发
	 * 不包括内部页面地址，这些地址需要再做进一步的发布
	 * 
	 * 
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
	 * 外部链接，当保存或编辑文档、模版内容时需要记录外部链接，以便进行本地化处理，暂不实现递归处理
	 */
	private CmsLinkTable externalPageLinkTable;
	
//	/**
//	 * 文档，模版备份时记录需要的所有附件路径
//	 */
//	private CmsLinkTable innerPageLinkTable;
	
	/** Current processing mode. */
	protected int m_mode;

//	/** Indicates if links should be generated for editing purposes. */
//	private boolean m_processEditorLinks;

	/**
	 * The relative path for relative links, if not set, relative links are
	 * treated as external links.
	 */
	private String m_relativePath;

	protected Context context;

	private CMSLink baseUrl = null;

	private int serverPort;

	private String serverProtocol;

	private String serverHost;

	private String serverAddr;

	private String serverName;

	private String relativeImagesPath = null;

	private String relativeProjectPath = null;

	private String relativeStylePath = null;

	private String relativeTemplatePath = null;

	private String contextPath;

	private String templatePath;
	
	private String sitedir;
	
	  /** The tidy to use. */
    Tidy m_tidy;

    public CmsLinkProcessor()
    {
    	super();
    }

	/**
	 * 链接处理器构造函数，用于模版处理
	 * @param context 内容管理上下文
	 * @param m_mode 处理类型：替换链接还是处理链接
	 * @param encode  链接处理字符编码
	 * @param templatePath  模版对应的相对路径
	 */
	public CmsLinkProcessor(HttpServletRequest request,
			String m_relativePath,String sitedir) {
		super(true);
		this.m_encoding = CmsEncoder.ENCODING_UTF_8;
		this.m_mode = REPLACE_LINKS;
		
		this.m_linkTable = new CmsLinkTable();
		this.m_relativePath = m_relativePath;
		this.sitedir = sitedir;
		serverPort = request.getServerPort();
		serverProtocol = request.getProtocol();
		serverHost = request.getRemoteHost();
		serverAddr = request.getRemoteAddr();
		serverName = request.getServerName();
		contextPath = request.getContextPath();

		relativeImagesPath = contextPath + getAbsoluteImagesRootPath();
		relativeProjectPath = contextPath + getAbsoluteProjectRootPath();
		relativeStylePath = contextPath + getAbsoluteStyleRootPath();
		relativeTemplatePath = contextPath + getAbsoluteTemplateRootPath();
		
		
	}
	
	public String getAbsoluteTemplateRootPath()
	{

		return this.getCMSContextPath() +  getWebSiteRootPath() + this.getTemplateRootPath() ;
		
	}
	
	

	public String getTemplateRootPath() {
		String templateRootPath = CMSUtil.getWebTemplateRootPath(this.sitedir);
		return templateRootPath;
	}
	
	public String getCMSContextPath()
	{
		return CMSUtil.getCMSContextPath();
	}
	
	
	/**
	 * 获取样式库相对路径
	 * @return
	 */
	public String getAbsoluteStyleRootPath()
	{
		return this.getCMSContextPath() + this.getWebSiteRootPath() + this.getStyleRootPath();
		
	}
	
	public String getStyleRootPath() {
		String styleRootPath = CMSUtil.getWebStyleRootPath(this.sitedir);
		
		return styleRootPath;
	}
	
	public String getAbsoluteProjectRootPath()
	{
//		if(this.isRootContext())
//			return this.getRendRootPath() + "/" + this.getRendPath();
//		else
//		{
//			return this.parentContext.getAbsoluteRendPath() + "/" + this.getRendPath();
//		}
		return this.getCMSContextPath() +  getWebSiteRootPath() + this.getProjectRootPath();
		
	}
	
	public String getProjectRootPath() {
		String projectRootPath = CMSUtil.getWebProjectRootPath(this.sitedir);
		
		return projectRootPath;
	}
	/**
	 * 获取图片库相对路径
	 * @return
	 */
	public String getAbsoluteImagesRootPath()
	{
		return this.getCMSContextPath() + this.getWebSiteRootPath() + this.getImagesRootPath();
	}
	
	public String getWebSiteRootPath()
	{
		return CMSUtil.getWebSiteRootPath();
	}
	
	/**
	 * 获取图片库物理路径
	 * @return
	 */
	public String getImagesRootPath()
	{
		
		
			
		String imagesRootPath = CMSUtil.getWebImagesRootPath(this.sitedir);
		
		return imagesRootPath;
	}
	
	
	/**
	 * 链接处理器构造函数，用于模版处理
	 * @param context 内容管理上下文
	 * @param m_mode 处理类型：替换链接还是处理链接
	 * @param encode  链接处理字符编码
	 * @param templatePath  模版对应的相对路径
	 */
	public CmsLinkProcessor(Context context, int m_mode, String encode,
			String templatePath) {
		super(true);
		this.m_encoding = encode;
		this.m_mode = m_mode;
		this.context = context;
		this.m_linkTable = new CmsLinkTable();
		m_relativePath = context.getRendPath();
		serverPort = context.getRequestContext()
				.getRequest().getServerPort();
		serverProtocol = context.getRequestContext()
				.getRequest().getProtocol();
		serverHost = context.getRequestContext()
				.getRequest().getRemoteHost();
		serverAddr = context.getRequestContext()
				.getRequest().getRemoteAddr();
		serverName = context.getRequestContext()
				.getRequest().getServerName();
		contextPath = context.getRequestContext()
				.getRequest().getContextPath();

		relativeImagesPath = contextPath + context.getAbsoluteImagesRootPath();
		relativeProjectPath = contextPath + context.getAbsoluteProjectRootPath();
		relativeStylePath = contextPath + context.getAbsoluteStyleRootPath();
		relativeTemplatePath = contextPath + context.getAbsoluteTemplateRootPath();
		
		this.templatePath = templatePath;
	}
	
	/**
	 * 链接处理器构造函数，用于模版处理
	 * @param context 内容管理上下文
	 * @param m_mode 处理类型：替换链接还是处理链接
	 * @param encode  链接处理字符编码
	 * @param templatePath  模版对应的相对路径
	 */
	public CmsLinkProcessor(Context context, int m_mode, String encode,
			String templatePath,String m_relativePath) {
		super(true);
		this.m_encoding = encode;
		this.m_mode = m_mode;
		this.context = context;
		this.m_linkTable = new CmsLinkTable();
		this.m_relativePath = m_relativePath;
		serverPort = context.getRequestContext()
				.getRequest().getServerPort();
		serverProtocol = context.getRequestContext()
				.getRequest().getProtocol();
		serverHost = context.getRequestContext()
				.getRequest().getRemoteHost();
		serverAddr = context.getRequestContext()
				.getRequest().getRemoteAddr();
		serverName = context.getRequestContext()
				.getRequest().getServerName();
		contextPath = context.getRequestContext()
				.getRequest().getContextPath();

		relativeImagesPath = contextPath + context.getAbsoluteImagesRootPath();
		relativeProjectPath = contextPath + context.getAbsoluteProjectRootPath();
		relativeStylePath = contextPath + context.getAbsoluteStyleRootPath();
		relativeTemplatePath = contextPath + context.getAbsoluteTemplateRootPath();
		
		this.templatePath = templatePath;
	}

	/**
	 * 判断当前得是否是处理模版
	 * 
	 * @return
	 */
	protected boolean isTemplateProcess() {
		return this.handletype == PROCESS_TEMPLATE;
	}

	/**
	 * 链接处理器类型
	 * @param context 内容管理上下文
	 * @param m_mode  处理模型，是替换链接，还是处理链接
	 * @param encode  文档处理编码类型
	 */

	public CmsLinkProcessor(Context context, int m_mode, String encode) {
		this(context, m_mode, encode, null);
	}

	// /**
	// * Creates a new link processor.<p>
	// *
	// * @param cms the cms object
	// * @param linkTable the link table to use
	// * @param encoding the encoding to use for parsing the HTML content
	// * @param relativePath additional path for links with relative path (only
	// used in "replace" mode)
	// */
	// public CmsLinkProcessor(CmsObject cms, CmsLinkTable linkTable, String
	// encoding, String relativePath) {
	//
	// // echo mode must be on for link processor
	// super(true);
	//
	// m_cms = cms;
	// if (m_cms != null) {
	// try {
	// m_rootCms = OpenCms.initCmsObject(cms);
	// m_rootCms.getRequestContext().setSiteRoot("/");
	// } catch (CmsException e) {
	// // this should not happen
	// m_rootCms = null;
	// }
	// }
	// m_linkTable = linkTable;
	// m_encoding = encoding;
	// m_processEditorLinks = ((null != m_cms) && (null !=
	// m_cms.getRequestContext().getAttribute(
	// CmsRequestContext.ATTRIBUTE_EDITOR)));
	// m_relativePath = relativePath;
	// }

	/**
	 * Escapes all <code>&</code>, e.g. replaces them with a
	 * <code>&amp;</code>.
	 * <p>
	 * 
	 * @param source
	 *            the String to escape
	 * @return the escaped String
	 */
	public static String escapeLink(String source) {

		if (source == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(source.length() * 2);
		int terminatorIndex;
		for (int i = 0; i < source.length(); ++i) {
			char ch = source.charAt(i);
			switch (ch) {
			case '&':
				// don't escape already escaped &amps;
				terminatorIndex = source.indexOf(';', i);
				if (terminatorIndex > 0) {
					String substr = source.substring(i + 1, terminatorIndex);
					if ("amp".equals(substr)) {
						result.append(ch);
					} else {
						result.append("&amp;");
					}
				} else {
					result.append("&amp;");
				}
				break;
			default:
				result.append(ch);
			}
		}
		return new String(result);
	}

	/**
	 * Unescapes all <code>&amp;</code>, that is replaces them with a
	 * <code>&</code>.
	 * <p>
	 * 
	 * @param source
	 *            the String to unescape
	 * @return the unescaped String
	 */
	public static String unescapeLink(String source) {

		if (source == null) {
			return null;
		}
		return CmsStringUtil.substitute(source, "&amp;", "&");

	}

	// /**
	// * Returns the link table this link processor was initialized with.<p>
	// *
	// * @return the link table this link processor was initialized with
	// */
	// public CmsLinkTable getLinkTable() {
	//
	// return m_linkTable;
	// }

	/**
	 * Starts link processing for the given content in processing mode.
	 * <p>
	 * 
	 * Macros are replaced by links.
	 * <p>
	 * 
	 * @param content
	 *            the content to process
	 * @return the processed content with replaced macros
	 * 
	 * @throws ParserException
	 *             if something goes wrong
	 */ 
	public String processLinks(String content) throws ParserException {

		m_mode = PROCESS_LINKS;
		return process(content, m_encoding);
	}

	/**
	 * Starts link processing for the given content in replacement mode.
	 * <p>
	 * 
	 * Links are replaced by macros.
	 * <p>
	 * 
	 * @param content
	 *            the content to process
	 * @return the processed content with replaced links
	 * 
	 * @throws ParserException
	 *             if something goes wrong
	 */
	public String replaceLinks(String content) throws ParserException {

		m_mode = REPLACE_LINKS;
		return process(content, m_encoding);
	}


	/**
	 * Visitor method to process a tag (start).
	 * <p>
	 * 
	 * @param tag
	 *            the tag to process
	 */
	public void visitTag(Tag tag) {
		String tagname = tag.getTagName();		
		boolean isdistributeTag = false;
		if (tag instanceof LinkTag )
		{
			processHrefTag((LinkTag) tag);
		} 
		
		else if (tag instanceof ImageTag) 
		{
			processImageTag((ImageTag) tag);

		} 
		
		else if (tag instanceof BaseHrefTag) {
			this.processBaseHrefTag((BaseHrefTag) tag);
		}
		else if(tag instanceof FrameTag)
		{
			processFrameTag((FrameTag) tag);
		}
		else if(tag instanceof StyleTag)
		{
			processStyleTag((StyleTag) tag);
		}
		else if(tag instanceof ScriptTag)
		{
			processScriptTag((ScriptTag) tag);
			
		}
	
		else if(tag.getTagName().equalsIgnoreCase("link"))
		{
			processLinkTag(tag);
		}
		else if(tag instanceof ObjectTag)
		{
			processObjectTag((ObjectTag)tag);
		}
		else if(tag.getTagName().equalsIgnoreCase("embed"))
		{
			processEmbedTag(tag);
		}
		else if(tag.getAttribute("src") != null && !tag.getAttribute("src").equals(""))
		{
			processSrcOfTag(tag);
			
		}
		
		
		else if(tag.getTagName().equalsIgnoreCase("cms:outline"))//对于概览页面，判断是否是分页概览标签
		{
			String _isList = tag.getAttribute("isList") ;
			if(_isList != null && _isList.trim().equalsIgnoreCase("false"))
				this.isList = false;
		}
		else if(tag.getTagName().equalsIgnoreCase("cms:distribute"))
		{
			isdistributeTag = true;
			this.processDir(tag, LINK_PARSER_DISTRIBUTE);
		}
		
		
		
		processBackgroungPropertyOfTag(tag);
		
		processStylePropertyOfTag(tag);
		
		/**
		 * 判断页面是否包含jsp标签（内部标签和自定义标签）
		 */
		if(!this.containJspTag)

		{
//			System.out.println("tag.getTagName():"+tag.getTagName());
			if((tag instanceof JspTag ) 
					|| tag.getTagName().toLowerCase().startsWith("cms:")) //jsp标签
			{
				this.containJspTag = true;
			}
		}
		
		
		//判断文档正文是否包含分页符
		if(!this.containSeparatorToken && this.handletype == PROCESS_CONTENT )
		{
			if(tag.getTagName().equalsIgnoreCase(PAGE_TOKEN))
			{
				containSeparatorToken = true;
			}
		}
		
//		判断细览模版是否包含分页标签
		if(!containSeparatorIndexTag 
				&& this.handletype == PROCESS_TEMPLATE 
				&& this.context instanceof ContentContext)
		{
			if(tag.getTagName().equalsIgnoreCase("cmsindex"))
			{
				this.containSeparatorIndexTag = true;
			}
		}
		
		
			

		// append text content of the tag (may have been changed by above
		// methods)
		if(!isdistributeTag)
			super.visitTag(tag);
	}
	/**
	 * 处理一般的链接
	 * @param link
	 * @return
	 */
	public CMSLink processDir(Tag tag,int linkhandletype)
	{	
		String dir = tag.getAttribute("dir");
		CMSLink link = m_linkTable.getLink(dir);
		if (link != null) {
			
			return link;
		}
		else
		{
			link = new CMSLink(dir,linkhandletype);
			link.setDirectory(true);
			recordTemplateLink( link);
			m_linkTable.addLink(link);
			return link;
		}
		
	}
	
	
	/**
	 * 标识当前页面是否有分页
	 */
	private boolean isList = true;
	
//	/**
//	 * 当前页起始位置
//	 */
//	private long offset = 0L;
//	/**
//	 * 缺省的每页纪录条数
//	 */
//	private int maxPageItems = 10;
	/**
	 * 处理概览标签，如果是分页概览，记录本信息，系统需要将每个页面都
	 * 发布出来，并且需要获取分页标签的相关属性，包括每页记录数
	 * @param tag
	 */
	protected void processOutlineTag(Tag tag)
	{
		String _isList = tag.getAttribute("isList");
		if(_isList != null && _isList.equalsIgnoreCase("false"))
			isList = false;
		if(!isList)
		{
			String _maxPageItems = tag.getAttribute("maxPageItems");
		}		
	}
	
	/**
	 * 返回文档正文是否包含分页符
	 * @return
	 */
	public boolean containSeparatorToken()
	{
		return this.containSeparatorToken;
	}
	
	/**
	 * 返回细览模版是否包含文档分页标签
	 * @return
	 */
	public boolean containSeparatorIndexTag()
	{
		return this.containSeparatorIndexTag;
	}
	
	/**
	 * 处理标记的src属性
	 * @param tag
	 */
	protected void processSrcOfTag(Tag tag) {
		String src = tag.getAttribute("src");
		int linkhandletype = needProcess(src); 
		if( linkhandletype == LINK_NO_PARSER_NO_DISTRIBUTE)
		{
			return;
		}
		

		CMSLink link = null;
		LinkParser parser = null;
		switch (m_mode) 
		{
			case PROCESS_LINKS:
				link = m_linkTable.getLink(src);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					tag.setAttribute("src",link.getHref());
				}  
				else 
				{
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, src,linkhandletype);
					else {
						link = new CMSLink(src,linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {
						
						this.origineTemplateLinkTable.addLink(link,context);
					}
					
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						tag.setAttribute("src",link.getHref());

				}
				break;
			
			case REPLACE_LINKS:
				link = m_linkTable.getLink(src);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					tag.setAttribute("background",link.getHref());
				} 
				else 
				{
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, src,linkhandletype);
					else {
						link = new CMSLink(src,linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {
						
						this.origineTemplateLinkTable.addLink(link,context);
					}
					
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						tag.setAttribute("src",link.getHref());

				}
				break;
			default: // noop
		}
	}
	
	/**
	 * 处理标记的背景图片
	 * @param tag
	 */
	protected void processBackgroungPropertyOfTag(Tag tag) {
		String background = tag.getAttribute("background");

		
		
		int linkhandletype = needProcess(background); 	
		if(linkhandletype == this.LINK_NO_PARSER_NO_DISTRIBUTE)
		{
			return;
		}
		

		CMSLink link = null;
		LinkParser parser = null;
		switch (m_mode) 
		{
			case PROCESS_LINKS:
				link = m_linkTable.getLink(background);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					tag.setAttribute("background",link.getHref());
				} 
				else 
				{
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, background,linkhandletype);
					else {
						link = new CMSLink(background,linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {
						
						this.origineTemplateLinkTable.addLink(link,context);
					}
					
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						tag.setAttribute("background",link.getHref());

				}
				break;
			
			case REPLACE_LINKS:
				link = m_linkTable.getLink(background);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					tag.setAttribute("background",link.getHref());
				} 
				else 
				{
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, background,linkhandletype);
					else {
						link = new CMSLink(background,linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {
						
						this.origineTemplateLinkTable.addLink(link,context);
					}
					
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						tag.setAttribute("background",link.getHref());

				}
				break;
			default: // noop
			
		}
	}
	
	/**
	 * 处理嵌入式<embed>标签的标记
	 * @param tag
	 */
	protected void processEmbedTag(Tag tag) {
		String scriptsrc = tag.getAttribute("src");
		int linkhandletype = needProcess(scriptsrc);
		if (linkhandletype != LINK_NO_PARSER_NO_DISTRIBUTE) {

			// href attribute is required
			LinkParser parser;

			CMSLink link;

			switch (m_mode) {

				case PROCESS_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(scriptsrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("src",link.getHref());
//						tag.setText(link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, scriptsrc,linkhandletype);
						else
							link = new CMSLink(scriptsrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发会将这些附件分发到发布的目的地
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							this.origineTemplateLinkTable.addLink(link,context);
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						
						/*
						 * 记录编辑文档和模版时的外部链接地址和修改过的本地地址，保存时将远程地址保存为本地文件
						 */
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.origineTemplateLinkTable.addLink(link,context);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("src",link.getHref());
//							tag.setText(link.getHref());
						}
						
	
					}
					break;
	
				case REPLACE_LINKS:
					link = m_linkTable.getLink(scriptsrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("src",link.getHref());
//						tag.setText(link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, scriptsrc,linkhandletype);
						else
							link = new CMSLink(scriptsrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							this.origineTemplateLinkTable.addLink(link,context);
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if(this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE)
						{
							if(link.isInternal())
								this.origineTemplateLinkTable.addLink(link,context);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("src",link.getHref());
//							System.out.println(tag.getText());
//							tag.setText(link.getHref());
						}
					}
					break;


				default: // noop
			}
		}
		
	}
	
	
	protected void processObjectTagParam(ObjectTag tag,String paramName) {
		String scriptsrc = tag.getParameter(paramName);
		if(StringUtil.isEmpty(scriptsrc))
			return;
//		tag.removeAttribute("movie");
		Hashtable parameters = tag.getObjectParams();
//		tag.setObjectParams(parameters);
		int linkhandletype = needProcess(scriptsrc);
		if (linkhandletype != LINK_NO_PARSER_NO_DISTRIBUTE) {

			// href attribute is required
			LinkParser parser;

			CMSLink link;

			switch (m_mode) {

				case PROCESS_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(scriptsrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						parameters.put(paramName,link.getHref());
						
						tag.setObjectParams(parameters);
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, scriptsrc,linkhandletype);
						else
							link = new CMSLink(scriptsrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发会将这些附件分发到发布的目的地
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							this.origineTemplateLinkTable.addLink(link,context);
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						
						/*
						 * 记录编辑文档和模版时的外部链接地址和修改过的本地地址，保存时将远程地址保存为本地文件
						 */
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.origineTemplateLinkTable.addLink(link,context);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							parameters.put(paramName,link.getHref());
							tag.setObjectParams(parameters);
						}
						
	
					}
					break;
	
				case REPLACE_LINKS:
					link = m_linkTable.getLink(scriptsrc);
					if (link != null) {
						parameters.put(paramName,link.getHref());
						tag.setObjectParams(parameters);
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, scriptsrc,linkhandletype);
						else
							link = new CMSLink(scriptsrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							this.origineTemplateLinkTable.addLink(link,context);
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if(this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE)
						{
							if(link.isInternal())
								this.origineTemplateLinkTable.addLink(link,context);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							parameters.put(paramName,link.getHref());
							tag.setObjectParams(parameters);
						}
					}
					break;


				default: // noop
			}
		}
	}
	/**
	 * 处理object标签
	 * @param tag
	 */
	protected void processObjectTag(ObjectTag tag) {
		
		processObjectTagParam(tag,"MOVIE");
		processObjectTagParam(tag,"SRC");
		
	}

	/**
	 * 处理javascript脚本标记
	 * @param tag
	 */
	protected void processScriptTag(ScriptTag tag) {
		String scriptsrc = tag.getAttribute("src");
		int linkhandletype = needProcess(scriptsrc);
		if (linkhandletype != this.LINK_NO_PARSER_NO_DISTRIBUTE) {

			// href attribute is required
			LinkParser parser;

			CMSLink link;

			switch (m_mode) {

				case PROCESS_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(scriptsrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("src",link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, scriptsrc,linkhandletype);
						else
							link = new CMSLink(scriptsrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发会将这些附件分发到发布的目的地
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							this.origineTemplateLinkTable.addLink(link,context);
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						/*
						 * 记录编辑文档和模版时的外部链接地址和修改过的本地地址，保存时将远程地址保存为本地文件
						 */
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.origineTemplateLinkTable.addLink(link,context);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("src",link.getHref());
						}
						
	
					}
					break;
	
				case REPLACE_LINKS:
					link = m_linkTable.getLink(scriptsrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("src",link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, scriptsrc,linkhandletype);
						else
							link = new CMSLink(scriptsrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							this.origineTemplateLinkTable.addLink(link,context);
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if(this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE)
						{
							if(link.isInternal())
								this.origineTemplateLinkTable.addLink(link,context);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("src",link.getHref());
						}
					}
					break;


				default: // noop
			}
		}
		
	}

	protected void handleStyleLink(String relativeFilePath)
	{
		if(this.context.getPublishMonitor().containDistributePage(context.getSiteID(),relativeFilePath))
			return;
//		System.out.println("relativeFilePath>>>>>>>>>>:" + relativeFilePath);
		String path = CMSUtil.getPath(CMSUtil.getAppRootPath() ,
				  this.context.getAbsoluteTemplateRootPath() + "/" + relativeFilePath);
		String dir = CMSUtil.getDirectroy(relativeFilePath);
		File f = new File(path);
		if(f.exists() && !f.isDirectory())
		{
			CMSCodeParser coderParser = this.create(f);
			coderParser.parser();
			
			String[] urls = coderParser.getResult();
			if(urls != null && urls.length > 0)
			{
				CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,dir);
				processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
				for(int i = 0; urls != null && i < urls.length; i ++)
				{
					
					processor.processHref(urls[i],LINK_PARSER_DISTRIBUTE);	
					//CMSUtil.getPathFromSimplePath(dir,urls[i]);
				}
			}
		}
	}
	/**
	 * 处理样式标记,
	 * 如果是引用的样式文件则分析处理样式文件，
	 * 从中提取包含的附件地址
	 * @param tag
	 */
	protected void processStyleTag(StyleTag tag) {
		
		String stylesrc = tag.getAttribute("src");
		int linkhandletype = needProcess(stylesrc); 
		if (linkhandletype != this.LINK_NO_PARSER_NO_DISTRIBUTE) {

			// href attribute is required
			

			CMSLink link = this.dealStyleCMSLink(stylesrc,linkhandletype);

			if(link != null)
			{
				if(this.handletype == PROCESS_BACKUPCONTENT 
						|| this.handletype == PROCESS_BACKUPTEMPLATE)
				{
//					if(link.isInternal())
//						this.origineTemplateLinkTable.addLink(link);
				}
				/*
				 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
				 */
				else
				{
					tag.setAttribute("src",link.getHref());
				}
			}
			
		}
		else //处理页面中引用的样式代码段定义中的包含附件链接地址
		{
			
//			String styleCode = tag.getStyleCode();//没有找到相应的设置方法，有待进一步的商讨和考虑
			
			String styleCode = tag.getStyleCode();
			String _styleCode = this.handleStyleCode(styleCode);
			if(_styleCode != null)
				tag.setStyleCode(_styleCode);
			
		}
		
	}
	
	/**
	 * 对样式文件链接进行内部处理
	 * @param href
	 * @return
	 */
	protected CMSLink dealStyleCMSLink(String href,int linkhandletype)
	{
		LinkParser parser;

		CMSLink link;

		switch (m_mode) {

			case PROCESS_LINKS:
				// macros are replaced with links
				link = m_linkTable.getLink(href);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					return link;
//					tag.setAttribute("src",link.getHref());
				} else {
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, href,linkhandletype);
					else
						link = new CMSLink(href,linkhandletype);
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {

						this.origineTemplateLinkTable.addLink(link,context);
						handleStyleLink(link.getRelativeFilePath());
					}
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					/*
					 * 备份文档或模版时，记录待备份的css文件，不需要修改链接
					 */
					if(this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE)
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else
					{
//						tag.setAttribute("src",link.getHref());
						return link;
					}

				}
				break;

			case REPLACE_LINKS:
				link = m_linkTable.getLink(href);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
//					tag.setAttribute("src",link.getHref());
					return link;
				} else {
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, href,linkhandletype);
					else
						link = new CMSLink(href,linkhandletype);
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {

						this.origineTemplateLinkTable.addLink(link,context);
						handleStyleLink(link.getRelativeFilePath());
					}
					
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					/*
					 * 备份文档或模版时，记录待备份的内部js文件，不需要修改链接
					 */
					if(this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE)
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else
					{
//						tag.setAttribute("src",link.getHref());
						return link;
					}

				}
				break;


			default: // noop
		}
		return null;
	}
	
	/**
	 * 对样式串中的链接进行处理，以便
	 * @param styleCode
	 */
	protected String handleStyleCode(String styleCode)
	{
		CMSCodeParser coderParser = this.create(styleCode);
		coderParser.parser();
		String[] urls = coderParser.getResult();
	
		boolean needReplace = false;
//			//记录样式中需要分发的urls
		for(int i = 0; urls != null && i < urls.length; i ++)
		{
			
			if(urls[i].startsWith("<cms:uri"))
			{
				this.containJspTag = true;
				continue;
			}
			CMSLink cmslink = this.processLink(urls[i]);
			/**
			 * 如果执行的是备份操作，无需修改原始代码的链接，否则需要修改
			 */
			if((this.handletype == PROCESS_BACKUPCONTENT 
					|| this.handletype == PROCESS_BACKUPTEMPLATE) )
			{
				
			}
			else
			{
				needReplace = true;
				/**
				 * 如果链接已经替换过了，就不需要再替换了
				 */
				if(!cmslink.isHanded())
				{
					styleCode = StringUtil.replaceAll(styleCode,urls[i],cmslink.getHref());
					cmslink.setHanded(true);					
				}
			}			
		}
		if(needReplace)
			return styleCode;
		else
			return null;
	}
	
	/**
	 * 处理style属性中应用的链接和图片，对于有些图片已经用<cms:uri>标签处理过时，是不需要再进行处理的
	 * @param tag
	 */
	protected void processStylePropertyOfTag(Tag tag)
	{
		String style = tag.getAttribute("style");
		int linkhandletype = needProcessStyleAtrribute(style);
		if(linkhandletype == LINK_NO_PARSER_NO_DISTRIBUTE)
			return ;
		
		style = this.handleStyleCode(style);
		if(style != null)
			tag.setAttribute("style",style);		
	}
	
	

	/**
	 * 处理frame,iframe标记
	 * @param tag
	 */
	protected void processFrameTag(FrameTag tag) {
		String framesrc = tag.getAttribute("src");
		int linkhandletype = needProcess(framesrc);
		if (linkhandletype != LINK_NO_PARSER_NO_DISTRIBUTE) {

			// href attribute is required
			LinkParser parser;

			CMSLink link;

			switch (m_mode) {

				case PROCESS_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(framesrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("src",link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, framesrc,linkhandletype);
						else
							link = new CMSLink(framesrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
							recordTemplateLink( link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.recordTemplateLink(link);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("src",link.getHref());
						}
	
					}
					break;
	
				case REPLACE_LINKS:
					link = m_linkTable.getLink(framesrc);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("src",link.getHref());
					} 
					else 
					{
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, framesrc,linkhandletype);
						else
							link = new CMSLink(framesrc,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							recordTemplateLink( link);
						}
						
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(link.needdownload())
									this.externalPageLinkTable.addLink(link);
							}
						}
						/*
						 * 备份文档或模版时，记录待备份的js文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) && link.isInternal())
						{
							if(link.isInternal() )
								this.recordTemplateLink(link);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("src",link.getHref());
						}
					}
					break;


				default: // noop
			}
		}
		
	}
	
	
	

	// /**
	// * 对链接进行处理
	// * @param link
	// * @return
	// */
	// protected String handlerLink(String link)
	// {
	// return link;
	// }
	/**
	 * Process an image tag.
	 * <p>
	 * 
	 * @param tag
	 *            the tag to process
	 */
	protected void processImageTag(ImageTag tag) {

		String href_ = tag.getAttribute("src");
		int linkhandletype = needProcess(href_);
		if (linkhandletype != LINK_NO_PARSER_NO_DISTRIBUTE) {
			

			CMSLink link = null;
			LinkParser parser = null;
			switch (m_mode) {

			case PROCESS_LINKS:
				link = m_linkTable.getLink(tag.getImageURL());
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					tag.setImageURL(link.getHref());
				} else {
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, tag.getImageURL(),linkhandletype);
					else {
						link = new CMSLink(tag.getImageURL(),linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {
						
						this.origineTemplateLinkTable.addLink(link,context);
					}
					
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						tag.setImageURL(link.getHref());

				}
				break;
			// macros are replaced with links
			// link =
			// m_linkTable.getLink(CmsMacroResolver.stripMacro(tag.getImageURL()));
			// if (link != null) {
			// tag.setImageURL(processLink(link));
			// }
			// break;

			case REPLACE_LINKS:
				link = m_linkTable.getLink(tag.getImageURL());
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					tag.setImageURL(link.getHref());
				} else {
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, tag.getImageURL(),linkhandletype);
					else {
						link = new CMSLink(tag.getImageURL(),linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {

						this.origineTemplateLinkTable.addLink(link,context);
					}
					m_linkTable.addLink(link);
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						tag.setImageURL(link.getHref());

				}
				break;
			

			default: // noop
			}
		}
	}
	
	/**
	 * Process an image tag.
	 * <p>
	 * 
	 * @param tag
	 *            the tag to process
	 */
	protected CMSLink processLink(String linkurl) {
		int linkhandletype = needProcess(linkurl); 
		if (linkhandletype != this.LINK_NO_PARSER_NO_DISTRIBUTE) {

			CMSLink link = null;
			LinkParser parser = null;
			switch (m_mode) {

			case PROCESS_LINKS:
				link = m_linkTable.getLink(linkurl);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					return link;
				} else {
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, linkurl,linkhandletype);
					else {
						link = new CMSLink(linkurl,linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {
						
						this.origineTemplateLinkTable.addLink(link,context);
					}
					
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
						return link;

				}
				break;
			// macros are replaced with links
			// link =
			// m_linkTable.getLink(CmsMacroResolver.stripMacro(tag.getImageURL()));
			// if (link != null) {
			// tag.setImageURL(processLink(link));
			// }
			// break;

			case REPLACE_LINKS:
				link = m_linkTable.getLink(linkurl);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
//					tag.setImageURL(link.getHref());
					return link;
				} else {
					if (this.baseUrl != null)
						link = new CMSLink(baseUrl, linkurl,linkhandletype);
					else {
						link = new CMSLink(linkurl,linkhandletype);
					}
					parser = new LinkParser(link);
					link = parser.getResult();
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {

						this.origineTemplateLinkTable.addLink(link,context);
					}
					m_linkTable.addLink(link);
					if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
					{
						if(link.isInternal() )
						{
//								this.innerPageLinkTable.addLink(link);
						}
						else
						{
							//记录外部链接
							if(link.needdownload())
								this.externalPageLinkTable.addLink(link);
						}
					}
					if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
					{						
						if(link.isInternal())
							((ContentContext)this.context).addLink(link);
					}
					/*
					 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
					 */
					if((this.handletype == PROCESS_BACKUPCONTENT 
							|| this.handletype == PROCESS_BACKUPTEMPLATE) )
					{
						if(link.isInternal())
							this.origineTemplateLinkTable.addLink(link,context);
					}
					/*
					 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
					 */
					else					
//						tag.setImageURL(link.getHref());
						return link;

				}
				break;


			default: // noop
			}
		}
		return null;
	}

	protected void processBaseHrefTag(BaseHrefTag baseTag) {
		this.baseUrl = new CMSLink(baseTag.getBaseUrl(),true,LINK_PARSER_DISTRIBUTE);
		LinkParser parser = new LinkParser(baseUrl);
		baseUrl = parser.getResult();

	}
	/**
	 * 记录模板解析过程中相关静态链接资源
	 */
	protected void recordTemplateLink(CMSLink link)
	{
		
		if(link.isJavaScript())
			return;
		if(!link.isDirectory())
		{
			if(link.getRelativeFilePathType() == CMSLink.TYPE_STATIC_PAGE)
			{
				if(!(this.handletype == PROCESS_BACKUPTEMPLATE))
				{
					if(context.getPublishMonitor() != null && !context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath(),false))
						this.origineStaticPageLinkTable.addLink(link);
				}
				else
				{
					this.origineTemplateLinkTable.addLink(link,context);
				}
						
			}
			else if(link.getRelativeFilePathType() == CMSLink.TYPE_DYNAMIC_PAGE)
			{
				if(context.getPublishMonitor() != null && !context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath(),false))
					this.origineDynamicPageLinkTable.addLink(link);
			}
			else	
				this.origineTemplateLinkTable.addLink(link,context);
		}
		else
		{
			this.origineTemplateLinkTable.addLink(link,context);
		}
	}
	/**需要处理地址并且分发资源标识*/
	public static final int LINK_PARSER_DISTRIBUTE = 0;
	/**1 需要处理地址，不需要分发资源 */
	public static final int LINK_PARSER = 1;
	/**不需要处理地址 不需要分发资源*/
	public static final int LINK_NO_PARSER_NO_DISTRIBUTE = 2;
	
	/**
	 * 
	 * 判断是否需要处理链接，以下情况不需要处理：
	 * 	链接为null
	 * 	链接为""
	 * 	链接为#
	 * 	链接为..
	 * 	链接匹配模式:\\S*<%\\S*^(%>)%>\\S*,及java代码块
	 *  链接为带标签体的jsp自定义标签，例如<cms:cell></cms:cell>
	 *  链接为不带标签体的jsp自定义标签,例如<cms:cell/>
	 *  aaa/aaa/
	 *  aaa/<%=aaa%>
	 *  aaa/<xx:ss/>
	 *  /aaa/aaa/
	 *  /aaa/<%=aaa%>
	 *  /aaa/<xx:ss/>
	 * @param link
	 * @return 0  需要处理地址并且分发资源，1 需要处理地址，不需要分发资源 2 不需要处理地址 不需要分发资源
	 */
	protected int needProcess(String link)
	{
		if(link == null || link.trim().equals("") || link.equals("#") || link.equals("..") || link.trim().startsWith("<"))
			return 2;
		else if(RegexUtil.isContain(link,Lexer.jsp_custom_tag_pattern) || RegexUtil.isContain(link,PATTERN_JSPCODE_BLOCK) )
		{
			return 1;
		}
		else
			return 0;
//		if(link == null || link.trim().equals("") 
//				|| link.equals("#") || link.equals("..") 
//				|| RegexUtil.isMatch(link,PATTERN_JSPCODE_BLOCK)
//				|| RegexUtil.isMatch(link,PATTAERN_JSPTAG_NOBODY)
//				|| RegexUtil.isMatch(link,PATTAERN_JSPTAG_HASBODY))
//			return false;
//		return true;
	}
	
	/**
	 * 
	 * 判断是否需要处理链接，以下情况不需要处理：
	 * 	链接为null
	 * 	链接为""
	 * 	链接为#
	 * 	链接为..
	 * 	链接匹配模式:\\S*<%\\S*^(%>)%>\\S*,及java代码块
	 *  链接为带标签体的jsp自定义标签，例如<cms:cell></cms:cell>
	 *  链接为不带标签体的jsp自定义标签,例如<cms:cell/>
	 *  aaa/aaa/
	 *  aaa/<%=aaa%>
	 *  aaa/<xx:ss/>
	 *  /aaa/aaa/
	 *  /aaa/<%=aaa%>
	 *  /aaa/<xx:ss/>
	 * @param link
	 * @return 0  需要处理地址并且分发资源，1 需要处理地址，不需要分发资源 2 不需要处理地址 不需要分发资源
	 */
	protected int needProcessStyleAtrribute(String link)
	{
		if(link == null || link.trim().equals("") || link.equals("#") || link.equals(".."))
				return 2;
		else
			return 0;
//		if(link == null || link.trim().equals("") 
//				|| link.equals("#") || link.equals("..") 
//				|| RegexUtil.isMatch(link,PATTERN_JSPCODE_BLOCK)
//				|| RegexUtil.isMatch(link,PATTAERN_JSPTAG_NOBODY)
//				|| RegexUtil.isMatch(link,PATTAERN_JSPTAG_HASBODY))
//			return false;
//		return true;
	}

	/**
	 * Process a link tag.
	 * <p>
	 * 
	 * @param tag
	 *            the tag to process
	 */
	protected void processHrefTag(LinkTag tag) {
		String href_ = tag.getAttribute("href");
		int linkhandletype=needProcess(href_);
		if (linkhandletype != this.LINK_NO_PARSER_NO_DISTRIBUTE) {
			

			// href attribute is required
			LinkParser parser;

			CMSLink link;

			switch (m_mode) {

				case PROCESS_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(tag.getLink());
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setLink(link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, tag.getLink(),linkhandletype);
						else
							link = new CMSLink(tag.getLink(),linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							recordTemplateLink( link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(!link.isJavaScript)
								{
									if(link.needdownload())
										this.externalPageLinkTable.addLink(link);
									
								}
							}
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
							{
								if(CMSUtil.isBinaryFile(link.getRelativeFilePath()))
									((ContentContext)this.context).addLink(link);
							}
						}
						/*
						 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.recordTemplateLink(link);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setLink(link.getHref());
						}
					}
					break;
	
				case REPLACE_LINKS:
					// links are replaced with macros
	
					String targetUri = tag.extractLink();
	
					link = m_linkTable.getLink(targetUri);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setLink(link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, targetUri,linkhandletype);
						else
							link = new CMSLink(targetUri,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 发布时，记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							recordTemplateLink( link);
						}
						
						
						if(this.handletype == PROCESS_EDITCONTENT 
							|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//								this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(!link.isJavaScript)
								{
									if(link.needdownload())
										this.externalPageLinkTable.addLink(link);
								}
							}
						}
						
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
							{
								if(CMSUtil.isBinaryFile(link.getRelativeFilePath()))
									((ContentContext)this.context).addLink(link);
							}
						}
						/*
						 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.recordTemplateLink(link);
						}
						
						
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setLink(link.getHref());
						}
	
					}
					break;
	
				default: // noop
			}
		}
	}
	

	/**
	 * Process a link tag.
	 * <p>
	 * 
	 * @param tag
	 *            the tag to process
	 */
	protected void processLinkTag(Tag tag) {

		String href_ = tag.getAttribute("href");
		int linkhandletype = needProcess(href_);
		if (linkhandletype != this.LINK_NO_PARSER_NO_DISTRIBUTE) {
			
			// href attribute is required
			LinkParser parser;

			CMSLink link;
			String href = tag.getAttribute("href");

			switch (m_mode) {

				case PROCESS_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(href);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("href",link.getHref());
					} else {
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, href,linkhandletype);
						else
							link = new CMSLink(href,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							recordTemplateLink( link);
							if(isStyleLink(href))
							{
								handleStyleLink(link.getRelativeFilePath());
							}
						}
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
								//记录外部链接
								if(!link.isJavaScript())
								{
									if(link.needdownload())
										this.externalPageLinkTable.addLink(link);
								}
							}
						}
						/*
						 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.recordTemplateLink(link);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("href",link.getHref());
						}
					}
					break;
	
				case REPLACE_LINKS:
					// macros are replaced with links
					link = m_linkTable.getLink(href);
					if (link != null) {
						// tag.setLink(escapeLink(link.getHref()));
						tag.setAttribute("href",link.getHref());
					} 
					else 
					{
						if (this.baseUrl != null)
							link = new CMSLink(baseUrl, href,linkhandletype);
						else
							link = new CMSLink(href,linkhandletype);
						parser = new LinkParser(link);
						link = parser.getResult();
						m_linkTable.addLink(link);
						/*
						 * 记录内部模板附件路径，以便后续得附件分发
						 */
						if (this.isTemplateProcess() && link.isInternal()) {
	
							recordTemplateLink( link);
							if(isStyleLink(href))
							{
								handleStyleLink(link.getRelativeFilePath());
							}
						}
						if(this.handletype == PROCESS_CONTENT && context instanceof ContentContext)
						{						
							if(link.isInternal())
								((ContentContext)this.context).addLink(link);
						}
						if(this.handletype == PROCESS_EDITCONTENT 
								|| this.handletype == PROCESS_EDITTEMPLATE)
						{
							if(link.isInternal() )
							{
//									this.innerPageLinkTable.addLink(link);
							}
							else
							{
//								记录外部链接
								if(!link.isJavaScript())
								{
									if(link.needdownload())
										this.externalPageLinkTable.addLink(link);
								}
							}
						}
						/*
						 * 备份文档或模版时，记录待备份的内部链接文件，不需要修改链接
						 */
						if((this.handletype == PROCESS_BACKUPCONTENT 
								|| this.handletype == PROCESS_BACKUPTEMPLATE) )
						{
							if(link.isInternal())
								this.recordTemplateLink(link);
						}
						/*
						 * 除了备份文档或模版外，其他情况都需要修改链接文档中的链接
						 */
						else
						{
							tag.setAttribute("href",link.getHref());
						}
					}
					break;
				default: // noop
			}
		}
	}


	// /**
	// * Returns the processed link of a given link.<p>
	// *
	// * @param link the link
	// * @return processed link
	// */
	// private String processLink(CMSLink link) {
	//    	
	//
	// // if (link.isInternal()) {
	// //
	// //// // if we have a local link, leave it unchanged
	// //// // cms may be null for unit tests
	// //// if ((m_cms == null) || (link.getUri().length() == 0) ||
	// (link.getUri().charAt(0) == '#')) {
	// //// return link.getUri();
	// //// }
	// //
	// // // Explanation why the "m_processEditorLinks" variable is required:
	// // // If the VFS is browsed in the root site, this indicates that a user
	// has switched
	// // // the context to the / in the Workplace. In this case the workplace
	// site must be
	// // // the active site. If normal link processing would be used, the site
	// root in the link
	// // // would be replaced with server name / port for the other sites. But
	// if a user clicks
	// // // on such a link he would leave the workplace site and loose his
	// session.
	// // // A result is that the "direct edit" mode does not work since he in
	// not longer logged in.
	// // // Therefore if the user is NOT in the editor, but in the root site,
	// the links are generated
	// // // without server name / port. However, if the editor is opened, the
	// links are generated
	// // // _with_ server name / port so that the source code looks identical
	// to code
	// // // that would normally created when running in a regular site.
	// //
	// //// // we are in the opencms root site but not in edit mode - use link
	// as stored
	// //// if (!m_processEditorLinks &&
	// (m_cms.getRequestContext().getSiteRoot().length() == 0)) {
	// //// return OpenCms.getLinkManager().substituteLink(m_cms,
	// link.getUri());
	// //// }
	// ////
	// //// // otherwise get the desired site root from the stored link
	// //// // if there is no site root, we have a /system link (or the site was
	// deleted),
	// //// // return the link prefixed with the opencms context
	// //// String siteRoot = link.getSiteRoot();
	// //// if (siteRoot == null) {
	// //// return OpenCms.getLinkManager().substituteLink(m_cms,
	// link.getUri());
	// //// }
	// ////
	// //// // return the link with the server prefix, if necessary
	// //// return OpenCms.getLinkManager().substituteLink(m_cms,
	// link.getVfsUri(), siteRoot);
	// // String link_s = link.getHref();
	// // String contextPath =
	// this.context.getRequestContext().getRequest().getContextPath();
	// // URL test = null;
	// // try {
	// // test = new URL(link_s);
	// // } catch (MalformedURLException e) {
	// // return link_s;
	// // }
	// // System.out.println("file:" + test.getFile());
	// // System.out.println("host:" + test.getHost());
	// //
	// // System.out.println("path:" + test.getPath());
	// // System.out.println("port:" + test.getPort());
	// // System.out.println("query:" + test.getQuery());
	// // System.out.println("ref:" + test.getRef());
	// // if(!link_s.startsWith("/"))
	// // {
	// // if(link_s.toLowerCase().startsWith("http://"))
	// // {
	// // String protocol =
	// this.context.getRequestContext().getRequest().getProtocol();
	// // int port =
	// this.context.getRequestContext().getRequest().getServerPort();
	// //
	// // }
	// // }
	// // else
	// // {
	// // if(link_s.startsWith(contextPath + context.getAbsoluteImagesPath()))
	// // {
	// //
	// // }
	// // else if(link_s.startsWith(context.getAbsoluteImagesPath()))
	// // {
	// //
	// // }
	// // else if(link_s.startsWith("../../../"))
	// // {
	// //
	// // }
	// //
	// // }
	// //
	// // return "";
	// // } else {
	// //
	// // // don't touch external links
	// // return link.getUri();
	// // }
	// }
	// /**
	// * 包含完整url（即协议、主机、端口、路径、参数）的匹配正则表达式
	// */
	// public static final String urlpattern =
	// "(http://)(([a-zA-Z0-9]+\\.)*[a-zA-Z0-9]+):([0-9]{0,4})(((/[^/^?]*)|(\\\\[^\\\\^?]*))+)\\?(.+)";
	// /**
	// * 分析带协议头的链接,并提取该协议头
	// * http://主机:端口
	// */
	// public static final String protocolpattern =
	// "(https?://([a-zA-Z0-9]+\\.)*[a-zA-Z0-9]+:?[0-9]{0,4})((/[^/^?]*)|(\\\\[^\\\\^?]*))+\\?.+";
	/**
	 * 分析纯域名的的链接，并且提取域名 xxxx.xxxx.xxxx:端口
	 */
	public static final String domainpattern = "http://([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+/?";

	/**
	 * 分析带绝对uri的链接，提取uri串 /xxx/xxxx等等
	 */
	public static final String uripattern = "((/+[^/]*)|(\\\\+[^\\\\]*))+";

	/**
	 * 分析带相对uri的链接，提取uri串 /xxx/xxxx等等
	 */
	public static final String r_uripattern = "(([^/^\\\\]+/*)|([^\\\\^/]+\\\\*))+";

	/**
	 * 分析带../相对uri的链接，提取uri串 ../../xxx/xxxx ./xxx/xxxx等等
	 */
	public static final String simple_uripattern = "(\\.{1,2}/)+[^/]*((/+[^/]*)|(\\\\+[^\\\\]*))*";
	
	
	/**
	 * 内容管理系统
	 */
	public static final String cmstag_pattern = "<cms:[^/^>]+/?>.*";

	/**
	 * 内容管理link信息
	 * 
	 * @author biaoping.yin
	 * 
	 */
	public static class CMSLink {
		CMSLink base;

		CMSLink origineLink;
		
		
		boolean isCmsTag = false;
		/**link是否为目录，如果为目录，那么直接分发目录*/
		boolean isDirectory = false;
		Map<String,Long> fileCacheTimeStamp ;
		
		/**
		 * 标识连接对应的文件是否已经发布过，
		 * 如果已经发布过则不再发布，但是如果在进行发布时，选择了强制发布已经发布过的连接文件
		 * 那么重新执行发布文件的操作，之所以这么做就是要考虑系统性能的问题
		 * 这些连接的范围包括：图片，
		 * 				   js，
		 * 				   媒体文件，
		 * 				   css,
		 *                 flash文件，
		 *                 不含标识的html页面等等
		 * released变量的值：true-标识已经发布过，如果没有强制设置，将不再发布该文件，在系统启动的过程中这个标识将一直存在，系统将定时将这些信息存储到
		 *                      数据库中 
		 * 				   false-标识未发布过，系统将重新发布该文件，发布成功后将released的值设置为true
		 */
		boolean released = false;
		
		/**
		 * 判断链接是否是模板目录下的文件链接，文档编辑和备份、发布的时候需要考虑
		 */
		boolean istemplate = false;
		
		/**
		 * 远程地址对应的本地还地址
		 */
		String toLocalPath;
		
		/**
		 * 判断链接是否是已经处理过的链接
		 */
		boolean handed = false;
		
		/**
		 * 外部连接的时间戳每次发布时识别被缓冲的文件的内容是否被改变，就是通过对比最近
		 * 发布该文件时的时间戳和当前文件的时间戳是否一样，如果一样则不发布该文件，否则不管released是否为true，都将发布该文件
		 * 保证系统的正常工作
		 */
		long fileTimestamp = -1l;
		
		

		/**
		 * 文档路径
		 */
		public static final int TYPE_WEBPRJ = 0;

		/**
		 * 模版路径
		 */
		public static final int TYPE_TEMPLATE = 1;

		/**
		 * 图片路径
		 */
		public static final int TYPE_IMAGES = 2;

		/**
		 * 样式路径
		 */
		public static final int TYPE_STYLE = 3;

		/**
		 * 绝对路径
		 */
		public static final int TYPE_ABSOLUTE = 4;
		
		/**
		 * 动态页面路径
		 */
		public static final int TYPE_DYNAMIC_PAGE = 5;
		
		
		/**
		 * 静态页面路径
		 */
		public static final int TYPE_STATIC_PAGE = 6;
		
		/**
		 * CSS路径
		 */
		public static final int TYPE_STATIC_CSS = 199;
		
		/**
		 * 图片、js路径
		 */
		public static final int TYPE_STATIC_RESOURCE = 198;
		
		
		
		/**
		 * 文档目录中静态页面路径
		 */
		public static final int TYPE_WEBPRJ_STATIC_PAGE = 7;
		
		/**
		 * 文档目录中的动态页面路径
		 */
		public static final int TYPE_WEBPRJ_DYNAMIC_PAGE = 8;
		
		/**
		 * 二进制类型文件路径
		 */
		public static final int TYPE_BINARY_PAGE = 9;
		
		/**
		 * 目录类型页面
		 */
		public static final int TYPE_DIRECTORY = 10;

		/**
		 * 链接对应的文件路径类型
		 */
		int relativeFilePathType = -1;

		/**
		 * 链接对应得系统文件相关路径,并且不包含链接参数，链接的参数存放在parameters属性中
		 */
		String relativeFilePath;
		
		/**
		 * 链接参数，如果链接包含参数，则将参数存放在本字段中，格式为
		 * name=value&name1=value1..&..
		 */
		String parameters = null;

		/**
		 * 良好的url true-良好 false-非良好
		 */
		boolean malformedURL = false;

		/**
		 * 是否是base href
		 */
		boolean hasBase = false;

		boolean isJavaScript = false;

		String href = null;

		/**
		 * 标识是否是内部链接 true－是 false－否
		 */
		boolean isInternal = true;
		
		boolean isBase = false;
		
		boolean needdownload = false;
		
		int linkhandletype;
		boolean isParserAndDistribute = true;

		public CMSLink(CMSLink base, String href,int linkhandletype) {
			if (base != null)
				this.hasBase = true;
			this.base = base;
			this.href = href;
			this.linkhandletype = linkhandletype;
			isParserAndDistribute = this.linkhandletype == LINK_PARSER_DISTRIBUTE;
		}
		
		public boolean isParserAndDistribute()
		{
			return isParserAndDistribute; 
		}

		public CMSLink(String href,boolean isBase,int linkhandletype) {
			this(null, href,linkhandletype);
			this.isBase = isBase;
		}
		
		public CMSLink(String href,int linkhandletype) {
			this(null, href,linkhandletype);
		}

		public void setFileTimestamp(long newfileTimestamp)
		{
			this.fileTimestamp = newfileTimestamp;
		}
		
		public boolean isModified(long newfileTimestamp)
		{			
			return fileTimestamp != newfileTimestamp;			
		}
		public boolean isModified(long newfileTimestamp,String canonicalPath)
		{
			Long fileTimestamp = -1L;
			if(this.fileCacheTimeStamp == null)
			{
				fileCacheTimeStamp = new HashMap<String,Long >();
				fileCacheTimeStamp.put(canonicalPath, new Long(newfileTimestamp));
				return true;
			}
			else
			{
				fileTimestamp = fileCacheTimeStamp.get(canonicalPath);
				if(fileTimestamp == null)
				{
					fileCacheTimeStamp.put(canonicalPath, new Long(newfileTimestamp));
					return true;
				}
				else
				{
					if( fileTimestamp.longValue() != newfileTimestamp)
					{
						fileCacheTimeStamp.put(canonicalPath, new Long(newfileTimestamp));
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			
		}
		public String getHref() {
			return href;
		}
		
		public boolean isTemplate()
		{
			return this.istemplate;
		}
		
		public void setISTemplate(boolean istemplate)
		{
			this.istemplate = istemplate;
		}

		public boolean hasBase() {
			return hasBase;
		}

		public CMSLink getBase() {
			return this.base;
		}

		public boolean isInternal() {
			return isInternal;
		}

		public void setInternal(boolean isInternal) {
			this.isInternal = isInternal;
		}

		public boolean isMalformedURL() {
			return malformedURL;
		}

		public void setMalformedURL(boolean malformedURL) {
			this.malformedURL = malformedURL;
		}

		public CMSLink getOrigineLink() {
			return origineLink;
		}

		public void setOrigineLink(CMSLink origineLink) {
			this.origineLink = origineLink;
		}

		public boolean isJavaScript() {
			return isJavaScript;
		}

		public void setJavaScript(boolean isJavaScript) {
			this.isJavaScript = isJavaScript;
		}

		/**
		 * 获取链接对应得文件路径
		 * 
		 * @return
		 */

		public String getRelativeFilePath() {
			return relativeFilePath;
		}

		public void setRelativeFilePath(String relativeFilePath) {
			this.relativeFilePath = relativeFilePath;
		}

		public int getRelativeFilePathType() {
			return relativeFilePathType;
		}

		public void setRelativeFilePathType(int relativeFilePathType) {
			this.relativeFilePathType = relativeFilePathType;
		}

		public String getToLocalPath() {
			return toLocalPath;
		}

		public void setToLocalPath(String toLocalPath) {
			this.toLocalPath = toLocalPath;
		}

		public boolean isBase() {
			return isBase;
		}

		public boolean isCmsTag() {
			return isCmsTag;
		}

		public void setCmsTag(boolean isCmsTag) {
			this.isCmsTag = isCmsTag;
		}

		public void setNeedDownLoad(boolean needdownload) {
			this.needdownload = needdownload;
			
		}
		
		public String getOriginHref()
		{
			return this.origineLink != null ?origineLink.getHref():this.getHref();
		}
		
		public boolean needdownload()
		{
			return this.needdownload;
		}

		public boolean isHanded() {
			return handed;
		}

		public void setHanded(boolean handed) {
			this.handed = handed;
		}

		public String getParameters() {
			return parameters;
		}

		public void setParameters(String parameters) {
			this.parameters = parameters;
		}

		public boolean isReleased() {
			return released;
		}

		public void setReleased(boolean released) {
			this.released = released;
		}

		public int getLinkhandletype() {
			return linkhandletype;
		}

		public void setLinkhandletype(int linkhandletype) {
			this.linkhandletype = linkhandletype;
		}

		public boolean isDirectory() {
			return isDirectory;
		}

		public void setDirectory(boolean isDirectory) {
			this.isDirectory = isDirectory;
		}

	}
	
	public CMSCodeParser create(String code)
	{
		return new CMSCodeParser(code,context);
	}
	
	
	public CMSCodeParser create(File file)
	{
		return new CMSCodeParser(file,context);
	}

	/**
	 * 实现内容管理中样式、script代码的分析和处理
	 * <p>Title: CMSCodeParser</p>
	 *
	 * <p>Description: </p>
	 *
	 * <p>Copyright: Copyright (c) 2006</p>
	 *
	 * <p>Company: 三一集团</p>
	 * @Date 2007-4-23 10:19:06
	 * @author biaoping.yin
	 * @version 1.0
	 */
	public static class CMSCodeParser
	{
		private String code;
		private File linkFile;
		/**
		 * 处理的脚本类型，如果是文件类型为1，代码类型为0
		 */
		private int codetype = 0; 
		private Context context;
		String[] urls ;
		/**
		 * 样式附件提取正则表达式模式
		 */
		private static final String URL_PATTERN = "\\s*url\\s*\\(([^\\)]+)\\)";
		/**
		 * 传递需要分析和处理的脚本，只需分析提取其中包含的链接信息，无需更新和修改
		 * @param code
		 */
		public CMSCodeParser(String code,Context context)
		{
			this.code = code;
			this.codetype = 0;
			this.urls = null;
			this.linkFile = null;
			this.context = context;
		}
		
		/**
		 * 传递需要分析和处理的链接文件，只需分析提取其中包含的链接信息，无需更新和修改
		 * @param linkFile
		 */
		public CMSCodeParser(File linkFile,Context context)
		{
			this.linkFile = linkFile;
			this.codetype = 1;
			this.code = null;
			this.urls = null;
			this.context = context;
			
		}
		
		/**
		 * 分析脚本
		 *
		 */
		public void parser()
		{
			if(this.codetype == 0)
			{
				
				urls = RegexUtil.containWithPatternMatcherInput(code,URL_PATTERN);
			}
			else
			{
				String content = FileUtil.getFileContent(this.linkFile,context.getCharset());
//				System.out.println(content);
				urls = RegexUtil.containWithPatternMatcherInput(content,URL_PATTERN);
			}
		} 
		
		/**
		 * 获取分析结果
		 * @return
		 */
		public String[] getResult()
		{
			return this.urls;
		}
		
	}
	public class LinkParser {
		CMSLink originelink;

		// String protocol ;
		// int port;
		// String path;
		// String query;
		//    	
		// String host;
		// String ref;
		// /**
		// * 标识链接是内部链接还是外部链接
		// * true标识外部链接
		// * false标识内部链接
		// */
		// boolean isInternal = false;

		/**
		 * 处理过后的新链接
		 */
		CMSLink newLink;

		public LinkParser(CMSLink link) {
			try {
				this.originelink = link;

			} catch (Exception e) {

			}

		}

		public CMSLink getResult() {
			if (newLink != null) {
				return newLink;
			} else {
				parserBase();
				return newLink == null ? this.originelink : newLink;
			}
		}

		// private String evaluatePath(String opath,String currentRelatepath)
		// {
		// String[] word = currentRelatepath.split("/");
		//    		
		// String pre = "";
		// for(int i = 0; i < word.length; i ++)
		// {
		// pre += "../";
		// }
		// opath = pre + opath;
		// return opath;
		// }

		/**
		 * 处理内部地址
		 * 文档、模版发布时需要处理内部地址为相对地址
		 * 文档读取和模版读取时需要处理内部地址为绝对地址,例如：http://ip:port/aaa/b.html
		 */
		private void handInternal(String path,String query,String ref) {
			String webprj = CMSUtil.getPath(relativeProjectPath,m_relativePath);
			String template = CMSUtil.getPath(relativeTemplatePath,m_relativePath);
			
			if(handletype == CmsLinkProcessor.PROCESS_CONTENT 
					|| handletype == CmsLinkProcessor.PROCESS_TEMPLATE )
			{
				if (path.startsWith(relativeImagesPath)) {
					String tempPath = CMSUtil.getPath("images", path
							.substring(relativeImagesPath.length()));
	
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					
					
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					newLink.setRelativeFilePath(tempPath);
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_IMAGES);
					if(query != null && !query.equals(""))
					
					{
						this.newLink.setParameters(query);
					}
					
					// this.newLink.setOrigineLink(originelink);
				}
				else if(path.startsWith(webprj))
				{
	//				 relativePath
					String tempPath = path.substring(relativeProjectPath.length() + 1);
					String href = path.substring(webprj.length() + 1);
	//				String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
	//						tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeProjectPath)) {
					// relativePath
					String tempPath = path.substring(relativeProjectPath.length() + 1);
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}
	
					// this.newLink.setOrigineLink(originelink);
				} else if (path.startsWith(relativeStylePath)) {
					// relativePath
					String tempPath = CMSUtil.getPath("style", path
							.substring(relativeStylePath.length()));
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
	
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_STYLE);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}
	
					// this.newLink.setOrigineLink(originelink);
				}
				else if(path.startsWith(template))
				{
					String tempPath = path.substring(relativeTemplatePath.length() + 1);
					String href = path.substring(template.length() + 1);
					/**
					 * 模版中包含的附件地址分为html,jsp和其他的附件
					 * 分别进行不同的页面类型设置：
					 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
					 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
					 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
					 */
					int relativeFilePathType = -1;
					if(href.toLowerCase().endsWith(".html") 
							|| href.toLowerCase().endsWith(".htm")
							|| href.toLowerCase().endsWith(".xhtml"))
					{
						
						relativeFilePathType = CMSLink.TYPE_STATIC_PAGE;
						
					}
					else if(href.toLowerCase().endsWith(".jsp") 
							|| href.toLowerCase().endsWith(".asp")
							|| href.toLowerCase().endsWith(".php"))
					{
						relativeFilePathType = CMSLink.TYPE_DYNAMIC_PAGE;
					}
						
					else
					{
						relativeFilePathType = CMSLink.TYPE_TEMPLATE;
					}
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(relativeFilePathType );
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeTemplatePath)) {
					// relativePath
	
					String tempPath = path
							.substring(relativeTemplatePath.length() + 1);
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					/**
					 * 模版中包含的附件地址分为html,jsp和其他的附件
					 * 分别进行不同的页面类型设置：
					 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
					 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
					 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
					 */
					int relativeFilePathType = -1;
					if(href.toLowerCase().endsWith(".html") 
							|| href.toLowerCase().endsWith(".htm")
							|| href.toLowerCase().endsWith(".xhtml"))
					{
						
						relativeFilePathType = CMSLink.TYPE_STATIC_PAGE;
						
					}
					else if(href.toLowerCase().endsWith(".jsp") 
							|| href.toLowerCase().endsWith(".asp")
							|| href.toLowerCase().endsWith(".php"))
					{
						relativeFilePathType = CMSLink.TYPE_DYNAMIC_PAGE;
					}
						
					else
					{
						relativeFilePathType = CMSLink.TYPE_TEMPLATE;
					}
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(relativeFilePathType );
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}
					// this.newLink.setOrigineLink(originelink);
				} else {
					String tempPath = "";
					if(path.startsWith("/" + contextPath + "/"))
						tempPath = path.substring(contextPath.length());
					else
						tempPath = path;
					String href = path;
					if(query == null || query.equals(""))
					{
						
					} 
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setOrigineLink(originelink);
					this.newLink.setRelativeFilePath(tempPath);
	
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_ABSOLUTE);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
	
				}
			}
			else if(handletype == CmsLinkProcessor.PROCESS_READCONTENT 
					|| handletype == CmsLinkProcessor.PROCESS_READTEMPLATE )
			{
				if (path.startsWith(relativeImagesPath)) {
					String href = path;
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					
					
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}

				}
				else if(path.startsWith(webprj))
				{

					String href = path;
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeProjectPath)) {
					
					String href = path;
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					if(query != null && !query.equals(""))
						
					{
						this.newLink.setParameters(query);
					}

				} else if (path.startsWith(relativeStylePath)) {

					String href = path;
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
	
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}

				}
				else if(path.startsWith(template))
				{

					String href = path;
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeTemplatePath)) 
				{					
					String href = path;
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else 
				{
					this.newLink = originelink;
					this.newLink.setOrigineLink(originelink);
	
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_ABSOLUTE);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
	
				}
			}
			/**
			 * 备份模版或者文档时获取模版中的附件
			 */
			else if(handletype == CmsLinkProcessor.PROCESS_BACKUPCONTENT
					|| handletype == CmsLinkProcessor.PROCESS_BACKUPTEMPLATE)
			{
				if (path.startsWith(relativeImagesPath)) {
					String href = CMSUtil.getPath("images", path
							.substring(relativeImagesPath.length()));
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}					
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					newLink.setRelativeFilePath(href);
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_IMAGES);		
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if(path.startsWith(webprj))
				{
	
					String tempPath = path.substring(relativeProjectPath.length() + 1);
					String href = path.substring(webprj.length() + 1);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeProjectPath)) {
					
					String tempPath = path.substring(relativeProjectPath.length() + 1);
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				} else if (path.startsWith(relativeStylePath)) {
					
					String tempPath = CMSUtil.getPath("style", path
							.substring(relativeStylePath.length()));
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
	
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_STYLE);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if(path.startsWith(template))
				{
					String tempPath = path.substring(relativeTemplatePath.length() + 1);
					String href = path.substring(template.length() + 1);
					/**
					 * 模版中包含的附件地址分为html,jsp和其他的附件
					 * 分别进行不同的页面类型设置：
					 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
					 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
					 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
					 */
					int relativeFilePathType = -1;
					if(href.toLowerCase().endsWith(".html") 
							|| href.toLowerCase().endsWith(".htm")
							|| href.toLowerCase().endsWith(".xhtml"))
					{
						
						relativeFilePathType = CMSLink.TYPE_STATIC_PAGE;
						
					}
					else if(href.toLowerCase().endsWith(".jsp") 
							|| href.toLowerCase().endsWith(".asp")
							|| href.toLowerCase().endsWith(".php"))
					{
						relativeFilePathType = CMSLink.TYPE_DYNAMIC_PAGE;
					}
						
					else
					{
						relativeFilePathType = CMSLink.TYPE_TEMPLATE;
					}
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(relativeFilePathType );
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeTemplatePath)) {
					String tempPath = path
							.substring(relativeTemplatePath.length() + 1);
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					/**
					 * 模版中包含的附件地址分为html,jsp和其他的附件
					 * 分别进行不同的页面类型设置：
					 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
					 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
					 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
					 */
					int relativeFilePathType = -1;
					if(href.toLowerCase().endsWith(".html") 
							|| href.toLowerCase().endsWith(".htm")
							|| href.toLowerCase().endsWith(".xhtml"))
					{
						
						relativeFilePathType = CMSLink.TYPE_STATIC_PAGE;
						
					}
					else if(href.toLowerCase().endsWith(".jsp") 
							|| href.toLowerCase().endsWith(".asp")
							|| href.toLowerCase().endsWith(".php"))
					{
						relativeFilePathType = CMSLink.TYPE_DYNAMIC_PAGE;
					}
						
					else
					{
						relativeFilePathType = CMSLink.TYPE_TEMPLATE;
					}
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(relativeFilePathType );
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				} else {
					this.newLink = originelink;
					this.newLink.setOrigineLink(originelink);
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_ABSOLUTE);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
			}
			
			else if(handletype == CmsLinkProcessor.PROCESS_EDITCONTENT
					|| handletype == CmsLinkProcessor.PROCESS_EDITTEMPLATE)
			{
				if (path.startsWith(relativeImagesPath)) {
					String tempPath = CMSUtil.getPath("images", path
							.substring(relativeImagesPath.length()));
	
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					
					
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					newLink.setRelativeFilePath(tempPath);
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_IMAGES);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if(path.startsWith(webprj))
				{
					String tempPath = path.substring(relativeProjectPath.length() + 1);
					String href = path.substring(webprj.length() + 1);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeProjectPath)) {					
					String tempPath = path.substring(relativeProjectPath.length() + 1);
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				} else if (path.startsWith(relativeStylePath)) {
					String tempPath = CMSUtil.getPath("style", path
							.substring(relativeStylePath.length()));
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
	
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_STYLE);
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if(path.startsWith(template))
				{
					String tempPath = path.substring(relativeTemplatePath.length() + 1);
					String href = path.substring(template.length() + 1);
					/**
					 * 模版中包含的附件地址分为html,jsp和其他的附件
					 * 分别进行不同的页面类型设置：
					 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
					 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
					 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
					 */
					int relativeFilePathType = -1;
					if(href.toLowerCase().endsWith(".html") 
							|| href.toLowerCase().endsWith(".htm")
							|| href.toLowerCase().endsWith(".xhtml"))
					{
						
						relativeFilePathType = CMSLink.TYPE_STATIC_PAGE;
						
					}
					else if(href.toLowerCase().endsWith(".jsp") 
							|| href.toLowerCase().endsWith(".asp")
							|| href.toLowerCase().endsWith(".php"))
					{
						relativeFilePathType = CMSLink.TYPE_DYNAMIC_PAGE;
					}
						
					else
					{
						relativeFilePathType = CMSLink.TYPE_TEMPLATE;
					}
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(relativeFilePathType );
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				}
				else if (path.startsWith(relativeTemplatePath)) {
					String tempPath = path
							.substring(relativeTemplatePath.length() + 1);
					String href = CMSUtil.getSimplePathFromfullPath(m_relativePath,
							tempPath);
					/**
					 * 模版中包含的附件地址分为html,jsp和其他的附件
					 * 分别进行不同的页面类型设置：
					 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
					 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
					 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
					 */
					int relativeFilePathType = -1;
					if(href.toLowerCase().endsWith(".html") 
							|| href.toLowerCase().endsWith(".htm")
							|| href.toLowerCase().endsWith(".xhtml"))
					{
						
						relativeFilePathType = CMSLink.TYPE_STATIC_PAGE;
						
					}
					else if(href.toLowerCase().endsWith(".jsp") 
							|| href.toLowerCase().endsWith(".asp")
							|| href.toLowerCase().endsWith(".php"))
					{
						relativeFilePathType = CMSLink.TYPE_DYNAMIC_PAGE;
					}
						
					else
					{
						relativeFilePathType = CMSLink.TYPE_TEMPLATE;
					}
					if(query == null || query.equals(""))
					{
						
					}
					else
					{
						href = href + "?"+ query;
					}
					
					if(ref == null || ref.equals(""))
					{
						
					}
					else
					{
						href = href + "#"+ ref;
					}
					this.newLink = new CMSLink(href,originelink.getLinkhandletype());
					this.newLink.setRelativeFilePathType(relativeFilePathType );
					this.newLink.setRelativeFilePath(tempPath);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
				} else {
					this.newLink = originelink;
					this.newLink.setOrigineLink(originelink);
	
					this.newLink.setRelativeFilePathType(CMSLink.TYPE_ABSOLUTE);
					if(query != null && !query.equals(""))
					{
						this.newLink.setParameters(query);
					}
	
				}
			}
		}
		
		/**
		 * 处理内部地址
		 * @param path
		 */
		private void handInternal(String path) {
			handInternal(path,null,null);
		}
		
		/**
		 * 处理外部地址
		 * @param path
		 */
		private void handExternal(String path)
		{
			if(!this.originelink.isBase())
			{
				/**
				 * 如果是在处理文档编辑和模版编辑保存之前的正文，
				 * 则需将远程外部地址本地化，
				 * 并且将这些外部文件存放到本地
				 */
				if(handletype == PROCESS_EDITCONTENT )
				{
					if(RegexUtil.isMatch(this.originelink.getHref(),CmsLinkProcessor.domainpattern))
					{
						this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
						this.newLink.setOrigineLink(originelink);
					}
					else
					{
//						String ext = FileUtil.getFileExtByFileName(this.originelink.getHref());
						
						/*
						 * 目前还不考虑，深度抓取
						 */
//						if(ext.toLowerCase().equals(".doc") 
//								|| ext.toLowerCase().equals(".zip") 
//								|| ext.toLowerCase().equals(".rar")
//								|| ext.toLowerCase().equals(".jar") 
//								|| ext.toLowerCase().equals(".bmp")
//								|| ext.toLowerCase().equals(".avi")
//								||ext.toLowerCase().equals(".gif") 
//								||ext.toLowerCase().equals(".jpg") 
//								||ext.toLowerCase().equals(".png")
//								||ext.toLowerCase().equals(".pdf")
//								||ext.toLowerCase().equals(".swf"))
						if(CMSUtil.isBinaryFile(this.originelink.getHref()))
						{
							String ext = FileUtil.getFileExtByFileName(this.originelink.getHref());
							
							String name = CMSUtil.getUUID() + (!ext.equals("") ? "." + ext : "");
							//System.out.println("content name:"+name);
							String trueHref = "content_files/" + name;
							this.newLink = new CMSLink(trueHref,originelink.getLinkhandletype());
							this.newLink.setOrigineLink(originelink);
							/*
							 * 得到本地文件路径
							 */
							String href = CMSUtil.getPath(m_relativePath,trueHref); 
							/*
							 * 设置本地化的路径
							 */
							newLink.setRelativeFilePath(href);
							newLink.setNeedDownLoad(true);
						}
						else
						{
							this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
							this.newLink.setOrigineLink(originelink);
						}
					}
					
					
				}
				else if(handletype == PROCESS_EDITTEMPLATE)
				{
					if(RegexUtil.isMatch(this.originelink.getHref(),CmsLinkProcessor.domainpattern))
					{
						this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
						this.newLink.setOrigineLink(originelink);
					}
					else
					{
						
						/*
						 * 目前不考虑模版附件的深度抓取
						 */
						if(CMSUtil.isBinaryFile(this.originelink.getHref()))
						{
							String ext = FileUtil.getFileExtByFileName(this.originelink.getHref());
							String name = CMSUtil.getUUID() + "." + ext;
							String trueHref = "template_files/" + name;
							this.newLink = new CMSLink(trueHref,originelink.getLinkhandletype());
							this.newLink.setOrigineLink(originelink);
							/*
							 * 得到本地文件路径
							 */
							String href = CMSUtil.getPath(m_relativePath,trueHref); 
							
							/*
							 * 设置本地化的路径
							 */
							newLink.setRelativeFilePath(href);
							newLink.setNeedDownLoad(true);
						}
						else
						{
							this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
							this.newLink.setOrigineLink(originelink);
						}
					}
				}
				else
				{
					this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
					this.newLink.setOrigineLink(originelink);
				}
				
				
			}
			else
			{
				this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
				this.newLink.setOrigineLink(originelink);
			}
			newLink.setInternal(false);
			
			
		}


		/**
		 * 分析带基本链接的页面地址
		 * 
		 * @return
		 */
		public CMSLink parserBase() {
			try {
				boolean isHttpUrl = false;
				String host = null;
				String path = null;
				int port = -1;

				String protocol = null;

				String query = null;
				String ref = null;

				try {
					URL test = new URL(originelink.getHref());
					host = test.getHost();
					path = test.getPath();
					port = test.getPort();
					if (port == -1)
						;
					protocol = test.getProtocol();

					query = test.getQuery();
					ref = test.getRef();
					isHttpUrl = true;
					this.originelink.setMalformedURL(true);

				} catch (MalformedURLException me) {

				}
				if (isHttpUrl) {
//				System.out.println("contextPath:" + contextPath);
					boolean a = path.startsWith(contextPath); //判断路径是否是以当前的上下文开头
					boolean b = serverProtocol.toLowerCase().startsWith(protocol); //判断路径协议是否是当前的协议
					boolean c = serverHost.equals(host) || serverAddr.equals(host) || serverName.equals(host);//判断路径所带的服务器的地址是否是当前的机器地址
					boolean d = serverPort == port || (serverPort == 80 && port == -1);//判断路径所带的端口与当前机器的端口一致

					boolean isInternal = a && b && c && d;
					if (isInternal) {
						// 处理内部地址,模版得相对地址，发布结果相对地址，
						// 处理过程当中记录内部链接对应得文件相对地址，以便后需分发相关文件时使用
						
						this.handInternal(path,query,ref);

					} 
					else 
					{
						handExternal(originelink.getHref());
					}
				}
				else 
				{
					if (StringUtil.isJavascript(this.originelink.getHref())) {
						this.newLink = new CMSLink(originelink.href,originelink.getLinkhandletype());
						this.newLink.setInternal(false);
						this.newLink.setJavaScript(true);
						// this.newLink.setOrigineLink(originelink);
					} 
					else 
					{
//						if (RegexUtil.isMatch(this.originelink.getHref(),domainpattern)) {
//							if (this.originelink.hasBase()) {
//								newLink = new CMSLink(CMSUtil.getPath(originelink.getBase().getHref(),originelink.href));
//								newLink.setInternal(originelink.getBase().isInternal());
//
//							} 
//							else 
//							{
//								/*
//								 * 无需进行外部地址处理，
//								 */
//								this.newLink = new CMSLink(originelink.getHref());
//								this.newLink.setInternal(false);
//							}
//
//						} 
//						else 
						if (RegexUtil.isMatch(this.originelink.getHref(), simple_uripattern)) {
							if (originelink.hasBase()) {
								String temp = CMSUtil.getPathFromSimplePath(originelink.getBase().getHref(),originelink.getHref());
								//执行子过程进行处理
								if (!originelink.getBase().isInternal()) {
									this.handExternal(temp);
//									this.newLink = new CMSLink(temp);
								} else {
									this.handInternal(temp);
								}
								this.newLink.setInternal(originelink.getBase().isInternal());
							} 
							else 
							{
								// this.newLink = new CMSLink();
								// 待补充和处理

								if (handletype == CmsLinkProcessor.PROCESS_CONTENT) {
									String temppath = CMSUtil.getPathFromSimplePath(m_relativePath, originelink.getHref());
									this.newLink = new CMSLink(originelink.getHref(),originelink.getLinkhandletype());
									this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
									this.newLink.setRelativeFilePath(temppath);
									
								} 
								else if (handletype == CmsLinkProcessor.PROCESS_TEMPLATE) {
									String href = CMSUtil.getPathFromSimplePath(templatePath, originelink.getHref());
									String temppath = CMSUtil.getSimplePathFromfullPath(m_relativePath, href);
									this.newLink = new CMSLink(temppath,originelink.getLinkhandletype());
									/**
									 * 模版中包含的附件地址分为html,jsp和其他的附件
									 * 分别进行不同的页面类型设置：
									 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
									 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
									 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
									 */
									if(href.toLowerCase().endsWith(".html") 
											|| href.toLowerCase().endsWith(".htm")
											|| href.toLowerCase().endsWith(".xhtml"))
									{
										
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_STATIC_PAGE);
										
									}
									else if(href.toLowerCase().endsWith(".jsp") 
											|| href.toLowerCase().endsWith(".asp")
											|| href.toLowerCase().endsWith(".php"))
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_DYNAMIC_PAGE);
									}
									else
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									}
//									this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									this.newLink.setRelativeFilePath(href);
								}
								else if(handletype == CmsLinkProcessor.PROCESS_READCONTENT )
								{
									String temppath = CMSUtil.getPathFromSimplePath(m_relativePath, originelink.getHref());
									String href = CMSUtil.getPath(relativeProjectPath,temppath);
									this.newLink = new CMSLink(href,originelink.getLinkhandletype());
									
								}
								else if(handletype == CmsLinkProcessor.PROCESS_READTEMPLATE)
								{
									String temppath = CMSUtil.getPathFromSimplePath(m_relativePath, originelink.getHref());
									String href = CMSUtil.getPath(relativeTemplatePath, temppath);
									this.newLink = new CMSLink(href,originelink.getLinkhandletype());
									
								}
								else if(handletype == CmsLinkProcessor.PROCESS_BACKUPCONTENT)
								{
									String href = CMSUtil.getPathFromSimplePath(m_relativePath, originelink.getHref());
									
									this.newLink = new CMSLink(href,originelink.getLinkhandletype());
									
									/**
									 * 模版中包含的附件地址分为html,jsp和其他的附件
									 * 分别进行不同的页面类型设置：
									 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
									 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
									 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
									 */
									if(href.toLowerCase().endsWith(".html") 
											|| href.toLowerCase().endsWith(".htm")
											|| href.toLowerCase().endsWith(".xhtml"))
									{
										
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ_STATIC_PAGE);
										
									}
									else if(href.toLowerCase().endsWith(".jsp") 
											|| href.toLowerCase().endsWith(".asp")
											|| href.toLowerCase().endsWith(".php"))
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ_STATIC_PAGE);
									}
									else
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
									}
//									this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									this.newLink.setRelativeFilePath(href);
								}
								else if(handletype == CmsLinkProcessor.PROCESS_BACKUPTEMPLATE)
								{
									String href = CMSUtil.getPathFromSimplePath(m_relativePath, originelink.getHref());
									
									this.newLink = new CMSLink(href,originelink.getLinkhandletype());
									
									/**
									 * 模版中包含的附件地址分为html,jsp和其他的附件
									 * 分别进行不同的页面类型设置：
									 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
									 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
									 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
									 */
									if(href.toLowerCase().endsWith(".html") 
											|| href.toLowerCase().endsWith(".htm")
											|| href.toLowerCase().endsWith(".xhtml"))
									{
										
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_STATIC_PAGE);
										
									}
									else if(href.toLowerCase().endsWith(".jsp") 
											|| href.toLowerCase().endsWith(".asp")
											|| href.toLowerCase().endsWith(".php"))
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_DYNAMIC_PAGE);
									}
									else
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									}
//									this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									this.newLink.setRelativeFilePath(href);
								}
								else
								{
									this.newLink = new CMSLink(this.originelink.getHref(),originelink.getLinkhandletype());
									this.newLink.setInternal(true);
								}

							}
						} 
						else if (RegexUtil.isMatch(this.originelink.getHref(), uripattern)) {

							if (this.originelink.hasBase()) {
								String temp = CMSUtil.getPath(originelink.getBase().getHref(), originelink.getHref());
								if (!originelink.getBase().isInternal()) {
									this.handExternal(temp);
//									this.newLink = new CMSLink(temp);
								} else {
									this.handInternal(temp);

								}
								this.newLink.setInternal(originelink.getBase().isInternal());
							} else {
								String temp = this.originelink.getHref();

								this.handInternal(temp);
							}
						} 
						else if (RegexUtil.isMatch(this.originelink.getHref(), r_uripattern)) {
							if (this.originelink.hasBase()) {
								String temp = CMSUtil.getPath(originelink.getBase().getHref(), originelink.getHref());
								if (!originelink.getBase().isInternal()) {
//									this.newLink = new CMSLink(temp);
									this.handExternal(temp);
								} else {
									this.handInternal(temp);

								}
								this.newLink.setInternal(originelink.getBase().isInternal());
							} 
							else 
							{
								//如果是处理文档内容
								if (handletype == CmsLinkProcessor.PROCESS_CONTENT) {
									newLink = new CMSLink(this.originelink.getHref(),originelink.getLinkhandletype());
									String temppath = CMSUtil.getPath(m_relativePath, originelink.getHref());
									this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
									this.newLink.setRelativeFilePath(temppath);
								} 
								//如果是处理模版内容
								else if (handletype == CmsLinkProcessor.PROCESS_TEMPLATE) 
								{
									
									String temppath = CMSUtil.getPath(templatePath, originelink.getHref());
									String simplePath = CMSUtil.getSimplePathFromfullPath(m_relativePath, temppath);
									newLink = new CMSLink(simplePath,originelink.getLinkhandletype());
									/**
									 * 模版中包含的附件地址分为html,jsp和其他的附件
									 * 分别进行不同的页面类型设置：
									 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
									 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
									 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
									 */
									if(temppath.toLowerCase().endsWith(".html") 
											|| temppath.toLowerCase().endsWith(".htm")
											|| temppath.toLowerCase().endsWith(".xhtml"))
									{
										
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_STATIC_PAGE);
										
									}
									else if(temppath.toLowerCase().endsWith(".jsp") 
											|| temppath.toLowerCase().endsWith(".asp")
											|| temppath.toLowerCase().endsWith(".php"))
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_DYNAMIC_PAGE);
									}
										
									else
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									}
//									this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									this.newLink.setRelativeFilePath(temppath);
								} 
								else if(handletype == CmsLinkProcessor.PROCESS_READCONTENT )
								{
									String temppath = CMSUtil.getPath(m_relativePath, originelink.getHref());
									String href = CMSUtil.getPath(relativeProjectPath,temppath);
									this.newLink = new CMSLink(href,originelink.getLinkhandletype());
									
								}
								else if(handletype == CmsLinkProcessor.PROCESS_READTEMPLATE)
								{
									String temppath = CMSUtil.getPath(m_relativePath, originelink.getHref());
									String href = CMSUtil.getPath(relativeTemplatePath, temppath);
									this.newLink = new CMSLink(href,originelink.getLinkhandletype());
									
								}
								else if(handletype == CmsLinkProcessor.PROCESS_BACKUPCONTENT)
								{
									String href = CMSUtil.getPath(m_relativePath, originelink.getHref());
									newLink = new CMSLink(href,originelink.getLinkhandletype());
									
									
									
									/**
									 * 模版中包含的附件地址分为html,jsp和其他的附件
									 * 分别进行不同的页面类型设置：
									 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
									 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
									 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
									 */
									if(href.toLowerCase().endsWith(".html") 
											|| href.toLowerCase().endsWith(".htm")
											|| href.toLowerCase().endsWith(".xhtml"))
									{
										
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ_STATIC_PAGE);
										
									}
									else if(href.toLowerCase().endsWith(".jsp") 
											|| href.toLowerCase().endsWith(".asp")
											|| href.toLowerCase().endsWith(".php"))
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ_DYNAMIC_PAGE);
									}
									else
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_WEBPRJ);
									}
//									
									this.newLink.setRelativeFilePath(href);
								}
								else if(handletype == CmsLinkProcessor.PROCESS_BACKUPTEMPLATE)
								{
									String href = CMSUtil.getPath(m_relativePath, originelink.getHref());
									newLink = new CMSLink(href,originelink.getLinkhandletype());
									
									/**
									 * 模版中包含的附件地址分为html,jsp和其他的附件
									 * 分别进行不同的页面类型设置：
									 * html-CMSLink.TYPE_STATIC_PAGE,表示需要进行静态发布处理
									 * jsp-CMSLink.TYPE_DYNAMIC_PAGE,表示需要进行动态发布处理
									 * 其他的类型页面直接作为模版附件进行发布,表示为CMSLink.TYPE_TEMPLATE
									 */
									if(href.toLowerCase().endsWith(".html") 
											|| href.toLowerCase().endsWith(".htm")
											|| href.toLowerCase().endsWith(".xhtml"))
									{
										
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_STATIC_PAGE);
										
									}
									else if(href.toLowerCase().endsWith(".jsp") 
											|| href.toLowerCase().endsWith(".asp")
											|| href.toLowerCase().endsWith(".php"))
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_DYNAMIC_PAGE);
									}
									else
									{
										this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									}
//									this.newLink.setRelativeFilePathType(CMSLink.TYPE_TEMPLATE);
									this.newLink.setRelativeFilePath(href);
								}
								
								//其他情况
								else
								{
									newLink = new CMSLink(this.originelink.getHref(),originelink.getLinkhandletype());
									this.newLink.setInternal(true);
								}
							}
						}
						else if(RegexUtil.isMatch(this.originelink.getHref(), cmstag_pattern))
						{
							this.newLink = this.originelink;
							this.newLink.setOrigineLink(originelink);
							this.newLink.setCmsTag(true);
							this.newLink.setInternal(true);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(newLink == null)
				this.newLink = this.originelink;
			this.newLink.setOrigineLink(originelink);
			return this.newLink;

		}

	}
//
//	public static void main(String[] args) {
//		CmsLinkProcessor processor = new CmsLinkProcessor(null, 1,
//				CmsEncoder.ENCODING_ISO_8859_1);
//		try {
//			String result = processor
//					.processLinks("<html><head><BASE HREF=\"http://msdn.microsoft.com/workshop/author/dhtml/reference/\"/></head><a href=\"../www.sina.com.cn\"></a>");
//			System.out.println(result);
//		} catch (ParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// LinkParser parser = new LinkParser("http://www.sina.com.cn:8080\\/");
//		// System.out.println(parser.getDomain());
//		// System.out.println(parser.getPotocol());
//		// System.out.println(parser.getURI());
//
//		// try {
//		//    		
//		// // URL test = new URL("/www.wwww.com.cn/index.jsp");
//		// // System.out.println("file:" + test.getFile());
//		// // System.out.println("host:" + test.getHost());
//		// //
//		// // System.out.println("path:" + test.getPath());
//		// // System.out.println("port:" + test.getPort());
//		// // System.out.println("query:" + test.getQuery());
//		// // System.out.println("ref:" + test.getRef());
//		// } catch (MalformedURLException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// String domain = "www.sina.com.cn";
//		// String aurl = "/news/index.jsp";
//		// String rurl = "news/index.jsp";
//		// System.out.println(domain + " is aboslute url:" +
//		// RegexUtil.isMatch(domain,uripattern));
//		// System.out.println(aurl + " is aboslute url:" +
//		// RegexUtil.isMatch(aurl,uripattern));
//		// System.out.println(rurl + " is aboslute url:" +
//		// RegexUtil.isMatch(rurl,uripattern));
//		// System.out.println(domain + " is relative url:" +
//		// RegexUtil.isMatch(domain,r_uripattern));
//		// System.out.println(aurl + " is relative url:" +
//		// RegexUtil.isMatch(aurl,r_uripattern));
//		// System.out.println(rurl + " is relative url:" +
//		// RegexUtil.isMatch(rurl,r_uripattern));
//		// System.out.println(domain + " is domainpattern url:" +
//		// RegexUtil.isMatch(domain,domainpattern));
//		// System.out.println(aurl + " is domainpattern url:" +
//		// RegexUtil.isMatch(aurl,domainpattern));
//		// System.out.println(rurl + " is domainpattern url:" +
//		// RegexUtil.isMatch(rurl,domainpattern));
//		//		
//
//		// String regex = "\\s*(http://)?([a-zA-Z0-9]+)*:?[0-9]{0,4}";
//		// String domainurl = "http://www.sina.com.cn:8080\\/";
//		// String url1 = "http://localhost:8080";
//		// // System.out.println(url + " is a url?:"
//		// +RegexUtil.contain(url,protocolpatter)[0]+RegexUtil.contain(url,protocolpatter)[1]);
//		// System.out.println(domainurl + " is a protocolpattern url?:"
//		// +RegexUtil.isMatch(domainurl,protocolpattern));
//		// System.out.println(url1 + " is a protocolpattern url?:"
//		// +RegexUtil.isMatch(url1,protocolpattern));
//		//    	
//		// System.out.println(domainurl + " is a domainpattern url?:"
//		// +RegexUtil.isMatch(domainurl,domainpattern));
//		// System.out.println(url1 + " is a domainpattern url?:"
//		// +RegexUtil.isMatch(url1,domainpattern));
//		//    	
//		// System.out.println(domainurl + " is a uripattern url?:"
//		// +RegexUtil.isMatch(domainurl,uripattern));
//		// System.out.println(url1 + " is a uripattern url?:"
//		// +RegexUtil.isMatch(url1,uripattern));
//	}

	public int getHandletype() {
		return handletype;
	}

	public void setHandletype(int handletype) {

		this.handletype = handletype;
		
		/**
		 * 目前处理发布时文档附件暂时没有考虑文档引用的相关附件，直接从数据库中记录的信息获取需要发布的文档附件
		 * 以后将改为从文档正文中扫描出来进行发布
		 */
		if (this.handletype == PROCESS_TEMPLATE 
				|| this.handletype == PROCESS_BACKUPCONTENT 
				|| handletype == PROCESS_BACKUPTEMPLATE)
		{
//			this.origineTemplateLinkTable = new CmsLinkTable(); //备份时存放文档、模版的附件地址
			this.origineTemplateLinkTable = new CMSTemplateLinkTable(new CmsLinkTable()); //备份时存放文档、模版的附件地址
			this.origineDynamicPageLinkTable = new CmsLinkTable();//备份时存放文档、模版的动态页面地址
			this.origineStaticPageLinkTable = new CmsLinkTable();//备份时存放文档、模版的静态页面地址
		}
		
		if(this.handletype == PROCESS_EDITCONTENT 
				|| this.handletype == PROCESS_EDITTEMPLATE)
		{
			/**
			 * 添加/编辑文档、模版时，外部地址要修改为本地地址，并且记录新的本地地址，以便将这些远程文件存为本地文件
			 */
			this.externalPageLinkTable = new CmsLinkTable();
//			/**
//			 * 内部地址需要处理并修改为相对内部地址
//			 */
//			this.innerPageLinkTable = new CmsLinkTable();
			
		}
		
		if(handletype == PROCESS_READCONTENT || handletype == PROCESS_READTEMPLATE)
		{
			/**
			 * 处理需要编辑的文档和模版内容，将文档的内部相对路径修改为上下文环境相关的路径，也就说和编辑器相关的路径
			 * 无需记录相关的路径
			 */
			
		}

	}

	public CMSTemplateLinkTable getOrigineTemplateLinkTable() {
		return origineTemplateLinkTable;
	}

	public CmsLinkTable getOrigineStaticPageLinkTable() {
		return origineStaticPageLinkTable;
	}

	public CmsLinkTable getOrigineDynamicPageLinkTable() {
		return origineDynamicPageLinkTable;
	}

	public CmsLinkTable getExternalPageLinkTable() {
		return externalPageLinkTable;
	}

//	public CmsLinkTable getInnerPageLinkTable() {
//		return innerPageLinkTable;
//	}

	public static void main(String[] args)
	{
//		/**
//		 * 文档采集/编辑保存时处理远程链接代码
//		 */
//		HttpServletRequest request = null;
//		String relative_path = null;
//		String sitedir = null;
//		CmsLinkProcessor processor = new CmsLinkProcessor(request,relative_path,sitedir);
//		processor.setHandletype(CmsLinkProcessor.PROCESS_EDITCONTENT);
//		try {
//			String content = processor.process("",CmsEncoder.ENCODING_UTF_8);
//			CmsLinkTable linktable = processor.getExternalPageLinkTable();
//			Iterator it = linktable.iterator();
//			while(it.hasNext())
//			{
//				CMSLink link = (CMSLink)it.next();
//				String remoteAddr = link.getOrigineLink().getHref();
//				String contentPath = link.getRelativeFilePath();
//			}
////			while()
//		} catch (ParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		processor.setHandletype(CmsLinkProcessor.PROCESS_READCONTENT);
//		try {
//			String content = processor.process("",CmsEncoder.ENCODING_UTF_8);
//			
////			while()
//		} catch (ParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPCONTENT);
//		try {
//			processor.process("",CmsEncoder.ENCODING_UTF_8);
//			
//			CmsLinkTable linktable = processor.getOrigineTemplateLinkTable();
//			
//			Iterator it = linktable.iterator();
//			while(it.hasNext())
//			{
//				CMSLink link = (CMSLink)it.next();
//				
//				String attachmentPath = link.getHref();
//			}
//			CmsLinkTable staticlinktable = processor.getOrigineStaticPageLinkTable();
//			
//			CmsLinkTable dynamiclinktable = processor.getOrigineDynamicPageLinkTable();
////			while()
//		} catch (ParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		CmsLinkProcessor processor = new CmsLinkProcessor();
//		CMSCodeParser parser = processor.create(new File("D:\\workspace\\cms\\creatorcms\\cms\\siteResource\\site200\\_template\\css\\index.css"));
//		parser.parser();
//		String result[] = parser.getResult();
//		for(int i = 0;  i < result.length; i ++)
//		{
//			System.out.println(result[i]);
//		}
//		
//		//测试jsp代码块模式是否正确
//		/**
//		 * jsp代码段<%.....%>的正则表达式匹配模式
//		 */
//		String PATTERN_JSPCODE_BLOCK = "\\s*<%.*%>\\s*";
//		
//		/**
//		 * JSP自定义标签带标签体匹配模式,例如<cms:cell></cms:cell>
//		 */
//		String PATTAERN_JSPTAG_HASBODY = "\\s*<[^>]+>.*</[^>]+>\\s*";
//		
//		
//		/**
//		 * JSP自定义标签不带标签体匹配模式,例如<cms:cell/>
//		 */
//		String PATTAERN_JSPTAG_NOBODY = "\\s*<[^/]+/\\s*>\\s*";
////		String tt = "\\s*<%.*%>\\s*";
//		System.out.println(RegexUtil.isMatch("  <%= ddf %>  ",PATTERN_JSPCODE_BLOCK));
//		
//		System.out.println(RegexUtil.isMatch("  <cms:cell  ></cms:cell  >  ",PATTAERN_JSPTAG_HASBODY));
//		
//		System.out.println(RegexUtil.isMatch("  <cms:cell/  >  ",PATTAERN_JSPTAG_NOBODY));
//		
//		String src= "background:#F6F6F6 url(images/help_06.gif) no-repeat center top";
//
//		String code = "background:url(images/help_03.gif) no-repeat center";
//		byte[] bytes = code.getBytes();
//		for(int i = 0; i < bytes.length; i ++)
//		{
//			System.out.println("byte[" + i +"]:" + bytes[i]);
//		}
//		String URL_PATTERN = "\\s*url\\s*\\(([^\\)]+)\\)";
//		String[] urls = RegexUtil.containWithPatternMatcherInput(code,URL_PATTERN);
//		String[] srcu = RegexUtil.containWithPatternMatcherInput(src,URL_PATTERN);
//
//		System.out.println(urls.length);
//		System.out.println(srcu.length);
		String href= "../../hbj/images/help_03.gif";
		String origin = "images/help_03.gif";
		String code = "background:url(images/help_03.gif) no-repeat center";
		System.out.println(StringUtil.replaceAll(code,origin,href));
	}


	
	/**
	 * 判断当前的链接是否是一个样式链接
	 * @param href
	 * @return
	 */
	protected boolean isStyleLink(String href)
	{
		return href != null && href.toLowerCase().endsWith(".css");
	}

	public boolean isList() {
		return isList;
	}
	
	/**
	 * 创建link对象
	 * @param link 原始link
	 * @param relativePath  相对路径
	 * @return
	 */
	public CMSLink createCMSLink(String link,String relativePath) {
		CMSLink link_ = new CMSLink(link,this.LINK_PARSER_DISTRIBUTE);
		link_.setRelativeFilePath(link);
		link_.setRelativeFilePathType(CMSUtil.getPageType(relativePath));
		return new CMSLink(link,this.LINK_PARSER_DISTRIBUTE);
	}
	//background:url(images/help_03.gif) no-repeat center

	

	

	
}