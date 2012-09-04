package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class PublishingDocList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {		
		String flag =request.getParameter("flag");
		String publisher =request.getParameter("publisher");
		String docTitle =request.getParameter("docTitle");
		String channelIds =request.getParameter("channelIds");
		if(channelIds!=null && channelIds.endsWith(",")){
			channelIds = channelIds.substring(0,channelIds.length()-1);
		}
		
		ListInfo listInfo=null;
		DocumentManager docManager = new DocumentManagerImpl();
		try{
			String sql = "select  a.document_id, a.title,a.subtitle,a.doctype," +
					"a.flow_id,a.createtime,c.channel_id, c.NAME channel_name, " +
					"d.site_id, d.NAME docsitename,f.publisher,f.pub_start_time,g.name docoldstatusname "
				+ "from td_cms_document a,td_cms_channel c,td_cms_site d,td_cms_doc_publishing f,tb_cms_doc_status g "
				+ "where  a.isdeleted != 1 and f.document_id = a.document_id "
				+ " and a.channel_id = c.channel_id and c.site_id = d.site_id and (d.status = 0 or d.status = 1) " +
				  " and f.old_status=g.id";
			if("query".equals(flag)){
				if(publisher!=null && publisher.length()>0)
					sql += " and f.publisher like '%" + publisher + "%'";
				if(docTitle!=null && docTitle.length()>0)
					sql += " and a.subtitle like '%" + docTitle + "%'";
				if(channelIds!=null && channelIds.length()>0)
					sql += " and a.channel_id in (" + channelIds + ")";
			}
			sql += " order by  f.pub_start_time desc, c.channel_id desc, a.document_id desc";
			listInfo = docManager.getPubishingDocList(sql, (int) offset, maxPagesize);
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