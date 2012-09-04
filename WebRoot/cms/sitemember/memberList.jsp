<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String succeed = (String)request.getAttribute("succeed");
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">

<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	
var succeed ="<%=succeed%>";
if(succeed=="ok"){
	alert("修改会员信息成功！");
}
//修改会员
function getMember(memberId,userType){
	userList.action="../member/memberManager.do?method=getMember&memberId="+memberId+"&userType="+userType;
	userList.submit();
}

//复选框全部选中
function checkAll(totalCheck,checkName){	
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
//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}		
//删除会员
function dealRecord(dealType) {
    var isSelect = false;
    var outMsg;
 	
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
		
		if (e.name == 'checkBoxOne'){
		
			if (e.checked){
	       		isSelect=true;
	       		
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
        	if (confirm(outMsg)){
				userList.action="../member/memberManager.do?method=deleteMember";
				userList.submit();
	 			return true;
			}
		} 
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}
//开通会员
function onMember(dealType) {
    var isSelect = false;
    var outMsg;
	    
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要开通使用所选会员吗？";
        	if (confirm(outMsg)){
				userList.action="../member/memberManager.do?method=onMember";
				userList.submit();
	 			return true;
			}
		} 
    }else{
    	alert("请选择您要开通的会员！");
    	return false;
    }
	return false;
}

//停用会员
function offMember(dealType) {
    var isSelect = false;
  
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
	
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要停用所选会员吗？";
        	if (confirm(outMsg)){
				userList.action="../member/memberManager.do?method=offMember";
				userList.submit();
	 			return true;
			}
		} 
    }else{
    	alert("请选择您要停用的会员！");
    	return false;
    }
	return false;
}

//会员等级设定
function rankMember(dealType) {
    var isSelect = false;
  
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
	
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要设定所选会员的角色吗？";
        	if (confirm(outMsg)){
				userList.action="../member/memberManager.do?method=rankMember";
				userList.submit();
	 			return true;
			}
		} 
    }else{
    	alert("请选择您要设定角色的会员！");
    	return false;
    }
	return false;
}

//新增会员
function newMember()
{
	document.location.href="../member/memberManager.do?method=newMember&flag=1";	
}
//会员查询
function queryUser()
{	
	
	userList.action="../sitemember/memberList.jsp"
	userList.submit();	
}


</SCRIPT>		
			<body>
				<form name="userList" method="post" >
					<TABLE cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
						<tr valign='top'>
           					<td height='30'valign='middle' ><img src='../../sysmanager/images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>会员列表</strong></td>
         				</tr>
         			
         				
         				<tr >
           				    <td height='30'valign='middle' align="left" >
           				     
           						<%
           							UserManager userManager = SecurityDatabase.getUserManager();
           							RoleManager roleManager = SecurityDatabase.getRoleManager();
           							List listtype = userManager.getmemberTypeList("3");
           							List listIsvalid = userManager.getmemberTypeList("19");
           							List listrole = roleManager.getRoleList("select * from td_sm_Role r where r.role_Type='member' and r.role_Id<>'15' order by r.role_Name");
           							request.setAttribute("mtllist",listtype);
									request.setAttribute("isllist",listIsvalid);
									request.setAttribute("listrole",listrole);           							
           						%>
           						
           						
								
           						有 效 性：
           						<select name="state" style="width:100px" >
           							<option value="">
										--请选择状态--
									</option>
									<option value="1">
										有效
									</option>
									<option value="0">
										无效
									</option>
								</select>
								
								会员角色：
           						<select name="rank" style="width:100px" >
           							<option value="">
										--请选择等级--
									</option>
									<pg:list requestKey="listrole">
										<option value="<pg:cell colName="roleId"/>">
										<pg:cell colName="roleName"/> 
										</option>
									</pg:list>	
								</select>
								
								会员状态：
           						<select name="Isvalid" style="width:100px" >
           							<option value="">
										--请选择状态--
									</option>
									<pg:list requestKey="isllist">
										<option value="<pg:cell colName="dictdataValue"/>">
										<pg:cell colName="dictdataName"/> 
										</option>
									</pg:list>	
								</select>	
							</td>				
           					
           				</tr>
         				<tr >
           				   			
           					 <td height='30'valign='middle' align="left">
           					    登 陆 名：  <input type="text" name="membername" style="width:100px" >
           					    联 系 人：  <input type="text" name="memberRealname" style="width:100px" >
							    单位名称：  <input type="text" name="companyName" style="width:100px" >           					
           					</td>
         				<TR>
         					<td  colspan="2" height='30'valign='middle' align="right" width="40%">
         						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">
           						<input name="newuser" type="button" class="input" value="新增会员" onClick="newMember()">
           						<input type="submit" value="删除会员" class="input" onclick="javascript:dealRecord(1); return false;">
           						<input name="onuser" type="button" class="input" value="开通会员" onclick="javascript:onMember(1); return false;">
           						<input name="offuser" type="button" class="input" value="停用会员" onclick="javascript:offMember(1); return false;">
           						<input name="rank" type="button" class="input" value="角色授予" onclick="javascript:rankMember(1); return false;">
           					</td>
           				</TR>
					</TABLE>
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
         			
         				<pg:listdata dataInfo="MemberList" keyName="MemberList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="13" scope="request" data="MemberList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="5%">
								<P align="left">
								<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on">
								</P>
								</td>
								<td class="headercolor"  width="10%">帐号</td>
								<td class="headercolor"  width="10%">密码</td>
								<td class="headercolor"  width="10%">用户姓名</td>
								<td class="headercolor"  width="35%">联系地址</td>
								<td class="headercolor"  width="10%">开始时间</td>
								<td class="headercolor"  width="10%">结束时间</td>								
								<td class="headercolor"  width="10%">用户类别</td>
								
							</tr>
							<pg:param name="sex" />
							<pg:param name="Isvalid" />
							<pg:param name="memberType" />
							<pg:param name="membername" />
							<pg:param name="memberRealname" />
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
							
							<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'"
							onDBLclick="getMember('<pg:cell colName='userId' defaultValue=''/>','<pg:cell colName='userType' defaultValue=''/>')" >
									<td class="tablecells" nowrap="true">
										<P align="left">
											<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="userId" defaultValue=""/>' >
										</P>
									</td>
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="userPassword" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="userRealname" defaultValue="" />
									
									</td>
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="userAddress" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="dredgeTime" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="past_Time" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" >
										<pg:equal colName="serviceType" value="A">A类会员</pg:equal>
										<pg:equal colName="serviceType" value="B">B类会员</pg:equal>
										<pg:equal colName="serviceType" value="C">C类会员</pg:equal>
										<pg:equal colName="serviceType" value="D">D类会员</pg:equal>
										<pg:equal colName="serviceType" value="E">E类会员</pg:equal>
										<pg:equal colName="serviceType" value="F">F类会员</pg:equal>
									</td>

							</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=10 align='center'>
									共
									<pg:rowcount />
									条记录
									<pg:index />
								</td>
								
						    </tr>
						
						</pg:pager>

				  </table>
				</form>
				

</body>
<center>
</html>
