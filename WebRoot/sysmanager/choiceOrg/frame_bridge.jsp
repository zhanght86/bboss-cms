<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.entity.Organization,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,com.frameworkset.platform.sysmgrcore.manager.OrgManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.common.poolman.DBUtil,
	org.apache.commons.lang.StringUtils,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.entity.Group
	,com.frameworkset.platform.sysmgrcore.manager.GroupManager,java.util.*,com.frameworkset.common.poolman.DBUtil"%>

<%
		
			String orgId = request.getParameter("orgId");
			String orgName = request.getParameter("orgName");
			

			String nodeType = "org";
			String recursive = request.getParameter("recursive");
			
			if(recursive == null)
			{%>
				<script language="javascript">
					var temp_ = parent.frames["userList"];
					if(temp_)
					{
						
						temp_.document.all.item("recursive").checked = false;
					}
				</script>
			<%
				recursive = "false";
			
			}
			
			 String ret = "";
		     List allUser = new ArrayList();
			 DBUtil db = new DBUtil();
			if(nodeType== null || nodeType.equals("null"))
				return;
			if(nodeType.equals("org"))
			{
					
				
				String sql ="select org_id,org_name from TD_SM_ORGANIZATION where org_id='" + orgId + "'";
				if(recursive.equals("true")){
					sql="select org_id,org_name from TD_SM_ORGANIZATION where org_id in(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH t.org_id='"+ orgId +"' "+
					         " CONNECT BY PRIOR t.org_id=t.PARENT_ID)";
					
				}
				
				 db.executeSelect(sql);
				 for(int i=0;i<db.size();i++){
							 Organization org = new Organization();
							 org.setOrgId(db.getString(i,"org_id"));
							 org.setOrgName(db.getString(i,"org_name"));
							 allUser.add(org);
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
					Organization temp_org = (Organization) allUser.get(i);
					if (flag) {
						ret += "," + temp_org.getOrgId() + "="
								+ temp_org.getOrgName();
					} else {
						ret += temp_org.getOrgId() + "="
								+ temp_org.getOrgName();
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

				frame.addone("allist",userinfo[1],userinfo[0]);				
			}
			
			
		}		
		
		
	</script>
	<head>
			String orgId = request.getParameter("orgId");
			//String groupId = request.getParameter("groupId");
			String nodeType = "org";	
			
			<%
			if(orgId != null && !orgId.equals(""))
			{%>
				<input type="hidden" name="orgId" value="<%=orgId%>">
			<%}
			
			if(nodeType != null && !nodeType.equals(""))
			{%>
				<input type="hidden" name="nodeType" value="<%=nodeType%>">
			<%}
			%>
			
	
<html>
