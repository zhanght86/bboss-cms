package com.frameworkset.platform.cms.documentmanager.tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.bean.ArrangeDoc;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author xinwang.jiao
 * 文档置顶管理列表
 * 2007.03.7
 * version 1.0
 */
public class DocArrangeList extends DataInfoImpl implements java.io.Serializable
{
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getDocArrangeList(String sql) throws DocumentManagerException 
	{
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		try 
		{
			dbUtil.executeSelect(sql);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			ArrangeDoc arrangeDoc;
			
			for (int i = 0; i < dbUtil.size(); i++) 
			{
				arrangeDoc = new ArrangeDoc();
				arrangeDoc.setDocumentId(dbUtil.getInt(i,"document_id"));
				arrangeDoc.setDoctitle(dbUtil.getString(i,"subtitle"));
				arrangeDoc.setEndTime(dbUtil.getString(i,"end_time"));
				arrangeDoc.setStartTime(dbUtil.getString(i,"start_time"));
				arrangeDoc.setOvertime(dbUtil.getInt(i,"overtime"));
				String str = "select USER_REALNAME from td_sm_user where user_id=" + dbUtil.getInt(i,"op_user");
				db.executeSelect(str);
				if(db.size()>0){
					arrangeDoc.setUsername(db.getString(0,"USER_REALNAME"));
				}
				
				list.add(arrangeDoc);
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
	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,int maxPagesize)
	{
		return null;
	}
	/**
	 * 
	 */
	protected ListInfo getDataList(String sortKey, boolean arg1)
	{
		ListInfo listInfo = null;
		
		String chnlid = request.getParameter("channelid");
		
		String sqlstr = "select a.document_id,a.start_time,a.end_time,a.order_no,a.op_user,a.op_time,b.subtitle,case  when " + DBUtil.getDBAdapter().to_date(new Date()) + ">to_date(end_time,'yyyy-mm-dd hh24-mi-ss') then 1 else 0 end as overtime " + 
			" from Td_Cms_Doc_Arrange a inner join td_cms_document b " +
			" on a.document_id=b.document_id and b.status!=7 and b.channel_id = " + chnlid +
			" order by overtime,a.order_no desc";
		try
		{
			listInfo = getDocArrangeList(sqlstr);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listInfo;
	}
	

}
