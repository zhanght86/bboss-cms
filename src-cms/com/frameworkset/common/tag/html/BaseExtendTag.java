package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseCellTag;
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
				extenddatas = dataSet.getMap("docExtField");
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

}
