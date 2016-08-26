package com.frameworkset.platform.cms.driver.jsp;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.publish.PublishMode;

public class JspletWindowImpl implements JspletWindow{
	private Context context;
	private JspFile jspFile;
	private JspletWindowID windowid;
	public JspletWindowImpl(Context context, JspFile jspFile) {
		this.context = context;
		this.jspFile = jspFile;
		this.windowid = JspletWindowIDImpl.createFromString(context.getID());
	}

	public String getContextPath() {
		
		return "creatorcms";
	}

	public JspletWindowID getJspletWindowID() {
		// TODO Auto-generated method stub
		return windowid;
	}

	public String getJspName() {
		// TODO Auto-generated method stub
		return jspFile.getName();
	}

	public PublishMode getPublishMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public Context getContext() {
		return context;
	}

	public JspFile getJspFile() {
		return jspFile;
	}

}
