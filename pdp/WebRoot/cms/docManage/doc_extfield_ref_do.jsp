<%
/**
  *	从所有扩展字段中选择一些作为自己的扩展字段，是一种引用关系
  * Action
  * type 1：站点的引用 2：频道的引用
  */
%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.customform.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	CustomFormManager cm = new CustomFormManagerImpl();

	String type = request.getParameter("type");//站点or频道
	String id = request.getParameter("id");//站点id or 频道id
	String[] ids = (request.getParameter("ids")).split(",");//所选扩展字段id

	boolean b = false;
	b = cm.addDocExtFields(ids,type,id);

	if(b){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("选择扩展字段成功！");
			var str = window.dialogArguments.dialogArguments.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			window.dialogArguments.dialogArguments.location.href = strArray+"?"+window.dialogArguments.dialogArguments.document.all.queryString.value;
			window.dialogArguments.close();
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("选择扩展字段失败！");
		</script>
	<%
		}
	%>			