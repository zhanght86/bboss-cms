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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.platform.cms.documentmanager.bean.DocExtValue;

/**
 * <p>DocExtends.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2014年2月10日
 * @author biaoping.yin
 * @version 1.0
 */
public class DocExtends  extends BaseCellTag {

	
	protected Entry<String, DocExtValue> currentdocExtValue;
	

	protected Map<String,DocExtValue> extenddatas;
	protected Iterator<Entry<String, DocExtValue>> extenddataIterator;
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
			if(extenddatas != null)
			{
				extenddataIterator = this.extenddatas.entrySet().iterator();
				if(extenddataIterator.hasNext())
				{
					currentdocExtValue = extenddataIterator.next();
				}
			}
			
		}
		if(currentdocExtValue == null)
			return this.SKIP_BODY;
		else
			return this.EVAL_BODY_INCLUDE;
	
	}
	
	
	
	public int doEndTag() throws JspException
	{
		int ret = super.doEndTag();
		
		return ret;
	}



	@Override
	public int doAfterBody() throws JspException {
		if(this.extenddataIterator.hasNext())
		{
			currentdocExtValue = extenddataIterator.next();
			return EVAL_BODY_AGAIN;
		}
		else
		{
			return SKIP_BODY;
		}
	}



	@Override
	public void doFinally() {
		this.extenddatas = null;
		this.currentdocExtValue = null;
		 currentdocExtValue = null;
			

			  extenddatas = null;
			  extenddataIterator = null;
		super.doFinally();
	}

}
