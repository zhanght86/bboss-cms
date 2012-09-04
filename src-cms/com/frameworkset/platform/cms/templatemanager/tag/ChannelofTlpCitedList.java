package com.frameworkset.platform.cms.templatemanager.tag;

import com.frameworkset.platform.cms.templatemanager.TemplateManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 模板的频道引用列表，用于引用查看
 * @author Administrator
 *
 */
public class ChannelofTlpCitedList extends DataInfoImpl{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = null;	
		String templateId=request.getParameter("templateId");
		String indexPagePath=request.getParameter("indexPagePath");
		String siteId=request.getParameter("siteId");
		TemplateManager tm = new TemplateManagerImpl();
		try {
			String sql = "";
			if(indexPagePath == null || indexPagePath.length()==0)
				sql = "select a.*,b.name as siteName from td_cms_channel a,td_cms_site b where a.site_id = b.site_id and (a.outline_tpl_id =" + templateId + " or a.detail_tpl_id =" + templateId + ")";
			else
				sql = "select a.*,b.name as siteName from td_cms_channel a,td_cms_site b where a.site_id = b.site_id and a.indexpagepath = '" + indexPagePath + "' and a.site_id = " + siteId; 
			listInfo = tm.getChannelListofTlpCited(sql,(int)offset,maxPagesize);
			
		} catch (TemplateManagerException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
		 *      boolean)
		 */
	protected ListInfo getDataList(String arg0, boolean arg1) {		
		return null;
	}
}
