<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
	String restype=(String)request.getParameter("restypeId");
	String restypeName=request.getParameter("restypeName");
	String parent_resId = request.getParameter("parent_resId");
	
	String rootpath = request.getContextPath();
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">	
				
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../resmanager/common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
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
	}
	
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要删除吗？(相关子资源将一并删除)";
        	if (confirm(outMsg)){
        		document.ResSearchList.target = "deleteRes";
        		//ResSearchList.action="<%=rootpath%>/resmanager/resource.do?method=deleteres&restypeName=<%=restypeName%>";
        		document.ResSearchList.action="saveResOp.jsp?opType=delete&restypeName=<%=restypeName%>";
				document.ResSearchList.submit();
	 			return true;
			}
		} 
    }
	return false;
}
function getRes(resId,restypeId){
	ResSearchList.action="<%=rootpath%>/sysmanager/resmanager/modifyRes.jsp?restypeId=<%=restype%>&restypeName=<%=restypeName%>&resId="+ resId;
	ResSearchList.submit();
}
function newRes(){
	var url = "<%=rootpath%>/sysmanager/resmanager/newres.jsp?restype=<%=restype%>&restypeName=<%=restypeName%>";
	window.location.href = url;
	//window.showModalDialog(url,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
	//document.location = document.location;	
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
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><body>
			
				<form name="ResSearchList" method="post" >
				<input type="hidden" name="resTypeName" value="<%=restypeName%>"/>
				<input type="hidden" name="restypeId" value="<%=restype%>"/>
					 <table width=100% border="0" cellpadding="0" cellspacing="1" class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="4"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>资源列表</strong></td>
         				</tr>
         			<pg:listdata dataInfo="ResSearchList" keyName="ResSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="20" scope="request" data="ResSearchList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="10">
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10">
								</td>
								<input class="text" type="hidden" name="selectId">
								<td class="headercolor">资源类型</td>
								<td class="headercolor">资源名称</td>
								<td class="headercolor">描述信息</td>
							</tr>
							<pg:param name="restypeId" />
							<pg:param name="title" />
							<pg:param name="path" />

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有资源项
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'"
								 
								onDBLclick="getRes('<pg:cell colName='resId' defaultValue=''/>','<pg:cell colName='restypeId' defaultValue=''/>')"
								>
									<td class="tablecells" nowrap="nowrap">
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="resId" defaultValue=""/>" width="10">
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="restypeName" defaultValue="" />
									</td>
									
									
									<td class="tablecells" nowrap="nowrap">
									<input type="hidden" name="title" value="<pg:cell colName="title" defaultValue="" />"/>
										<pg:cell colName="title" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="path" defaultValue="暂时为空" />
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									 <pg:index />
									<%
									 if (control.checkPermission(restype,
						                       "resdelete", AccessControl.RES_RESOURCE))
						            {%>
									<input type="submit" value="删除" class="input" onclick="javascript:dealRecord(1); return false;">
									<%}%>
									
									<%
									 if (control.checkPermission(restype,
						                       "resadd", AccessControl.RES_RESOURCE))
						            {%>
									<input name="newuser" type="button" class="input" value="新增" onClick="newRes()">
									<%}%>
									<!-- <input type="submit" value="批量授权" class="input" onclick="javascript:accredit(1); return false;">-->
																
								</td>
						</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

				  </table>
				</form>
		<iframe name="deleteRes" width="0" height="0" ></iframe>
</body>
<center>
</html>
