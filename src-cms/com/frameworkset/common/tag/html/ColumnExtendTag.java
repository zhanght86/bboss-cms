package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.htmlparser.util.ParserException;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.util.StringUtil;

/**
 * 
 * <p>Title: ColumnExtendTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date May 30, 2007 5:02:22 PM
 * @author ge.tao
 * @version 1.0
 */
public class ColumnExtendTag extends BaseCellTag {
	protected String field = null;
	/**
	 * 标识是否对扩展字段的内容进行发布处理
	 * true:处理
	 * false:不处理
	 */
	protected boolean process = true;
	protected Map extenddatas;
	public int doStartTag() throws JspException {
		super.doStartTag();
		if(super.dataSet != null)
		{
			if(this.getColName() != null)
			{
				extenddatas = dataSet.getMap(this.getColName());
			}
			else
			{
				extenddatas = dataSet.getMap("extColumn");
			}
			
		}
		String outStr = super.getOutStr();
		
		
		if(outStr != null)
		{
			if(getEncode() != null && getEncode().equals("true"))
				outStr = URLEncoder.encode(outStr);
			if(getDecode() != null && getDecode().equals("true"))
				outStr = URLDecoder.decode(outStr);
			
		}
		if(StringUtil.isEmpty(outStr))
		{
			return this.SKIP_BODY;
		}
		
		try { 
			if(this.process)
			{
				String encoding = super.context.getSite().getEncoding();
				
				CmsLinkProcessor processor = new CmsLinkProcessor(context,
																  CmsLinkProcessor.REPLACE_LINKS,
																  encoding);
				processor.setHandletype(CmsLinkProcessor.PROCESS_CONTENT);
				try {
					outStr = processor.process(outStr,encoding);
					this.context.addContentOrigineTemplateLinkTable(processor.getOrigineTemplateLinkTable());
					
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
	
	
	protected String getOut()
	{
		if(extenddatas != null)
		{
			Object value = extenddatas.get(field);
			if(value != null)
				return value.toString();
			else
			{
				if(this.defaultValue != null)
					return defaultValue.toString();
			}
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
		return ret;
	}


	public boolean isProcess() {
		return process;
	}


	public void setProcess(boolean process) {
		this.process = process;
	}


	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		super.doFinally();
		process = true;
	}

}
