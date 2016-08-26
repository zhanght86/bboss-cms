package com.frameworkset.platform.cms.docCommentManager;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.ListInfo;

public class DocCommentList extends
		com.frameworkset.common.tag.pager.DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String docId = request.getParameter("docId");
		
		String docCommentPublishedFlag = (String)session.getAttribute("docCommentPublished");    //热门留言
		session.removeAttribute("docCommentPublished");
		String docCommentHotFlag = (String)session.getAttribute("docCommentHot");    //热门留言
		session.removeAttribute("docCommentHot");
		String docCommentFilteredFlag = (String)session.getAttribute("docCommentFiltered");  //过滤水贴
		session.removeAttribute("docCommentFiltered");
		
		docId = docId==null?"33430":docId;    //测试用
		String docComment = request.getParameter("docComment");
		String userName = request.getParameter("userName");
		String subTimeBgin = request.getParameter("subTimeBgin");
		String subTimeEnd = request.getParameter("subTimeEnd");
		String queryFlag = request.getParameter("queryFlag");
		String status = request.getParameter("status");	
		String hasImpeach = request.getParameter("hasImpeach");	
		
		DocCommentManager dcm = new DocCommentManagerImpl();
		
		String sql = "select a.*,b.subtitle as docTitle " +
				"from td_cms_doc_comment a inner join td_cms_document b on b.document_id=a.doc_id " +
				"where a.doc_id=" + docId;
		ListInfo listInfo = null;
		if("part".equals(queryFlag)){
			if(docComment!=null && docComment.length()>0)
				sql += " and a.doc_comment like '%" + docComment + "%'";
			if(userName!=null && userName.length()>0)
				sql += " and a.user_name like '%" + userName + "%'"; 
			if(subTimeBgin!=null && subTimeBgin.length()>0)
				sql += " and a.comtime >" + DBUtil.getDBDate(subTimeBgin)+ "-1";
			if(subTimeEnd!=null && subTimeEnd.length()>0)
				sql += " and a.comtime <" + DBUtil.getDBDate(subTimeEnd) + "+1";
			if(status!=null && status.length()>0)
				sql += " and a.status = " + status ;
			if(hasImpeach!=null && hasImpeach.length()>0)
				if("0".equals(hasImpeach))
					sql += " and (select count(c.comment_id) from td_cms_doccom_impeachinfo c where c.comment_id=a.comment_id)=0" ;
				if("1".equals(hasImpeach))
					sql += " and (select count(c.comment_id) from td_cms_doccom_impeachinfo c where c.comment_id=a.comment_id)>0" ;
		}
		if(docCommentPublishedFlag!=null && docCommentPublishedFlag.length()>0){      //查询所有已审通过的热贴
			sql += " and a.status = 1 "; 
		}
		if(docCommentHotFlag!=null && docCommentHotFlag.length()>0){      //查询热贴   暂时定为引用两次即算是热贴,且过滤水贴
			sql += " and a.status = 1 and (select count(c.comment_id) from td_cms_doc_comment c where a.comment_id = c.src_comment_id) >= 2"; 
			sql += " and a.comment_id not in (select d.comment_id from td_cms_doc_comment d where d.doc_comment like '顶' or d.doc_comment like '不好说' or d.doc_comment like'不知所云' or d.doc_comment like '反对')";
		}
		if(docCommentFilteredFlag!=null && docCommentFilteredFlag.length()>0){  		//过滤水贴
			sql += " and a.status = 1 and a.comment_id not in (select d.comment_id from td_cms_doc_comment d where d.doc_comment like '顶' or d.doc_comment like '不好说' or d.doc_comment like'不知所云' or d.doc_comment like '反对')"; 
		}
		try{
			sql += " order by comtime desc,comtime desc";
			listInfo = dcm.getCommnetList(sql,(int)offset,maxPagesize);
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
