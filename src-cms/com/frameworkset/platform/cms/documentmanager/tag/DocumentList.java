package com.frameworkset.platform.cms.documentmanager.tag;

import org.apache.log4j.Logger;

import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class DocumentList extends DataInfoImpl implements java.io.Serializable
{
     protected Logger log = Logger.getLogger(DocumentList.class);
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {	
		 	//普通查询字段，针对文档标题状态类型以及发稿人查询
	  		String title = request.getParameter("title");
			String status = request.getParameter("status");
			String user = request.getParameter("userid");
			String doctype = request.getParameter("doctype");
			String docLevel = request.getParameter("docLevel");
			String docClass = request.getParameter("docClass");
			
			//查询标志
			String flag = request.getParameter("flag");
			
			String orderByHit = request.getParameter("orderByHit");
			String orderByDocWtime = request.getParameter("orderByDocWtime");
			String orderStr  = "";
			
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
				StringBuffer sql = new StringBuffer();
				
				sql.append("select x.document_id,x.title,x.subtitle,x.author,x.channel_id,nvl(w.name,'未知') as channelName,x.status,nvl(u.name,'未知状态') as statusname,")
				   .append("x.doctype,nvl(x.doc_class, '未分类') as doc_class,x.docwtime,x.createuser,nvl(v.USER_REALNAME,'未知') as username,x.flow_id,nvl(z.name,'未知流程') as flowname,x.docabstract,nvl(y.order_no,-1) as order_no,y.end_time,nvl(x.ordertime,x.docwtime) as ordertime,x.seq,x.count ")
				   .append("from  TD_CMS_DOCUMENT x left outer join td_cms_doc_arrange y ")
				   .append("on x.document_id = y.document_id ")
				   .append(" left outer join tb_cms_flow z on x.flow_id = z.id ")
				   .append(" left outer join tb_cms_doc_status u on x.status = u.id ")
				   .append(" left outer join td_sm_user v on x.createuser = v.user_id ")
				   .append(" left outer join td_cms_channel w on x.channel_id = w.channel_id ")
				   .append(" where x.channel_id =")
				   .append(channelid)
				   .append(" and ")
				   .append("x.status not in(")
				   .append(DocumentStatus.RECYCLED.getStatus())
				   .append(",20,100) and isdeleted != 1 and ")
				   .append("(select count(*) from td_cms_site a, td_cms_channel b ")
				   .append("where b.site_id = a.site_id and (a.status = 0 or a.status = 1) and b.channel_id =")
				   .append(channelid)
				   .append(")>0 ");
				if(ConfigManager.getInstance().getConfigBooleanValue("cms.channel.doclist.onlycurrentuser",false))
				{
					if(accessControl.isGrantedRole("部门管理员"))
					{
						/**
						 * 目前只考虑显示用户自己采集的文档
						 * 以后还需考虑将用户可以操作的其他文档放进来，如：审核，发布，撤销发布，回收等等
						 * 还需考虑部门管理员，可以查看其所管理的所有用户采集的文档
						 */
						String currentUser = super.accessControl.getUserID();
						sql.append(" and x.createuser='").append(currentUser).append("'");
					}
				}
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
						sql.append(" and x.docwtime > "+ DBUtil.getDBDate(createTimeBegin));  
					}
					if(createTimeEnd!=null && createTimeEnd.length()>0){
						sql.append(" and x.docwtime < "+ DBUtil.getDBDate(createTimeEnd) + "+1 ");  
					}
					if(content!=null && content.length()>0){
						sql.append(" and x.content like '%"+ content +"%'");
					}
					if(docLevel != null && docLevel.length() != 0){
						sql.append(" and x.doc_level = "+ docLevel);
					} 
					if(docClass!= null && docClass.length() != 0){
						sql.append(" and x.doc_class = "+ "'"+ docClass +"'");
					} 
					if(orderByHit != null && orderByHit.equals("true")){
						orderStr = "x.count desc,";
					} 
					if(orderByHit != null && orderByHit.equals("false")){
						orderStr = "x.count,";
					} 
					if(orderByDocWtime != null && orderByDocWtime.equals("true")){
						orderStr = "x.docwtime desc,";
					}
					if(orderByDocWtime != null && orderByDocWtime.equals("false")){
						orderStr = "x.docwtime,";
					}
					
				}	
				if(!sql.equals("")){
					sql.append(" order by "+orderStr+"order_no desc,x.seq,x.ordertime desc,x.document_id desc");
					listInfo = docManager.getDocList(sql.toString(),(int)offset,maxPagesize);
				}
                log.warn(sql.toString());
       }catch(Exception e){
             e.printStackTrace();
       }
		return listInfo;	
 }	
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
           
            return null;
      }
}