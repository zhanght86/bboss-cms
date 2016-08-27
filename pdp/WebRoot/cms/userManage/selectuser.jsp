<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);	
	String roleId = request.getParameter("roleId");

	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String rolename = role.getRoleName();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>

	<title>角色用户列表</title>

	<script language="javascript">
		 function checkAll2(totalCheck,checkName){	
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	
	
	function selectuser(roleId)
	{	
		form2.action="../userManage/selectuser.jsp?roleId="+roleId;
		form2.submit();	
	}
	
	function deleteRoleuser(dealType) {
	    var isSelect = false;
	    var outMsg;
		    
	    for (var i=0;i<form2.elements.length;i++) {
			var e = form2.elements[i];
				
			if (e.name == 'ID'){
				if (e.checked){
		       		isSelect=true;
		       		
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要删除选定用户吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
					form2.action="../userManage/deleteRoleuser.jsp?roleId=<%=roleId%>";
					form2.submit();
		 			return true;
				}
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
			return false;
	}
	
	function back()
	{	
		form2.action="../userManage/main.jsp";
		form2.submit();	
	}
	
	function openWinSelf(url,swidth,sheight){
	
     var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	 if(w=="ok"){
	 	window.location.href = window.location.href;
	 }
	}
	</script>
</head>
<body  bgcolor="#F7F8FC" >

	<table width="90%" border="0" cellpadding="0" cellspacing="0" align="center">
	    <tr height="10">
	       <td>
	       </td>
	    </tr>
		<tr >
			<td colspan="2" class="topDIV"  align="left" valign="top">
					<FONT color="#8080c0"><A href="../userManage/main.jsp" >用户权限管理</A>>>查看<%=rolename%> 所包含的用户</FONT>
					
				</td>
		</tr>
		<tr height="20">
	       <td>
	       </td>
	    </tr>
		<tr>
			<td colspan="2">
				
<!-------------------------------------------------------------------------------------------------------------------------------->
					
					  <FORM id="form2" name="form2" method="post" onSubmit="">
						 <table  width="100%" height="30" >
						      <tr>
						      
						         <td  align="right">
						            
	                             </td>
	                           </tr>
	                           <tr>
	                           		<td  height="10" align=right >
						  			    <a style="cursor:hand" onClick="openWinSelf('../userManage/org2user.jsp?roleId=<%=roleId%>',screen.availWidth-200,screen.availHeight-200)">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">添加用户</div></a>
	
										<a style="cursor:hand" onClick="javascript:deleteRoleuser(1); return false;">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">删除</div></a>
										<a style="cursor:hand" onClick="back()">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">返回</div></a>
						 			</td>
	                           </tr>
						    </table>
					 
						   
						    
						 
					
						<hr width="100%">
						<table width="100%" border="0" cellpadding="3" cellspacing="0" class="Templateedittable">
						    <pg:listdata dataInfo="RoleSubUserList" keyName="RoleSubUserList" />
							<!--分页显示开始,分页标签初始化-->
							<pg:pager maxPageItems="10" scope="request" data="RoleSubUserList" isList="false">
							<tr>
								<td width="10%" height="24"   bgcolor="#EDEFF6">
									<input type="checkBox" name="checkBoxAll2" onClick="checkAll2('checkBoxAll2','ID')">全选
								</td>
								<td width="15%" height="24"   bgcolor="#EDEFF6">用户帐号</td>
								<td width="25%" height="24"   bgcolor="#EDEFF6">真实姓名</td>
								<td width="15%" height="24"   bgcolor="#EDEFF6">电子邮件</td>
								<td width="10%" height="24"   bgcolor="#EDEFF6">当前状态</td>
								<td width="10%" height="24"   bgcolor="#EDEFF6">注册时间</td>
							</tr>
							<pg:param name="userId" />
							<pg:param name="userName" />
							<pg:param name="roleId"/>
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有用户
									</td>
								</tr>
							</pg:notify>	
								
								<!--list标签循环输出每条记录-->
							<pg:list>
								
								<tr class="cms_data_tr" id="<pg:cell colName="userId" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
									<td height='25' class="tablecells" nowrap="nowrap">
										<input onClick="" type="checkbox" name="ID" value="<pg:cell colName="userId" defaultValue=""/>">
									</td>
									<td height='25' class="tablecells" nowrap="nowrap">
										<a style="cursor:hand" onclick="openWin('../userManage/userManager.do?method=getUser&userId=<pg:cell colName="userId" defaultValue=""/>',screen.availWidth-300,screen.availHeight-100)">
						  			    <pg:cell colName="userName" defaultValue="" />
						  			    </a>
									</td>
									<td height='25' class="tablecells" nowrap="nowrap">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
								
									<td height='25' class="tablecells" nowrap="nowrap">
										<pg:cell colName="userEmail" defaultValue="" />
									</td>									
									<td height='25' class="tablecells" nowrap="nowrap">
<!--										<pg:cell colName="userIsvalid" defaultValue="" />-->
										<pg:equal colName="userIsvalid" value="0">已删除</pg:equal>
							         	<pg:equal colName="userIsvalid" value="1">新申请</pg:equal>
							         	<pg:equal colName="userIsvalid" value="2">已开通</pg:equal>
							        	<pg:equal colName="userIsvalid" value="3">停用</pg:equal>
									</td>
									<td height='25' class="tablecells" nowrap="nowrap">
										<pg:cell colName="user_Regdate" defaultValue="" />
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=6 align='center'>
								<pg:index />
								</td>
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>						
					  </table>	
					  </FORM>
							
			</td>
		</tr>
  </table>	

<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>
