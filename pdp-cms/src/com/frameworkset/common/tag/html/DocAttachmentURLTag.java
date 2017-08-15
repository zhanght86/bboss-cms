package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title: DocAttachmentURLTag.java
 * </p>
 * 
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
 * @Date 2012-8-15 下午3:36:25
 * @author biaoping.yin
 * @version 1.0.0
 */
public class DocAttachmentURLTag extends BaseCellTag {

	public int doStartTag() throws JspException {
		if (this.getColName() == null) {
			this.setColName("url");
		}
		int ret = super.doStartTag();
		if (dataSet == null) {
			return SKIP_BODY;
		}

		String url = super.getOutStr();

		try {
			String link = CMSUtil.PUBLISHFILEFORDER + url;
			Document document = (Document) this.dataSet.getValue("document");
			link = CMSUtil.getPublishedContentAttachPath(context, document,
					link);
			out.print(link);

		} catch (Exception e) {
			context.getPublishMonitor().addFailedMessage(
					StringUtil.exceptionToString(e), context.getPublisher());
		}
		return ret;
	}

	public int doEndTag() throws JspException {
		int ret = super.doEndTag();

		this.setColName(null);
		return ret;
	}

}
