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
    String isok =(String)request.getAttribute("isok"); 
    String name =(String)request.getAttribute("name");
    String pass =(String)request.getAttribute("pass");
    String deleteuser =(String)request.getAttribute("deleteuser");
    String password =(String)request.getAttribute("password");
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
var pass ="<%=pass%>";
var deleteuser ="<%=deleteuser%>";
if(succeed=="ok"){
	alert("修改会员信息成功！");
}
if(pass=="ok"){
	alert("密码已恢复为初始密码！");
}
if(deleteuser=="ok"){
	alert("删除收费用户成功！");
}
var isok = "<%=isok%>";
var name ="<%=name%>";
var password ="<%=password%>";
if ( isok == "ok"){
	alert("新增收费用户成功！用户帐号："+name+"  密码："+password);
}
//修改会员
function getMember(memberId,userType){
	userList.action="../member/memberManager.do?method=getMember&flag=2&memberId="+memberId+"&userType="+userType;
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
				userList.action="../member/memberManager.do?method=deleteMember&flag=2";
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
				userList.action="../member/memberManager.do?method=dredgeMember";
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
	document.location.href="../member/memberManager.do?method=newMember&flag=2";	
}
//会员查询
function queryUser()
{	
	
	if(userList.nologin.value.search(/[^0-9]/g) !=-1){
		alert("只能输入数字！");
		userList.nologin.focus();
		return false;
	}
	userList.action="../sitemember/charge_memberList.jsp"
	userList.submit();	
}


</SCRIPT>		
			<body>
				<form name="userList" method="post" >
					<TABLE cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
						<tr valign='top'>
           					<td height='30'valign='middle' ><img src='../../sysmanager/images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>收费用户列表</strong></td>
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
           						
           						
							
								
								会员类别：
								<select name="rank" style="width:100px" >
								<option value="">
										--请选择等级--
								</option>
       							<option value="A">
									A类会员
								</option>
								<option value="B">
									B类会员
								</option>
								<option value="C">
									C类会员
								</option>
								<option value="D">
									D类会员
								</option>
								<option value="E">
									E类会员
								</option>
								<option value="F">
									F类会员
								</option>
							</select>
<!--           						<select name="rank" style="width:100px" >-->
<!--           							<option value="">-->
<!--										--请选择等级---->
<!--									</option>-->
<!--									<pg:list requestKey="listrole">-->
<!--										<option value="<pg:cell colName="roleId"/>">-->
<!--										<pg:cell colName="roleName"/> -->
<!--										</option>-->
<!--									</pg:list>	-->
<!--								</select>-->
								
								会员状态：
           						<select name="state" style="width:100px" >
           							<option value="">
										--请选择状态--
									</option>
									<pg:list requestKey="isllist">
										<option value="<pg:cell colName="dictdataValue"/>">
										<pg:cell colName="dictdataName"/> 
										</option>
									</pg:list>	
								</select>
									
           						正常/过期用户：
           						<select name="Isvalid" style="width:100px" >
           							<option value="">
										--请选择状态--
									</option>
									<option value="1">
										正常用户
									</option>
									<option value="0">
										过期用户
									</option>
								</select>	
							
							</td>				
           					
           				</tr>
         				<tr >
           				   			
           					 <td height='30'valign='middle' align="left">
           					    用户帐号：  <input type="text" name="membername" style="width:100px" >
           					    用户姓名：  <input type="text" name="realname" style="width:100px" >
           					    未登陆天数 ：  <input type="text" name="nologin" style="width:100px" >
							    联  系 地  址：  <input type="text" name="address" style="width:100px" >
							             					
           					</td>
         				<TR>
         					<td  colspan="2" height='30'valign='middle' align="right" width="40%">
         						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">
           						<input name="newuser" type="button" class="input" value="新增用户" onClick="newMember()">
           						<input type="submit" value="删除用户" class="input" onclick="javascript:dealRecord(1); return false;">
           						<input name="onuser" type="button" class="input" value="开通用户" onclick="javascript:onMember(1); return false;">
           						<input name="offuser" type="button" class="input" value="停用用户" onclick="javascript:offMember(1); return false;">
           						<input name="rank" type="button" class="input" value="角色授予" onclick="javascript:rankMember(1); return false;">
           					</td>
           				</TR>
					</TABLE>
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
         			
         				<pg:listdata dataInfo="ChargeMemberList" keyName="ChargeMemberList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="13" scope="request" data="ChargeMemberList" isList="false">
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
								<td class="headercolor"  width="15%">联系地址</td>
								<td class="headercolor"  width="10%">开始时间</td>
								<td class="headercolor"  width="10%">结束时间</td>								
								<td class="headercolor"  width="10%">用户类别</td>
								<td class="headercolor"  width="10%">会员状态</td>								
								<td class="headercolor"  width="10%">最后登陆时间</td>
							</tr>
							<pg:param name="realname" />
							<pg:param name="Isvalid" />
							<pg:param name="memberType" />
							<pg:param name="membername" />
							<pg:param name="address" />
							<pg:param name="nologin" />
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
							onDBLclick="getMember('<pg:cell colName='userId' defaultValue=''/>','<pg:cell colName='userType' defaultValue=''/>')"  >
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
									<td class="tablecells" nowrap="nowrap">
									
										<pg:equal colName="remark1" value="1">新申请</pg:equal>
										<pg:equal colName="remark1" value="2">已开通</pg:equal>
										<pg:equal colName="remark1" value="0">停用</pg:equal>
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="lastlogindate" defaultValue="" dateformat="yyyy-MM-dd" />
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
