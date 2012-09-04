<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
	String restype=(String)request.getParameter("restypeId");	
	restype = restype == null ? "" : restype;
	String restypeName=request.getParameter("restypeName");
	String parent_resId = request.getParameter("parent_resId");
%>
<html>
	<head>
		<title>资源列表</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="<%=rootpath%>/sysmanager/resmanager/common.js" type="text/javascript"></script>
<script language="JavaScript" src="<%=rootpath%>/sysmanager/include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	
var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
function getResDetail(e,resId,resTypeId,resTypeName,title)
{
	
	document.all("selectId").value = resId;
	if (jsAccessControl.setBackColor(e)){
	//	getPropertiesToolbar().location.href="properties_toolbar.jsp?resId2="+resId+"&resTypeId2="+resTypeId+"&resTypeName="+resTypeName+"&title="+title;
	}
}
	
function dealRecord(dealType) {
	var resId = document.all("selectId").value;
	var isSelect = false;
    var outMsg;
	    
    for (var i=0;i<ResSearchList.elements.length;i++) {
		var e = ResSearchList.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
	if(resId.length <1 && !isSelect){
		alert("请选择要删除的资源！");
		return;
	}
	
	if(resId.length >1 && !isSelect){
		alert("请选择要删除的资源！");
		return;
	//	if(window.confirm("你确定要删除吗？(相关子资源将一并删除)")){	
	//	ResSearchList.action="<%=rootpath%>/resmanager/resource.do?method=deleteresId&resId="+resId;
	//	ResSearchList.submit();
	//	}
	}
	
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要删除吗？(相关子资源将一并删除)";
        	if (confirm(outMsg)){
        		ResSearchList.action="<%=rootpath%>/resmanager/resource.do?method=deleteCmsRes&restypeName=<%=restypeName%>";
				ResSearchList.submit();
	 			return true;
			}
		} 
    }//else{
    //	alert("至少要选择一条记录！");
    //	return false;
   	//}
	return false;
}
function getRes(resId,restypeId){
	ResSearchList.action="<%=rootpath%>/sysmanager/resmanager/cmsModifyRes.jsp?restypeId=<%=restype%>&restypeName=<%=restypeName%>&resId="+ resId;
	ResSearchList.submit();
}
function newRes(){
	window.location.href="<%=rootpath%>/sysmanager/resmanager/cmsnewres.jsp?restype=<%=restype%>&restypeName=<%=restypeName%>";	
}

//资源访问授权
function accredit(dealType) {
	var resId = document.all("selectId").value;
	var isSelect = false;
    var outMsg;
	    
    for (var i=0;i<ResSearchList.elements.length;i++) {
		var e = ResSearchList.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
	
	
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要删除吗？(相关子资源将一并删除)";
        	if (confirm(outMsg)){
        		ResSearchList.action="<%=rootpath%>/resmanager/resource.do?method=getAllResList&restypeName=<%=restype%>";
				ResSearchList.submit();
	 			return true;
			}
		} 
    }//else{
    //	alert("至少要选择一条记录！");
    //	return false;
   	//}
	return false;
	    			//ResSearchList.action="<%=rootpath%>/resmanager/resource.do?method=getAllResList&restypeName=<%=restypeName%>&restype=<%=restype%>";
		
}



</SCRIPT>		
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<body>
			<%if(!"".equals(restype)) {%>
				<form name="ResSearchList" method="post" >
				<input type="hidden" name="resTypeName" value="<%=restypeName%>"/>
				<input type="hidden" name="restypeId" value="<%=restype%>"/>
					 <table width=100% border="0" cellpadding="0" cellspacing="1" bordercolor="#B7CBE4"  class="Datalisttable">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="7">
							<%
							if(restypeName!=null)
							{
								%>
								<div align="left">&nbsp;<b>
								<%=restypeName%></b>&nbsp;>>&nbsp;
								<%
				 				if (control.checkPermission(restype,
	                       		"resadd", AccessControl.RES_RESOURCE))
				            	{
				            	%>
								<a href="#" onclick="newRes()"><u>新增</u></a> |
								<%
								}
								%>
								<%
				 				if (control.checkPermission(restype,
	                       		"resdelete", AccessControl.RES_RESOURCE))
				            	{
				            	%>
								<a href="#" onclick="javascript:dealRecord(1); return false;"><u>删除</u></a></div>
								<%
								}
								%>
							<%}%>
							<hr size="1">
							<div align="left">&nbsp;</div>
							</td>
         				</tr>
         			<pg:listdata dataInfo="ResSearchList" keyName="ResSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="10" scope="request" data="ResSearchList" isList="false">
							<tr class="cms_report_tr">
								<!--设置分页表头-->
								<td class="headercolor" height='30' width="4%">
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')">
								</td>
								<input class="text" type="hidden" name="selectId">
								<td class="headercolor" height='30' width="18%">资源类型</td>
								<td class="headercolor" height='30' width="18%">资源名称</td>
								<td class="headercolor" height='30' width="30%">描述信息</td>
								<td class="headercolor" height='30' width="10%">授予用户</td>
								<td class="headercolor" height='30' width="10%">授予角色</td>
								<td class="headercolor" height='30' width="10%">授予机构</td>
							</tr>
							<pg:param name="restypeId" />
							<pg:param name="title" />
							<pg:param name="path" />

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center' height='30'>
										暂时没有资源项
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								<tr class="cms_data_tr" onMouseOver="high(this)" onMouseOut="unhigh(this)">
									<td class="tablecells" nowrap="nowrap" height='30'>
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="resId" defaultValue=""/>" width="10">
									</td>
									<td class="tablecells" nowrap="nowrap" height='30'>
										<pg:cell colName="restypeName" defaultValue="" />
									</td>
									
									
									<td class="tablecells" nowrap="nowrap" height='30' style="cursor:hand" onclick="getRes('<pg:cell colName='resId' defaultValue=''/>','<pg:cell colName='restypeId' defaultValue=''/>')">
									<input type="hidden" name="title" value="<pg:cell colName="title" defaultValue="" />"/>
										<pg:cell colName="title" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" height='30'>
										<pg:cell colName="path" defaultValue="暂时为空" maxlength="14" />
									</td>
									
									<%
				 					if (control.checkPermission(restype,
	                       			"userpermission", AccessControl.RES_RESOURCE))
				            		{
				            		%>
									<td onclick="openWin('user_iframe.jsp?resId2=<pg:cell colName="title" defaultValue=""/>&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="restypeName" defaultValue=""/>&title=<pg:cell colName="title" defaultValue=""/>',screen.availWidth-100,screen.availHeight-50)">
									<IMG src="../images/new_doc.gif" border="0"  style="cursor:hand">
									</td>
									<%
									}
									else
									{%><td></td><%}%>
									
									<%
				 					if (control.checkPermission(restype,
	                       			"userpermission", AccessControl.RES_RESOURCE))
				            		{
				            		%>
									<td onclick="openWin('role_iframe.jsp?resId2=<pg:cell colName="title" defaultValue=""/>&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="restypeName" defaultValue=""/>&title=<pg:cell colName="title" defaultValue=""/>',screen.availWidth-100,screen.availHeight-50)">
									<IMG src="../images/new_doc.gif" border="0"  style="cursor:hand">
									</td>
									<%
									}
									else
									{%><td></td><%}%>
									
									<%
				 					if (control.checkPermission(restype,
	                       			"userpermission", AccessControl.RES_RESOURCE))
				            		{
				            		%>
									<td onclick="openWin('org_iframe.jsp?resId2=<pg:cell colName="title" defaultValue=""/>&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="restypeName" defaultValue=""/>&title=<pg:cell colName="title" defaultValue=""/>',screen.availWidth-100,screen.availHeight-50)">
									<IMG src="../images/new_doc.gif" border="0"  style="cursor:hand">
									</td>
									<%
									}
									else
									{%><td></td><%}%>
									
								</tr>
							</pg:list>
							<tr height="18px">
								<td colspan=7 align='center' class="Data_List_Table_Bottom">
									<pg:index />						
								</td>
						</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

				  </table>
				</form>
		<%}else{out.println("<br><br><div align=center height=30><font size=3>请选择左边的一个资源进行操作！</font></div>");}%>
</body>
<center>
</html>
