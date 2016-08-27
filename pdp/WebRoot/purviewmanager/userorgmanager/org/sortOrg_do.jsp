<%
/**
 * <p>Title: 子机构排序</p>
 * <p>Description: 子机构排序处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
	String orgId = request.getParameter("orgId");//机构id
	String orgIdsStr = request.getParameter("orgIds");//机构orgName
	String[] orgIds = orgIdsStr.split(",");

	boolean flag = false;//System.out.println(orgIds.length);
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	flag = orgManager.sortOrg(orgId,orgIds);
	if(flag)
	{
%>
<SCRIPT LANGUAGE="JavaScript">
	var api = parent.frameElement.api, W = api.opener;
	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
</SCRIPT>
<%
	}
	else
	{
%>
<SCRIPT LANGUAGE="JavaScript">
	var api = parent.frameElement.api, W = api.opener;
	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.fail.nocause"/>');
</SCRIPT>
<%
	}
%>
<SCRIPT LANGUAGE="JavaScript">
	window.onload = function prompt(){
        //parent.divProcessing.style.display="none";
       // parent.document.getElementById("saveButton").disabled = false;
		//parent.document.getElementById("backButton").disabled = false;
		//parent.document.getElementById("button1").disabled = false;
		//parent.document.getElementById("button2").disabled = false;
		//parent.document.getElementById("button3").disabled = false;
		//parent.document.getElementById("button4").disabled = false;
    }
</SCRIPT>

