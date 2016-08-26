package com.frameworkset.platform.cms.docCommentManager;

import com.frameworkset.util.ListInfo;

public class CommentImpeachList extends
		com.frameworkset.common.tag.pager.DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String commentId = request.getParameter("commentId");
		ListInfo listInfo = null;
		DocCommentManager dcm = new DocCommentManagerImpl();
		
		String sql = "select * from td_cms_doccom_impeachinfo where comment_id =" + commentId;
		try{
			sql += " order by comtime desc,id desc";
			listInfo = dcm.getCommnetImpeachList(sql,(int)offset,maxPagesize);
		}catch(DocCommentManagerException de){
			de.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
