package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;

/**
 * <p>
 * Title: DistributeTag.java
 * </p>
 * 分发模板公共控件依赖的附件信息，例如日历控件依赖的文件，这个标签在运行时不做任何操作，只能
 * 在模板解析时进行处理
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-8-7 下午5:25:33
 * @author biaoping.yin
 * @version 1.0.0
 */
public class DistributeTag extends CMSBaseTag {
	private String dir;

	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	@Override
	public int doStartTag() throws JspException {
		
		int ret = super.doStartTag();
//		if(StringUtil.isEmpty(dir))
//			return ret;
//		CMSUtil.addPublishedLinkDirPath(context, "", dir);
		return ret;
	}
	@Override
	public void doFinally() {
		this.dir = null;
		super.doFinally();
	}
	

}
