<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="../../WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="../../WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.cms.addressmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userId = request.getParameter("userId");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script language="javascript" >
function checkAll(totalCheck,checkName){//复选框全部选中
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

function checkOne(totalCheck,checkName,id){//单个选中复选框
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

function usersearch(){
	userList.action="address_list.jsp"
	userList.submit();
}
function usercreator(){
	var v = window.showModalDialog("address_creator.jsp",window,"dialogHeight:500px; dialogWidth:600px;");
	if(v){
		userList.action = "address_list.jsp?userId=<%=userId%>";
		userList.target = "";
		userList.submit();
	}
}
function showUpdate(obj){
	var v = obj.id;
	var rv = window.showModalDialog("address_update.jsp?addressBookId="+v,window,"dialogHeight:500px; dialogWidth:600px;");
	if(rv){
		userList.action = "address_list.jsp?userId=<%=userId%>";
		userList.target = "";
		userList.submit();
	}
}
function userupdate(){
	var addressBookIds = document.all.item("checkBoxOne");
	var count = 0;
	var addressBookId = null;
	for(var i=0;i<addressBookIds.length;i++){
		if(addressBookIds[i].checked){
			count++;
			addressBookId = addressBookIds[i].id;
		}
	}
	if(count == 0){
		alert("请选择要一个联系人修改！");
		return;
	}else if(count == 1){
		var v = window.showModalDialog("address_update.jsp?addressBookId="+addressBookId,window,"dialogHeight:500px; dialogWidth:600px;");
		if(v){
			userList.action = "address_list.jsp?userId=<%=userId%>";
			userList.target = "";
			userList.submit();
		}
	}else{
		alert("只能选择一个联系人进行修改！");
		return;
	}
}
function userselect(){
	var userId = document.all.item("checkBoxOne");
	var id = "";
	if(userId.length){
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < userId.length; i ++){
			if(!userId[i].checked){
				id = userId.value;
				continue;
			}else{				
				count ++;
				index = i;
			}
		}
		if(count == 0){
			alert("请选择要一个联系人查看！");
			return;
		}else if(count == 1){
			alert(id)
			getPropertiesContent().location.href="address_update.jsp?userId="+userId[index].value;
		}else{
			alert("只能选择一个联系人进行查看！");
			return;
		}
	}
}
function userdelete(){
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
		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
    	if (confirm(outMsg)){
    		userList.action="address_delete.jsp"
			userList.submit();
		}
		return true;
    }else{
    	alert("至少要选择一条记录！");
	    return false;
    }
	return false;
}
</script>
<style type="text/css">
<!--
.STYLE1 {color: #0000FF}
.STYLE2 {color: #000099}
-->
</style></head>

<body >
<form id="userList" target="deleteFrame" name="userList" method="post" action="">
			<table width="100%" border="0" class="Datalisttable" align="center">
 				<pg:listdata dataInfo="AddressList" keyName="AddressList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="5" scope="request" data="AddressList" isList="false">
				<tr class="cms_report_tr">
					<!--设置分页表头-->
					<td class="headercolor"  width="3%" align="left"></td>
					<td class="headercolor" width="7%" align="left">
					<P align="left">
						<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')">
					</P>
					</td>
					<td class="headercolor"  width="8%" align="left">联系人姓名</td>
					<td class="headercolor"  width="15%" align="left">公司</td>
					<td class="headercolor"  width="10%" align="left">移动电话1</td>
					<td class="headercolor"  width="10%" align="left">移动电话2</td>
					<td class="headercolor"  width="8%" align="left">办公电话</td>
					<td class="headercolor"  width="8%" align="left">住宅电话</td>
					<td class="headercolor"  width="8%" align="left">传真</td>
					<td class="headercolor"  align="left">Email</td>
				</tr>
				<pg:param name="userId" />
				<pg:param name="presonName" />
				<pg:param name="phoneType" />
				<pg:param name="phone" />
				<pg:param name="company" />
				<pg:param name="fax" />
				<pg:param name="isAll"/>
				<!--检测当前页面是否有记录-->
				<pg:notify>
				<tr height="18px" class="labeltable_middle_tr_01">
					<td colspan=100 align='center'>
						暂时没有联系人
					</td>
				</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				<pg:list>
				<tr id="<pg:cell colName="addressBookId" defaultValue="" />">
					<td class="tablecells" nowrap="nowrap" width="3%">
						<div onclick="showUpdate(this)" align="left" id="<pg:cell colName="addressBookId" defaultValue="" />">
							<a href="javascript:void(0)"><img src="../images/note.gif" width="16" height="15"></a>
						</div>
					</td>
					<td class="tablecells" nowrap="true">
					<P align="left">
						<INPUT type="checkbox" name="checkBoxOne" id="<pg:cell colName="addressBookId" defaultValue=""/>"
						onclick="checkOne('checkBoxAll','checkBoxOne',
						'<pg:cell colName="addressBookId" defaultValue="" />')" value='<pg:cell colName="addressBookId" defaultValue=""/>' >
					</P>
					</td>
					<td class="tablecells" nowrap="nowrap" >
						<pg:cell colName="personName" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" >
						<pg:cell colName="personCompany" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" width="8%">
						<pg:cell colName="personMobileTel1" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="personMobileTel2" defaultValue=" "/>
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="personWorkTel" defaultValue=" "/>
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="personHomeTel" defaultValue=" "/>
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="personFax" defaultValue=" "/>
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="personEmail" defaultValue=" "/>
					</td>
				</tr>
				</pg:list>
				<tr height="18px" class="labeltable_middle_tr_01">
					<td colspan=10 align='center'>
						<pg:index />&nbsp;
						<!-- input name="usersearcha" type="button" class="cms_button" value="详细信息" onClick="userselect()"-->
						<input name="usercreatea" type="button" class="cms_button" value="新增" onClick="usercreator()">
						<input name="userupdatea" type="button" class="cms_button" value="修改" onClick="userupdate()">
						<input name="userdeletea" type="button" class="cms_button" value="删除" onClick="userdelete()">
					</td>
				</tr>
				<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
			</table>
</form>
<iframe frameborder="0" marginheight="0" marginwidth="0" height="0" width="0" name="deleteFrame" />
</body>
</html>
