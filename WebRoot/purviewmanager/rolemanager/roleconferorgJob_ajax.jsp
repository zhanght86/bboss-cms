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
				com.frameworkset.platform.sysmgrcore.manager.JobManager,
				com.frameworkset.platform.sysmgrcore.entity.Organization,
				com.frameworkset.common.poolman.DBUtil"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,
				 com.frameworkset.platform.sysmgrcore.entity.Job,
				 java.util.List,
				 java.util.ArrayList"%>

<%@ page import="com.frameworkset.platform.security.AccessControl"%>


<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);


			String roleId = (String) request.getParameter("roleId");				
			String orgId = request.getParameter("orgId");

		
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			JobManager jobManager = SecurityDatabase.getJobManager();
			
			//获取机构下的所有岗位
			Organization org = orgManager.getOrgById(orgId);
			List allJobsofOrg = jobManager.getJobList(org);
			
			
			String sql = "select ojr.JOB_ID,j.JOB_NAME " 
							+ "from TD_SM_ORGJOBROLE ojr , TD_SM_JOB j where ojr.JOB_ID = j.JOB_ID " 
							+ " and ojr.ORG_ID = '" + orgId 
							+ "' and ojr.ROLE_ID ='" + roleId+"'" ;
																	
			List existjobsofOrg = new ArrayList();
			
			try
			{							
				DBUtil db = new DBUtil();
				db.executeSelect(sql);
				if(db.size()>0)
				{
					for(int i=0; i< db.size(); i++)
					{
						String jobId =  db.getString(i,"JOB_ID");
						String jobName = db.getString(i,"JOB_NAME");
						
						Job job = new Job();
						job.setJobId(jobId);
						job.setJobName(jobName);
						if(accesscontroler.checkPermission(jobId,"jobset",AccessControl.JOB_RESOURCE)){				
							existjobsofOrg.add(job);
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			List jobSetAlljobsofOrg = null;
			if(allJobsofOrg != null && allJobsofOrg.size() > 0){
				jobSetAlljobsofOrg = new ArrayList();
				for(int i = 0; i < allJobsofOrg.size(); i++){
					Job jobAll = (Job)allJobsofOrg.get(i);
					if(accesscontroler.checkPermission(jobAll.getJobId(),"jobset",AccessControl.JOB_RESOURCE)){
						jobSetAlljobsofOrg.add(jobAll);
					}
				}
			}
							
			request.setAttribute("existjobsofOrg", existjobsofOrg);
			//request.setAttribute("allJobsofOrg", allJobsofOrg);
			request.setAttribute("allJobsofOrg", jobSetAlljobsofOrg);

			%>
<html>
	<head>
		<title>属性容器</title>
		<SCRIPT LANGUAGE="JavaScript"> 
			var api = parent.frameElement.api, W = api.opener;
			
			function addOrg()
			{	
			  var n=document.all("orgId").options.length-1;
			  var jobIds = "";

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
				   		if(jobIds==""){ 
				   			jobIds = op.value;
				   		}else {
				   			jobIds += "," + op.value;
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
			       // if(jobIds =="") jobIds = jobIds + op.value;
			        //else jobIds = jobIds + "," + op.value;
			    }
			  }
			  
		   	if(jobIds != "")
		    {
		    	send_request('saveRoleOrgJobs.jsp?orgId=<%=orgId%>&roleId='+<%=roleId%>+'&jobIds='+jobIds+'&tag=add');
		    }else if(exist != ""){
		   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.post.had"/>'+"：\n" + exist);
	  			return;
		   }else{
		   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.select"/>');
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
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.select.null"/>');
				return;
			}
		 	var jobIds = "";
			for (var m=document.all("orgId").options.length-1;m>=0;m--)
			{
				if(jobIds =="") jobIds = jobIds + document.all("orgId").options[m].value;
		        else jobIds = jobIds + "," + document.all("orgId").options[m].value;
				
		   		document.all("orgId").options[m]=null
			}    
			 if(jobIds != "")
		    {
		    	send_request('saveRoleOrgJobs.jsp?orgId=<%=orgId%>&roleId='+<%=roleId%>+'&jobIds='+jobIds+'&tag=delete');
		    }
		  
		}
	      
		function addall()
		{
			var n=document.all("orgId").options.length-1;
			var p=document.all("allist").options.length-1;	
			
			var jobIds = "";
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
			   		if(jobIds==""){ 
			   			jobIds = op.value;
			   		}else {
			   			jobIds += "," + op.value;
			   		}
			   		addone(op.text,op.value,n);		
		   		}else{
		   			if(exist == ""){
		   				exist = op.text;
		   			}else{
		   				exist += "\n" + op.text;
		   			}
		   		}   	
		        //addone(op.text,op.value,n);  
		       // if(jobIds =="") jobIds = jobIds + op.value;
			    //else jobIds = jobIds + "," + op.value;
		    }
		    if(jobIds != "")
		    {
		    	send_request('saveRoleOrgJobs.jsp?orgId=<%=orgId%>&roleId='+<%=roleId%>+'&jobIds='+jobIds+'&tag=add');
		    }	 else if(exist != ""){
		   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.post.had"/>'+"：\n" + exist);
	  			return;
		   } 
		    
		   
		}
			
		function deleteorg()
		{ 
			if(document.all("orgId").options.length < 1){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.select.null"/>');
				return;
			}
			if(document.all("orgId").selectedIndex==-1){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.remove.select"/>');
				return;
			}
		 	var jobIds = "";		
		    for (var m=document.all("orgId").options.length-1;m>=0;m--)
		    {
			    if(document.all("orgId").options[m].selected)
			    {
		      	    var op = document.all("orgId").options[m]
		      	    if(jobIds =="") jobIds = jobIds + op.value;
			        else jobIds = jobIds + "," + op.value;
			        document.all("orgId").options[m]=null;
		        }
		    }
			if(jobIds != "")
		    {
		    	send_request('saveRoleOrgJobs.jsp?orgId=<%=orgId%>&roleId='+<%=roleId%>+'&jobIds='+jobIds+'&tag=delete');
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
	</SCRIPT>
	</head>

	<body class="contentbodymargin">
	
		<div id="contentborder">
			<center>
				<form name="RoleOrgForm" action="" method="post">
					<table width="100%" border="0" cellpadding="0" cellspacing="1">
						<tr class="tabletop">
							<td width="45%" align="center">
								&nbsp;
							</td>
							<td width="10%" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr class="tabletop">
							<td width="45%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.can"/>
							</td>
							<td width="10%" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.had"/>
							</td>
						</tr>
						<tr class="tabletop">
							<td width="45%" align="center">
								&nbsp;
							</td>
							<td width="10%" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td align="right">
								<select name="allist" multiple style="width:100%" onDBLclick="addOrg()" size="18">
									<pg:list requestKey="allJobsofOrg">
										<option value="<pg:cell colName="jobId"/>">
											<pg:cell colName="jobName" />
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
									<pg:list requestKey="existjobsofOrg">
										<option value="<pg:cell colName="jobId"/>">
											<pg:cell colName="jobName"/>
										</option>
									</pg:list>
								</select>

							</td>

						</tr>
						<tr class="tabletop">
							<td width="40%" align="center">
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

