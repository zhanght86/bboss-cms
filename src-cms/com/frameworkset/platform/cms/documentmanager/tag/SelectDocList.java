package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 频道》引用管理》文档引用的文档列表数据获取接口
 * <p>Title: SelectDocList</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-12 13:19:01
 * @author huiqiong.zeng
 * @version 1.0
 */
public class SelectDocList extends DataInfoImpl implements java.io.Serializable
{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {	
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
			request.setAttribute("channelId",channelid);
			request.setAttribute("channelName",channelName);
			request.setAttribute("siteid",siteid);
			
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
				StringBuffer sql= new StringBuffer();
				sql.append("select x.document_id,title,subtitle,author,x.channel_id,nvl(w.name,'未知') as channelName,x.status,nvl(u.name,'未知状态') as statusname,")
				   .append("doctype,docwtime,createuser,nvl(v.USER_REALNAME,'未知') as username,x.flow_id,nvl(z.name,'未知流程') as flowname,docabstract,nvl(y.order_no,-1) as order_no,end_time,x.count ")
				   .append("from  TD_CMS_DOCUMENT x left outer join td_cms_doc_arrange y ")
				   .append("on x.document_id = y.document_id ")
				   .append(" left outer join tb_cms_flow z on x.flow_id = z.id ")
				   .append(" left outer join tb_cms_doc_status u on x.status = u.id ")
				    .append(" left outer join td_sm_user v on x.createuser = v.user_id ")
				    .append(" left outer join td_cms_channel w on x.channel_id = w.channel_id ")
				   .append(" where x.channel_id =")
				   .append(channelid)
				   .append(" and ")
				   .append("status not in(8,20,100,10,12) and isdeleted != 1 and " )
				   .append("(select count(*) from td_cms_site a, td_cms_channel b " )
				   .append("where b.site_id = a.site_id and (a.status = 0 or a.status = 1) " )
				   .append(" and b.channel_id =" )
				   .append( channelid )
				   .append( ")>0 " )
				   .append(" and doctype != 3 ");//过滤聚合文档类型
				if(flag!=null && flag.equals("query")){        //这个是条件查询
					if (title != null && title.length() > 0) {
						sql.append(" and subtitle like '%"+ title +"%'");
					}
					if (status != null && status.length() > 0) {
						sql.append(" and status = '"+ status +"'");
					}
					if (user != null && user.length() > 0) {					
						sql.append(" and createuser = "+ user +" ");
					}
					if (doctype != null && doctype.length() > 0) {
						sql.append(" and doctype = '"+ doctype +"'");
					}
					if(author!=null && author.length()>0){
						sql.append(" and author like '%"+ author +"%'");
					}
					if(docAbstract!=null && docAbstract.length()>0){
						sql.append(" and docabstract like '%"+ docAbstract +"%'");
					}
					if(docsorid!=null && docsorid.length()>0){
						sql.append(" and docsource_id like '%"+ docsorid +"%'");
					}
					if(keyword!=null && keyword.length()>0){
						sql.append(" and keywords like '%"+ keyword +"%'");
					}
					if(createTimeBegin!=null && createTimeBegin.length()>0){
						sql.append(" and docwtime > "+ DBUtil.getDBDate(createTimeBegin));  
					}
					if(createTimeEnd!=null && createTimeEnd.length()>0){
						sql.append(" and docwtime < "+ DBUtil.getDBDate(createTimeEnd) + "+1 ");  
					}
					if(content!=null && content.length()>0){
						sql.append(" and content like '%"+ content +"%'");
					}
					if(docLevel != null && docLevel.length() != 0){
						sql.append(" and doc_level = "+ docLevel);
					} 
				}
				if(!sql.equals("")){
					sql.append(" order by docwtime desc,document_id desc");
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
}