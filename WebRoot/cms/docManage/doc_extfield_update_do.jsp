<%
/**
  *	修改扩展字段
  * Action
  */
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.customform.*"%>
<%@page import="java.util.ArrayList"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	CustomFormManager cm = new CustomFormManagerImpl();

	DocExtField docExtField = new DocExtField();

	docExtField.setFieldId(Integer.parseInt(request.getParameter("id")));
	docExtField.setFieldName(request.getParameter("fieldName"));
	docExtField.setFieldLable(request.getParameter("fieldLable"));
	docExtField.setFieldDesc(request.getParameter("fieldDesc"));
	docExtField.setFieldType(request.getParameter("fieldType"));
	if("1".equals(request.getParameter("fieldType")))
		docExtField.setMaxlen(Integer.parseInt(request.getParameter("maxlen")));
	docExtField.setType("2");//暂时只考虑频道，因为没有考虑继承，设置站点的就没实际的意义
	docExtField.setInputType(Integer.parseInt(request.getParameter("scopeType")));
	//保存取值范围信息
	if(!"".equals(request.getParameter("minscope")))
		docExtField.setMinvalue(Integer.parseInt(request.getParameter("minscope")));
	if(!"".equals(request.getParameter("maxscope")))
		docExtField.setMaxvalue(Integer.parseInt(request.getParameter("maxscope")));
		
	if(!"".equals(request.getParameter("list"))){
		String s = request.getParameter("list");
		String[] items = s.split(";");
		ArrayList list = new ArrayList();
		for(int i=0;i<items.length;i++)
			list.add(items[i].split(",")[0]+";"+items[i].split(",")[1]);
		docExtField.setEno(list);
	}


	boolean b = cm.updateDocExtField(docExtField);

	if(b){
		if("null".equals(request.getParameter("id")))
		{
		%>
		<SCRIPT LANGUAGE="JavaScript"> 
			//window.dialogArguments.document.location.reload();
			alert("新增扩展字段成功！");
			var str = window.dialogArguments.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		</script>
		<%
		}else{
		%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("修改扩展字段成功！");
			var str = window.dialogArguments.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		</script>
	<%}
		}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("修改扩展字段失败！");
		</script>
	<%
		}
	%>			