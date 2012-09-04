<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Res"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String rootpath = request.getContextPath();
	String operContent="";        
    String operSource=control.getMachinedID();
    String openModle="资源管理";
    String userName = control.getUserName();
    String description="";
    LogManager logManager = SecurityDatabase.getLogManager(); 
	
	String opType = request.getParameter("opType");
	ResManagerImpl resManagerImpl = new ResManagerImpl();
	boolean state = false;
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
				operContent="存储资源: "+res.getTitle();
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
				operContent="修改资源，修改资源为"+restypeName+" 的资源标题为："+title; 
				description="";
		        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
	        } 
		}
	}else if("delete".equals(opType)){
		String[] resIds = request.getParameterValues("checkBoxOne");
		String restypeName = request.getParameter("restypeName");
		for(int i = 0; i < resIds.length; i++){
			Res res = resManagerImpl.getRes("res_id",resIds[i]);
			operContent="删除资源: "+ restypeName +"  删除角色中关联资源: "+ res.getTitle();
			description="";
			logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
		}
		state = resManagerImpl.deleteRes(resIds);     
	}
%>

<%
	if("new".equals(opType)){
		if(isResExisTitle){//存在相同的title
%>
			<script language="Javascript">
				alert("操作失败!已有相同的资源名称!");
				parent.document.all("Submit1").disabled = false;
				parent.document.all("Submit3").disabled = false;
			</script>
<%
		}else if(state && !isResExisTitle){//不存在相同的title，保存成功
%>
			<script language="Javascript">
				alert("操作成功！");
				parent.window.close();
			</script>
<%		
		}else{//保存失败
%>
			<script language="Javascript">
				alert("操作失败！");
				parent.document.all("Submit1").disabled = false;
				parent.document.all("Submit3").disabled = false;
			</script>
<%		
		}
	}else if("update".equals(opType)){
		if(isResExisTitle){
%>
			<script language="Javascript">
				alert("操作失败!已有相同的资源名称!");
				parent.document.all("Submit1").disabled = false;
				parent.document.all("Submit3").disabled = false;
			</script>
<%
		}else if(state && !isResExisTitle){
%>
			<script language="Javascript">
				alert("操作成功！");
				parent.window.close();
			</script>
<%
		}else{
%>
			<script language="Javascript">
				alert("操作失败！");
				parent.document.all("Submit1").disabled = false;
				parent.document.all("Submit3").disabled = false;
			</script>
<%		
		}
	}else if("delete".equals(opType)){
		if(state){
%>
			<script language="Javascript">
				alert("删除成功！");
				parent.refsh();
			</script>
<% 
		}else{
%>
			<script language="Javascript">
				alert("删除失败！");
			</script>
<%
		}
	}
%>
