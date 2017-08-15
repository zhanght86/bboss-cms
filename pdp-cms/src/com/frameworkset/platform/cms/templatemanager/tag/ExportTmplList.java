package com.frameworkset.platform.cms.templatemanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 导出模板列表
 * <p>Title: ExportTmplList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-9-29 16:08:32
 * @author ge.tao
 * @version 1.0
 */
public class ExportTmplList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		
		String siteId = request.getParameter("siteId");
		ListInfo listInfo = new ListInfo();
		try {
			listInfo = (CMSUtil.getCMSDriverConfiguration().getCMSService()
					.getTemplateManager().getExportTmplList(offSet,pageItemsize,Integer.parseInt(siteId)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listInfo;
	}

	
	protected ListInfo getDataList(String sortKey, boolean desc) {
		return null;
	}
}