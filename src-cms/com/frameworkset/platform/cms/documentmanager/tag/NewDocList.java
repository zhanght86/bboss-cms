package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 新文档列表
 */
public class NewDocList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String curUserid = (String) request.getAttribute("curUserid");
		
		String flag = request.getParameter("flag");
		String subTitle = request.getParameter("title");
		String keyword = request.getParameter("keyword");
		String docAbstract = request.getParameter("docAbstract");
		String docType = request.getParameter("doctype");
		String docLevel = request.getParameter("docLevel");
		String creatTime = request.getParameter("creatTime");
		String channelIds = request.getParameter("channelIds");
		if(channelIds!=null && channelIds.endsWith(",")){
			channelIds = channelIds.substring(0,channelIds.length()-1);
		}
		
		ListInfo listInfo = null;
		DocumentManagerImpl docManager = new DocumentManagerImpl();
		try {
			String sql = "SELECT  a.document_id, a.title,a.subtitle,a.doctype,a.flow_id,a.createtime,c.channel_id, c.NAME channel_name, d.site_id, d.NAME docsitename, b.user_name "
					+ "FROM td_cms_document a,td_sm_user b,td_cms_channel c,td_cms_site d "
					+ "WHERE  a.isdeleted != 1 AND a.status = 1 AND a.CREATEUSER = "
					+ curUserid
					+ " AND b.user_id = a.CREATEUSER "
					+ "AND a.channel_id = c.channel_id AND c.site_id = d.site_id AND (d.status = 0 OR  d.status = 1) ";
			if ("query".equals(flag)) {
				if (subTitle != null && subTitle.length() > 0) 
					sql = sql + " and a.subtitle like '%" + subTitle + "%'";
				if (docType != null && docType.length() > 0) 
					sql = sql + " and a.doctype = '" + docType + "'";
				if (docAbstract != null && docAbstract.length() > 0) 
					sql = sql + " and a.docabstract like '%" + docAbstract + "%'";	
				if (keyword != null && keyword.length() > 0) 
					sql = sql + " and a.keywords like '%" + keyword + "%'";								
				if (docLevel != null && docLevel.length() != 0) 
					sql = sql + " and a.doc_level = " + docLevel;
				if(creatTime != null && !creatTime.equals(""))
					sql += " and a.createtime <" + DBUtil.getDBDate(creatTime) + "+1";
				if(channelIds != null && !channelIds.equals(""))
					sql += " and c.channel_id in (" + channelIds + ")";				
			}
			sql += " order by  a.createtime desc, c.channel_id desc, a.document_id desc";
			listInfo = docManager.getNewDocList(sql, (int) offset, maxPagesize);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		
		return null;
	}

}
