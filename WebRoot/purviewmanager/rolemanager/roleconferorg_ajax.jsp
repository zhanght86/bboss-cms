<%
/*
 * <p>Title: 角色隶属机构的操作页面</p>
 * <p>Description: 角色隶属机构的操作页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-25
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				com.frameworkset.platform.sysmgrcore.entity.Organization"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,
				java.util.List,
				java.util.ArrayList"%>

<%@ page import="com.frameworkset.platform.security.AccessControl"%>


<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			String roleId = (String) request.getParameter("roleId");
						
			String orgId = request.getParameter("orgId");

			if (orgId == null)
			{
				orgId = "0";
			}	
			OrgManager orgManager = SecurityDatabase.getOrgManager();

			List existOrg = null;//orgManager.getOrgListOfRole(roleId);// 该角色关联机构
			List allOrg = null;
			//List allGrantOrg = null;
			//List allOrgAfterCheck = new ArrayList();
			//List existOrgAfterCheck = new ArrayList();
			//得到所选机构与该机构直接子集机构
			StringBuffer allOrgSql = new StringBuffer()
				.append("select * from (select o.*,1 as com_level from td_sm_Organization o where o.org_Id='")
				.append(orgId).append("' ")
				.append(" union select o.*,2 as com_level from td_sm_Organization o where ")
				.append(" o.parent_Id='").append(orgId)
				.append("') dd order by com_level asc,org_sn asc");
			//得到所选机构与该机构直接子集机构已设置的机构
			StringBuffer existOrgSql = new StringBuffer()
				.append("select * from td_sm_organization where org_id in(")
				.append("select org_id from td_sm_orgrole where org_id in(")
				.append("select dd.org_id from (select o.*,1 as com_level from td_sm_Organization o where o.org_Id='")
				.append(orgId).append("' ")
				.append(" union select o.*,2 as com_level from td_sm_Organization o where ")
				.append(" o.parent_Id='").append(orgId)
				.append("') dd ) and role_id='")
				.append(roleId).append("')");
			
			
			existOrg = orgManager.getOrgListBySql(existOrgSql.toString());
			
			if("0".equals(orgId) && accesscontroler.isAdmin()){//orgId为零时，显示所有一级机构，只有拥有超级管理员角色的用户才能查看
				allOrg = orgManager.getOrgListBySql(allOrgSql.toString());				
			}else if(!"0".equals(orgId)){
				allOrg = orgManager.getOrgListBySql(allOrgSql.toString());
			}

			//增加权限判断，如果用户无权对机构设置角色，就不显示此机构
			/*
			if (allOrg != null) {
				for (int k = 0; k < allOrg.size(); k++) {
					Organization organization = (Organization) allOrg.get(k);
					if (organization != null) {
						if (accesscontroler.checkPermission(organization
								.getOrgId(), "orgroleset",
								AccessControl.ORGUNIT_RESOURCE)) {
							allOrgAfterCheck.add(organization);
						}
					}
				}
			}
			if (existOrg != null) {
				for (int k = 0; k < existOrg.size(); k++) {
					Organization organization = (Organization) existOrg.get(k);
					if (organization != null) {
						if (accesscontroler.checkPermission(organization
								.getOrgId(), "orgroleset",
								AccessControl.ORGUNIT_RESOURCE)) {
							existOrgAfterCheck.add(organization);
						}
					}
				}
			}
			*/
			request.setAttribute("existOrg", existOrg);
			request.setAttribute("allOrg", allOrg);
		

			%>
<html>
	<head>
		<title>属性容器</title>
		<SCRIPT LANGUAGE="JavaScript"> 
		var api = parent.frameElement.api, W = api.opener;
		
		function loadAllGrantOrg(){
			 
		}
			function addOrg()
			{	
			  var n=document.all("orgId").options.length-1;
			  var orgIds = "";

              var existorg = document.all("orgId");
              var exist = "";
			  
			  for(var i=0;i<document.all("allist").options.length;i++)
			  {
			    var op=document.all("allist").options[i];
			    if(op.selected)
			    {
			    	var flag2 = false;
			   		for(var j = 0; j < existorg.length; j++){
			   			if(op.text==existorg.options[j].text){
			   				flag2 = true;
			   			}
			   		}
			   		if(!flag2){
				   		if(orgIds==""){ 
				   			orgIds = op.value;
				   		}else {
				   			orgIds += "," + op.value;
				   		}
				   		addone(op.text,op.value,n);		
			   		}else{
			   			if(exist == ""){
			   				exist = op.text;
			   			}else{
			   				exist += "\n" + op.text;
			   			}
			   		}   	
			       // addone(op.text,op.value,n);
			       // if(orgIds =="") orgIds = orgIds + op.value;
			       // else orgIds = orgIds + "," + op.value;
			    }
			  }
			  document.all.orgids.value = orgIds; 
		   	if(orgIds != "")
		    {
		    	//send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=add');
		    	send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&tag=add');
		    }
		   	else if(exist != ""){
		   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.org.had"/>' + "：\n" + exist);
	  			return;
		   }else{
			   W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.org.select"/>');
		   		return;
		   }	
			
		}
			
		function addone(name,value,n)
		{
		
		   for(var i=n;i>=0;i--)
		   {
				if(value==document.all("orgId").options[i].value)
				{
				  return;
				}
			}
		   var op=new Option(name,value);
		   document.all("orgId").add(op);
		}
			
		function deleteall()
		{
			if(document.all("orgId").options.length < 1){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.org.select.null"/>');
			}
		 	var orgIds = "";
			for (var m=document.all("orgId").options.length-1;m>=0;m--)
			{
				if(orgIds =="") orgIds = orgIds + document.all("orgId").options[m].value;
		        else orgIds = orgIds + "," + document.all("orgId").options[m].value;
				
		   		document.all("orgId").options[m]=null
			}    
			document.all.orgids.value = orgIds; 
			 if(orgIds != "")
		    {
		    	send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&tag=delete');
		    	//send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=delete');
		    }
		  
		}
	      
		function addall()
		{
			var n=document.all("orgId").options.length-1;
			var p=document.all("allist").options.length-1;	
			
			var orgIds = "";

			 var existorg = document.all("orgId");
             var exist = "";
					  
		    for(var i=0;i<document.all("allist").options.length;i++){
		        var op=document.all("allist").options[i];
		        var flag2 = false;
		   		for(var j = 0; j < existorg.length; j++){
		   			if(op.text==existorg.options[j].text){
		   				flag2 = true;
		   			}
		   		}
		   		if(!flag2){
			   		if(orgIds==""){ 
			   			orgIds = op.value;
			   		}else {
			   			orgIds += "," + op.value;
			   		}
			   		addone(op.text,op.value,n);		
		   		}else{
		   			if(exist == ""){
		   				exist = op.text;
		   			}else{
		   				exist += "\n" + op.text;
		   			}
		   		}   	
		       // addone(op.text,op.value,n);  
		      //  if(orgIds =="") orgIds = orgIds + op.value;
			    //else orgIds = orgIds + "," + op.value;
		    }
		    document.all.orgids.value = orgIds; 
		    if(orgIds != "")
		    {
		    	send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&tag=add');
		    	//send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=add');
		    }	else if(exist != ""){
		    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.org.had"/>' + "：\n" + exist);
	  			return;
		   }  
		    
		   
		}
			
		function deleteorg()
		{ 
			if(document.all("orgId").options.length < 1){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.org.select.null"/>');
				return;
			}
			if(document.all("orgId").selectedIndex==-1){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.org.remove.select"/>');
				return;
			}
		 	var orgIds = "";		
		    for (var m=document.all("orgId").options.length-1;m>=0;m--)
		    {
			    if(document.all("orgId").options[m].selected)
			    {
		      	    var op = document.all("orgId").options[m]
		      	    if(orgIds =="") orgIds = orgIds + op.value;
			        else orgIds = orgIds + "," + op.value;
			        document.all("orgId").options[m]=null;
		        }
		    }
		    document.all.orgids.value = orgIds; 
			if(orgIds != "")
		    {
		    	//send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=delete');
		    	send_request('saveRoleOrg.jsp?roleId='+<%=roleId%>+'&tag=delete');
		    }	
		}
			
	
		function send_request(url)
		{						
			//document.all.divProcessing.style.display = "block";
			
			//document.all("button1").disabled = true;
			//document.all("button2").disabled = true;
			//document.all("button3").disabled = true;
			//document.all("button4").disabled = true;
			//document.all("back").disabled = true;
			
			
			document.RoleOrgForm.action = url;
			document.RoleOrgForm.target = "hiddenFrame";
			document.RoleOrgForm.submit();
		}
		
		function refreshAllGrantedOrgs(){
	
		  		$('#allGrantOrgs').load("loadAllRoleGrantedOrgs.jsp?roleId=<%=roleId %>");
		  		
		}
	</SCRIPT>
	</head>

	<body class="contentbodymargin">
		<div id="contentborder">
			<center>
				<form name="RoleOrgForm" action="" method="post">
				<input type="hidden" name="orgids" value=""/>
					<table width="100%" border="0" cellpadding="0" cellspacing="1">
						<tr class="tabletop">
							<td width="30%" align="center">
								&nbsp;
							</td>
							<td width="5%" align="center">
								&nbsp;
							</td>
							<td width="30%" align="center">
								&nbsp;
							</td>
							<td width="5%" align="center">
								&nbsp;
							</td>
							<td width="30%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr class="tabletop">
							<td width="30%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.org.select.can"/>
							</td>
							<td width="5%" align="center">
								&nbsp;
							</td>
							<td width="30%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.org.authorize.had"/>
							</td>
							<td width="5%" align="center">
								&nbsp;
							</td>
							<td width="30%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.authorize.role.to.org.list"/>
							</td>
						</tr>
						<tr class="tabletop">
							<td width="30%" align="center">
								&nbsp;
							</td>
							<td width="5%" align="center">
								&nbsp;
							</td>
							<td width="30%" align="center">
								&nbsp;
							</td>
							<td width="5%" align="center">
								&nbsp;
							</td>
							<td width="30%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td align="right">
								<select name="allist" multiple style="width:100%" onDBLclick="addOrg()" size="18">
									<pg:list requestKey="allOrg">
										<option value="<pg:cell colName="orgId"/>">
											<pg:null colName="remark5">
												<pg:cell colName="orgName" />
											</pg:null>
											<pg:notnull colName="orgName">
												<pg:equal colName="remark5" value="">
													<pg:cell colName="orgName" />
												</pg:equal>
												<pg:notequal colName="remark5" value="">
													<pg:cell colName="remark5" />
												</pg:notequal>
											</pg:notnull>
										</option>
									</pg:list>
								</select>
							</td>

							<td align="center">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="center">
											<a href="javascript:void(0)" class="bt_2" name="button1" onclick="addOrg()"><span>&gt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<a href="javascript:void(0)" class="bt_2" name="button2" onclick="addall()"><span>&gt;&gt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<a href="javascript:void(0)" class="bt_2" name="button3" onclick="deleteall()"><span>&lt;&lt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<a href="javascript:void(0)" class="bt_2" name="button4" onclick="deleteorg()"><span>&lt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td>
								<select name="orgId" multiple style="width:100%" onDBLclick="deleteorg()" size="18">
									<pg:list requestKey="existOrg">

										<option value="<pg:cell colName="orgId"/>">
											<pg:null colName="remark5">
												<pg:cell colName="orgName" />
											</pg:null>
											<pg:notnull colName="remark5">
												<pg:equal colName="remark5" value="">
													<pg:cell colName="remark5" />
												</pg:equal>
												<pg:notequal colName="remark5" value="">
													<pg:cell colName="remark5" />
												</pg:notequal>
											</pg:notnull>
										</option>
									</pg:list>
								</select>

							</td>
							<td>
							</td>
							<td id="allGrantOrgs">
								<script type="text/javascript">
								 $(document).ready(refreshAllGrantedOrgs());
								</script>
							</td>

						<tr>
							
						</tr>
						
						<tr class="tabletop">
							<td width="30%" align="center">
								&nbsp;
							</td>

						</tr>
					</table>

					<input type="hidden" name="roleId" value="<%=roleId%>" />

				</form>
			</center>
		</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>

