package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;
/**
 * flash新闻标签 m_second时间没有控制
 * <p>Title: FlashNewsTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-9-12 11:00:31
 * @author ge.tao
 * @version 1.0
 */
public class FlashNewsTag extends CMSListTag{
	protected Logger log = LoggerFactory.getLogger(FlashNewsTag.class);
	protected CMSTagUtil tagUtil = new CMSTagUtil();
	protected SiteManagerImpl siteUtil = new SiteManagerImpl();
	protected String cssVmpath = "css/imageNews.css";
	protected String target = "_self";
	protected String isMarquee ;  
	protected int m_second = 3;
	protected int count = 5;
	protected int imgWidth = 240;
	protected int imgHeight=180; 
	
	/**
	 * Added by biaoping.yin on 2007.12.20
	 */
	
	protected String classname;
	protected int maxlength;
	protected String replace;
	protected boolean htmlencode = false; 
	protected boolean htmldecode = false; 
		
	
	/**
	 * 
	 * true:   获得图片新闻频道下所有最近发布图片
	 * false:  获得普通频道下所有发布的带主题图片的文档图片
	 */
	protected String isLatest = "true" ;

	public int getImgHeight() {
		
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}



	public String getCssVmpath() {
		return cssVmpath;
	}

	public void setCssVmpath(String cssVmpath) {
		this.cssVmpath = cssVmpath;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIsMarquee() {
		return isMarquee;
	}

	public void setIsMarquee(String isMarquee) {
		this.isMarquee = isMarquee;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(String isLatest) {
		this.isLatest = isLatest;
	}
	
	/**
	 * 处理链接地址或者文件
	 * @param path 原路径
	 * @return String 处理后的路径
	 */
	public String dealPath(String path){
		return CMSTagUtil.getPublishedLinkPath(context,path);
	}
	
	/**
	 * 加载top模板
	 * @param second
	 * @return 
	 * ImageNewsTag.java
	 * @author: 陶格
	 */
	protected String loadTopTemplete(int second){
		StringBuffer sb = new StringBuffer();
		Context top = new VelocityContext();
		top.put("second","1");		
		sb.append(CMSTagUtil.loadTemplate("publish/flashNews/content-top.vm",top));
		return sb.toString();
	}
	
	private String getStartClassCode()
	{
//		if(this.classname != null && !this.classname.equals(""))
//		{
//			return new StringBuffer("<span className=").append(this.classname).append(">").toString();
//		}
//		else
		{
			return "";
		}
	}
	
	private String getEndClassCode()
	{
//		if(this.classname != null && !this.classname.equals(""))
//		{
//			return "</span>";
//		}
//		else
		{
			return "";
		}
	}
	
	
	
	private String getTitleString(int rowid)
	{
		String title = null;
		if(this.colName != null && !this.colName.equals(""))
		{
			title = CellTag.getCommonFormulaValue(this,rowid,colName).toString();
			
//			return new StringBuffer().append(this.getStartClassCode()).append(title).append(this.getEndClassCode()).toString();
			
		}
		else
			title = this.getString(rowid,"title");
		if(title == null || title.equals(""))
			title = "undefine";
		
		title = StringUtil.getHandleString(maxlength,this.replace,htmlencode,htmldecode,title);
		return new StringBuffer().append(this.getStartClassCode()).append(title).append(this.getEndClassCode()).toString();
	}
	
	
	/**
	 * 加载down模板
	 * @return 
	 * ImageNewsTag.java
	 * @author: 陶格
	 */
	protected String loadLoopTemplete(String sid){
		StringBuffer sb = new StringBuffer();
		Context down = new VelocityContext();
		String title = "";
		String link_path = "";
		String pic_path = "";
		String document_id = "";		
		try {
			boolean flag = false;
		    for(int i=0;i<size();i++){
		    	if( i+1 > this.count) break;
		    	Document document = (Document)this.getOrigineObject(i);
		    	if(!flag)
		    	{
		    		title =  this.getTitleString(i) ;
		    		pic_path = dealPath(this.getString(i,"picPath"))  ;
		    		document_id = this.getString(i,"document_id");
		    		link_path +=  CMSUtil.getPublishedContentPath(context,document) ;
		    		flag = true;
		    	}
				else 
				{
					title += "|" + this.getTitleString(i) ;
					pic_path += "|" + dealPath(this.getString(i,"picPath"))  ;
					document_id += "|" +  this.getString(i,"document_id");
					link_path += "|" + CMSUtil.getPublishedContentPath(context,document) ;
				}
				
//				if("".equalsIgnoreCase(pic_path)) pic_path += dealPath(this.getString(i,"picPath"))  ;
//				else pic_path += "|" + dealPath(this.getString(i,"picPath"))  ;
//				
//				if("".equalsIgnoreCase(document_id)) document_id += this.getString(i,"document_id");
//				else document_id += "|" +  this.getString(i,"document_id");
//				
//				if("".equalsIgnoreCase(link_path)) link_path +=  CMSUtil.getPublishedContentPath(context,document) ;
//				else link_path += "|" + CMSUtil.getPublishedContentPath(context,document) ;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		down.put("imgWidth",this.imgWidth+"");
		down.put("imgHeight",this.imgHeight+"");
        //pics是图片的地址
		//links是图片链接到的url地址
		//texts是相关图片的文字说明。
		//图片名称
		down.put("title",title);
		//图片路径
		down.put("pic_path",pic_path);
		//图片ID
		down.put("document_id",document_id);
		//图片链接
		down.put("link_path",link_path);
		//flash路径
		down.put("flash_path",dealPath("images/flashNews.swf"));
		down.put("target",this.target);
		down.put("m_second",this.m_second + "");
		sb.append(CMSUtil.loadTemplate("publish/flashNews/content-down.vm",down));
		return sb.toString();
	}
	
	/**
	 * 构造方法,ImageRollTag继承,重载改方法
	 * @return 
	 * FlashNewsTag.java
	 * @author: ge.tao
	 */
	protected String loadDownTemplete(){		
		return "";
	}
	
	
	/**
     * 生成图片新闻标签的HTML代码.
     * 加载外部的模板.vm文件,辅助生成HTML
     * 根据isLatest的取值,true:获得图片新闻频道的图片
     *                  false:获得制定频道里面的文档图片
     * @return StringBuffer
     * @throws Exception
     */
	public StringBuffer Img_newsTag(){
		int second = 1;
		StringBuffer sb = new StringBuffer();
		//分块加载
		sb.append(this.loadTopTemplete(second));
		String siteid = "";  
		Site siteob = null;
		/*指定siteid ;其次指定sitename 最后从上下文获取context.getSiteID()*/
		if(this.getSite()!=null && this.getSite().length()>0){
			try {
				siteob = CMSUtil.getSiteCacheManager().getSiteByEname(this.getSite());
				siteid = String.valueOf(siteob.getSiteId());
			} catch (SiteManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(context!=null) siteid = context.getSiteID();			
		}
		sb.append(this.loadLoopTemplete(siteid));
		sb.append(this.loadDownTemplete());
	    return sb;
	}
	
	
	public int doStartTag() throws JspException{
		if(this.datatype == null || this.datatype.trim().length()==0 ) {
			this.datatype = "imageChannel";			
		}
		super.datatype = this.datatype;
		return super.doStartTag();
	}
	
	public int doEndTag() throws JspException{
		StringBuffer buffer = Img_newsTag();
	    try {
	        pageContext.getOut().println(buffer.toString());
	    }catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
		return super.doEndTag();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getM_second() {
		return m_second;
	}

	public void setM_second(int m_second) {
		this.m_second = m_second;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public boolean isHtmldecode() {
		return htmldecode;
	}

	public void setHtmldecode(boolean htmldecode) {
		this.htmldecode = htmldecode;
	}

	public boolean isHtmlencode() {
		return htmlencode;
	}

	public void setHtmlencode(boolean htmlencode) {
		this.htmlencode = htmlencode;
	}

	@Override
	public void doFinally() {
		cssVmpath = "css/imageNews.css";
		target = "_self";
		isMarquee = null;  
		 m_second = 3;
		 count = 5;
		 imgWidth = 240;
		 imgHeight=180; 
		
		/**
		 * Added by biaoping.yin on 2007.12.20
		 */
		
		classname = null;  
		 maxlength = 0;  
		replace = null;  
		 htmlencode = false; 
		 htmldecode = false; 
			
		
		/**
		 * 
		 * true:   获得图片新闻频道下所有最近发布图片
		 * false:  获得普通频道下所有发布的带主题图片的文档图片
		 */
		isLatest = "true" ;
		super.doFinally();
	}


	
	

}
