
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.List,java.util.ArrayList,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%
	   String roleId = request.getParameter("roleId");
		String orgid = request.getParameter("orgId");
		String rootpath = request.getContextPath();
		if (roleId == null) {
			out.println("没有选择角色！");
			return ;
		}
		
		UserManager userManager = SecurityDatabase.getUserManager();
		List existUser = userManager.getUsersListOfRole(roleId);
		if (existUser == null) {
			existUser = new ArrayList();
		}
		request.setAttribute("existUser", existUser);
		

		if (roleId != null && !roleId.equals("") && orgid != null && !orgid.equals("")) {
			
			//获取机构下的用户列表，并且安照机构/岗位序号和机构/岗位/人员序号排序
			 List allUser = userManager.getOrgUserList(orgid);
			 


			
			if (allUser == null) {
				allUser = new ArrayList();
			}
			
			
			request.setAttribute("allUser", allUser);
			
		}
%>
<html>
	<head>
		<title>属性容器</title>

		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
   var n=document.all("userIds").options.length-1;
    var selectusers = "";
   for(var i=0;i<document.all("allist").options.length;i++){
	   var op=document.all("allist").options[i];
	   if(op.selected)
	   {
	    	var retvalue =addone(op.text,op.value,n);
	    	if(retvalue)
	    	{
	    		if(selectusers == "" )
	    		{
		    		selectusers  += retvalue;
		    	}
		    	else
		    	{
		    		selectusers  +="," + retvalue;
		    	}
		    		
	    	}
	    	
	   }
   
  }
  if(selectusers != "")
  {
  	send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=add');
  }
  
}
function addone(name,value,n){
   for(var i=n;i>=0;i--){
		if(value==document.all("userIds").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("userIds").add(op);
   return value;
   
}
function deleteall(){
	var selectusers = "";
	for (var m=document.all("userIds").options.length-1;m>=0;m--)
	{
		var retvalue = document.all("userIds").options[m].value
		if(selectusers == "" )
		{
    		selectusers  += retvalue;
    	}
    	else
    	{
    		selectusers  += "," + retvalue;
    	}
		document.all("userIds").options[m]=null;
	}
	if(selectusers != "")
	{
		send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=delete');
	}
	else
	{
		alert("没有要删除的用户。");
	}
    //changebox();
}
function addall(){
	var n=document.all("userIds").options.length-1;

    var selectusers = "";
     for(var i=0;i<document.all("allist").options.length;i++){     
	     var op=document.all("allist").options[i];
	     var retvalue = addone(op.text,op.value,n); 
	     if(retvalue)
    	{
    		if(selectusers == "" )
    		{
	    		selectusers  += retvalue;
	    	}
	    	else
	    	{
	    		selectusers  +="," + retvalue;
	    	}
	    		
    	}
    	 
	}
	
	if(selectusers != "")
	{
		send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=add');
	}
	else
	{
		alert("没有要添加的用户");
	}
   
}
function deleterole()
{
	var selectusers = "";
	 for (var m=document.all("userIds").options.length-1;m>=0;m--)
	 {
	      if(document.all("userIds").options[m].selected)
	      {
		    var retvalue = document.all("userIds").options[m].value
		   	if(selectusers == "" )
			{
	    		selectusers  += retvalue;
	    	}
	    	else
	    	{
	    		selectusers  += "," + retvalue;
	    	}
	    	document.all("userIds").options[m]=null;
	     }
		      
		 
	  }
	  if(selectusers != "")
	  {
	  	send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=delete');
	  }
	  else
	  {
	  	alert("没有选择要删除的用户");
	  }
      
}


			
		
			
	function send_request(url){
		document.all.divProcessing.style.display = "block";
		document.OrgJobForm.action = url;
		document.OrgJobForm.target = "hiddenFrame";
		document.OrgJobForm.submit();
	}
	
	function okadd(){
	
		parent.window.close();
		parent.window.returnValue="ok";
	
	}
	
			
</SCRIPT>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
					<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" width="40%" align="center">
								可选用户
							</td>
							<td width="20%" class="td" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center" class="td">
								已授予用户
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="center" >
								<select class="select" name="allist" multiple style="width:90%" onDBLclick="addRole()" size="18">
									<pg:list requestKey="allUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select>
							</td>

							<td align="center" class="td">
								<table class="table" width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr class="tr">
										<td align="center" class="td">
											<input name="button1" type="button" class="input" value="&gt;" onclick="addRole()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button4" type="button" class="input" value="&lt;" onclick="deleterole()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td class="td" align="center" >
								<select class="select" name="userIds" multiple style="width:90%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select>

							</td>

						</tr>
						<tr class="tabletop">
						    <td  align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td colspan="3" class="td" align="center">
								

							</td>
						</tr>
						<tr>
							<td colspan="3" align="center" >
								<input name="but" type="button" value="关闭" class="input" onClick="okadd();" />
							</td>
						</tr>
					</table>
				</form>
			</center>
		</div>
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150px;top:450px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			    <tr>
				    <td bgcolor=#3A6EA5>
					    <marquee align="middle" behavior="alternate" scrollamount="5">
						    <font color=#FFFFFF>...处理中...请等待...</font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
