<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
//	String currUserId = (String)session.getAttribute("currUserId");
//	String desc = (String)request.getParameter("pager.desc");
//	String curOrgId = (String)request.getParameter("orgId");
	String restype=(String)request.getParameter("restypeId");
	String restypeName=request.getParameter("restypeName");
	String parent_resId = request.getParameter("parent_resId");
	String parent_resName = request.getParameter("parent_resName");
	
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	
var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
function dealRecord(dealType) {
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
				ResSearchList.action="<%=rootpath%>/resmanager/resource.do?method=deleteres";
				ResSearchList.submit();
	 			return true;
			}
		} 
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}
function getRes(resId,restypeId){
	
	getPropertiesContent().location.href="<%=rootpath%>/resmanager/resource.do?method=getResInfo&resId="+resId + "&parent_resId=<%=parent_resId%>&parent_resName=<%=parent_resName%>&restypeName=<%=restypeName%>&restypeId="+restypeId;		
	
}
function newRes(){
	
	window.location.href="<%=rootpath%>/sysmanager/resmanager/newsubres.jsp?restype=<%=restype%>&restypeName=<%=restypeName%>&parent_resId=<%=parent_resId%>&parent_resName=<%=parent_resName%>";	
}

function getResDetail(e,resId,resTypeId,resTypeName,title)
{
	if (jsAccessControl.setBackColor(e)){
		getPropertiesToolbar().location.href="resource_toolbar.jsp?resId2="+resId+"&resTypeId2="+resTypeId+"&restypeName="+resTypeName+"&title="+title;
	}
}


</SCRIPT>		
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><body>
			
				<form name="ResSearchList" method="post" >
					 <table width=98% border="0" cellpadding="0" cellspacing="1" class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="4"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>资源列表</strong></td>
         				</tr>
         			
						<pg:listdata dataInfo="ResParentList" keyName="ResParentList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="20" scope="request" data="ResParentList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="10">
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10">
								</td>
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
								<tr onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="getResDetail(this,'<pg:cell colName="resId" defaultValue=""/>','<pg:cell colName="restypeId" defaultValue=""/>','<pg:cell colName="restypeName" defaultValue="" />','<pg:cell colName="title" defaultValue="" />')" onDBLclick="getRes('<pg:cell colName='resId' defaultValue=''/>','<pg:cell colName='restypeId' defaultValue=''/>')" >
									<td class="tablecells" nowrap="nowrap">
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="resId" defaultValue=""/>" width="10">
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="restypeName" defaultValue="" />
									</td>
									
									
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="title" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="path" defaultValue="" />
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									<pg:index /><input type="submit" value="删除" class="input" onclick="javascript:dealRecord(1); return false;">
									<input name="newuser" type="button" class="input" value="新增" onClick="newRes()">
							
								</td>
								</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

					</table>
				</form>
		<%@include file="../sysMsg.jsp" %>
</body>
<center>
</html>

