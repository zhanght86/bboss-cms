package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 站内（整站文档查询）
 * <p>Title: SiteDocumentList</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-12 13:19:53
 * @author xingwang.jiao
 * @version 1.0
 */
public class SiteDocumentList extends DataInfoImpl implements java.io.Serializable
{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {	
		//取当前用户username
		String curUser = accessControl.getUserAccount();
		
		//普通查询字段，针对文档标题状态类型以及发稿人查询
		String title = request.getParameter("title");
		String status = request.getParameter("status");
		String user = request.getParameter("userid");
		String doctype = request.getParameter("doctype");
		String docLevel = request.getParameter("docLevel");
			
		//查询标志
		String flag = request.getParameter("flag");

	 	String channelid = request.getParameter("channelId");
		String channelName = request.getParameter("channelName");
		String siteid = request.getParameter("siteid");
		if(siteid == null || siteid.equals("") || siteid.equals("null"))
		{
			CMSManager cms = new CMSManager();
			cms.init(request,session,this.accessControl);
			siteid = cms.getSiteID();
		}
		request.setAttribute("channelId",channelid);
		request.setAttribute("channelName",channelName);
		request.setAttribute("siteid",siteid);
		
		String channelIds = request.getParameter("channelIds");
		if(channelIds!=null && channelIds.endsWith(",")){
			channelIds = channelIds.substring(0,channelIds.length()-1);
		}
			
		//高级查询增加的字段
		String author = request.getParameter("author");
		String docAbstract = request.getParameter("docAbstract");
		String docsorid = request.getParameter("docsorid");
		String keyword = request.getParameter("keyword");
		String createTimeBegin = request.getParameter("createTimeBegin");
		String createTimeEnd = request.getParameter("createTimeEnd");
		String content = request.getParameter("content");
		
		ListInfo listInfo = new ListInfo();
		DocumentManager docManager = new DocumentManagerImpl();
		try {	
			StringBuffer sql = new StringBuffer();
			sql.append("select abc.*,nvl(z.name,'未知流程') as flowname,nvl(u.name,'未知状态') as statusname,nvl(v.USER_REALNAME,'未知') as username ")
			   .append("from (select x.document_id,x.title,x.subtitle,x.author,")
			   .append("x.channel_id,x.status,")
			   .append("x.doctype,x.createtime,")
			   .append("x.docwtime,")
			   .append("nvl(w.name,'未知') as channelName,")
			   .append("x.createuser,x.flow_id, x.docabstract,-1 as order_no,count ")
			   .append("from TD_CMS_DOCUMENT x ,td_cms_channel w ,td_cms_site c ")
			   .append("where c.site_id = ")
			   .append(siteid)
			   .append(" and x.channel_id = w.channel_id and w.site_id = c.site_id ");
			if( !AccessControl.isAdmin(curUser) )
				sql.append(" and x.channel_id in (" + getChnlIds(siteid) + ") ");
			if(flag!=null && flag.equals("query")){        //这个是条件查询
				if (title != null && title.length() > 0) {
					sql.append(" and x.subtitle like '%"+ title +"%'");
				}
				if (status != null && status.length() > 0) {
					sql.append(" and x.status = '"+ status +"'");
				}
				if (user != null && user.length() > 0) {					
					sql.append(" and x.createuser = "+ user +" ");
				}
				if (doctype != null && doctype.length() > 0) {
					sql.append(" and x.doctype = '"+ doctype +"'");
				}
				if(author!=null && author.length()>0){
					sql.append(" and x.author like '%"+ author +"%'");
				}
				if(docAbstract!=null && docAbstract.length()>0){
					sql.append(" and x.docabstract like '%"+ docAbstract +"%'");
				}
				if(docsorid!=null && docsorid.length()>0){
					sql.append(" and x.docsource_id like '%"+ docsorid +"%'");
				}
				if(keyword!=null && keyword.length()>0){
					sql.append(" and x.keywords like '%"+ keyword +"%'");
				}
				if(createTimeBegin!=null && createTimeBegin.length()>0){
					sql.append(" and x.createTime > "+ DBUtil.getDBDate(createTimeBegin));  
				}
				if(createTimeEnd!=null && createTimeEnd.length()>0){
					sql.append(" and x.createTime < "+ DBUtil.getDBDate(createTimeEnd) + "+1 ");  
				}
				if(content!=null && content.length()>0){
					sql.append(" and x.content like '%"+ content +"%'");
				}
				if(docLevel != null && docLevel.length() != 0){
					sql.append(" and x.doc_level = "+ docLevel);
				} 
				if(channelIds != null && !channelIds.equals("")){
					//sql += " and x.channel_id in (" + channelIds + ")";
				}
			}	
			if(!sql.equals("")){
				sql.append(" ) abc ")
			   .append("left outer join tb_cms_flow z on abc.flow_id = z.id  ")
			   .append("left outer join tb_cms_doc_status u on abc.status = u.id  ")
			   .append("left outer join td_sm_user v on abc.createuser = v.user_id  ")
			   .append("order by abc.channel_id desc,abc.docwtime desc,abc.document_id desc");
				listInfo = docManager.getDocList(sql.toString(),(int)offset,maxPagesize);
			}
				   		
		}catch(Exception e){
	         e.printStackTrace();
		}
		return listInfo;	
	}	
      
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
	
	protected String getChnlIds(String siteid)
	{
		String chlIds = "";
		//取当前用户username
		//String curUserId = accessControl.getUserID();
		DBUtil db = new DBUtil();
		String sql = "select channel_id from td_cms_channel where site_id = " + siteid;
		try
		{
			db.executeSelect(sql);
			if( db.size() > 0)
			{
				for( int i=0;i<db.size();i++)
				{
					String chlid = db.getInt(i,0) + "";
					if( accessControl.checkPermission(chlid,AccessControl.WRITE_PERMISSION,AccessControl.CHANNEL_RESOURCE)
						|| accessControl.checkPermission(siteid,AccessControl.WRITE_PERMISSION,AccessControl.SITECHANNEL_RESOURCE))
					{
						if( !chlIds.equals(""))
							chlIds += "," + db.getInt(i,0);
						else
							chlIds += db.getInt(i,0);
					}
				}
				if( chlIds.equals(""))
					chlIds = "''";
			}
			else
				chlIds = "''";
		}
		catch(Exception e)
		{
			chlIds = "''";
			e.printStackTrace();
		}
		return chlIds;
	}
}