<%
/**
 * <p>Title: 角色类型管理页面</p>
 * <p>Description: 角色类型管理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
        accesscontroler.checkManagerAccess(request,response);
        String typeName = ""; 
		typeName = request.getParameter("typeName");
		if(typeName == null)
		{
			typeName = "";
		} 
		String typeDesc = ""; 
		typeDesc = request.getParameter("typeDesc");
		if(typeDesc == null)
		{
			typeDesc = "";
		}        
%>

<html >
<head>				
	<tab:tabConfig/>
	<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.manager"/></title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/treeview.css">

	<script language="JavaScript" src="../scripts/func.js" type="text/javascript"></script>
	<SCRIPT language="javascript">
	var api = frameElement.api, W = api.opener;
	
	function resetQuery()
	{
		document.all("typeName").value="";
		document.all("typeDesc").value="";
	}
	function queryRoleType()
	{
		var typeName = document.all("typeName").value;
		var typeDesc = document.all("typeDesc").value;
		
		document.all.roletypelist.src = 'roletypelist.jsp?typeName='+typeName+'&typeDesc='+typeDesc;
	}
	function newRoleType()
	{
		var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/roletype_add.jsp";
	 	 W.$.dialog({id:'roleTypeFrame',title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.add"/>',width:280,height:200, content:'url:'+url,parent:api,currentwindow:this});  
		
	 	/*
		var url = "roletype_add.jsp";
		var winValue = window.showModalDialog(url,window,"dialogWidth:"+(500)+"px;dialogHeight:"+(440)+"px;help:no;scroll:auto;status:no");
		
		if(winValue)
		{
			var typeName = document.all("typeName").value;
			var typeDesc = document.all("typeDesc").value;
			document.all.roletypelist.src = 'roletypelist.jsp?typeName='+typeName+'&typeDesc='+typeDesc;
		}
		*/
	}
	function delRoleType()
	{
		var all = window.frames['roletypelist'].document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++)
		{
			if(all[i].checked == true)
			{
				if(all[i].value==1)
				{
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.remove.cannot"/>', function(){}, api);
					return false;			
				}
				docidStr = docidStr + all[i].value + ";";
			}
		}
		if(docidStr!="")
		{		
			if(W.$.dialog.confirm('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.remove.comfirm"/>', function() {
			  	searchForm.action = "roletype_delete.jsp?roletype_id=" + docidStr;
				searchForm.submit();
			}));
		}else{
	    	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.remove.select"/>', function(){}, api);
	    	return false;
	   	}	
    }
	
	function doreset(){
		$("#reset").click();
	}
	</SCRIPT>	
</head>
<body>
	<div class="mcontent">
	<br/>
	<div id="searchblock">
		<div class="search_top">
			<div class="right_top"></div>
			<div class="left_top"></div>
		</div>
		<div class="search_box">
		<form name='searchForm' method='post'>
			<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="left_box"></td>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
							<tr>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type" /> : </th>
								<td><input type="text" name="typeName" value="<%=typeName%>"></td>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.description"/> : </th>
								<td><input type="text" name="typeDesc" value="<%=typeDesc%>"></td>
								<th>&nbsp;</th>
								<td>
									<a href="javascript:void(0)" class="bt_1" name="search" onClick='queryRoleType()'><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
									<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
									<input type="reset" id="reset" style="display:none"/>
								</td>
							</tr>
						</table>
					</td>
					<td class="right_box"></td>
				</tr>
	        </table>
    	</form> 
		</div>
		<div class="search_bottom">
			<div class="right_bottom"></div>
			<div class="left_bottom"></div>
		</div>
	</div>
	
	<div class="title_box">
		<div class="rightbtn">
			<a href="javascript:void(0)" onclick="newRoleType()" class="bt_small"><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.add"/></span></a> 
			<a href="javascript:void(0)" onclick="delRoleType()" class="bt_small"><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.remove"/></span></a>
		</div>
		
		<strong><span><pg:message code="sany.pdp.common.data.list"/></strong>
	</div>
    				
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<iframe width="100%" height=410px frameborder=0 noResize scrolling="false" marginWidth=0 name="roletypelist" src="roletypelist.jsp"></iframe>
		</table>
		</div>
	 </body>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</html>

