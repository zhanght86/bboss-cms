package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class PigeonholeDocList extends DataInfoImpl implements java.io.Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	     int maxPagesize) {
		AccessControl accesscontroler = AccessControl.getInstance();
		String operate = request.getParameter("operate");
		String channelIds = request.getParameter("channelIds");
		if(channelIds!=null && channelIds.endsWith(",")){
			channelIds = channelIds.substring(0,channelIds.length()-1);
		}
		String flowId = request.getParameter("flowId");
		String title = request.getParameter("title");
		String flag = request.getParameter("flag");
		String pigeonholer = request.getParameter("pigeonholer");
		String userId = (String)request.getAttribute("userId");
		String currentSiteid = (String)request.getAttribute("currentSiteid");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		ListInfo listInfo = null;
		boolean adminFlag = false;
		try {
			//当前用户是否是管理员
			UserManager userManager = SecurityDatabase.getUserManager();
			User user = userManager.getUserById(userId);
			adminFlag = accesscontroler.isAdmin(user.getUserName());
		
			DocumentManager docManager = new DocumentManagerImpl();
			String status = "";
			String order = "";
			String sql1 = "";
			if(operate.equals("pigeonholeQuery")){
				status = "7";
				order = " order by a.guidangtime desc,a.createuser desc,a.document_id desc";
				//若当前用户为非归档管理员，则只能看他本人归档的文档
				//if(!adminFlag && !docManager.isPigeonholeManager(Integer.parseInt(userId))){
				sql1 = " and a.guidangman=" + userId;
				//}	
			}else if(operate.equals("toPigeonhole")){ 
				//只有已发文档才能归档,而且站点状态必须为开通状态
				//status = "5 or (a.status=3 and a.doctype!=0)";
				status = "5 ";
				order = " and (c.status=0 or c.status=1) order by a.publishtime desc,a.createuser desc,a.document_id desc";
				//权限控制段,当前用户具有归档权限的文档才显示
				sql1 = " and (" +
							 "(select count(*) from td_sm_roleresop where op_id='archive' and role_id ='" + userId + "' and " +
							 "types='user' and res_id = a.channel_id and restype_id ='channel')>0 " +
							 "or " +
							 "(select count(*) from td_sm_roleresop where role_id in" +
							 "(select role_id from td_sm_userrole where user_id =" + userId + ") and op_id='archive' and types='role' " +
							 "and res_id =a.channel_id and restype_id ='channel')>0" +
							 ")";
			}	
			String	sql = "select a.document_id as document_id,a.subtitle as subtitle," +
					"b.name as channelName,a.doctype as doctype,a.guidangtime as pigeonholeTime," +
					"a.createtime as createtime,a.recycletime as recycletime,a.publishtime as publishtime," +
					"a.channel_id as channel_id,b.site_id as site_id," +
					"e.user_name as createuserName,d.name as flowName,c.name as siteName,f.name as statusname," +
					"a.guidangman as pigeonholePerfomer,a.recycleman as recycleman,a.flow_id as flow_id " +
					"from td_cms_document a, td_cms_channel b,td_cms_site c,tb_cms_flow d,td_sm_user e,tb_cms_doc_status f " +
					"where a.isdeleted!=1 and (a.status=" + status + ") and a.channel_id=b.channel_id and c.site_id=b.site_id " +
					"and d.id=a.flow_id and e.user_id=a.createuser and f.id=a.status and b.site_id=" + currentSiteid + " ";
//			if(operate.equals("pigeonholeQuery")){
//				if(!adminFlag && !docManager.isPigeonholeManager(Integer.parseInt(userId)))
//					sql = sql + sql1;
//			}else if(operate.equals("toPigeonhole")){
//				if(!adminFlag)
//					sql = sql + sql1;
//			}
			if(!adminFlag && !docManager.isPigeonholeManager(Integer.parseInt(userId)))
				sql = sql + sql1;

			if(flag!=null && flag.equals("query")){
				if(channelIds!=null && !channelIds.equals(""))
					sql += " and a.channel_id in (" + channelIds + ")";
				if(title!=null && !title.equals(""))
					sql +=" and a.subtitle like '%" + title + "%'";
				if(flowId!=null && !flowId.equals(""))
					sql +=" and a.flow_id=" + flowId;
				if(pigeonholer!=null && pigeonholer.equals("1"))
					sql +=" and a.guidangman=" + userId;
				if(operate.equals("pigeonholeQuery")){
					if(startTime!=null && !startTime.equals(""))
						sql +=" and a.guidangtime>" + DBUtil.getDBDate(startTime)+"-1";
					if(endTime!=null && !endTime.equals(""))
						sql +=" and a.guidangtime<" + DBUtil.getDBDate(endTime)+"+1";
				}else if(operate.equals("toPigeonhole")){ 
					if(startTime!=null && !startTime.equals(""))
						sql +=" and a.publishtime>" + DBUtil.getDBDate(startTime)+"-1";
					if(endTime!=null && !endTime.equals(""))
						sql +=" and a.publishtime<" + DBUtil.getDBDate(endTime)+"+1";
				}
			}
			sql += order;
				listInfo = docManager.getPigeonholeDocList(sql,(int)offset,maxPagesize);
		}catch(Exception e){
             e.printStackTrace();
       }
		return listInfo;
	}
	 protected ListInfo getDataList(String arg0, boolean arg1) {
	     return null;
	}
}
