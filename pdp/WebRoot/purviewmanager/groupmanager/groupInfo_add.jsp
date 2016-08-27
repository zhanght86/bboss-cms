<%
/*
 * <p>Title: 新增一级用户组</p>
 * <p>Description: 新增一级用户组</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新增一级用户组</title>
		<script language="javascript"> 	
			var api = frameElement.api, W = api.opener;
		
		  //保存用户组
		  function saveGroup() 
		  {
		    var submitFlag=0;
			var gn = groupForm.groupName.value;
			var gd = groupForm.groupDesc.value;
			
			if(gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="")
			{
	        	W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.not.null'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        	return false;
			}
			if(gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.not.null'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        	return false;
			}
		
			var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
			
			if(!re.test(gn))
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false; 
			}
			
			if(!re.test(gd))
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false; 
			}
			
			
			if(gn.length>100)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			if(gd.length>100)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			
			groupForm.action= "submitGroup.jsp?flag=1";
			groupForm.target="submitGroup";
			groupForm.submit(); 
			
		  } 
		  
		  function back()
		  {
		  	window.returnValue = true;
		  	window.close();
		  }
		</script>
	</head>
	<body class="contentbodymargin" scroll="no">
	        <iframe src="" name="submitGroup" style="display:none"></iframe>
	        <div id="" align="center">
	        <center>
			<form action="" id="groupForm" method="post">
				<table width="50%" border="0"   cellpadding="0" cellspacing="1" class="table2">
					<tr>
						<th><pg:message code="sany.pdp.groupmanage.group.name"/>*：</th>
						<td align="left" class="detailcontent">
							<input name="groupName" type="text"  />
						
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.groupmanage.group.description"/>*：</th>
						<td align="left" class="detailcontent">
							<textarea name="groupDesc" ></textarea>
						
						</td>
					</tr>
			</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="2">
					<tr>
						<td>
							<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td align="center">
										<a class="bt_1" onclick="saveGroup();" ><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
										<a class="bt_2" onclick="closeDlg();" ><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
			</center>
			</div>
			<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:260px;display:none">
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
</html>
