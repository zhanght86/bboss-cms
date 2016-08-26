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

import javax.servlet.jsp.JspException;

import org.htmlparser.util.ParserException;

import com.frameworkset.common.tag.CMSBaseCellTag;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;

/**
 * <p>CMSContentCellTag.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2014年1月19日
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSContentCellTag extends CMSBaseCellTag{

	public CMSContentCellTag() {
		
	}
	
	

	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		int ret = super.doStartTag();
		String content = this.getOutStr();
		String encoding = super.context.getSite().getEncoding();
		
		
		
		CmsLinkProcessor processor = new CmsLinkProcessor(context,
														  CmsLinkProcessor.REPLACE_LINKS,
														  encoding);
		
		String currentChannelDir = this.getCurrentChannelDir();
		
		processor.setCurrentChannelDir(currentChannelDir);
		processor.setHandletype(CmsLinkProcessor.PROCESS_CONTENT);
		
		try {
			content = processor.process(content,encoding);
			/**暂时不需要考虑,内容的发布随内容的发布而发布
			
			*/
			this.context.addContentOrigineTemplateLinkTable(processor.getOrigineTemplateLinkTable());
			out.print(content);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

}
