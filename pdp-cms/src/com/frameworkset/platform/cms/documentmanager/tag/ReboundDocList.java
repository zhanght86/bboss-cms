package com.frameworkset.platform.cms.documentmanager.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.util.ListInfo;

public class ReboundDocList extends DataInfoImpl implements java.io.Serializable {
    private Logger log = LoggerFactory.getLogger(ReboundDocList.class);
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String flag = request.getParameter("flag");
		String docTitle = request.getParameter("docTitle");
		String submitter = request.getParameter("submitter");
		String channelIds = request.getParameter("channelIds");
		if(channelIds!=null && channelIds.endsWith(",")){
			channelIds = channelIds.substring(0,channelIds.length()-1);
		}
		String subTime = request.getParameter("subTime");
		String curUserid = (String)request.getAttribute("curUserid");
		ListInfo listInfo=null;
		DocumentManagerImpl docManager = new DocumentManagerImpl();
		try{
			String sql = "select a.task_id as task_id, a.document_id " +
					"as document_id, a.submit_time as submit_time,f.name as docSiteName, " +
					" c.subtitle as subtitle, d.user_name as user_name, e.name as channel_name," +
					"c.channel_id as channel_id, c.doctype as doctype, e.site_id as site_id " +
					"from td_cms_doc_task a, td_cms_doc_task_detail b, " +
					"td_cms_document c, td_sm_user d ,td_cms_channel e,td_cms_site f " +
					"where c.isdeleted!=1 and a.pre_status=4 and c.status=4 and a.task_id=b.task_id and c.document_id=a.document_id " +
					"and e.channel_id=c.channel_id and d.user_id=a.submit_id and f.site_id=e.site_id and (f.status=0 or f.status=1) and " +
					"b.performer = " + curUserid + " and b.valid = 1 and complete_time is null " ;
			if(docTitle == null && submitter == null && channelIds == null && subTime == null && curUserid != null 
					|| flag.equals("all")){
				sql += " order by a.submit_time desc,e.channel_id,a.document_id desc";
				log.warn(sql);
				listInfo = docManager.getReboundtDocList(sql,(int)offset,maxPagesize);
			}
			else{
				if(docTitle != null && !docTitle.equals(""))
					sql += " and c.subtitle like '%" + docTitle + "%'";
				if(submitter != null && !submitter.equals(""))
					sql += " and d.user_name like '%" + submitter + "%'";
				if(channelIds != null && !channelIds.equals(""))
					sql += " and c.channel_id in(" + channelIds + ")";
				if(subTime != null && !subTime.equals(""))
					sql += " and a.submit_time <" + DBUtil.getDBDate(subTime) + "+1";
				sql += " order by a.submit_time desc,e.channel_id,a.document_id desc";
				log.warn(sql);
				listInfo = docManager.getAuditDocList(sql,(int)offset,maxPagesize);		
			}		
		}catch(Exception ex){
			ex.printStackTrace();
			}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}

