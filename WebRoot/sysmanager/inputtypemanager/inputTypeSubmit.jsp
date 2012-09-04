<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManager"%>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManagerImpl"%>
<%@ page import="com.frameworkset.platform.dictionary.input.InputType"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%

		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String queryString = request.getParameter("querystring");
		String currentId=request.getParameter("inputTypeId");
		int flag =Integer.parseInt(request.getParameter("flag"));
		String typeName = request.getParameter("typeName");
		String typeDesc = request.getParameter("typeDesc");
		String typeScript = request.getParameter("typeScript");
		String sourcePath = request.getParameter("sourcePath");
		InputTypeManager inputTypeManager = new InputTypeManagerImpl();		
		InputType inputType=new InputType();
		boolean success=false;
		int deleteFlag = 0;
		//对操作状态进行判断	
		switch(flag)
		{
			//新增
			case 1:
			inputType.setInputTypeDesc(typeDesc);
			inputType.setInputTypeName(typeName);
			inputType.setInputScript(typeScript);
			inputType.setDataSourcePath(sourcePath);	
			success=inputTypeManager.insertInputType(inputType);			
			break;
			//修改
			case 2:
			inputType.setInputTypeId(Integer.valueOf(currentId));
			inputType.setInputTypeDesc(typeDesc);
			inputType.setInputTypeName(typeName);
			inputType.setInputScript(typeScript);
			inputType.setDataSourcePath(sourcePath);	
			success=inputTypeManager.updateInputType(inputType);			
			break;
			//删除 如果该输入类型被引用, 不允许删除
			case 3:
			String[] currentIds = currentId.split(",");
			deleteFlag=inputTypeManager.deleteInputType(currentIds);
			break;
		}
%>
<html>
<head>
<title></title>
<script language="javascript">
var api;
var W;

//判断新增是否成功
if('<%=flag%>'=='1'){
	api = parent.frameElement.api, W = api.opener;
	
	if(<%=success%>){
		W.$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.save.success"/>', function() {
			W.location.reload();
			api.close();
		}, api);
	}else{
		W.$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.save.fail"/>');
	}
}

//判断修改是否成功
if('<%=flag%>'=='2'){
	api = parent.frameElement.api, W = api.opener;
	
	if(<%=success%>){
		W.$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.modfiy.success"/>', function() {
			W.location.reload();
			api.close();
		}, api);
	}else{
		W.$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.modfiy.success"/>');
	}
}

//判断删除操作
if(!<%=success%> && '<%=flag%>'!='1' && '<%=flag%>'!='2')
{
	if('<%=flag%>'=='3')
	{
	    if(<%=deleteFlag%>==0){
	    	//全部删除成功
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.delete.success"/>', function() {
				parent.location.href = "inputTypeList.jsp?<%=queryString%>";
			});
	    }else if(<%=deleteFlag%>==1){
	    	//部分删除成功
	        $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.delete.fail.part"/>', function() {
	        	parent.location.href = "inputTypeList.jsp?<%=queryString%>";
	        });
	    }else{
	    	//全部删除不成功
	        $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.delete.fail"/>');
	    }
	    
	}else{
		//对删除意外的其他操作
		$.dialog.alert('<pg:message code="sany.pdp.common.operation.fail.nocause"/>');
	}
}

</script>
</head>
<body>
</body>
</html>


