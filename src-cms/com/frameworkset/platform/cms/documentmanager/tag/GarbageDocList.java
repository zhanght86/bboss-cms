package com.frameworkset.platform.cms.documentmanager.tag;

import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class GarbageDocList extends DataInfoImpl implements java.io.Serializable{
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
		 	//AccessControl accesscontroler = AccessControl.getInstance();
		    String preStatus = request.getParameter("preStatus");
		    String channelIds = request.getParameter("channelIds");
			if(channelIds!=null && channelIds.endsWith(",")){
				channelIds = channelIds.substring(0,channelIds.length()-1);
			}
			String flowId = request.getParameter("flowId");
			String title = request.getParameter("title");
			String flag = request.getParameter("flag");
			String garbager = request.getParameter("garbager");
			String userId = (String)request.getAttribute("userId");
			String currentSiteid = (String)request.getAttribute("currentSiteid");
			
			ListInfo listInfo = null;
			boolean adminFlag = false;
			try{
//				//当前用户是否是管理员
//				UserManager userManager = SecurityDatabase.getUserManager();
//				User user = userManager.getUser("userId",userId);
//				adminFlag = accesscontroler.isAdmin(user.getUserName());
				
				//取当前用户username
				String curUser = accessControl.getUserName();
				adminFlag = AccessControl.isAdmin(curUser);
				
				DocumentManager docManager = new DocumentManagerImpl();
				String	sql = "select a.document_id as document_id,a.subtitle as subtitle," +
						"b.name as channelName,a.doctype as doctype,a.createtime as createtime,a.publishtime as publishtime," +
						"a.guidangtime as pigeonholeTime,a.recycletime as recycletime,a.channel_id as channel_id,b.site_id as site_id, " +
						"e.user_name as createuserName,d.name as flowName,c.name as siteName,f.name as statusname," +
						"a.guidangman as pigeonholePerfomer,a.recycleman as recycleman,a.flow_id as flow_id " +
						"from td_cms_document a, td_cms_channel b,td_cms_site c,tb_cms_flow d,td_sm_user e,tb_cms_doc_status f  " +
						"where a.isdeleted = 1 and a.channel_id=b.channel_id and c.site_id=b.site_id " +
						"and d.id=a.flow_id and e.user_id=a.createuser and f.id=a.status and b.site_id=" + currentSiteid + " ";
				if(!adminFlag){
					sql += " and a.recycleman=" + userId;
				}
				if(flag!=null && flag.equals("query")){
					if(preStatus!=null && !preStatus.equals(""))
						sql += " and a.status=" + preStatus;
					if(channelIds!=null && !channelIds.equals(""))
						sql += " and a.channel_id in (" + channelIds + ")";
					if(title!=null && !title.equals(""))
						sql +=" and a.subtitle like '%" + title + "%'";
					if(flowId!=null && !flowId.equals(""))
						sql +=" and a.flow_id=" + flowId;
					if(garbager!=null && garbager.equals("1"))
						sql +=" and a.recycleman=" + userId;
				}
				sql += " order by a.recycletime desc,a.createuser desc,a.document_id desc";
				//由于回收文档和归档的文档所显示的字段相同，所以直接调用getPigeonholeDocList查询符合条件的文档
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
