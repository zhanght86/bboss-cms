<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.entity.User,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,com.frameworkset.platform.sysmgrcore.manager.OrgManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.common.poolman.DBUtil,
	org.apache.commons.lang.StringUtils,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.entity.Group
	,com.frameworkset.platform.sysmgrcore.manager.GroupManager,java.util.*,com.frameworkset.common.poolman.DBUtil"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.OrgSubUserList"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList"%>
<%
		
			String orgId = request.getParameter("orgId");
			String groupId = request.getParameter("groupId");
			String nodeType = request.getParameter("nodeType");	
			String recursive = request.getParameter("recursive");
			
			//add by ge.tao 2007-8-16
			OrgSubUserList orgsublist = new OrgSubUserList();
			UserSearchList uselist = new UserSearchList();
			String orderMethod = ConfigManager.getInstance().getConfigValue("cms.userOrder.orderMethod");
			
			if(recursive == null)
			{%>
			<%
				recursive = "false";
			
			}
			
			 			
			
			DBUtil dbUtil=new DBUtil();		
			String ret = "";
			//List allUser = null;
			List allUser = new ArrayList();
			if(nodeType== null || nodeType.equals("null"))
				return;
			if(nodeType.equals("org"))
			{
				UserManager userManager = SecurityDatabase.getUserManager();			
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				Organization org = orgManager.getOrgById(orgId);
				
				if (org != null) {
					//allUser = userManager.getUserList(org);
					
					 DBUtil db = new DBUtil();
					 String sql = "";
					 //add by ge.tao 2007-08-16	
					 //modify by ge.tao 2007-09-10				 
					 if(recursive.equals("true")){
						 sql = orgsublist.sortSQL(orgId,"true"); 
					 }else{
					     sql = orgsublist.sortSQL(orgId,"");	
					 }
					 			        
					 db.executeSelect(sql);
					 for(int i=0;i<db.size();i++){
						 User user=new User();
						 int userid=db.getInt(i,"user_id");
						 user.setUserId(new Integer(userid));
						 user.setUserName(db.getString(i,"USER_NAME"));
						 user.setUserRealname(db.getString(i,"USER_REALNAME"));
						 allUser.add(user);
					 }					
				}
			}
			else if(nodeType.equals("sysgroup"))
			{
				UserManager userManager = SecurityDatabase.getUserManager();
			
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				Group group = groupManager.getGroupByID(groupId);
			
				if (group != null) {
					allUser = userManager.getUserList(group);					
				}
			}
			else if(nodeType.equals("persongroup"))
			{
				String pgroupId = request.getParameter("pgroupId");
				String sqlstr="select userid,username from td_childgroup where id='"+pgroupId+"'";
				dbUtil.executeSelect(sqlstr);
				String userid="";
				String username="";
			  	 if(dbUtil!= null && dbUtil.size()>0)
			     {
				     userid = dbUtil.getString(0,"userid");
				     username = dbUtil.getString(0,"username"); 
				    
			     }
			    String [] user_id = userid.split(",");
			    String [] user_name = username.split(",");
			    allUser = new ArrayList();
			    for(int i = 0;i<user_id.length;i++)
			    {
			      if("".equals(user_id[i]))
			        break;
			      User user=new User();
			      user.setUserId(Integer.valueOf(user_id[i]));
			      user.setUserRealname(user_name[i]);
			      allUser.add(user);
			    }			    
			}
			
			request.setAttribute("allUser",allUser);
			if (allUser == null) {
				allUser = new ArrayList();
				out.print("");
			} 
			else 
			{
				
				boolean flag = false;
				for (int i = 0; i < allUser.size(); i++) {
					User temp_user = (User) allUser.get(i);
					if (flag) {
						ret += "," + temp_user.getUserId() + "="
								+ temp_user.getUserRealname();
					} else {
						ret += temp_user.getUserId() + "="
								+ temp_user.getUserRealname();
						flag = true;
					}
				}
			}
	
					
%>

<html>
	<head>
		<script language="javascript">
		
		var userinfos  = "<%=ret%>";
		//alert("userinfos :"+userinfos );
		var frame = parent.frames["userList"];	
		frame.deleteall("allist");
        if(userinfos != "")
        {			
			var userinfoArray = userinfos.split(",");	
			for(var i = 0; i < userinfoArray.length; i ++ )
			{
				//			alert(userinfoArray[i]);
				var userinfo = userinfoArray[i].split("=");
				frame.addone("allist",userinfo[0]+" "+userinfo[1],userinfo[0]);				
			}
			
			
		}		
		
		
	</script>
	<head>
			String orgId = request.getParameter("orgId");
			String groupId = request.getParameter("groupId");
			String nodeType = request.getParameter("nodeType");	
			
			<%
			if(orgId != null && !orgId.equals(""))
			{%>
				<input type="hidden" name="orgId" value="<%=orgId%>">
			<%}
			
			if(groupId != null && !groupId.equals(""))
			{%>
				<input type="hidden" name="groupId" value="<%=groupId%>">
			<%}
			if(nodeType != null && !nodeType.equals(""))
			{%>
				<input type="hidden" name="nodeType" value="<%=nodeType%>">
			<%}
			%>
	
<html>
