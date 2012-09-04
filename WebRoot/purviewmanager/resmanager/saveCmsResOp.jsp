<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Res"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
  <script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
		<link href="../../html/stylesheet/common.css" rel="stylesheet"
					type="text/css" />
		<script type="text/javascript" src="../../html/js/commontool.js"></script>
		<script type="text/javascript" src="../../html/js/dialog/lhgdialog.js?self=false"></script>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String rootpath = request.getContextPath();
	String operContent="";    
	String systemResID=""; 
    String operSource=control.getMachinedID();
    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage", request);
    String userName = control.getUserName();
    String description="";
    LogManager logManager = SecurityDatabase.getLogManager(); 
	
	String opType = request.getParameter("opType");
	ResManagerImpl resManagerImpl = new ResManagerImpl();
	boolean state = false;
	boolean isContainSysRes = false;
	boolean isResExisTitle = true;
	String mappingPath = "";
	
	if("new".equals(opType)){//新增资源
		Res res = new Res();
		res.setRestypeId(request.getParameter("restypeId"));
		res.setTitle(request.getParameter("title"));
		res.setPath(request.getParameter("path"));
		res.setRestypeName(request.getParameter("restypeName"));
		res.setRoleUsage(request.getParameter("roleUsage"));
		res.setMarker(request.getParameter("marker"));
		res.setReserved1(request.getParameter("reserved1"));
		res.setReserved3(request.getParameter("reserved3"));
		res.setReserved4(request.getParameter("reserved4"));
		res.setReserved5(request.getParameter("reserved5"));
		res.setParentId(request.getParameter("parentId"));
		
		res.setAttr1(request.getParameter("attr1"));
		res.setAttr2(request.getParameter("attr2"));
		res.setAttr3(request.getParameter("attr3"));
		res.setAttr4(request.getParameter("attr4"));
		res.setAttr5(request.getParameter("attr5"));
		res.setAttr6(request.getParameter("attr6"));
		res.setAttr7(request.getParameter("attr7"));
		res.setAttr8(request.getParameter("attr8"));
		res.setAttr9(request.getParameter("attr9"));
		res.setAttr10(request.getParameter("attr10"));
		res.setAttr11(request.getParameter("attr11"));
		res.setAttr12(request.getParameter("attr12"));
		res.setAttr13(request.getParameter("attr13"));
		res.setAttr14(request.getParameter("attr14"));
		res.setAttr15(request.getParameter("attr15"));
		res.setAttr16(request.getParameter("attr16"));
		res.setAttr17(request.getParameter("attr17"));
		res.setAttr18(request.getParameter("attr18"));
		res.setAttr19(request.getParameter("attr19"));
		res.setAttr20(request.getParameter("attr20"));
		res.setAttr21(request.getParameter("attr21"));
		res.setAttr22(request.getParameter("attr22"));
		res.setAttr23(request.getParameter("attr23"));
		res.setAttr24(request.getParameter("attr24"));
		res.setAttr25(request.getParameter("attr25"));
		res.setAttr26(request.getParameter("attr26"));
		res.setAttr27(request.getParameter("attr27"));
		isResExisTitle = resManagerImpl.isResExistitle(request.getParameter("title"));
		if(!isResExisTitle){
			state = resManagerImpl.storeRes(res);
			if(state){//记录日志
				operContent=RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.storage.resource", request)+": "+res.getTitle();
			 	description="";
	        	logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
	        } 
		}
	}else if("update".equals(opType)){//修改资源
		String restypeId = request.getParameter("restypeId");
		String restypeName = request.getParameter("restypeName");
		String resId = request.getParameter("resId");
		String oldTitle = request.getParameter("oldTitle");
		String title = request.getParameter("title");
		String path = request.getParameter("path");
		if(oldTitle.equals(title)){
			isResExisTitle = false;
		}else{
			isResExisTitle = resManagerImpl.isResExistitle(request.getParameter("title"));
		}
		if(!isResExisTitle){
			state = resManagerImpl.updateRes(resId,path,title);
			if(state){
				mappingPath = rootpath+"/reslist.jsp?restypeId=" + restypeId + "&restypeName=" + restypeName;
				operContent=RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.modify.resource.is", request)+restypeName+" ,"+RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.resource.titile.is", request)+":"+title; 
				description="";
		        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
	        } 
		}
	}else if("delete".equals(opType)){
		String[] resIds = request.getParameterValues("checkBoxOne");
		String restypeName = request.getParameter("restypeName");
		for(int i = 0; i < resIds.length; i++){
			Res res = resManagerImpl.getRes("res_id",resIds[i]);
			if(Integer.parseInt(res.getAttr26()) == 1)
			{
				systemResID += res.getAttr27();
				isContainSysRes = true;
			}
			operContent=RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.delte.resource", request)+": "
			+ restypeName +RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.delete.role's resource", request)+": "+
			res.getTitle() + RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.but.system.resource", request)+"： " +
			systemResID + RequestContextUtils.getI18nMessage("sany.pdp.resourcemanage.be.not.delete", request);
			description="";
			logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
		}
		state = resManagerImpl.deleteResandAuth(resIds);     
	}
%>
<script>
var api = parent.frameElement.api;
if(api){
	W = api.opener;
}
</script>
<%
	if("new".equals(opType)){
		if(isResExisTitle){//存在相同的title
%>
			<script language="Javascript">
				W.$.dialog.alert("<pg:message code='sany.pdp.resourcemanage.resource.name.exist'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			</script>
<%
		}else if(state && !isResExisTitle){//不存在相同的title，保存成功
%>
			<script language="Javascript">
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			</script>
<%		
		}else{//保存失败
%>
			<script language="Javascript">
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			</script>
<%		
		}
	}else if("update".equals(opType)){
		if(isResExisTitle){
%>
			<script language="Javascript">
				W.$.dialog.alert("<pg:message code='sany.pdp.resourcemanage.resource.name.exist'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			</script>
<%
		}else if(state && !isResExisTitle){
%>
			<script language="Javascript">
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			</script>
<%
		}else{
%>
			<script language="Javascript">
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			</script>
<%		
		}
	}else if("delete".equals(opType)){
		
		if(state){
			if(isContainSysRes){
%>
			<script language="Javascript">
				parent.refsh();
			</script>
<% 
			}
			else
			{
%>
           <script language="Javascript">
           		parent.refsh();
			</script>
<% 
			}
		}else{
%>
			<script language="Javascript">
				parent.refsh();
			</script>
<%
		}
		}
	
%>
