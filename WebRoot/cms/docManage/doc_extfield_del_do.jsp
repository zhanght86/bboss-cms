<%
/**
  *	删除扩展字段
  * Action
  * type 1：在维护界面上删 2：在具体的站点或频道上删
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
	if(type!=null&&id!=null)
		b = cm.delDocExtFieldOfSiteOrChl(ids,type,id);
	else
		b = cm.delDocExtField(ids);

	if(b){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("删除扩展字段成功！");
			var str = window.dialogArguments.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("删除扩展字段失败！");
		</script>
	<%
		}
	%>			