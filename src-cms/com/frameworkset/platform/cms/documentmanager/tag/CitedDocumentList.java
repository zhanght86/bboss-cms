package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class CitedDocumentList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String channelid = request.getParameter("channelid");
		
		String docName = request.getParameter("docName");
		String chnlName = request.getParameter("chnlName");
		
		String citeUserid = request.getParameter("citeUserid");
		String srcChnlid = request.getParameter("srcChnlid");
		String citeTimeBgin = request.getParameter("citeTimeBgin");
		String citeTimeEnd = request.getParameter("citeTimeEnd");
		String queryFlag = request.getParameter("queryFlag");
		
		ListInfo listInfo = new ListInfo();
		DocumentManagerImpl docManager = new DocumentManagerImpl();
		String sql;
		try{
			if(channelid !=null && !channelid.equals("")){
				
				sql = "select a.* from td_cms_chnl_ref_doc a where a.chnl_id = " + channelid;
				if("part".equals(queryFlag)){
					sql = "select distinct a.* from td_cms_chnl_ref_doc a,td_cms_document b,td_cms_channel c " +
							"where a.chnl_id = " + channelid;
					if(docName != null && docName.length()>0)
						sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.subtitle like '%" + docName +"%' ";
					if(chnlName != null && chnlName.length()>0)
						sql += " and c.status = 0 and a.doc_id = c.channel_id and c.display_name like '%" + chnlName +"%'";
					if(citeUserid != null && citeUserid.length()>0)
						sql += " and a.op_user_id =" + citeUserid ;
					if(srcChnlid != null && srcChnlid.length()>0)			//文档源频道查询
						sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.channel_id = " + srcChnlid;
					if(citeTimeBgin != null && citeTimeBgin.length()>0)
						sql += " and a.op_time >=to_date('"+citeTimeBgin+"','yyyy-mm-dd')";
					if(citeTimeEnd != null && citeTimeEnd.length()>0)
						sql += " and a.op_time < to_date('"+citeTimeEnd+"','yyyy-mm-dd')+1";
				}
				sql +=" order by a.op_time desc";
				listInfo = docManager.getCitedDocList(sql,(int)offset,maxPagesize);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}