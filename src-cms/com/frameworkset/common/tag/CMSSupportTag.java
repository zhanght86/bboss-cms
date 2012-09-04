package com.frameworkset.common.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.frameworkset.common.tag.html.CMSListTag;

public class CMSSupportTag extends CMSBaseTag {
	protected CMSListTag listTag = null;
	protected CMSListTag searchCMSListTag(Tag obj, Class clazz) {
		CMSListTag listTag = null;
//		if (this.getIndex() < 0) {
		listTag = (CMSListTag) findAncestorWithClass(obj, clazz);
//		} else {
//			dataSet = getPagerDataSet(getIndex());
//			// java.util.Stack stack =
//			// (java.util.Stack) request.getAttribute(
//			// PagerDataSet.PAGERDATASET_STACK);
//			// dataSet = (PagerDataSet) stack.elementAt(getIndex());
//		}
		return listTag;
	}
	
	public int doStartTag() throws JspException
	{
		
		listTag = this.searchCMSListTag(this,CMSListTag.class);
		if(listTag == null)
			super.doStartTag();
		return SKIP_BODY;
	}
	
	
	
	

}
