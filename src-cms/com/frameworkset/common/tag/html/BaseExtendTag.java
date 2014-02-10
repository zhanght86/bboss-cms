package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.platform.cms.documentmanager.bean.DocExtValue;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
/**
 * 扩展字段显示标签
 * <p>Title: BaseExtendTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-17 16:24:33
 * @author biaoping.yin
 * @version 1.0
 */
public class BaseExtendTag extends BaseCellTag{
	protected String field = null;
	protected boolean isclob = false;
	protected DocExtValue docExtValue;
	/**
	 * 标识是否对扩展字段的内容进行发布处理
	 * true:处理
	 * false:不处理,默认值
	 */
	protected boolean process = false;
	public boolean isProcess() {
		return process;
	}


	public void setProcess(boolean process) {
		this.process = process;
	}

	protected Map<String,DocExtValue> extenddatas;
	public int doStartTag() throws JspException {
		super.doStartTag();
		DocExtends docExtends = (DocExtends) this.findAncestorWithClass(this, DocExtends.class);
		if(docExtends == null)
		{
			this.doaloneTag();
		}
		else
		{
			doInnerTag(docExtends);
		}
		
		String outStr =getOut(this.docExtValue);
		if(this.process)
		{
			
			if(!docExtValue.isProcessed())
			{
				String encoding = super.context.getSite().getEncoding();
				
				CmsLinkProcessor processor = new CmsLinkProcessor(context,
																  CmsLinkProcessor.REPLACE_LINKS,
																  encoding);
				String cchanneldir = null;
				if(docExtends == null)
				{
					cchanneldir = getCurrentChannelDir();
				}
				else
				{
					cchanneldir = docExtends.getCurrentChannelDir();
				}
				
				processor.setCurrentChannelDir(cchanneldir);
				processor.setHandletype(CmsLinkProcessor.PROCESS_CONTENT);
				try {
					outStr = processor.process(outStr,encoding);
					this.context.addContentOrigineTemplateLinkTable(processor.getOrigineTemplateLinkTable());
					this.docExtValue.setStringvalue(outStr);
					this.docExtValue.setProcessed(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		else 
		{
			if( !isclob)
			{
				
			}
			else
			{
				if(!docExtValue.isProcessed())
				{
					String encoding = super.context.getSite().getEncoding();
					
					CmsLinkProcessor processor = new CmsLinkProcessor(context,
																	  CmsLinkProcessor.REPLACE_LINKS,
																	  encoding);
					String cchanneldir = null;
					if(docExtends == null)
					{
						cchanneldir = getCurrentChannelDir();
					}
					else
					{
						cchanneldir = docExtends.getCurrentChannelDir();
					}
					processor.setCurrentChannelDir(cchanneldir);
					processor.setHandletype(CmsLinkProcessor.PROCESS_CONTENT);
					try {
						outStr = processor.process(outStr,encoding);
						this.context.addContentOrigineTemplateLinkTable(processor.getOrigineTemplateLinkTable());
						this.docExtValue.setStringvalue(outStr);
						this.docExtValue.setProcessed(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		if(outStr != null)
		{
			if(getEncode() != null && getEncode().equals("true"))
				outStr = URLEncoder.encode(outStr);
			if(getDecode() != null && getDecode().equals("true"))
				outStr = URLDecoder.decode(outStr);
		}
		try { 
			
			if(this.maxlength > 0 && outStr != null && outStr.length() > maxlength)
			{
				outStr = outStr.substring(0,this.maxlength);
				if(replace != null)
					outStr += replace;
			}
			out.print(outStr);
			
		}
		catch(Exception e)
		{
			try {
				out.print(outStr);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return this.SKIP_BODY;
	
	}
	
	private void doaloneTag()
	{
		if(super.dataSet != null)
		{
			if(this.getColName() != null)
			{
				extenddatas = dataSet.getMap(this.getColName());
			}
			else
			{
				extenddatas = dataSet.getMap("docExtField");
			}
			
		}
		if(extenddatas != null)
		{
			DocExtValue value = extenddatas.get(field);
			docExtValue = value;
		}
		
	}
	
	private void doInnerTag(DocExtends docExtends)
	{
		this.docExtValue = docExtends.currentdocExtValue.getValue();
		this.extenddatas = docExtends.extenddatas;
		
	}
	
	
	protected String getOut(DocExtValue value)
	{
		
			if(value != null)
			{
				if(value.getFieldtype().equals("0"))
					return String.valueOf(value.getIntvalue());
				else if(value.getFieldtype().equals("2"))
				{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					return format.format(value.getDatevalue());
				}
				else
				{
					isclob = value.getFieldtype().equals("3");
					
					return value.getStringvalue();
				}
			}
			else
			{
				if(this.defaultValue != null)
					return defaultValue.toString();
			}
		
		return "";
	}
	


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}
	
	public int doEndTag() throws JspException
	{
		int ret = super.doEndTag();
		this.extenddatas = null;
		process = false;
		docExtValue = null;
		isclob = false;
		return ret;
	}

}
