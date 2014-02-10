/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.platform.cms.documentmanager.bean.DocExtValue;

/**
 * <p>ExtendLableTag.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2014年2月10日
 * @author biaoping.yin
 * @version 1.0
 */
public class ExtendLableTag extends BaseCellTag{
	protected String field = null;
	
	protected DocExtValue docExtValue;
	
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
		
		try { 
			
			
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
			return value.getLabel();				
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
		docExtValue = null;
		return ret;
	}

}
