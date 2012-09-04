package com.sany.common.menu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.platform.security.AccessControl;

public class AccessControlTag extends BaseTag {
	private String userattribute;
	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		super.doFinally();
		userattribute = null;
	}

	@Override
	public int doStartTag() throws JspException {
		
		int ret = super.doStartTag();
		AccessControl control = AccessControl.getAccessControl();
		if(control == null)
		{
			
		}
		else
		{
			try {
				out.print(control.getUserAttribute(userattribute));
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return ret;
	}

	public String getUserattribute() {
		return userattribute;
	}

	public void setUserattribute(String userattribute) {
		this.userattribute = userattribute;
	}
	

}
