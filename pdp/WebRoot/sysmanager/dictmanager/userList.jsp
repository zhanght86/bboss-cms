<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>

<%			
			String displayNameInput = request.getParameter("displayNameInput");
			String displayValueInput = request.getParameter("displayValueInput");
			//被选中的用户
			String userNames = request.getParameter("userNames")==null?"":request.getParameter("userNames");
			String[] infos =  userNames.trim().split(",");
			
%>
<html>
	<head>
		<title>属性容器</title>
		
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
 	var selected = false;  
   for(var i=0;i < document.all("allist").options.length;i++)
   {
	   var op=document.all("allist").options[i];
	   
	   if(op.selected)
	   {
	   		addone("userIds",op.text,op.value);
	   		if(!selected)
	   			selected = true;
	    }   		    
   }
   if(!selected)
   		alert("请选择执行人");
}


function addone(inputname,name,value){
	
   for(var i= document.all(inputname).options.length - 1;i>=0;i--){
		if(value == document.all(inputname).options[i].value){
		  return;
		}
	}
	
   var op=new Option(name,value);
   
   op.setAttribute("optionname",name);
   document.all(inputname).add(op);
   
}


function deleteall(inputselect){
	for (var m=document.all(inputselect).options.length-1;m>=0;m--)
    document.all(inputselect).options[m]=null
}
      
function addall(){

	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone("userIds",op.text,op.value);  
   }
}
function deleterole(){
 var selected = false;
 for (var m=document.all("userIds").options.length-1;m>=0;m--)
 {
      if(document.all("userIds").options[m].selected)
      {
     		document.all("userIds").options[m]=null;
     		if(!selected)
     			selected = true;
      }
 }
 if(!selected)
   		alert("请选择要删除的执行人");
}
function okadd(){	
	var len1=document.all("allist").options.length;	
	var len=document.all("userIds").options.length;	
	var ids = "";
	var names = "";
	var flag = false;
	for (var i=0;i<len;i++){	      
         document.all("userIds").options[i].selected = true;
     	 if(flag)
     	 {
	         ids += "," + document.all("userIds").options[i].value;
	         names += "," + document.all("userIds").options[i].optionname;
		 }
		 else
		 {
			 ids += document.all("userIds").options[i].value;
	         names += document.all("userIds").options[i].optionname;
	         flag = true;
		 }
        
     }
         
		/**if(flag)	
		{
	         window.returnValue=names + "^" + ids;
	     }
	     else
	     {
		     window.returnValue="";
	     }
	     */
	    if(flag)	
		{
	         parent.setUser(names + "^" + ids);  
	     }
	     else
	     {
		     parent.setUser("");  
	     } 
	    
      parent.api.close();

         
}
function init()
{
	
         
	var displayNameInput = parent.window.opener.document.all.item("<%=displayNameInput%>").value ;

	
	var displayValueInput = parent.window.opener.document.all.item("<%=displayValueInput%>").value ;

	if(!displayNameInput || !displayValueInput)
	{
		return ;
	}

	var userNameArray = displayNameInput.split(",");		
	
	var userIdArray = displayValueInput.split(",");		
	
	deleteall("userIds");
	for(var i = 0; i < userNameArray.length; i ++ )
	{
		
		addone("userIds",userNameArray[i],userIdArray[i]);				
	}
	
	
}

function setRecursive()
{
	var ischeck = document.all.item("recursive").checked;
	if(ischeck)
		recursive();
	else
		unRecursive();
		
}

var link_ = "<%=request.getContextPath()%>/sysmanager/schedularmanage/frame_bridge.jsp";
function unRecursive()
{
	var frame_bridge = getFrame_bridge();
	if(!frame_bridge) 
		return;
	
	var link_t = link_ + "?recursive=false";
	if(frame_bridge.orgId)
	{
		link_t += "&orgId=" + frame_bridge.orgId.value;
	}
	
	if(frame_bridge.groupId)
	{
		link_t += "&groupId=" + frame_bridge.groupId.value;
	}
	if(frame_bridge.nodeType)
	{
		link_t += "&nodeType=" + frame_bridge.nodeType.value;
	}

	frame_bridge.location = link_t;
}

function recursive()
{
	var frame_bridge = getFrame_bridge();
	if(!frame_bridge) 
		return;
	
	var link_t = link_ + "?recursive=true";
	if(frame_bridge.orgId)
	{
		link_t += "&orgId=" + frame_bridge.orgId.value;
	}
	
	if(frame_bridge.groupId)
	{
		link_t += "&groupId=" + frame_bridge.groupId.value;
	}
	if(frame_bridge.nodeType)
	{
		link_t += "&nodeType=" + frame_bridge.nodeType.value;
	}
	
	frame_bridge.location = link_t;
}

function getFrame_bridge()
{
	var frame_bridge = parent.frames["frame_bridge"];
	return frame_bridge;
}

window.onload = function autoRun(){
	//1 系统管理员,20696 蒋之春,10006888 qiangW
	var selectedText = "";
	var selectedValue = "";
	<%
	for(int i =0;i < infos.length;i ++)
	{
	  String user[] = infos[i].split(" ");
	%>
	 selectedText = "<%=infos[i]%>"
	 selectedValue = "<%=user[0]%>"
	if(selectedText.length>0 && selectedValue.length>0){
	    addone("userIds",selectedText,selectedValue);
	} 
	<%}%>
}
</SCRIPT>


	<body class="contentbodymargin" scroll="no" >
		<div id="contentborder">

			<form name="OrgJobForm" action="" method="post">

				<div align="center">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
						<tr class="tabletop">
							<td width="40%" align="center">
								&nbsp;
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td><!--
							<input type="checkBox" name="recursive" id="recursive" onClick="setRecursive()">
							递归选择用户-->
						</td>
						<tr>
							<td width="40%" align="center">
								&nbsp;
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center">
								&nbsp;
							</td>
						</tr>
					
						<tr class="tr">
							<td class="td" width="40%" align="center">
								可选人员
							</td>
							<td width="20%" class="td" align="center">
								&nbsp;

							</td>
							<td width="40%" align="center" class="td">
								已选择
							</td>
						</tr>
						<tr class="tabletop">
							<td width="40%" align="center">
								&nbsp;
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr class="tr">
							<td class="td" align="center">
								<select class="select" name="allist" id="allist" multiple style="width:90%" onDBLclick="addRole()" size="18">
									<pg:list requestKey="allUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />
											(<pg:cell colName="userName" />)
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
											<input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall('userIds')">
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
							<td class="td" align="center">
								<select class="select" name="userIds" multiple style="width:90%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />
											(
											<pg:cell colName="userName" />
											)
										</option>
									</pg:list>
								</select>

							</td>

						</tr>
						<tr class="tabletop">
							<td align="center">
								&nbsp;
							</td>
						</tr>
						<tr class="tr">
							<td colspan="3" class="td" align="center">
								<input name="add" type="button" class="input" value="确定" onclick="okadd()">
								<input name="add" type="button" class="input" value="放弃" onclick="parent.api.close();">

							</td>
						</tr>
					</table>
				</div>
				<div align="center"></div>
				<pg:beaninfo requestKey="userRoleForm">

					<input type="hidden" name="uid" value="<pg:cell colName="userId"  defaultValue=""/>" />
				</pg:beaninfo>
			</form>

		</div>
	</body>
</html>

