package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.exception.FormulaException;

/**
 * 
 * <p>Title: DocAttachmentTag</p>
 *
 * <p>Description: 文档附件标签和文档概览数据获取接口结合使用</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-10-22 10:09:00
 * @author biaoping.yin
 * @version 1.0
 */
public class DocAttachmentTag extends BaseCellTag {

	private String style = "";
	private String target = "";
	
	private String extend ;
	
	
	/**
	 * 文档下载展示的文字
	 */
	private String download ;
	private String downloadpic;
	
	/**
	 * 下载附件或者多媒体文件
	 * scr=="attachment" 附件
	 * src=="media" 多媒体
	 */
	private String srcfield;
	
	public int doStartTag() throws JspException{
		
		super.doStartTag();
		if(dataSet == null) {				
			return SKIP_BODY;
		}
		String colName = this.getColName();
		String textStr = null;
		
		
	

		

		try {
			if(srcfield == null || srcfield.equals(""))
			{
				if(colName != null && !colName.equals("") )
				{
					colName = "{document." + colName + "}";
					try {
						textStr = this.dataSet.getFormula(colName).getValue().toString();
					} catch (FormulaException e) {
						
						e.printStackTrace();
					}
				}
				A a = new A();
				String link = CMSUtil.PUBLISHFILEFORDER + dataSet.getString("url");
				
				
				if(super.listTag.getDatatype().equals("attach")) //文档附件列表
				{
					if(context instanceof ContentContext)
					{
	//						link = CMSUtil.getPublishedContentAttachPath(context,((ContentContext)context).getDocument(),link);
					}
					else
					{
						String documentid = super.listTag.getDocumentid();
						
						Document document = CMSUtil.getCMSDriverConfiguration()
												.getCMSService()
												.getDocumentManager()
												.getPartDocInfoById(documentid);
						link = CMSUtil.getPublishedContentAttachPath(context,document,link);
					}
					
				}
				else if(super.listTag.getDatatype().equals("specialattachment")) //下载专区
				{
					
					Document document = (Document)this.dataSet.getValue("document");
					link = CMSUtil.getPublishedContentAttachPath(context,document,link);
					
				}
				
				if(textStr != null && !textStr.trim().equals("")){
					a.setTagText(textStr);				
				}else{
					a.setTagText(dataSet.getString("description"));
				}
				
				a.setHref(link);
				if (style != null && !"".equals(this.style) || this.style.length() > 0)
					a.setStyle(this.style);
				if (target != null && !"".equals(this.target) || this.target.length() > 0)
					a.setTarget(this.target);
				if(this.extend != null)
				{
					a.setExtend(extend);
				}
				out.println(a.toString());
				
				if(this.download != null && !download.equals(""))
				{
					A d = new A();
					d.setTagText(download);
					d.setHref(link);
					if (style != null && !"".equals(this.style) || this.style.length() > 0)
						d.setStyle(this.style);
					if (target != null && !"".equals(this.target) || this.target.length() > 0)
						d.setTarget(this.target);
					
					out.print("&nbsp;" + d.toString());
				}
				else if(downloadpic != null && !downloadpic.equals(""))
				{
					String templatePath = CMSUtil.getTemplatePath(context);
					if(templatePath == null)
						templatePath = "";
					String src = CMSUtil.getPublishedLinkPath(context,templatePath,downloadpic);
					IMG img = new IMG();
					img.setSrc(src);
					img.setOnClick("window.open('" + link + "','" + target + "')");
					out.print("&nbsp;" + img.toString());
				}
			}
			else
			{
				A a = new A();
				String link = dataSet.getString(srcfield);
				if(link == null || link.equals(""))
					return SKIP_BODY;
				link = CMSUtil.getPublishedLinkPath(context,link);
				
				if(colName != null && !colName.equals("") )
				{
					
					try {
						textStr = this.dataSet.getString(colName);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				
			
				
				
				if(textStr != null && !textStr.trim().equals("")){
					a.setTagText(textStr);				
				}else{
					a.setTagText(dataSet.getString("title"));
				}
				
				a.setHref(link);
				if (style != null && !"".equals(this.style) || this.style.length() > 0)
					a.setStyle(this.style);
				if (target != null && !"".equals(this.target) || this.target.length() > 0)
					a.setTarget(this.target);
				if(this.extend != null)
				{
					a.setExtend(extend);
				}
				out.println(a.toString());
				
				if(this.download != null && !download.equals(""))
				{
					A d = new A();
					d.setTagText(download);
					d.setHref(link);
					if (style != null && !"".equals(this.style) || this.style.length() > 0)
						d.setStyle(this.style);
					if (target != null && !"".equals(this.target) || this.target.length() > 0)
						d.setTarget(this.target);
					
					out.print("&nbsp;" + d.toString());
				}
				else if(downloadpic != null && !downloadpic.equals(""))
				{
					String templatePath = CMSUtil.getTemplatePath(context);
					if(templatePath == null)
						templatePath = "";
					String src = CMSUtil.getPublishedLinkPath(context,templatePath,downloadpic);
					IMG img = new IMG();
					img.setSrc(src);
					img.setOnClick("window.open('" + link + "','" + target + "')");
					out.print("&nbsp;" + img.toString());
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}



	public int doEndTag() throws JspException
	{
		int ret = super.doEndTag();
		/* style 被清空 连续多个附件的时候 modify by ge.tao */
		style = "";
		target = "";
		this.setColName(null);
		return ret;
	}



	public String getStyle() {
		return style;
	}



	public void setStyle(String style) {
		this.style = style;
	}



	public String getTarget() {
		return target;
	}



	public void setTarget(String target) {
		this.target = target;
	}



	public String getDownload() {
		return download;
	}



	public void setDownload(String download) {
		this.download = download;
	}



	public String getDownloadpic() {
		return downloadpic;
	}



	public void setDownloadpic(String downloadpic) {
		this.downloadpic = downloadpic;
	}



	public String getExtend() {
		return extend;
	}



	public void setExtend(String extend) {
		this.extend = extend;
	}



	public String getSrcfield() {
		return srcfield;
	}



	public void setSrcfield(String src) {
		this.srcfield = src;
	}
}
