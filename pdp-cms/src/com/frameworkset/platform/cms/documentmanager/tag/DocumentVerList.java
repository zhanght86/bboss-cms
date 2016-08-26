package com.frameworkset.platform.cms.documentmanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author xinwang.jiao
 * 文档版本管理列表
 * 2007.02.1
 * version 1.0
 */
public class DocumentVerList extends DataInfoImpl implements java.io.Serializable
{
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getDocVerList(String sql, int offset, int maxItem) throws DocumentManagerException 
	{
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		try 
		{
			dbUtil.executeSelect(sql, offset, maxItem);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			Document document;
			
			for (int i = 0; i < dbUtil.size(); i++) 
			{
				document = new Document();
				document.setDocument_id(dbUtil.getInt(i,"document_id"));
				document.setVersion(dbUtil.getInt(i,"version"));
				document.setTitle(dbUtil.getString(i,"title"));
				document.setSubtitle(dbUtil.getString(i,"SUBTITLE"));
				document.setOpTime(dbUtil.getDate(i,"op_time"));
				document.setVerLable(dbUtil.getString(i,"label"));
				document.setVerDiscription(dbUtil.getString(i,"versiondescription"));
				
				String str = "select USER_REALNAME from td_sm_user where user_id=" + dbUtil.getInt(i,"op_user");
				db.executeSelect(str);
				if(db.size()>0){
					document.setUsername(db.getString(0,"USER_REALNAME"));
				}
				
				list.add(document);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
			return listInfo;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}
	/**
	 * 
	 */
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,int maxPagesize)
	{
		ListInfo listInfo = new ListInfo();
		
		String docId = request.getParameter("docid");	
		String queryFlag = request.getParameter("queryFlag");
		String versionNum = request.getParameter("versionNum");
		String docVerTimeBgin = request.getParameter("docVerTimeBgin");
		String docVerTimeEnd = request.getParameter("docVerTimeEnd");
		String docVerUser = request.getParameter("docVerUser");
		String docVerLable = request.getParameter("docVerLable");
		
		String sql="select document_id,version,title,subtitle,op_time,op_user,label,versiondescription " +
				"from  TD_CMS_DOC_VER where document_id = "+ docId;
		try
		{
			if("part".equals(queryFlag)){
				if(versionNum != null && versionNum.length() > 0){
					sql += " and version = " + versionNum; 
				}
				if(docVerTimeBgin != null && docVerTimeBgin.length() > 0){
					sql += " and op_time >" + DBUtil.getDBDate(docVerTimeBgin); 
				}
				if(docVerTimeEnd != null && docVerTimeEnd.length() > 0){
					sql = sql + " and op_time <" + DBUtil.getDBDate(docVerTimeEnd) + "+1";
				}
				if(docVerUser != null && docVerUser.length() > 0){
					sql += " and op_user = " + docVerUser; 
				}
				if(docVerLable != null && docVerLable.length() > 0){
					sql += " and label like '%" + docVerLable + "%'"; 
				}
			}
			sql += " order by version desc";
			listInfo = getDocVerList(sql,(int)offset,maxPagesize);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listInfo;
	}
	/**
	 * 
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) 
	{
        return null;
	}

}
