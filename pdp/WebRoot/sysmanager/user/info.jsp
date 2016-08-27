
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*, com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%
	List list = (List)session.getAttribute("userList");
	//List list = (List)request.getAttribute("userList");
	if ( list == null )	{
		System.out.println("list is null");
	}	 
	else{
		System.out.println("list size="+list.size());
	}
	String currOrgId = (String)session.getAttribute("orgId");
	UserInfoForm userInfoForm = new UserInfoForm();
	if ( (UserInfoForm)request.getAttribute("currUser") != null )
	{
		userInfoForm = (UserInfoForm)request.getAttribute("currUser") ;
	}

%>

<html>
<head>
<script language="JavaScript" src="/sysmanager/user/common.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理</title>
<link href="Images/style.css" rel="stylesheet" type="text/css">
</head>

<script language="JavaScript" >
var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
function storeUser()
{
	document.userInfo.action="../user/storeUser.do?method=storeUser";
	document.userInfo.submit();
}

function deleteUser()
{
   var UserIdList = new Array();
   var o = document.getElementsByName("checkBoxOne");
   var reLoadList = "false";
   for (var i=0; i<o.length; i++){
	   if (o[i].checked){
		   var attrs = o[i].attributes;
		   var id = attrs['value'].value;
		   UserIdList[i] = id;
		   if ( i == o.length - 1 )
		   {
				reLoadList = "true";
		   }
	 	   document.userList.action="../user/storeUser.do?method=deleteUser"+"&userId="+id+"&reLoadList="+reLoadList;
	   	   document.userList.submit();		   
	   }
   }
/*
	if  ( UserIdList.length != 0 )
	{
		document.userList.submit();
		document.userInfo.submit();
		document.userInfo.action="../user/userManager.do?method=getUserList";
		document.userInfo.submit();
	}
*/	
	//document.userInfo.submit();
}

function getUserInfo(e,userName,userId,userRealname,userSn,userPassword)
{
	if (jsAccessControl.setBackColor(e))
	{/*
		document.userInfo.userName.value = userName;
		document.userInfo.userId.value = userId;
		document.userInfo.userRealname.value = userRealname;
		document.userInfo.userSn.value = userSn;
		document.userInfo.userPassword.value = userPassword;		*/

		document.userInfo.action="../user/storeUser.do?method=getUser"+"&userId="+userId;
		document.userInfo.submit();	
	}

//	document.userInfo.submit();
}

function reloadUser(){
	document.userInfo.submit();
   //var o = document.getElementsByName("checkBoxOne");	
}

</script>

<body class="info">

<form name="userInfo" method="post" action="">
<table width="100%" height="25" border="0" cellpadding="0" cellspacing="2">
  <tr>
    <td height="25" bgcolor="#8B9FB9"><strong>　用户名称</strong></td>
    <td height="25"><input type="text" name="userName" value="<%=userInfoForm.getUserName()==null?"":userInfoForm.getUserName()%>"></td>
    <td height="25" bgcolor="#8B9FB9"><strong>　性别</strong></td>
    <td height="25"><input type="text" name="userRealname" value="<%=userInfoForm.getUserRealname()==null?"":userInfoForm.getUserRealname()%>"></td>    
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td height="25" bgcolor="#8B9FB9"><strong>　SN</strong></td>
    <td height="25"><input type="text" name="userSn" value="<%=userInfoForm.getUserSn()%>"></td>
    <td height="25" bgcolor="#8B9FB9"><strong>　口令</strong></td>
    <td height="25"><input type="password" name="userPassword" value="<%=userInfoForm.getUserPassword()==null?"":userInfoForm.getUserPassword()%>"></td>
    <td align="right">&nbsp;</td>
  </tr>  
  <tr>
    <td height="25" bgcolor="#8B9FB9"><strong>	电话</strong></td>
    <td height="25"><input type="text" name="homePhone" value="<%=userInfoForm.getHomePhone()==null?"":userInfoForm.getHomePhone()%>"></td>
    <td height="25" bgcolor="#8B9FB9"><strong>　电子邮件</strong></td>
    <td height="25"><input type="text" name="mail" value="<%=userInfoForm.getMail()==null?"":userInfoForm.getMail()%>"></td>
    <td align="right">&nbsp;</td>
  </tr>    
  
  <tr>
    <td height="25" bgcolor="#8B9FB9"><strong>	移动电话</strong></td>
    <td height="25"><input type="text" name="mobile" value="<%=userInfoForm.getMobile()==null?"":userInfoForm.getMobile()%>"></td>
    <td height="25" bgcolor="#8B9FB9"><strong>　组织机构</strong></td>
    <td height="25"><input type="text" name="ou" value="<%=userInfoForm.getOu()==null?"":userInfoForm.getOu()%>"></td>
    <td align="right">&nbsp;</td>
  </tr>    

  <tr>
    <td height="25" bgcolor="#8B9FB9"><strong>	邮政编码</strong></td>
    <td height="25"><input type="text" name="postalCode" value="<%=userInfoForm.getPostalCode()==null?"":userInfoForm.getPostalCode()%>"></td>
    <td align="right">&nbsp;</td>
  </tr>    
      
  <input type="hidden" name="userId" value="<%=userInfoForm.getUserId()==null?"":userInfoForm.getUserId()%>">
  <input name="orgId" value="<%=currOrgId%>" type="hidden">
</table>
</form>

<hr width="98%">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
    <td><table width="60%" border="0" align="right" cellpadding="0" cellspacing="0" >
        <tr>
          <td><div align="center">
            <input name="resave" type="button" class="input" value="保存" onclick="storeUser()">
          </div></td>
          <td><div align="center">
            <input name="Submit2" type="submit" class="input" value="删除" onclick="deleteUser()">
          </div></td>
          <td><div align="center">
            <input name="Submit3" type="submit" class="input" value="转移">
          </div></td>
          <td><input name="Submit32" type="submit" class="input" value="重置" onclick="reloadUser()"></td>
          <td><input name="Submit33" type="submit" class="input" value="配置"></td>
        </tr>
    </table></td>
  </tr>
</table>
<p>&nbsp;</p>

<form name="userList" method="post" action="">
<table width="100%" border="0" cellpadding="1" cellspacing="1">
  <tr class="tabletop">
    <td width="25" align="center"><input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')"></td>
    <td align="center">用户名称</td>
    <td align="center">用户ID</td>
    <td align="center">基本信息</td>
    <td align="center">用户实名</td>    
    <td align="center">用户口令</td>    
  </TR>

  <%
	for (int i=0;i<list.size();i++){
      	User user = (User)list.get(i);
    	String userId = user.getUserId().toString();
	   	String userName = user.getUserName();
 		String userRealname = user.getUserRealname();
 		int userSn = (user.getUserSn()).intValue();
 		String userPassword  = user.getUserPassword();
  %>
		<tr class="row" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="getUserInfo(this,'<%=userName%>','<%=userId%>','<%=userRealname%>','<%=userSn%>','<%=userPassword%>')" >
		      <td width="25" align="center"><input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<%=userId%>" class="mouse"></td>
              <TD height="22" name="userName"><%=userName%></TD>
              <TD height="22" ><%=userId%></TD>
              <TD height="22" ><%=userRealname%></TD>
              <TD height="22" ><%=userSn%></TD>
              <TD height="22" ><%=userPassword%></TD>              
        </TR>
    <%}%>

    </TABLE>  
</form>     
<!--     
<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="right">首页	上一页	下一页	尾页 共1页 [第1页]</td>
  </tr>
</table>
-->
<%@ include file="../sysMsg.jsp"%>
</body>
</html>

